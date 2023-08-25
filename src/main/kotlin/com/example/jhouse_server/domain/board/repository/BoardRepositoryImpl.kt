package com.example.jhouse_server.domain.board.repository

import com.example.jhouse_server.admin.board.dto.AdminBoardSearch
import com.example.jhouse_server.admin.board.dto.BoardSearchFilter
import com.example.jhouse_server.domain.board.*
import com.example.jhouse_server.domain.board.dto.BoardMyPageResDto
import com.example.jhouse_server.domain.board.dto.BoardResDto
import com.example.jhouse_server.domain.board.dto.PreviewPrefixType
import com.example.jhouse_server.domain.board.dto.toMyPageListDto
import com.example.jhouse_server.domain.board.entity.Board
import com.example.jhouse_server.domain.board.entity.BoardCategory
import com.example.jhouse_server.domain.board.entity.QBoard.board
import com.example.jhouse_server.domain.board.entity.QBoardCode.boardCode
import com.example.jhouse_server.domain.comment.entity.QComment.comment
import com.example.jhouse_server.domain.love.entity.QLove.love
import com.example.jhouse_server.domain.user.entity.QUser.user
import com.example.jhouse_server.domain.user.entity.User
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.core.types.dsl.NumberTemplate
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.util.StringUtils.hasText
import java.lang.Exception

class BoardRepositoryImpl(
        private var jpaQueryFactory: JPAQueryFactory
) : BoardRepositoryCustom {

    override fun getFixableBoardListWithPaging(adminBoardSearch: AdminBoardSearch, pageable: Pageable): Page<Board> {
        val result = jpaQueryFactory
                .selectFrom(board)
                .where(searchFilter(adminBoardSearch), board.prefixCategory.eq(PrefixCategory.ADVERTISEMENT), board.useYn.eq(true))
                .orderBy(board.fixed.desc(), board.id.asc())
                .limit(pageable.pageSize.toLong())
                .offset(pageable.offset)
                .fetch()

        val countQuery = jpaQueryFactory
                .selectFrom(board)
                .where(searchFilter(adminBoardSearch),board.prefixCategory.eq(PrefixCategory.ADVERTISEMENT), board.useYn.eq(true))

        return PageableExecutionUtils.getPage(result, pageable) {countQuery.fetch().size.toLong()}
    }

    override fun getDeletableBoardListWithPaging(adminBoardSearch: AdminBoardSearch, pageable: Pageable): Page<Board> {
        val result = jpaQueryFactory
                .selectFrom(board)
                .where(searchFilter(adminBoardSearch), board.useYn.eq(false))
                .orderBy(board.id.asc())
                .limit(pageable.pageSize.toLong())
                .offset(pageable.offset)
                .fetch()

        val countQuery = jpaQueryFactory
                .selectFrom(board)
                .where(searchFilter(adminBoardSearch), board.useYn.eq(false))

        return PageableExecutionUtils.getPage(result, pageable) {countQuery.fetch().size.toLong()}
    }


    override fun getBoardAll(boardListDto: BoardListDto, pageable: Pageable): Page<BoardResDto> {
        val result = jpaQueryFactory
                .selectFrom(board)
                .join(board.boardCode, boardCode).fetchJoin()
                .join(board.user, user).fetchJoin()
                .leftJoin(board.comment, comment)
                .where(board.useYn.eq(true), filterWithPrefixCategory(boardListDto.prefix) ,searchWithBoardCategory(boardListDto.category),searchWithKeyword(boardListDto.search))
                .groupBy(board.id)
                .orderBy(board.fixed.desc(), searchWithOrder(boardListDto.order))
                .limit(pageable.pageSize.toLong())
                .offset(pageable.offset)
                .fetch()
        val countQuery = jpaQueryFactory
                .selectFrom(board)
                .where(board.useYn.eq(true), board.prefixCategory.eq(PrefixCategory.valueOf(boardListDto.prefix)),searchWithBoardCategory(boardListDto.category),searchWithKeyword(boardListDto.search))

        return PageableExecutionUtils.getPage(result, pageable) {countQuery.fetch().size.toLong()}.map { toListDto(it) }
    }

    private fun filterWithPrefixCategory(prefix: String): BooleanExpression? {
        return if(!hasText(prefix)) null else board.prefixCategory.eq(PrefixCategory.valueOf(prefix))
    }


    override fun getBoardPreviewAll(boardPreviewListDto: BoardPreviewListDto): List<Board> {
        return jpaQueryFactory
                .selectFrom(board)
                .join(board.boardCode, boardCode).fetchJoin()
                .join(board.user, user).fetchJoin()
                .where(board.useYn.eq(true),searchPreviewWithPrefixCategory(boardPreviewListDto.prefix), searchWithBoardCategory(boardPreviewListDto.category))
                .orderBy(board.fixed.desc(), board.love.size().desc())
                .limit(boardPreviewListDto.limit)
                .offset(0)
                .fetch()
    }

    /**
     * 자신이 작성한 게시글 목록 조회
     */
    override fun getUserBoardAll(user: User, pageable: Pageable): Page<BoardMyPageResDto> {
        val result = jpaQueryFactory
            .selectFrom(board)
            .where(board.useYn.eq(true), board.user.eq(user))
            .orderBy(board.fixed.desc())
            .limit(pageable.pageSize.toLong())
            .offset(pageable.offset)
            .fetch()
        val countQuery = jpaQueryFactory
            .selectFrom(board)
            .where(board.useYn.eq(true), board.user.eq(user))

        return PageableExecutionUtils.getPage(result, pageable) {countQuery.fetch().size.toLong()}.map { toMyPageListDto(it) }
    }

    /**
     * 자신이 작성한 댓글의 게시글 목록 조회
     */
    override fun getUserCommentAll(user: User, pageable: Pageable): Page<BoardMyPageResDto> {
        val result = jpaQueryFactory
            .selectFrom(board).distinct()
            .join(board.comment, comment).fetchJoin()
            .where(board.useYn.eq(true), comment.user.eq(user))
            .orderBy(board.fixed.desc())
            .limit(pageable.pageSize.toLong())
            .offset(pageable.offset)
            .fetch()
        val countQuery = jpaQueryFactory
            .selectFrom(board).distinct()
            .where(board.useYn.eq(true), comment.user.eq(user))

        return PageableExecutionUtils.getPage(result, pageable) {countQuery.fetch().size.toLong()}.map { toMyPageListDto(it) }
    }

    /**
     * 자신이 좋아요한 게시글 목록 조회
     */
    override fun getUserLoveAll(user: User, pageable: Pageable): Page<BoardMyPageResDto> {
        val result = jpaQueryFactory
            .selectFrom(board).distinct()
            .join(board.love, love).fetchJoin()
            .where(board.useYn.eq(true), love.user.eq(user))
            .orderBy(board.fixed.desc())
            .limit(pageable.pageSize.toLong())
            .offset(pageable.offset)
            .fetch()
        val countQuery = jpaQueryFactory
            .selectFrom(board).distinct()
            .where(board.useYn.eq(true), love.user.eq(user))

        return PageableExecutionUtils.getPage(result, pageable) {countQuery.fetch().size.toLong()}.map { toMyPageListDto(it) }
    }

    private fun searchPreviewWithPrefixCategory(prefix: String): BooleanExpression {
        return if(PreviewPrefixType.valueOf(prefix) == PreviewPrefixType.COMMUNITY) board.prefixCategory.eq(PrefixCategory.ADVERTISEMENT).or(board.prefixCategory.eq(PrefixCategory.DEFAULT))
                else board.prefixCategory.eq(PrefixCategory.valueOf(prefix))
    }


    private fun searchWithBoardCategory(category: String?): BooleanExpression? {
        return if(category == null) null else board.category.eq(BoardCategory.valueOf(category))
    }


    // 랜덤 데이터 100건 중, 제목의 특정 키워드 검색 성능 like : 111ms | full text : 12ms
    private fun searchWithKeyword(keyword: String?): BooleanExpression? {
        if(keyword == null) return null
        val titleBoolean = Expressions.numberTemplate(
            Integer::class.java,
            "function('match',{0},{1})", board.title, "+*$keyword*"
        )
        val contentBoolean = Expressions.numberTemplate(
            Integer::class.java, "function('match',{0},{1})", board.content,
            "+*$keyword*"
        )
        return titleBoolean.gt(0).or(contentBoolean.gt(0))
    }

    private fun searchWithOrder(order: String?): OrderSpecifier<*>?{
        return when(order){
            null -> board.updatedAt.desc()
            "RECENT" -> board.updatedAt.desc()
            "POPULAR" -> board.love.size().desc()
            else -> null
        }
    }


    private fun searchFilter(adminBoardSearch: AdminBoardSearch) : BooleanExpression? {
        return when (adminBoardSearch.filter) {
            BoardSearchFilter.TITLE -> board.title.contains(adminBoardSearch.keyword)
            BoardSearchFilter.CONTENT -> board.content.contains(adminBoardSearch.keyword)
            BoardSearchFilter.WRITER -> user.nickName.contains(adminBoardSearch.keyword)
            else -> null
        }
    }
}