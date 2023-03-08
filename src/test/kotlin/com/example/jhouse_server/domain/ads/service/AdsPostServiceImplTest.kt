package com.example.jhouse_server.domain.ads.service

import com.example.jhouse_server.domain.ads.repository.AdsPostRepository
import com.example.jhouse_server.domain.user.UserSignInReqDto
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.domain.user.repository.UserRepository
import com.example.jhouse_server.domain.user.service.UserService
import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode
import com.example.jhouse_server.global.response.ApplicationResponse
import com.example.jhouse_server.global.util.MockEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Assertions.assertThrows

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
internal class AdsPostServiceImplTest @Autowired constructor(
    val userRepository: UserRepository,
    val adsPostService: AdsPostService,
    val userService: UserService,
    val adsPostRepository: AdsPostRepository
){
    private val userSignUpDto = MockEntity.testUserSignUpDto()
    private var adPostDummyList : MutableList<Long> = mutableListOf()
    private lateinit var user : User
    @BeforeEach
    fun saveDummy() {
        userService.signUp(userSignUpDto)
        userService.signIn(UserSignInReqDto(userSignUpDto.email, userSignUpDto.password))
        user = userRepository.findByEmail(userSignUpDto.email).get()
        val req = MockEntity.testAdsPostDto()
        val trueId = adsPostService.createPost(req, user)
        val reqFalse = MockEntity.testAdsTmpPostDto()
        val falseId = adsPostService.createPost(reqFalse, user)
        adPostDummyList.add(trueId)
        adPostDummyList.add(falseId)
    }

    @AfterEach
    fun flushDummy() {
        userRepository.delete(userRepository.findByEmail(userSignUpDto.email).get())
        adsPostRepository.deleteAllById(adPostDummyList)
        adPostDummyList.clear()
    }

    @Test
    @DisplayName("홍보 게시글 전체 조회")
    fun getPostAll() {
        // given
        val pageable = PageRequest.of(0, 10)
        // when
        val res = adsPostService.getPostAll(pageable)
        // then
        assertThat(res.content[1].code).isNotEqualTo(MockEntity.testAdsTmpPostDto().code)
    }

    @Test
    @DisplayName("홍보 게시글 수정")
    fun updatePost() {
        // given
        val adPostTrueId = adPostDummyList[0]
        val req = MockEntity.testAdsPostUpdateDto()
        // when
        adsPostService.updatePost(adPostTrueId, req, user)
        // then
        assertThat(adsPostRepository.findById(adPostTrueId).get().title).isEqualTo(req.title)
    }

    @Test
    @DisplayName("홍보 게시글 생성")
    fun createPost() {
        // given
        val req = MockEntity.testAdsPostDto()
        // when
        val res = adsPostService.createPost(req, user)
        // then
        assertThat(adsPostRepository.findById(res).get().title).isEqualTo(req.title)
    }

    @Test
    @DisplayName("홍보 게시글 검색")
    fun getPostAllByKeywordCustom() {
        // given
        val keyword = "안녕"
        val pageable = PageRequest.of(0, 10)
        // when
        val res = adsPostService.getPostAllByKeywordCustom(keyword, pageable)
        // then
        assertThat(res.isEmpty).isFalse
    }

    @Test
    @DisplayName("홍보 게시글 고정_일반 유저")
    fun fixPost() {
        // given
        val adPostTrueId = adPostDummyList[0]
        // when
        val exception = assertThrows(ApplicationException::class.java) {
            adsPostService.fixPost(adPostTrueId, user)
        }
        // then
        assertEquals(exception.errorCode, ErrorCode.UNAUTHORIZED_EXCEPTION)
    }
    @Test
    @DisplayName("홍보 게시글 고정_관리자 유저")
    fun fixPostByAdmin() {
        // given
        val admin = userRepository.save(MockEntity.testAdmin())
        val adPostTrueId = adPostDummyList[0]
        // when
        val res = adsPostService.fixPost(adPostTrueId, admin)
        // then
        assertThat(adsPostRepository.findById(res).get().isFixed).isTrue
        userRepository.delete(admin)
    }
}