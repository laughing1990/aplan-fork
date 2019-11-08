package com.augurit.aplanmis.front.subject.linkman.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel("保存联系人或者申报人vo")
@Validated
public class PersonalInfoListVo {
    @ApiModelProperty(value = "申报人或联系人数组", required = true, dataType = "java.util.List")
    @NotNull(message = "personalInfos is null")
    private List<PersonalInfoVo> personalInfos;
}
