package com.example.jhouse_server.domain.board.dto

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