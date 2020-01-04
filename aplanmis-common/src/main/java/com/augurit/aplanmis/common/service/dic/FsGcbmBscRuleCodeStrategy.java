package com.augurit.aplanmis.common.service.dic;

import com.augurit.agcloud.bsc.domain.BscRuleCode;
import com.augurit.agcloud.bsc.mapper.BscRuleCodeMapper;
import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.util.CollectionUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.bridge.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

/**
 * 佛山项目编码生成工具类
 */
@Service
@Transactional
@Slf4j
public class FsGcbmBscRuleCodeStrategy {

    @Autowired
    protected BscRuleCodeMapper bscRuleCodeMapper;
    @Autowired
    GcbmBscRuleCodeStrategy bscRuleCodeStrategy;

    public static final String TEMPLATE_STR = "{value}";
    //(立项类型，来自于数据字典，包括：审批、核准、备案)
    public static final String PORJ_TYPE = "{projType}";
    //邮编
    public static final String POST_CODE = "{postCode}";
    private static final long CODE_VALUE_INCREASE = 1L;
    private static final long CODE_VALUE_COUNT = 6L;
    private static final String CODE_NAME = "佛山项目代码生成规则";

    public static Configuration cfg = new Configuration(Configuration.getVersion());


        /**
         * 递增值 默认: 1
         *
         * @return 递增值
         */
    protected long codeValueIncrease() {
        return CODE_VALUE_INCREASE;
    }

    public String buildCodeName() {
        return CODE_NAME;
    }

    public long buildCodeValueCount() {
        return CODE_VALUE_COUNT;
    }

    public String buildCodeTemplate() {
        return "FS-"+String.valueOf(Calendar.getInstance().get(Calendar.YEAR))+"-"+POST_CODE+"-"+PORJ_TYPE+"-"+TEMPLATE_STR;
    }

    public String buildCodeMemo(String codeMemoFactor) {
        return codeMemoFactor;
    }


    /**
     *
     * @param projType 立项类型
     * @param postCode 邮编
     * @param orgId
     * @return
     */
    public synchronized String generateCode(String projType,String postCode, String orgId) {
        // 确认编码规则参数之一(当前年份，每年流水号重置，既利用codeIc新生成一条规则)
        String codeIc = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        //确认编码规则参数之一(本方法值为:佛山项目代码生成规则)
        String codeMemo = buildCodeMemo(CODE_NAME);
        String code = "";
;        if (log.isInfoEnabled()) {
            log.info("Generate Code, codeIc: {}, codeMemo: {}, orgId: {}", codeIc, codeMemo, orgId);
        }

        long currentValue;
        //获得规则
        BscRuleCode bscRuleCode = bscRuleCodeStrategy.getBscRuleCode(codeIc, orgId, codeMemo);
        // 规则编码为空，需要新增一条
        if (Objects.isNull(bscRuleCode)) {
            bscRuleCode = this.createBscRuleCode(codeIc, codeMemo, orgId);
        }
        // 获取当前值并更新
        currentValue = bscRuleCode.getCodeValueNext();
        long codeValueNext = currentValue + codeValueIncrease();
        long codeValueCount = buildCodeValueCount();
        if (("" + codeValueNext).length() > codeValueCount) {
            throw new RuntimeException("Execeed codeValueCount: codeValueNext: " + codeValueNext);
        }
        bscRuleCode.setCodeValueNext(codeValueNext);
        bscRuleCodeMapper.updateBscRuleCode(bscRuleCode);

        Template template = null;
        try {
            Map map = new HashMap();
            map.put("postCode", postCode);
            map.put("projType", projType);
            map.put("value", String.format("%0" + codeValueCount + "d", currentValue));
            template = new Template("strTpl", bscRuleCode.getCodeTemplate(),cfg);
            StringWriter result = new StringWriter();
            template.process(map, result);
            code = result.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        return code;
    }

    public BscRuleCode createBscRuleCode(String codeIc, String codeMemo, String orgId) {
        BscRuleCode bscRuleCode = new BscRuleCode();
        bscRuleCode.setCodeId(UuidUtil.generateUuid());
        bscRuleCode.setCodeIc(codeIc);
        bscRuleCode.setCodeName(buildCodeName());
        bscRuleCode.setCodeValueNext(1L);
        bscRuleCode.setCodeValueIncrease(codeValueIncrease());
        bscRuleCode.setCodeValueCharCount(buildCodeValueCount());
        bscRuleCode.setCodeTemplate(buildCodeTemplate());
        bscRuleCode.setCodeMemo(codeMemo);
        bscRuleCode.setOrgId(orgId);
        bscRuleCodeMapper.insertBscRuleCode(bscRuleCode);
        return bscRuleCode;
    }


    public static void main(String[] args){
        try {
            FsGcbmBscRuleCodeStrategy fs = new FsGcbmBscRuleCodeStrategy();
            Map map = new HashMap();
            map.put("name", "张三");
            map.put("money", 10.155);
            map.put("value", 10);
            Template template = new Template("strTpl", "${name}通过${value}天的努力挣了${money}",cfg);
            StringWriter result = new StringWriter();
            template.process(map, result);
            System.out.println(result.toString());
            String value = "2";
            System.out.println(String.format("%07d", 1));
            System.out.println(fs.generateCode("01","000001","1"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
