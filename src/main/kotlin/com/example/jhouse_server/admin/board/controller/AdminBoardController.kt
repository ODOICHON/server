package com.example.jhouse_server.admin.board.controller

import com.example.jhouse_server.admin.board.dto.AdminBoardDeleteList
import com.example.jhouse_server.admin.board.dto.AdminBoardFixList
import com.example.jhouse_server.admin.board.dto.AdminBoardSearch
import com.example.jhouse_server.admin.board.dto.SearchFilter
import com.example.jhouse_server.admin.board.service.AdminBoardService
import com.example.jhouse_server.domain.board.PrefixCategory
import com.example.jhouse_server.domain.board.PrefixCategory.*
import com.example.jhouse_server.domain.board.repository.BoardRepository
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/admin/board")
class AdminBoardController(
        var adminBoardService: AdminBoardService,
        var boardRepository: BoardRepository
) {


    @GetMapping("/fix")
    fun getDeletedBoardList(@ModelAttribute("searchForm") adminBoardSearch: AdminBoardSearch
                            , @ModelAttribute("fixList") adminBoardFixList: AdminBoardFixList
                            , model: Model
                            , @PageableDefault(size = 10, page = 0) pageable: Pageable): String {
        val result = adminBoardService.getSearchFixableBoardResult(adminBoardSearch, pageable)
        model.addAttribute("boardList", result)

        val pageCom = pageable.pageNumber / 5
        model.addAttribute("pageCom", pageCom)
        model.addAttribute("filterList", SearchFilter.values())
        model.addAttribute("total", boardRepository.countByPrefixCategoryAndUseYn(ADVERTISEMENT, true))

        val fixedBoards = boardRepository.findByFixedAndPrefixCategoryAndUseYn(true, ADVERTISEMENT, true)
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
        model.addAttribute("filterList", SearchFilter.values())
        model.addAttribute("total", boardRepository.countByUseYn(false))

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