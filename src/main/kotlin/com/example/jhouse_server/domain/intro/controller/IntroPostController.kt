package com.example.jhouse_server.domain.intro.controller

import com.example.jhouse_server.domain.intro.dto.IntroPostCreateReqDto
import com.example.jhouse_server.domain.intro.dto.IntroPostListResDto
import com.example.jhouse_server.domain.intro.dto.IntroPostResDto
import com.example.jhouse_server.domain.intro.dto.IntroPostUpdateReqDto
import com.example.jhouse_server.domain.intro.service.IntroPostService
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
@RequestMapping("/api/v1/posts/intro")
class IntroPostController(
    val introPostService: IntroPostService
) {

    @Auth(Authority.ADMIN)
    @PostMapping
    fun createPost(
        @RequestBody @Validated req: IntroPostCreateReqDto,
        @AuthUser user: User
    ) : ApplicationResponse<Long> {
        return ApplicationResponse.ok(introPostService.createPost(req, user))
    }

    @Auth(Authority.ADMIN)
    @PutMapping("/{postId}")
    fun updatePost(
        @PathVariable postId : Long,
        @RequestBody @Validated req: IntroPostUpdateReqDto,
        @AuthUser user: User
    ) : ApplicationResponse<Long> {
        return ApplicationResponse.ok(introPostService.updatePost(postId, req, user))
    }

    @Auth(Authority.ADMIN)
    @DeleteMapping("/{postId}")
    fun deletePost(
        @PathVariable postId : Long,
        @AuthUser user: User
    ) : ApplicationResponse<Nothing> {
        introPostService.deletePost(postId, user)
        return ApplicationResponse.ok()
    }

    @GetMapping
    fun getPostAll() :ApplicationResponse<List<IntroPostResDto>> {
        return ApplicationResponse.ok(introPostService.getPostAll())
    }

    @GetMapping("/{postId}")
    fun getPostOne(
        @PathVariable postId : Long
    ) : ApplicationResponse<IntroPostResDto> {
        return ApplicationResponse.ok(introPostService.getPostOne(postId))
    }

    @GetMapping("/search")
    fun getPostAllCustom(
        @RequestParam keyword: String,
        pageable: Pageable
    ) : ApplicationResponse<Page<IntroPostListResDto>> {
        return ApplicationResponse.ok(introPostService.getPostAllByKeywordCustom(keyword, pageable))
    }

    @GetMapping("/category")
    fun getPostCategory() : ApplicationResponse<List<CodeResDto>> {
        return ApplicationResponse.ok(introPostService.getPostCategory())
    }
}