package com.augurit.aplanmis.mall.cert.vo;

import com.augurit.aplanmis.common.domain.AeaCert;
import com.augurit.aplanmis.common.domain.AeaCertType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Getter
@Setter
@ApiModel("电子证照VO")
public class AeaCertVo extends AeaCertType {

    @ApiModelProperty(value = "电子证照列表")
    private List<AeaCert> certList;

    public static AeaCertVo forMat(AeaCertType aeaCertType){
        AeaCertVo aeaCertVo=new AeaCertVo();
        BeanUtils.copyProperties(aeaCertType,aeaCertVo);
        return aeaCertVo;
    }
}
