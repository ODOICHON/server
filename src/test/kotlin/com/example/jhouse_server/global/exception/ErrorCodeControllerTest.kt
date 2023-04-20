package com.example.jhouse_server.global.exception

import com.example.jhouse_server.global.util.ApiControllerConfig
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation.beneathPath
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadSubsectionExtractor
import org.springframework.restdocs.snippet.Attributes.attributes
import org.springframework.restdocs.snippet.Attributes.key
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

class ErrorCodeControllerTest : ApiControllerConfig(uri = "/api/v1/errors") {

    @Test
    @DisplayName("에러 코드 문서화")
    fun errorCode() {
        val result = this.mockMvc.perform(get("$uri")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andDo(document("error-code",
                codeResponseFields("code-response", beneathPath("data.errorCodes").withSubsectionId("code"),
                attributes(key("title").value("에러 코드")),
                enumConvertFieldDescriptor(ErrorCode.values()))))
    }

    private fun enumConvertFieldDescriptor(errorCodes: Array<ErrorCode>): Array<FieldDescriptor> {
        return errorCodes
            .map { fieldWithPath(it.code).description(it.message) }
            .toTypedArray()
    }

    private fun codeResponseFields(
        type: String,
        subsectionExtractor: PayloadSubsectionExtractor<*>?,
        attributes: Map<String, Any>,
        descriptior: Array<FieldDescriptor>
    ): CodeResponseFieldSnippet {
        return CodeResponseFieldSnippet(type, subsectionExtractor, descriptior.toMutableList(), attributes.toMutableMap(), true)
    }
}