package com.augurit.aplanmis.common.service.dic;

import com.augurit.agcloud.bsc.domain.BscRuleCode;
import com.augurit.agcloud.bsc.mapper.BscRuleCodeMapper;
import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@Slf4j
public abstract class BscRuleCodeStrategy {

    public static final String TEMPLATE_STR = "{value}";
    private static final long CODE_VALUE_INCREASE = 1L;

    @Autowired
    protected BscRuleCodeMapper bscRuleCodeMapper;

    /**
     * 递增值 默认: 1
     *
     * @return 递增值
     */
    protected long codeValueIncrease() {
        return CODE_VALUE_INCREASE;
    }

    /**
     * 规则编码名称
     *
     * @return 名称
     */
    public abstract String buildCodeName();

    /**
     * 值字符数，不足的补0
     */
    public abstract long buildCodeValueCount();

    /**
     * 编号全文本模板
     */
    public abstract String buildCodeTemplate();

    /**
     * 备注，不同类型的流水号可用作其它用途
     *
     * @param codeMemoFactor 生成备注因子
     * @return 规则编码备注
     */
    public abstract String buildCodeMemo(String codeMemoFactor);

    /**
     * 模板方法
     * 需要加锁， 解决并发更新可能出现的序号重复问题
     *
     * @return 返回最终生成的编号
     */
    public synchronized String generateCode(String prefix, String codeIc, String codeMemoFactor, String orgId) {
        String codeMemo = buildCodeMemo(codeMemoFactor);

        if (log.isInfoEnabled()) {
            log.info("Generate Code, codeIc: {}, codeMemo: {}, orgId: {}", codeIc, codeMemo, orgId);
        }

        long currentValue;
        BscRuleCode bscRuleCode = getBscRuleCode(codeIc, orgId, codeMemo);
        // 规则编码为空，需要新增一条
        if (Objects.isNull(bscRuleCode)) {
            bscRuleCode = createBscRuleCode(codeIc, codeMemo, orgId);
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

        String template = bscRuleCode.getCodeTemplate();
        String sequence = template.replace(TEMPLATE_STR, String.format("%0" + codeValueCount + "d", currentValue));
        return (StringUtils.isBlank(prefix) ? "" : prefix) + sequence;
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

    private BscRuleCode getBscRuleCode(String codeIc, String orgId, String codeMemo) {
        BscRuleCode bscRuleCode = new BscRuleCode();
        bscRuleCode.setCodeIc(codeIc);
        bscRuleCode.setCodeMemo(codeMemo);
        bscRuleCode.setOrgId(orgId);
        List<BscRuleCode> bscRuleCodes = bscRuleCodeMapper.listBscRuleCode(bscRuleCode);
        if (CollectionUtils.isNotEmpty(bscRuleCodes)) {
            if (bscRuleCodes.size() != 1) {
                log.warn("BscRuleCode not unique, please check， codeIc: {}, codeMemo: {}", codeIc, codeMemo);
            }
            return bscRuleCodes.get(0);
        }
        return null;
    }
}
