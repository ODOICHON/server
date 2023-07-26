package com.example.jhouse_server.admin.board.dto


data class AdminBoardDeleteList(
        var deleteBoardList: List<Long>?
)

data class AdminBoardFixList(
        val fixBoardIds : List<Long>?
)

data class AdminBoardSearch(
    val filter: BoardSearchFilter?,
    val keyword: String?
)

enum class BoardSearchFilter (val value: String){
    TITLE("게시물 제목"),
    CONTENT("게시물 내용"),
    WRITER("게시물 작성자");
}