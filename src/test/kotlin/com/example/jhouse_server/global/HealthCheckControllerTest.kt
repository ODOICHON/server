package com.example.jhouse_server.global

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(HealthCheckController::class)
@AutoConfigureRestDocs
class HealthCheckControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    @DisplayName("WAS 서버 Health-Check")
    fun healthCheck() {
        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/")
        )
                .andExpect(status().isOk)
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("index"))
    }
}