package com.example.jhouse_server.domain.board.dto

import com.example.jhouse_server.domain.board.entity.Board
import com.example.jhouse_server.domain.board.sliceContentWithRegex
import java.sql.Timestamp
import java.util.*

class BoardResDto() {
    var boardId: Long = 0
    lateinit var title: String
    lateinit var code: String
    lateinit var oneLineContent: String
    lateinit var nickName: String
    var createdAt: Date? = null
    var imageUrl: String? = null
    var commentCount: Int = 0
    lateinit var category: String
    lateinit var prefixCategory: String
    var fixed: Boolean = false

    constructor(boardId: Long,
                title: String,
                code: String,
                oneLineContent: String,
                nickName: String,
                createdAt: Date?,
                imageUrl: String?,
                commentCount: Int,
                category: String,
                prefixCategory: String,
                fixed: Boolean
    ) : this() {
        this.boardId = boardId
        this.title = title
        this.code = code
        this.oneLineContent = oneLineContent
        this.nickName = nickName
        this.createdAt = createdAt
        this.imageUrl = imageUrl
        this.commentCount = commentCount
        this.category = category
        this.prefixCategory = prefixCategory
        this.fixed = fixed
    }
}

class BoardMyPageResDto() {
    var boardId: Long = 0
    lateinit var title: String
    lateinit var oneLineContent: String
    var createdAt: Date? = null
    var imageUrl: String? = null
    lateinit var category: String
    lateinit var prefixCategory: String

    constructor(boardId: Long,
                title: String,
                oneLineContent: String,
                createdAt: Date?,
                imageUrl: String?,
                category: String,
                prefixCategory: String,
    ) : this() {
        this.boardId = boardId
        this.title = title
        this.oneLineContent = oneLineContent
        this.createdAt = createdAt
        this.imageUrl = imageUrl
        this.category = category
        this.prefixCategory = prefixCategory
    }
}

fun toMyPageListDto(board : Board) : BoardMyPageResDto {
    val oneLineContent = sliceContentWithRegex(board.content)
    if (board.imageUrls.isEmpty()) {
        return BoardMyPageResDto(board.id, board.title, oneLineContent, Timestamp.valueOf(board.createdAt), null, board.category.name, board.prefixCategory.name)
    }
    return BoardMyPageResDto(board.id, board.title, oneLineContent, Timestamp.valueOf(board.createdAt), board.imageUrls[0], board.category.name, board.prefixCategory.name)
}