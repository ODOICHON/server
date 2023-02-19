package com.example.jhouse_server.domain.post.controller

import com.example.jhouse_server.domain.post.service.PostService
import org.springframework.web.bind.annotation.RestController

@RestController
class PostController(
        val postService: PostService
) {

}