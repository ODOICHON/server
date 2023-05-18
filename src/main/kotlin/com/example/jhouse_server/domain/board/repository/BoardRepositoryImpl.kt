package com.example.jhouse_server.domain.board.repository

import com.example.jhouse_server.admin.board.dto.AdminBoardSearch
import com.example.jhouse_server.admin.board.dto.SearchFilter
import com.example.jhouse_server.domain.board.PrefixCategory
import com.example.jhouse_server.domain.board.entity.Board
import com.example.jhouse_server.domain.board.entity.BoardCategory
import com.example.jhouse_server.domain.board.entity.QBoard.board
import com.example.jhouse_server.domain.user.entity.QUser.user
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
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

    override fun getBoardAllWithPrefixCategory(name: PrefixCategory, keyword: String, pageable: Pageable): Page<Board> {
        val result = jpaQueryFactory
                .select(board)
                .from(board)
                .where(searchWithPrefixCategory(name), searchWithKeyword(keyword), board.useYn.eq(true))
                .fetch()
        return PageImpl(result, pageable, result.size.toLong())
    }

    override fun getBoardAllWithBoardCategory(
        name: BoardCategory,
        keyword: String,
        pageable: Pageable
    ): Page<Board> {
        val result = jpaQueryFactory
            .select(board)
            .from(board)
            .where(searchWithBoardCategory(name), searchWithKeyword(keyword), board.useYn.eq(true))
            .fetch()
        return PageImpl(result, pageable, result.size.toLong())
    }

    private fun searchWithBoardCategory(name: BoardCategory): BooleanExpression? {
        return board.category.eq(name)
    }


    private fun searchWithKeyword(keyword: String): BooleanExpression? {
        return board.content.contains(keyword)
                .or(board.title.contains(keyword))
                .or(board.user.nickName.eq(keyword))
    }
    private fun searchFilter(adminBoardSearch: AdminBoardSearch) : BooleanExpression? {
        return when (adminBoardSearch.filter) {
            SearchFilter.TITLE -> board.title.contains(adminBoardSearch.keyword)
            SearchFilter.CONTENT -> board.content.contains(adminBoardSearch.keyword)
            SearchFilter.WRITER -> user.nickName.contains(adminBoardSearch.keyword)
            else -> null
        }
    }
    private fun searchWithPrefixCategory(name: PrefixCategory) : BooleanExpression? {
        return board.prefixCategory.eq(name)
    }
}