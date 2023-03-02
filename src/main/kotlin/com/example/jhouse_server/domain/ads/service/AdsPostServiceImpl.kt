package com.example.jhouse_server.domain.ads.service

import com.example.jhouse_server.domain.ads.dto.*
import com.example.jhouse_server.domain.ads.entity.AdPost
import com.example.jhouse_server.domain.ads.entity.AdsPostCategory
import com.example.jhouse_server.domain.ads.repository.AdsPostRepository
import com.example.jhouse_server.domain.post.dto.CodeResDto
import com.example.jhouse_server.domain.user.entity.Authority
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
class AdsPostServiceImpl(
    val adsPostRepository: AdsPostRepository
): AdsPostService {
    override fun getPostAll(): List<AdsPostResDto> {
        return adsPostRepository.findAll().map { toDto(it) }
    }

    override fun getPostOne(postId: Long): AdsPostResDto {
        return adsPostRepository.findByIdOrThrow(postId).run { toDto(this) }
    }

    @Transactional
    override fun updatePost(postId: Long, req: AdsPostUpdateReqDto, user: User): Long {
        val adPost = adsPostRepository.findByIdOrThrow(postId)
        if(user != adPost.user) throw ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION)
        return adPost.updateEntity(req.code!!, req.title!!, req.category!!, req.imageUrls, req.isSaved!!).id
    }
    @Transactional
    override fun createPost(req: AdsPostCreateReqDto, user: User): Long {
        val adPost = AdPost(
        req.code!!, req.title!!, req.category!!, req.imageUrls, req.isSaved!!, user
        )
        return adsPostRepository.save(adPost).id
    }
    @Transactional
    override fun deletePost(postId: Long, user: User) {
        val adPost = adsPostRepository.findByIdOrThrow(postId)
        if(user == adPost.user) adPost.deleteEntity()
        else throw ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION)
    }

    override fun getPostAllByKeywordCustom(
        keyword: String,
        pageable: Pageable
    ): Page<AdsPostListResDto> {
        return adsPostRepository.findAllByKeywordCustom(keyword, pageable).map { toListDto(it) }
    }
    @Transactional
    override fun fixPost(postId: Long, user: User): Long {
        val adPost = adsPostRepository.findByIdOrThrow(postId)
        return if(user.authority == Authority.ADMIN) adPost.updateFixed().id
        else throw ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION)
    }

    override fun getPostCategory(): List<CodeResDto> {
        return AdsPostCategory.values().map { CodeResDto(it.value, it.name) }
    }

    @Transactional
    override fun updatePostLove(postId: Long, user: User): Long {
        val adPost = adsPostRepository.findByIdOrThrow(postId)
        return adPost.updateLove().id
    }
}