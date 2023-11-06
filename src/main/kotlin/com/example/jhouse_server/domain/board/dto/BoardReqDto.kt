package com.example.jhouse_server.domain.board

import com.example.jhouse_server.domain.board.dto.BoardResDto
import com.example.jhouse_server.domain.board.dto.BoardResultDto
import com.example.jhouse_server.domain.board.entity.Board
import com.example.jhouse_server.domain.board.entity.BoardCategory
import com.example.jhouse_server.domain.comment.dto.CommentResDto
import java.sql.Timestamp
import java.util.*
import javax.validation.constraints.NotNull
/**
 * =============================================================================================
 *  REQUEST DTO
 * =============================================================================================
 * */
data class BoardReqDto(
    @field:NotNull(message = "게시글 제목은 필수값입니다.")
    val title: String? = null,
    @field:NotNull(message = "code는 필수값입니다.")
    val code: String? = null,
    val category: BoardCategory?,
    val imageUrls: List<String>,
    @field:NotNull(message = "게시글 타입은 필수값입니다.")
    val prefixCategory: PrefixCategory? = null,
    val fixed: Boolean? = null, // 홍보 게시글에 대해서만 true 설정 가능
)

data class BoardUpdateReqDto(
    @field:NotNull(message = "게시글 제목은 필수값입니다.")
    val title: String? = null,
    @field:NotNull(message = "code는 필수값입니다.")
    val code: String? = null,
    val category: String?,
    val imageUrls: List<String>,
    @field:NotNull(message = "게시글 타입은 필수값입니다.")
    val prefixCategory: String? = null,
    val fixed: Boolean? = null,
)

data class BoardListDto(
    val prefix: String,
    val category: String?,
    val search: String?,
    val order: String?
)

data class BoardPreviewListDto(
    val prefix: String,
    val category: String?,
    val limit: Long
)

/**
 * =============================================================================================
 *  RESPONSE DTO
 * =============================================================================================
 * */
data class BoardResOneDto(
    val boardId : Long,
    val title : String,
    val code: String,
    val nickName: String,
    val createdAt : Date,
    val imageUrls: List<String>,
    val loveCount : Int,
    val category: String,
    val prefixCategory: String,
    val commentCount : Int,
    val comments : List<CommentResDto>
)
/**
 * =============================================================================================
 *  PUBLIC FUNCTION -- DTO <> Entity
 * =============================================================================================
 * */
fun toListDto(board : BoardResultDto) : BoardResDto {
    val oneLineContent = sliceContentWithRegex(board.content)
    if (board.imageUrls.isEmpty()) {
        return BoardResDto(board.boardId, board.title, board.code, oneLineContent, board.nickName, Timestamp.valueOf(board.createdAt), null, board.commentCount, board.category, board.prefixCategory, board.fixed)
    }
    return BoardResDto(board.boardId, board.title, board.code, oneLineContent, board.nickName, Timestamp.valueOf(board.createdAt), board.imageUrls[0], board.commentCount, board.category, board.prefixCategory, board.fixed)
}

fun toDto(board: Board) : BoardResOneDto {
    return BoardResOneDto(board.id, board.title, board.boardCode.code, board.user.nickName, Timestamp.valueOf(board.createdAt), board.imageUrls, board.love.size, board.category.name, board.prefixCategory.name, board.comment.size, board.comment.stream().map { com.example.jhouse_server.domain.comment.dto.toDto(it) }.toList())
}

/**
 * =============================================================================================
 *  UTIL FUNCTION
 * =============================================================================================
 * */
fun sliceContentWithRegex(content : String) : String {
    return if (content.length >= 200) {
        content.take(200)
    } else {
        content
    }
}
/**
 * =============================================================================================
 *  ENUM CLASS
 * =============================================================================================
 * */
data class CodeResDto(
    val code: String,
    val name : String
)
