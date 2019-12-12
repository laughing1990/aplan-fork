package com.augurit.aplanmis.front.apply.controller;

import com.augurit.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@DisplayName("申报接口测试")
class RestApplyCotrollerTest extends BaseControllerTest {

    @Test
    @DisplayName("回显")
    void unstash() throws Exception {
        String applyinstId = "ba41f5e5-c8f0-4f50-af0b-0ff24c3bf352";
        mockMvc.perform(MockMvcRequestBuilders.get("/rest/apply/unstash").param("applyinstId", applyinstId)
                .contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.success").value("true"));
    }
}