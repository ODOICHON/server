package com.example.jhouse_server.domain.post.controller

import com.example.jhouse_server.domain.post.dto.PostCreateReqDto
import com.example.jhouse_server.domain.post.dto.PostResDto
import com.example.jhouse_server.domain.post.dto.PostUpdateReqDto
import com.example.jhouse_server.domain.post.service.PostService
import com.example.jhouse_server.global.response.ApplicationResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/posts")
class PostController(
        val postService: PostService
) {
    @PostMapping
    fun createPost(
            @RequestBody req : PostCreateReqDto
    ) : ApplicationResponse<PostResDto> {
        return ApplicationResponse.ok(postService.createPost(req))
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
        @RequestBody req : PostUpdateReqDto
    ) : ApplicationResponse<PostResDto> {
        return ApplicationResponse.ok(postService.updatePost(postId, req))
    }

    @DeleteMapping("/{postId}")
    fun deletePost(
        @PathVariable postId: Long,
        @RequestParam userId: Long // 변경해야 함
    ) : ApplicationResponse<Nothing> {
        postService.deletePost(postId, userId)
        return ApplicationResponse.ok()
    }
}