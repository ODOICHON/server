package com.example.jhouse_server.domain.post.service

import com.example.jhouse_server.domain.post.dto.*
import com.example.jhouse_server.domain.post.entity.Post
import com.example.jhouse_server.domain.post.entity.PostCategory
import com.example.jhouse_server.domain.post.repository.PostRepository
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode
import com.example.jhouse_server.global.findByIdOrThrow
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class PostServiceImpl(
        val postRepository: PostRepository
): PostService {
    override fun getPostAll(): List<PostResDto> {
        return postRepository.findAll().map { toDto(it) }
    }

    override fun getPostOne(postId: Long): PostResDto {
        return postRepository.findByIdOrThrow(postId).run { toDto(this) }
    }

    @Transactional
    override fun updatePost(postId: Long, req: PostUpdateReqDto, user: User): Long {
        val post = postRepository.findByIdOrThrow(postId)
        if(user != post.user) throw ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION)
        return post.updateEntity(
            req.code!!, req.title!!, req.category!!, req.imageUrls, req.isSaved!!
        ).id
    }

    @Transactional
    override fun createPost(req: PostCreateReqDto, user: User): Long {
        val post = Post(
                req.code!!, req.title!!, req.category!!, req.imageUrls, req.isSaved!!, user
        )
        return postRepository.save(post).id
    }

    @Transactional
    override fun deletePost(postId: Long, userId: User) {
        val post = postRepository.findByIdOrThrow(postId)
        if (userId == post.user) postRepository.delete(post)
        else throw ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION)
    }

    override fun getPostAllByKeywordCustom(keyword: String, pageable: Pageable): Page<PostListResDto> {
        return postRepository.findAllByKeywordCustom(keyword, pageable).map { toListDto(it) }
    }

    override fun getPostCategory(): List<CodeResDto> {
        return PostCategory.values().map { CodeResDto(it.value, it.name) }
    }
}