package com.augurit.aplanmis.common.constants;

import com.augurit.aplanmis.common.handler.BaseEnum;
import lombok.Getter;

/**
 * @author ZhangXinhui
 * @date 2019/8/5 005 15:11
 * @desc
 **/
@Getter
public enum PublishStatus implements BaseEnum<PublishStatus, String> {

    UNPUBLISHED("未发布", "0"),
    PUBLISHED("已发布", "1"),
    TEST_RUN("试运行", "2"),
    DEPRECATED("已过时", "3");

    private String name;
    private String value;

    PublishStatus(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
