package com.example.jhouse_server.domain.board

import com.example.jhouse_server.domain.board.entity.Board
import com.example.jhouse_server.domain.board.entity.BoardCategory
import com.example.jhouse_server.domain.comment.dto.CommentResDto
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.validation.constraints.NotNull
import kotlin.streams.toList

data class BoardReqDto(
    @field:NotNull(message = "게시글 제목은 필수값입니다.")
    val title: String? = null,
    @field:NotNull(message = "code는 필수값입니다.")
    val code: String? = null,
    @field:NotNull(message = "말머리는 필수값입니다.")
    val category: BoardCategory? = null,
    val imageUrls: List<String>,
    @field:NotNull(message = "게시글 타입은 필수값입니다.")
    val prefixCategory: PrefixCategory? = null,
    val fixed: Boolean? = null, // 홍보 게시글에 대해서만 true 설정 가능
)

data class BoardResDto(
    val boardId: Long,
    val title : String,
    val code: String,
    val oneLineContent: String,
    val nickName: String,
    val createdAt : LocalDate,
    val imageUrl : String,
    val commentCount : Int,
    val category: String,
    val prefixCategory: String
)

data class BoardUpdateReqDto(
    @field:NotNull(message = "게시글 제목은 필수값입니다.")
    val title: String? = null,
    @field:NotNull(message = "code는 필수값입니다.")
    val code: String? = null,
    @field:NotNull(message = "말머리는 필수값입니다.")
    val category: String? = null,
    val imageUrls: List<String>,
    @field:NotNull(message = "게시글 타입은 필수값입니다.")
    val prefixCategory: String? = null,
    val fixed: Boolean? = null,
)

data class BoardResOneDto(
    val boardId : Long,
    val title : String,
    val code: String,
    val nickName: String,
    val createdAt : LocalDate,
    val imageUrls: List<String>,
    val loveCount : Int,
    val category: String,
    val prefixCategory: String,
    val commentCount : Int,
    val comments : List<CommentResDto>
)

fun toListDto(board : Board) : BoardResDto {
    val oneLineContent = if(board.content.length >= 50) board.content.substring(0 until 50) else board.content// 50자 슬라이싱
    return BoardResDto(board.id, board.title, board.code, oneLineContent, board.user.nickName, board.createdAt.toLocalDate(), board.imageUrls[0], board.comment.size, board.category.name, board.prefixCategory.name)
}

fun toDto(board: Board) : BoardResOneDto {
    return BoardResOneDto(board.id, board.title, board.code, board.user.nickName, board.createdAt.toLocalDate(), board.imageUrls, board.love.size, board.category.name, board.prefixCategory.name, board.comment.size, board.comment.stream().map { com.example.jhouse_server.domain.comment.dto.toDto(it) }.toList())
}

data class CodeResDto(
    val code: String,
    val name : String
)
