<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ page import="java.util.Date" %>
<%@ page import="org.springframework.context.ApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="com.augurit.agcloud.framework.config.FrameworkUiProperties" %>
<%@ page import="com.augurit.agcloud.framework.util.StringUtils" %>
<%@ page import="com.augurit.agcloud.framework.security.SecurityContext" %>
<%@ page import="freemarker.core.Environment" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="com.augurit.agcloud.agx.common.service.AgxPtlSchemeService" %>
<%@ page import="com.augurit.agcloud.agx.common.service.impl.AgxPtlSchemeServiceImpl" %>
<%@ page import="com.augurit.agcloud.agx.common.service.impl.AgxPtlHomepageServiceImpl" %>
<%@ page import="com.augurit.agcloud.agx.common.service.AgxPtlHomepageService" %>
<%@ page import="com.augurit.agcloud.agx.common.service.AgxPtlThemeService" %>
<%@ page import="com.augurit.agcloud.agx.common.service.impl.AgxPtlThemeServiceImpl" %>
<%@ page import="com.augurit.agcloud.opus.common.service.ptl.service.impl.OpuPtlSchemeOrgServiceImpl" %>
<%@ page import="com.augurit.agcloud.opus.common.service.ptl.service.OpuPtlSchemeOrgService" %>
<%@ page import="com.augurit.agcloud.agx.common.domain.AgxPtlScheme" %>
<%@ page import="com.augurit.agcloud.agx.common.domain.AgxPtlHomepage" %>
<%@ page import="com.augurit.agcloud.agx.common.domain.AgxPtlTheme" %>
<meta charset="utf-8" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1" name="viewport"/>

<%
     Logger logger = LoggerFactory.getLogger(this.getClass());
     long s=new Date().getTime();
    //获取平台配置
    ServletContext context = request.getSession().getServletContext();
    ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);
    FrameworkUiProperties uiProperties = (FrameworkUiProperties)ctx.getBean(StringUtils.replaceFirstCharToLower(FrameworkUiProperties.class.getSimpleName()));

    String tmnId="1";  //目前写死 1为pc  2为手机
    if(StringUtils.isNotBlank(request.getParameter("tmdId")) )tmnId=request.getParameter("tmdId");
    //用户登录之后,由每个项目自行判断 再根据请求
    //根据ORGiD确定 方案 scheme
    //根据方案以及TMN确定 首页 homepage
    //从首页获取主题theme
    AgxPtlSchemeService AgxPtlSchemeService=(AgxPtlSchemeService)ctx.getBean(StringUtils.replaceFirstCharToLower(AgxPtlSchemeServiceImpl.class.getSimpleName()));
    AgxPtlHomepageService opuPtlHomepageService=(AgxPtlHomepageService)ctx.getBean(StringUtils.replaceFirstCharToLower(AgxPtlHomepageServiceImpl.class.getSimpleName()));
    AgxPtlThemeService opuPtlThemeService=(AgxPtlThemeService)ctx.getBean(StringUtils.replaceFirstCharToLower(AgxPtlThemeServiceImpl.class.getSimpleName()));
    OpuPtlSchemeOrgService OpuPtlSchemeOrgService=(OpuPtlSchemeOrgService)ctx.getBean(StringUtils.replaceFirstCharToLower(OpuPtlSchemeOrgServiceImpl.class.getSimpleName()));

    AgxPtlScheme currentScheme = new AgxPtlScheme();
    AgxPtlHomepage currentHomePage = new AgxPtlHomepage();
    AgxPtlTheme currentTheme = new AgxPtlTheme();
    org.springframework.core.env.Environment env=ctx.getEnvironment();
    String orgId = StringUtils.isBlank(SecurityContext.getCurrentOrgId())?"A":SecurityContext.getCurrentOrgId();
    currentScheme = OpuPtlSchemeOrgService.getActiveOnlySchemeByOrg(orgId);
    if(currentScheme!=null){
        long s1=new Date().getTime();
        if(StringUtils.isNotBlank(currentScheme.getSchemeId())){
            currentHomePage = opuPtlHomepageService.getAgxPtlHomepageBySchemeAndTmn(tmnId,currentScheme.getSchemeId());
        }
        long s2=new Date().getTime();
        if(currentHomePage != null&&StringUtils.isNotBlank(currentHomePage.getHomepageId())){
            currentTheme = opuPtlThemeService.getAgxPtlThemeBySchemeAndHomePage(currentScheme.getSchemeId(),currentHomePage.getHomepageId());
        }
        long s3 = new Date().getTime();
        if(currentHomePage!= null) {
            String homePageFilePath = currentHomePage.getHomepagePath();
        }
    }
    request.setAttribute("currentTheme",currentTheme);
    if(currentTheme!=null){
        request.setAttribute("currentCssPath",env.getProperty("currentCssPath") + currentTheme.getThemeId());
        request.setAttribute("currentCssPathNew",env.getProperty("portalcss.root") );
        request.setAttribute("currentThemeCode",currentTheme.getThemeCode());
    }

    //是否调试模式运行
//    boolean debug = true; //uiProperties.isDebug();
    boolean debug = uiProperties.isDebug();
    String isDebugMode = debug ? "date="+new Date().getTime() : "1=1";
    long ell=new Date().getTime();
    logger.debug("totaltimes:"+Long.valueOf(ell-s).toString());
%>

<!-- JS全局变量 -->
<script type="text/javascript">
    var ctx = "${pageContext.request.contextPath}";
</script>

<!-- jquery and AgCloud Public JS -->
<script src="${pageContext.request.contextPath}/ui-static/agcloud/framework/js-libs/jquery1/jquery.min.js?<%=isDebugMode%>" type="text/javascript"></script>