package com.example.jhouse_server.admin.board.controller

import com.example.jhouse_server.admin.board.dto.AdminBoardDeleteList
import com.example.jhouse_server.admin.board.dto.AdminBoardFixList
import com.example.jhouse_server.admin.board.dto.AdminBoardSearch
import com.example.jhouse_server.admin.board.dto.BoardSearchFilter
import com.example.jhouse_server.admin.board.service.AdminBoardService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/admin/board")
class AdminBoardController(
    /**
     * =============================================================================================
     *  DI for Repository
     * =============================================================================================
     * */
        val adminBoardService: AdminBoardService
) {
    /**
     * =============================================================================================
     * 홍보 게시글 상단 고정
     *
     * @author YoonTaeminnnn
     * @param adminBoardSearch
     * @param adminBoardFixList
     * @param model
     * @param pageable
     * @return board/fix 게시글 관리-고정 페이지
     * =============================================================================================
     * */
    @GetMapping("/fix")
    fun getDeletedBoardList(@ModelAttribute("searchForm") adminBoardSearch: AdminBoardSearch
                            , @ModelAttribute("fixList") adminBoardFixList: AdminBoardFixList
                            , model: Model
                            , @PageableDefault(size = 10, page = 0) pageable: Pageable): String {
        // 고정할 수 있는 게시글 검색
        val result = adminBoardService.getSearchFixableBoardResult(adminBoardSearch, pageable)
        model.addAttribute("boardList", result)
        // 페이징
        val pageCom = pageable.pageNumber / 5
        model.addAttribute("pageCom", pageCom)
        model.addAttribute("filterList", BoardSearchFilter.values())
        val total = adminBoardService.getFixableBoardTotal()
        model.addAttribute("total", total)
        // 고정된 게시글 리스트 및 고정 게시글 개수
        val fixedBoards = adminBoardService.getFixedBoardList()
        model.addAttribute("fixedBoardList", fixedBoards)
        model.addAttribute("fixedCount", fixedBoards.size)

        return "board/fix"
    }

    @GetMapping("/{type}/{board_id}")
    fun getBoardDetail(@PathVariable("type") type : String, @PathVariable("board_id") id : Long, model: Model, redirectAttributes: RedirectAttributes): String {
        val code = if (type == "fix"){
            adminBoardService.getBoardDetail(id, redirectAttributes) ?: return "redirect:/admin/board/fix"
        } else {
            adminBoardService.getDeletableBoardDetail(id, redirectAttributes) ?: return "redirect:/admin/board/delete"
        }
        model.addAttribute("code", code)
        return "board/boardDetail"
    }

    @GetMapping("/delete")
    fun deleteBoardList(@ModelAttribute("searchForm") adminBoardSearch: AdminBoardSearch
                        , @ModelAttribute("deleteList") adminBoardDeleteList: AdminBoardDeleteList
                        , model: Model
                        , @PageableDefault(size = 10, page = 0) pageable: Pageable): String {

        val result = adminBoardService.getSearchDeletableBoardResult(adminBoardSearch, pageable)
        model.addAttribute("boardList", result)

        val pageCom = pageable.pageNumber / 5
        model.addAttribute("pageCom", pageCom)
        model.addAttribute("filterList", BoardSearchFilter.values())
        val total = adminBoardService.getDeletableBoardTotal()
        model.addAttribute("total", total)

        return "board/delete"
    }

    @PostMapping("/delete")
    fun deleteBoards(@ModelAttribute("deleteList") adminBoardDeleteList: AdminBoardDeleteList): String{
        adminBoardService.deleteBoards(adminBoardDeleteList)
        return "redirect:/admin/board/delete"
    }

    @PostMapping("/fix")
    fun fixBoards(@ModelAttribute("fixList") adminBoardFixList: AdminBoardFixList, redirectAttributes: RedirectAttributes): String {
        adminBoardService.fixBoards(adminBoardFixList, redirectAttributes)
        return "redirect:/admin/board/fix"
    }

    @PostMapping("/unfix")
    fun unfixBoards(@ModelAttribute("fixList") adminBoardFixList: AdminBoardFixList, redirectAttributes: RedirectAttributes): String {
        adminBoardService.unfixBoards(adminBoardFixList,redirectAttributes)
        return "redirect:/admin/board/fix"
    }


}