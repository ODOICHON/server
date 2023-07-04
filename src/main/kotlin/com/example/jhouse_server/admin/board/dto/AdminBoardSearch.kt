package com.example.jhouse_server.admin.board.dto

data class AdminBoardSearch(
        val filter: BoardSearchFilter?,
        val keyword: String?
)
