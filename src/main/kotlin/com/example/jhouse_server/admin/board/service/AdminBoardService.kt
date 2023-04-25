package com.example.jhouse_server.admin.board.service

import com.example.jhouse_server.admin.board.dto.AdminBoardDeleteList
import com.example.jhouse_server.admin.board.dto.AdminBoardFixList
import com.example.jhouse_server.admin.board.dto.AdminBoardSearch
import com.example.jhouse_server.domain.board.PrefixCategory.*
import com.example.jhouse_server.domain.board.entity.Board
import com.example.jhouse_server.domain.board.repository.BoardCodeRepository
import com.example.jhouse_server.domain.board.repository.BoardRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Service
@Transactional(readOnly = true)
class AdminBoardService(
        var boardRepository: BoardRepository
) {

    /*
    고정 탭에서의 게시물 목록 조회
     */
    fun getSearchFixableBoardResult(adminBoardSearch: AdminBoardSearch, pageable: Pageable): Page<Board> {
        return boardRepository.getFixableBoardListWithPaging(adminBoardSearch, pageable)
    }

    /*
    고정
     */
    @Transactional
    fun fixBoards(adminBoardFixList: AdminBoardFixList, redirectAttributes: RedirectAttributes) {
        val findBoards = boardRepository.findByFixedAndPrefixCategoryAndUseYn(true, ADVERTISEMENT, true)
        val boardSet = findBoards.toCollection(mutableSetOf())
        val requestBoards = adminBoardFixList.fixBoardIds?.let { boardRepository.findByIds(it) }
        requestBoards?.forEach { b ->
            boardSet.add(b)
            if (b.fixed) {
                redirectAttributes.addAttribute("already", "고정 게시물을 포함하고 있습니다")
            }
        }
        if (redirectAttributes.containsAttribute("already")) return
        if (boardSet.size > 10) {
            redirectAttributes.addAttribute("overFix", "최대 10개까지 고정가능합니다")
            return
        }
        requestBoards?.forEach { b -> b.updateFixed(true) }
    }

    /*
    고정 해제
     */
    @Transactional
    fun unfixBoards(adminBoardFixList: AdminBoardFixList, redirectAttributes: RedirectAttributes) {
        val requestBoards = adminBoardFixList.fixBoardIds?.let { boardRepository.findByIds(it) }
        requestBoards?.forEach { b ->
            if (!b.fixed) {
                redirectAttributes.addAttribute("unfix_fail", "고정되어 있지 않은 게시물을 포함하고 있습니다")
                return
            }
        }
        requestBoards?.forEach { b -> b.updateFixed(false) }
    }

    /*
    고정 탭에서의 게시물 조회 : useYn 여부 확인 필요
     */
    fun getBoardDetail(id: Long, redirectAttributes: RedirectAttributes): String? {
        val findBoard = boardRepository.findByIdAndUseYn(id, true)
        if (findBoard.isPresent) {
            return findBoard.get().boardCode.code
        }
        redirectAttributes.addAttribute("not_found_board", "게시물을 찾을 수 없습니다")
        return null
    }

    /*
    영구삭제 탭에서의 게시물 조회
     */
    fun getDeletableBoardDetail(id: Long, redirectAttributes: RedirectAttributes): String? {
        val findBoard = boardRepository.findById(id)
        if (findBoard.isPresent) {
            return findBoard.get().boardCode.code
        }
        redirectAttributes.addAttribute("not_found_board", "게시물을 찾을 수 없습니다")
        return null
    }
    /*
    영구삭제 탭에서의 게시물 목록 조회
     */
    @Transactional
    fun getSearchDeletableBoardResult(adminBoardSearch: AdminBoardSearch, pageable: Pageable): Page<Board> {
        return boardRepository.getDeletableBoardListWithPaging(adminBoardSearch, pageable)
    }

    /*
    영구삭제
     */
    @Transactional
    fun deleteBoards(adminBoardDeleteList: AdminBoardDeleteList) {
        val requestBoards = adminBoardDeleteList.deleteBoardList?.let { boardRepository.findByIds(it) }
        requestBoards?.forEach { b -> boardRepository.delete(b) }
    }
}