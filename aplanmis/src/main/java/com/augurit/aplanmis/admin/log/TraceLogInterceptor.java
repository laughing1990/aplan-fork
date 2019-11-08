package com.augurit.aplanmis.admin.log;

import com.augurit.agcloud.bpm.common.utils.SpringContextHolder;
import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.security.user.OpuOmUser;
import com.augurit.agcloud.framework.security.user.OpusLoginUser;
import com.augurit.agcloud.framework.util.HttpUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.mapper.OpuOmOrgMapper;
import com.augurit.agcloud.opus.common.mapper.OpuOmUserMapper;
import com.augurit.aplanmis.common.domain.AeaTraceLog;
import com.augurit.aplanmis.common.service.admin.log.AeaTraceLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.core.env.Environment;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 跟踪日志 class
 *
 * @author jjt
 * @date 2019/10/15
 */
public class TraceLogInterceptor implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private static Environment environment = SpringContextHolder.getBean(Environment.class);
    private static OpuOmUserMapper userMapper = SpringContextHolder.getBean(OpuOmUserMapper.class);
    private static OpuOmOrgMapper orgMapper = SpringContextHolder.getBean(OpuOmOrgMapper.class);
    private static AeaTraceLogService logService = SpringContextHolder.getBean(AeaTraceLogService.class);
    private static final ThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal<Long>("ThreadLocal StartTime");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        logger.info("Controller方法调用前："+handler.toString());
        // 开始时间
        long beginTime = System.currentTimeMillis();
        // 线程绑定变量（该数据只有当前请求的线程可见）
        startTimeThreadLocal.set(beginTime);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        logger.info("Controller方法调用后，视图渲染前："+handler.toString()+",");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        logger.info("视图渲染后：" + handler.toString());

        // 开始时间
        long beginTime = startTimeThreadLocal.get();
        // 结束时间
        long endTime = System.currentTimeMillis();
        // 保存日志
        traceRequest(request, (endTime-beginTime), handler, ex);
        //删除线程变量中的数据，防止内存泄漏
        startTimeThreadLocal.remove();
    }

    /**
     * 日志跟踪request请求
     *
     * @param request
     * @param operationDuration
     *
     */
    private void traceRequest(HttpServletRequest request, Long operationDuration, Object handler, Exception exception){

        //记录当前用户
        OpusLoginUser loginUser = null;
        try{
            loginUser = SecurityContext.getOpusLoginUser();
        }catch (Exception e){
            logger.error("======== 此处已经报错,错误信息：=========" + e.getMessage());
        }
        if (loginUser != null) {
            AeaTraceLog log = new AeaTraceLog();
            log.setLogId(UuidUtil.generateUuid());
            log.setLogTitle(TraceLogInterceptor.class.getSimpleName());
            log.setLogLevel("INFO");
            log.setLogTime(new Date());
            log.setLogThread("[CURR_THREAD]");
            log.setOperationMethod("");
            log.setOperationDuration(operationDuration);

            //获取访问地址
            log.setRequestPath(request.getRequestURI());
            log.setRemoteAddr(HttpUtils.getRemoteIP(request));
            log.setUserAgent(request.getHeader("user-agent"));
            log.setRequestParams(log.convertParams(request.getParameterMap()));
            log.setRequestMethod(request.getMethod());

            //获取action对象名称和方法名称
            if(handler instanceof HandlerMethod){
                HandlerMethod h = (HandlerMethod) handler;
                String name = h.getBean().getClass().getName();
                if(StringUtils.isNotBlank(name)){
                    int index = name.indexOf("$$");
                    if(index>-1){
                      name = name.substring(0, index);
                    }
                }
                log.setOperationClass(name);
                log.setOperationMethod(h.getMethod().getName());
            }

            //获取request调用描述
            String operationDesc = "调用Controller对象 [" + log.getOperationClass() + "] 的 ["+log.getOperationMethod() + "] 方法";
            log.setOperationDesc(operationDesc);

            //当异常对象不为空时，记录异常信息
            log.setLogMessage(getLogMessage(exception));
            log.setLogException(getLogException(exception));

            //记录当前访问的应用功能、模块、应用、所属部门、岗位
            log.setOperationApp(environment.getProperty("spring.application.name"));
            log.setOperationFunc(null);
            log.setOperationModule(null);
            String orgId = loginUser.getCurrentOrgId();
            OpuOmOrg org = orgMapper.getOrg(orgId);
            log.setRootOrgId(orgId);
            log.setOrgId(org==null?null:org.getOrgId());
            log.setOrgName(org==null?null:org.getOrgName());
            log.setPosId(null);
            log.setPosName(null);
            OpuOmUser user = loginUser.getUser();
            log.setUserId(user==null?null:user.getUserId());
            log.setUserName(user==null?null:user.getLoginName());
            // 保存日志
            logService.saveAeaTraceLog(log);
        }
    }

    /**
     * 获取异常的概要异常信息
     * @param e
     * @return
     */
    private String getLogMessage(Exception e){

        if(e != null){
            String msg = e.toString();
            return msg.length() > 900? (msg.substring(0, 900) + "...") : msg;
        }else {
            return null;
        }
    }

    /**
     * 获取异常的最详细的异常信息
     * @param e
     * @return
     */
    public static String getLogException(Exception e){

        if(e != null){
            StackTraceElement[] stacks = e.getStackTrace();
            if(stacks != null && stacks.length > 0){
                String result = stacks[0].toString();
                for(int i=1; i<stacks.length; i++){
                    result += "\r\n" + stacks[i].toString();
                }
                return result;
            }
        }
        return null;
    }
}
