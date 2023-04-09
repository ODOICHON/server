package com.example.jhouse_server.domain.board.repository

import com.example.jhouse_server.admin.board.dto.AdminBoardSearch
import com.example.jhouse_server.domain.board.entity.QBoard.*
import com.example.jhouse_server.domain.board.repository.dto.AdminBoardResult
import com.example.jhouse_server.domain.board.repository.dto.QAdminBoardResult
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils

class BoardRepositoryImpl(
        private var jpaQueryFactory: JPAQueryFactory
) : BoardRepositoryCustom {

    override fun getBoardListWithPaging(adminBoardSearch: AdminBoardSearch, pageable: Pageable): Page<AdminBoardResult> {
        val result = jpaQueryFactory
                .select(QAdminBoardResult(board.category, board.title))
                .from(board)
                .where(searchFilter(adminBoardSearch))
                .limit(pageable.pageSize.toLong())
                .offset(pageable.offset)
                .fetch()

        val countQuery = jpaQueryFactory
                .selectFrom(board)
                .where(searchFilter(adminBoardSearch))

        return PageableExecutionUtils.getPage(result, pageable) {countQuery.fetchCount()!!}
    }

    private fun searchFilter(adminBoardSearch: AdminBoardSearch) : BooleanExpression? {
        if (adminBoardSearch.filter == "title"){
            return board.title.contains(adminBoardSearch.keyword)
        } else if (adminBoardSearch.filter == "content"){
            return board.content.contains(adminBoardSearch.keyword)
        }
        return null
    }


}