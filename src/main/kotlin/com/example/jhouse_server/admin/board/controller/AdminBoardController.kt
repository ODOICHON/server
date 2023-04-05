package com.example.jhouse_server.admin.board.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/admin/board")
class AdminBoardController (

        ) {

    @GetMapping("/delete")
    fun getDeletedBoardList() : String {
        return "ads"
    }


}