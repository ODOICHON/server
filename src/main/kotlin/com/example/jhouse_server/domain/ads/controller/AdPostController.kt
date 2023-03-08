package com.example.jhouse_server.domain.ads.controller

import com.example.jhouse_server.domain.ads.dto.AdsPostCreateReqDto
import com.example.jhouse_server.domain.ads.dto.AdsPostListResDto
import com.example.jhouse_server.domain.ads.dto.AdsPostResDto
import com.example.jhouse_server.domain.ads.dto.AdsPostUpdateReqDto
import com.example.jhouse_server.domain.ads.service.AdsPostService
import com.example.jhouse_server.domain.post.dto.CodeResDto
import com.example.jhouse_server.domain.user.entity.Authority
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.annotation.Auth
import com.example.jhouse_server.global.annotation.AuthUser
import com.example.jhouse_server.global.response.ApplicationResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/posts/ads")
class AdPostController(
    val adsPostService: AdsPostService
) {
    /**
     * 홍보게시글 생성
     *
     * @author 이은비
     * */
    @Auth
    @PostMapping
    fun createPost(
        @RequestBody @Validated req : AdsPostCreateReqDto,
        @AuthUser user : User
    ) : ApplicationResponse<Long> {
        return ApplicationResponse.ok(adsPostService.createPost(req, user))
    }
    /**
     * 홍보게시글 수정 ( 임시저장 상태도 해당 API로 수정 )
     *
     * @author 이은비
     * */
    @Auth
    @PutMapping("/{postId}")
    fun updatePost(
        @PathVariable postId : Long,
        @RequestBody @Validated req: AdsPostUpdateReqDto,
        @AuthUser user: User
    ) : ApplicationResponse<Long> {
        return ApplicationResponse.ok(adsPostService.updatePost(postId, req, user))
    }
    /**
     * 홍보게시글 삭제 ( soft delete 적용 )
     *
     * @author 이은비
     * */
    @Auth
    @DeleteMapping("/{postId}")
    fun deletePost(
        @PathVariable postId : Long,
        @AuthUser user: User
    ) : ApplicationResponse<Nothing> {
        adsPostService.deletePost(postId, user)
        return ApplicationResponse.ok()
    }
    /**
     * 홍보게시글 전체 조회 ( 페이징 처리 )
     *
     * @author 이은비
     * */
    @GetMapping
    fun getPostAll(pageable: Pageable) :ApplicationResponse<Page<AdsPostResDto>> {
        return ApplicationResponse.ok(adsPostService.getPostAll(pageable))
    }
    /**
     * 홍보게시글 상세 조회
     *
     * @author 이은비
     * */
    @GetMapping("/{postId}")
    fun getPostOne(
        @PathVariable postId : Long
    ) : ApplicationResponse<AdsPostResDto> {
        return ApplicationResponse.ok(adsPostService.getPostOne(postId))
    }
    /**
     * 홍보게시글 검색 ( 페이징 처리 )
     *
     * @author 이은비
     * */
    @GetMapping("/search")
    fun getPostAllCustom(
        @RequestParam keyword: String,
        pageable: Pageable
    ) : ApplicationResponse<Page<AdsPostListResDto>> {
        return ApplicationResponse.ok(adsPostService.getPostAllByKeywordCustom(keyword, pageable))
    }
    /**
     * 홍보게시글 말머리 조회 ( 영문코드, 코드명 )
     *
     * @author 이은비
     * */
    @GetMapping("/category")
    fun getPostCategory() : ApplicationResponse<List<CodeResDto>> {
        return ApplicationResponse.ok(adsPostService.getPostCategory())
    }
    /**
     * 홍보게시글 좋아요 ( 작성자 포함 좋아요 클릭 가능 )
     *
     * @author 이은비
     * */
    @Auth
    @PutMapping("/love/{postId}")
    fun updatePostLove(
        @PathVariable postId: Long,
        @AuthUser user: User
    ) : ApplicationResponse<Long> {
        return ApplicationResponse.ok(adsPostService.updatePostLove(postId, user))
    }
    /**
     * 유저가 갖는 임시저장된 홍보게시글 목록 조회 ( 페이징 처리 )
     *
     * @author 이은비
     * */
    @Auth
    @GetMapping("/temporary")
    fun getTemporaryPostList(
        @AuthUser user: User,
        pageable: Pageable
    ) : ApplicationResponse<Page<AdsPostResDto>> {
        return ApplicationResponse.ok(adsPostService.getTemporaryPostList(user, pageable))
    }

    /**
     * 관라자가 홍보 게시글을 상단에 고정
     *
     * @author 이은비
     * */
    @Auth(Authority.ADMIN)
    @PutMapping("/fix/{postId}")
    fun fixPost(
        @AuthUser user: User,
        @PathVariable postId: Long
    ) : ApplicationResponse<Long> {
        return ApplicationResponse.ok(adsPostService.fixPost(postId, user))
    }
}