package com.augurit.aplanmis.front.basis.stage.controller;

import base.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@DisplayName("申报页表单查询测试")
class RestStageControllerTest extends BaseTest {

    @Test
    @DisplayName("查询阶段扩展表单")
    void listAeaParStagePartformsByStageId() throws Exception {
        String stageId = "f39985ed-9119-444f-b744-4167762a3872";
        mockMvc.perform(MockMvcRequestBuilders.get("/rest/stage/part/forms").param("stageId", stageId)
                .contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.content.length()").value(3))

        ;
    }

    @Test
    @DisplayName("事项表单")
    void listAeaItemPartformsByItemVerIds() throws Exception {
        String itemVerIds = "b5080b8d-9c6e-40dd-a09f-fdcc9630308a, 015b5ac9-04b6-4473-829e-1064e58dffd2";
        mockMvc.perform(MockMvcRequestBuilders.get("/rest/stage/item/part/forms").param("itemVerIds", itemVerIds)
                .contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.content.length()").value(2))
        ;
    }
}