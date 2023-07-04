package com.example.jhouse_server.admin.board.dto


enum class BoardSearchFilter (val value: String){
    TITLE("게시물 제목"),
    CONTENT("게시물 내용"),
    WRITER("게시물 작성자");

}