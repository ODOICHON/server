package com.example.jhouse_server.domain.ads.controller

import com.example.jhouse_server.domain.ads.dto.AdsPostCreateReqDto
import com.example.jhouse_server.domain.ads.dto.AdsPostListResDto
import com.example.jhouse_server.domain.ads.dto.AdsPostResDto
import com.example.jhouse_server.domain.ads.dto.AdsPostUpdateReqDto
import com.example.jhouse_server.domain.ads.service.AdsPostService
import com.example.jhouse_server.domain.post.dto.CodeResDto
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

    @Auth
    @PostMapping
    fun createPost(
        @RequestBody @Validated req : AdsPostCreateReqDto,
        @AuthUser user : User
    ) : ApplicationResponse<Long> {
        return ApplicationResponse.ok(adsPostService.createPost(req, user))
    }


    @Auth
    @PutMapping("/{postId}")
    fun updatePost(
        @PathVariable postId : Long,
        @RequestBody @Validated req: AdsPostUpdateReqDto,
        @AuthUser user: User
    ) : ApplicationResponse<Long> {
        return ApplicationResponse.ok(adsPostService.updatePost(postId, req, user))
    }

    @Auth
    @DeleteMapping("/{postId}")
    fun deletePost(
        @PathVariable postId : Long,
        @AuthUser user: User
    ) : ApplicationResponse<Nothing> {
        adsPostService.deletePost(postId, user)
        return ApplicationResponse.ok()
    }

    @GetMapping
    fun getPostAll() :ApplicationResponse<List<AdsPostResDto>> {
        return ApplicationResponse.ok(adsPostService.getPostAll())
    }

    @GetMapping("/{postId}")
    fun getPostOne(
        @PathVariable postId : Long
    ) : ApplicationResponse<AdsPostResDto> {
        return ApplicationResponse.ok(adsPostService.getPostOne(postId))
    }

    @GetMapping("/search")
    fun getPostAllCustom(
        @RequestParam keyword: String,
        pageable: Pageable
    ) : ApplicationResponse<Page<AdsPostListResDto>> {
        return ApplicationResponse.ok(adsPostService.getPostAllByKeywordCustom(keyword, pageable))
    }

    @GetMapping("/category")
    fun getPostCategory() : ApplicationResponse<List<CodeResDto>> {
        return ApplicationResponse.ok(adsPostService.getPostCategory())
    }
}