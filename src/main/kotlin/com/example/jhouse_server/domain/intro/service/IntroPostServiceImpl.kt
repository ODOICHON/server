package com.example.jhouse_server.domain.intro.service

import com.example.jhouse_server.domain.intro.dto.*
import com.example.jhouse_server.domain.intro.entity.IntroPost
import com.example.jhouse_server.domain.intro.entity.IntroPostCategory
import com.example.jhouse_server.domain.intro.repository.IntroPostRepository
import com.example.jhouse_server.domain.post.dto.CodeResDto
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.util.findByIdOrThrow
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class IntroPostServiceImpl(
    val introPostRepository: IntroPostRepository
) : IntroPostService {
    override fun getPostAll(pageable: Pageable): Page<IntroPostResDto> {
        return introPostRepository.findAllByIsSavedAndUseYn(true, useYn = true, pageable = pageable).map { toDto(it) }
    }

    override fun getPostOne(postId: Long): IntroPostResDto {
       return introPostRepository.findByIdOrThrow(postId).run { toDto(this) }
    }
    @Transactional
    override fun updatePost(postId: Long, req: IntroPostUpdateReqDto, user: User): Long {
        val introPost = introPostRepository.findByIdOrThrow(postId)
        return introPost.updateEntity(
            req.code!!, req.title!!, req.category!!, req.imageUrls, req.isSaved!!
        ).id
    }
    @Transactional
    override fun createPost(req: IntroPostCreateReqDto, user: User): Long {
        val introPost = IntroPost(
            req.code!!, req.title!!, req.category!!, req.imageUrls, req.isSaved!!, user
        )
        return introPostRepository.save(introPost).id
    }
    @Transactional
    override fun deletePost(postId: Long, user: User) {
        val introPost = introPostRepository.findByIdOrThrow(postId)
        introPost.deleteEntity()
    }

    override fun getPostAllByKeywordCustom(
        keyword: String,
        pageable: Pageable
    ): Page<IntroPostListResDto> {
        return introPostRepository.findAllByKeywordCustom(keyword, pageable).map { toListDto(it) }
    }

    override fun getPostCategory(): List<CodeResDto> {
        return IntroPostCategory.values().map { CodeResDto(it.value, it.name) }
    }

    override fun getTemporaryPostList(user: User, pageable: Pageable): Page<IntroPostResDto> {
        return introPostRepository.findAllByIsSavedAndUseYnAndUser(isSaved = false, useYn = true, user, pageable).map { toDto(it) }
    }
}