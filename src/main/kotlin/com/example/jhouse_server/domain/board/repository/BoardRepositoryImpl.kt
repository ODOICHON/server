package com.example.jhouse_server.domain.board.repository

import com.example.jhouse_server.admin.board.dto.AdminBoardSearch
import com.example.jhouse_server.admin.board.dto.BoardSearchFilter
import com.example.jhouse_server.domain.board.*
import com.example.jhouse_server.domain.board.dto.BoardResDto
import com.example.jhouse_server.domain.board.dto.PreviewPrefixType
import com.example.jhouse_server.domain.board.entity.Board
import com.example.jhouse_server.domain.board.entity.BoardCategory
import com.example.jhouse_server.domain.board.entity.QBoard.board
import com.example.jhouse_server.domain.board.entity.QBoardCode.boardCode
import com.example.jhouse_server.domain.user.entity.QUser.user
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils

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
                .where(board.useYn.eq(true), board.prefixCategory.eq(PrefixCategory.valueOf(boardListDto.prefix)),searchWithBoardCategory(boardListDto.category),searchWithKeyword(boardListDto.search))
                .orderBy(board.fixed.desc(), searchWithOrder(boardListDto.order))
                .limit(pageable.pageSize.toLong())
                .offset(pageable.offset)
                .fetch()
        val countQuery = jpaQueryFactory
                .selectFrom(board)
                .where(board.useYn.eq(true), board.prefixCategory.eq(PrefixCategory.valueOf(boardListDto.prefix)),searchWithBoardCategory(boardListDto.category),searchWithKeyword(boardListDto.search))

        return PageableExecutionUtils.getPage(result, pageable) {countQuery.fetch().size.toLong()}.map { toListDto(it) }
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

    private fun searchPreviewWithPrefixCategory(prefix: String): BooleanExpression {
        return if(PreviewPrefixType.valueOf(prefix) == PreviewPrefixType.COMMUNITY) board.prefixCategory.eq(PrefixCategory.ADVERTISEMENT).or(board.prefixCategory.eq(PrefixCategory.DEFAULT))
                else board.prefixCategory.eq(PrefixCategory.valueOf(prefix))
    }


    private fun searchWithBoardCategory(category: String?): BooleanExpression? {
        return if(category == null) null else board.category.eq(BoardCategory.valueOf(category))
    }


    private fun searchWithKeyword(keyword: String?): BooleanExpression? {
        return if(keyword == null) null else board.content.contains(keyword)
                .or(board.title.contains(keyword))
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