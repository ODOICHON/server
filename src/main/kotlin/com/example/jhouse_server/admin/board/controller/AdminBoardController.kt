package com.example.jhouse_server.admin.board.controller

import com.example.jhouse_server.admin.board.dto.AdminBoardSearch
import com.example.jhouse_server.admin.board.dto.FilterList
import com.example.jhouse_server.admin.board.service.AdminBoardService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/admin/board")
class AdminBoardController (
    var adminBoardService: AdminBoardService
        ) {

    @GetMapping("/fix")
    fun getDeletedBoardList(@ModelAttribute("searchForm") adminBoardSearch: AdminBoardSearch
                            , model: Model
                            , @PageableDefault(size=8, page=0) pageable: Pageable) : String {
        val result = adminBoardService.getSearchBoardResult(adminBoardSearch, pageable)
        model.addAttribute("boardList", result)

        val pageCom = pageable.pageNumber / 5
        model.addAttribute("pageCom", pageCom)

        val filterList = mutableListOf<FilterList>()
        filterList.add(FilterList("title", "게시물 제목"))
        filterList.add(FilterList("content", "게시물 내용"))
        model.addAttribute("filterList", filterList)

        return "board/fix"
    }



}