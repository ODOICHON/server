package com.example.jhouse_server.domain.post.controller

import com.example.jhouse_server.domain.post.dto.PostCreateReqDto
import com.example.jhouse_server.domain.post.dto.PostResDto
import com.example.jhouse_server.domain.post.dto.PostUpdateReqDto
import com.example.jhouse_server.domain.post.service.PostService
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.annotation.AuthUser
import com.example.jhouse_server.global.response.ApplicationResponse
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/posts")
class PostController(
        val postService: PostService
) {
    @PostMapping
    fun createPost(
            @RequestBody @Validated req : PostCreateReqDto,
            @AuthUser user: User
    ) : ApplicationResponse<PostResDto> {
        return ApplicationResponse.ok(postService.createPost(req, user))
    }

    @GetMapping
    fun getPostAll() : ApplicationResponse<List<PostResDto>> {
        return ApplicationResponse.ok(postService.getPostAll())
    }

    @GetMapping("/{postId}")
    fun getPostOne(
        @PathVariable postId: Long
    ) : ApplicationResponse<PostResDto> {
        return ApplicationResponse.ok(postService.getPostOne(postId))
    }

    @PutMapping("/{postId}")
    fun updatePost(
        @PathVariable postId: Long,
        @RequestBody @Validated req : PostUpdateReqDto,
        @AuthUser user: User
    ) : ApplicationResponse<PostResDto> {
        return ApplicationResponse.ok(postService.updatePost(postId, req, user))
    }

    @DeleteMapping("/{postId}")
    fun deletePost(
        @PathVariable postId: Long,
        @AuthUser user: User
    ) : ApplicationResponse<Nothing> {
        postService.deletePost(postId, user)
        return ApplicationResponse.ok()
    }
}