package com.example.jhouse_server.domain.post.service

import com.example.jhouse_server.domain.post.dto.PostCreateReqDto
import com.example.jhouse_server.domain.post.dto.PostResDto
import com.example.jhouse_server.domain.post.dto.PostUpdateReqDto
import com.example.jhouse_server.domain.post.dto.toDto
import com.example.jhouse_server.domain.post.entity.Post
import com.example.jhouse_server.domain.post.repository.PostRepository
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.domain.user.repository.UserRepository
import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode
import com.example.jhouse_server.global.findByIdOrThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class PostServiceImpl(
        val postRepository: PostRepository,
        val userRepository: UserRepository
): PostService {
    override fun getPostAll(): List<PostResDto> {
        return postRepository.findAll().map { toDto(it) }
    }

    override fun getPostOne(postId: Long): PostResDto {
        return postRepository.findById(postId).run { toDto(this.get()) }
    }

    @Transactional
    override fun updatePost(postId: Long, req: PostUpdateReqDto, user: User): PostResDto {
        val post = postRepository.findByIdOrThrow(postId)
        if(user != post.user) throw ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION)
        return post.updateEntity(
            req.code, req.category, req.content, req.imageUrls, req.isSaved
        ).run { toDto(this) }
    }

    @Transactional
    override fun createPost(req: PostCreateReqDto, user: User): PostResDto {
        val post = Post(
                req.code, req.title, req.category, req.content, req.imageUrls, req.address, req.isSaved, user
        )
        return postRepository.save(post).run {
            toDto(this)
        }
    }

    @Transactional
    override fun deletePost(postId: Long, user: User) {
        val post = postRepository.findByIdOrThrow(postId)
        if (user == post.user) postRepository.delete(post)
        else throw ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION)
    }
}