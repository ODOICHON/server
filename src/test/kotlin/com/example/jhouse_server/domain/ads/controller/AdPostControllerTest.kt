package com.example.jhouse_server.domain.ads.controller

import com.example.jhouse_server.domain.ads.service.AdsPostService
import com.example.jhouse_server.domain.user.repository.UserRepository
import com.example.jhouse_server.domain.user.service.UserService
import com.example.jhouse_server.global.jwt.TokenDto
import com.example.jhouse_server.global.jwt.TokenProvider
import com.example.jhouse_server.global.util.ApiControllerConfig
import com.example.jhouse_server.global.util.MockEntity
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class AdPostControllerTest @Autowired constructor(
    val tokenProvider: TokenProvider,
    val adPostService: AdsPostService,
    val userService: UserService,
    val userRepository: UserRepository,

) : ApiControllerConfig(uri = "/api/v1/posts/ads"){
    private val userSignUpDto = MockEntity.testUser1()
    private val userSignInDto = MockEntity.testUser1SignIn()
    private lateinit var tokenDto : TokenDto
    private var adPost : Long = 0

    fun `로그인`() {
        if(!userRepository.existsByEmail(userSignUpDto.email)) {
            userService.signUp(userSignUpDto)
        }
        tokenDto = userService.signIn(userSignInDto)
    }

    @BeforeAll
    fun `홍보 게시글 더미 데이터 생성`() {
        로그인()
        val user = userRepository.findByEmail(userSignInDto.email)
        adPost = adPostService.createPost(MockEntity.testAdsPostDto(), user.get())
    }

    @Test
    @DisplayName("홍보게시글 조회")
    fun `홍보게시글 조회`() {
        // given
        val uri = "$uri"
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )
        resultActions.andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document("ads-post-get-all",
                responseFields(
                    beneathPath("data").withSubsectionId("data"),
                    *postPageResponseFieldSnippet())
                )
            )
    }

    @Test
    @DisplayName("홍보게시글 생성")
    fun createPost() {
        // given
        val req = MockEntity.testAdsPostDto()
        val uri = "$uri"

        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post(uri)
                .header(AUTHORIZATION, tokenDto.accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(req))
                .characterEncoding("UTF-8")
        )
        resultActions
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "ads-post-create",
                    requestFields(*postCreateRequestFieldSnippet()),
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("결과 메세지"),
                        fieldWithPath("data").description("결과 데이터")
                    )
                )
            )
    }

    @Test
    @DisplayName("홍보 게시글 수정")
    fun updatePost() {
        // given
        val req = MockEntity.testAdsPostDto()
        val adPostId = adPost
        val uri = "$uri/{adPostId}"

        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .put(uri, adPostId)
                .header(AUTHORIZATION, tokenDto.accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(req))
                .characterEncoding("UTF-8")
        )
        resultActions
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "ads-post-update",
                    requestFields(*postCreateRequestFieldSnippet()),
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("결과 메세지"),
                        fieldWithPath("data").description("결과 데이터")
                    )
                )
            )
    }

    @Test
    @DisplayName("홍보 게시글 삭제")
    fun deletePost() {
        //given
        val adPostId = adPost
        val uri = "$uri/{adPostId}"
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(uri, adPostId)
                .header(AUTHORIZATION, tokenDto.accessToken)
        )
        resultActions
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "ads-post-delete",
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("결과 메세지"),
                    )
                )
            )
    }

    @Test
    @DisplayName("홍보 게시글 상세 조회")
    fun getPostOne() {
        val adPostId = adPost
        val uri = "$uri/{adPostId}"
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(uri, adPostId)
                .header(AUTHORIZATION, tokenDto.accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )
        resultActions
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "ads-post-get-one",
                    responseFields(
                        beneathPath("data").withSubsectionId("data"),
                        *postResponseFieldSnippet()
                    )
                )
            )
    }

    @Test
    @DisplayName("홍보 게시글 검색")
    fun getPostAllCustom() {
        val keyword = "짱구"
        val uri = "$uri/search?keyword="
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(uri, keyword)
                .header(AUTHORIZATION, tokenDto.accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )
        resultActions
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "ads-post-search-all",
                    responseFields(
                        beneathPath("data").withSubsectionId("data"),
                        *postListResponseFieldSnippet()
                    )
                )
            )
    }

    @Test
    @DisplayName("홍보 게시글 말머리 조회")
    fun getPostCategory() {
        val uri = "$uri/category"
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(uri)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )
        resultActions
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "ads-post-category",
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("결과 메세지"),
                        fieldWithPath("data[].code").description("영문 코드"),
                        fieldWithPath("data[].name").description("한글 코드명")
                    )
                )
            )
    }

    @Test
    @DisplayName("홍보 게시글 좋아요")
    fun updatePostLove() {
        val adPostId = adPost
        val uri = "$uri/love/{adPostId}"
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .put(uri, adPostId)
                .header(AUTHORIZATION, tokenDto.accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )
        resultActions
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "ads-post-love",
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("결과 메세지"),
                        fieldWithPath("data").description("결과 데이터")
                    )
                )
            )
    }

    @Test
    @DisplayName("유저가 갖는 임시 저장된 홍보 게시글 목록 조회")
    fun getTemporaryPostList() {
        val user = userRepository.findByEmail(userSignInDto.email)
        adPostService.createPost(MockEntity.testAdsTmpPostDto(), user.get())
        val uri = "$uri/temporary"
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(uri)
                .header(AUTHORIZATION, tokenDto.accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )
        resultActions.andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document("ads-post-get-temporary",
                    responseFields(
                        beneathPath("data").withSubsectionId("data"),
                        *postPageResponseFieldSnippet())
                )
            )
    }

    private fun postCreateRequestFieldSnippet() : Array<FieldDescriptor> {
        return arrayOf(
            fieldWithPath("code").description("DB에 저장할 html 정적 코드"),
            fieldWithPath("title").description("게시글 제목"),
            fieldWithPath("imageUrls").description("이미지 url ',' 구분자로 넘겨주기 "),
            fieldWithPath("saved").description("임시저장여부, 임시저장 false, 저장 true"),
            fieldWithPath("category").description("게시글 말머리"),
        )
    }
    private fun postPageResponseFieldSnippet() : Array<FieldDescriptor> {
        return arrayOf(
            fieldWithPath("content[].postId").description("홍보 게시글 ID"),
            fieldWithPath("content[].code").description("HTML 정적 코드"),
            fieldWithPath("content[].isFixed").description("상단 고정 여부"),
            fieldWithPath("content[].love").description("좋아요 수"),
            fieldWithPath("pageable.sort.empty").description(""),
            fieldWithPath("pageable.sort.unsorted").description(""),
            fieldWithPath("pageable.sort.sorted").description(""),
            fieldWithPath("pageable.offset").description(""),
            fieldWithPath("pageable.pageNumber").description(""),
            fieldWithPath("pageable.pageSize").description(""),
            fieldWithPath("pageable.unpaged").description(""),
            fieldWithPath("pageable.paged").description(""),
            fieldWithPath("last").description(""),
            fieldWithPath("totalPages").description(""),
            fieldWithPath("totalElements").description(""),
            fieldWithPath("size").description(""),
            fieldWithPath("number").description(""),
            fieldWithPath("sort.empty").description(""),
            fieldWithPath("sort.unsorted").description(""),
            fieldWithPath("sort.sorted").description(""),
            fieldWithPath("first").description(""),
            fieldWithPath("numberOfElements").description(""),
            fieldWithPath("empty").description(""),
        )
    }

    private fun postResponseFieldSnippet() : Array<FieldDescriptor> {
        return arrayOf(
            fieldWithPath("postId").description("홍보 게시글 ID"),
            fieldWithPath("code").description("HTML 정적 코드"),
            fieldWithPath("isFixed").description("상단 고정 여부"),
            fieldWithPath("love").description("좋아요 수"),
        )
    }

    private fun postListResponseFieldSnippet() : Array<FieldDescriptor> {
        return arrayOf(
            fieldWithPath("content[].postId").description("홍보 게시글 ID"),
            fieldWithPath("content[].isFixed").description("상단 고정 여부"),
            fieldWithPath("content[].title").description("게시글 제목"),
            fieldWithPath("content[].oneLineContent").description("한줄 미리보기"),
            fieldWithPath("content[].commentCount").description("댓글 개수"),
            fieldWithPath("content[].love").description("좋아요 수"),
            fieldWithPath("content[].nickname").description("게시글 작성자 닉네임"),
            fieldWithPath("content[].createdAt").description("게시글 생성 일자"),
            fieldWithPath("content[].imageUrl").description("게시글 대표 사진"),
            fieldWithPath("pageable.sort.empty").description(""),
            fieldWithPath("pageable.sort.unsorted").description(""),
            fieldWithPath("pageable.sort.sorted").description(""),
            fieldWithPath("pageable.offset").description(""),
            fieldWithPath("pageable.pageNumber").description(""),
            fieldWithPath("pageable.pageSize").description(""),
            fieldWithPath("pageable.unpaged").description(""),
            fieldWithPath("pageable.paged").description(""),
            fieldWithPath("last").description(""),
            fieldWithPath("totalPages").description(""),
            fieldWithPath("totalElements").description(""),
            fieldWithPath("size").description(""),
            fieldWithPath("number").description(""),
            fieldWithPath("sort.empty").description(""),
            fieldWithPath("sort.unsorted").description(""),
            fieldWithPath("sort.sorted").description(""),
            fieldWithPath("first").description(""),
            fieldWithPath("numberOfElements").description(""),
            fieldWithPath("empty").description(""),
        )
    }
}