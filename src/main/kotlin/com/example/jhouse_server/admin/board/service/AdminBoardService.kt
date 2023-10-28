package com.example.jhouse_server.admin.board.service

import com.example.jhouse_server.admin.board.dto.AdminBoardDeleteList
import com.example.jhouse_server.admin.board.dto.AdminBoardFixList
import com.example.jhouse_server.admin.board.dto.AdminBoardSearch
import com.example.jhouse_server.domain.board.PrefixCategory.ADVERTISEMENT
import com.example.jhouse_server.domain.board.entity.Board
import com.example.jhouse_server.domain.board.repository.BoardRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Service
@Transactional(readOnly = true)
class AdminBoardService(
    /**
     * =============================================================================================
     *  DI for Repository
     * =============================================================================================
     * */
        val boardRepository: BoardRepository
) {
    /**
     * =============================================================================================
     *  고정 탭에서의 게시물 목록 조회
     *  @param adminBoardSearch
     *  @param pageable
     *  @return Page<Board>
     * =============================================================================================
     * */
    fun getSearchFixableBoardResult(adminBoardSearch: AdminBoardSearch, pageable: Pageable): Page<Board> {
        return boardRepository.getFixableBoardListWithPaging(adminBoardSearch, pageable)
    }
    /**
     * =============================================================================================
     *  게시글 고정 처리
     *  @param adminBoardFixList
     *  @param redirectAttributes
     * =============================================================================================
     * */
    @Transactional
    fun fixBoards(adminBoardFixList: AdminBoardFixList, redirectAttributes: RedirectAttributes) {
        // (1) 고정 가능한 게시글 목록 조회
        val findBoards = boardRepository.findByFixedAndPrefixCategoryAndUseYn(true, ADVERTISEMENT, true)
        // (2) 중복값을 허용하지 않기 위해 Set 자료 구조 정의
        val boardSet = findBoards.toCollection(mutableSetOf())
        // (3) 다중 선택으로 가져온 고정하고자 하는 게시글 목록 조회
        val requestBoards = adminBoardFixList.fixBoardIds?.let { boardRepository.findByIds(it) }
        // (4) 비교하며 게시글 고정 상태로 변경
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
    /**
     * =============================================================================================
     *  게시글 고정 해제
     *  @param adminBoardFixList
     *  @param redirectAttributes
     * =============================================================================================
     * */
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

    /**
     * =============================================================================================
     *  고정 탭에서의 게시물 상세 조회
     *  @param id
     *  @param redirectAttributes
     * =============================================================================================
     * */
    fun getBoardDetail(id: Long, redirectAttributes: RedirectAttributes): String? {
        val findBoard = boardRepository.findByIdAndUseYn(id, true)
        if (findBoard.isPresent) {
            return findBoard.get().boardCode.code
        }
        redirectAttributes.addAttribute("not_found_board", "게시물을 찾을 수 없습니다")
        return null
    }
    /**
     * =============================================================================================
     *  고정 가능한 게시글 총 개수
     * =============================================================================================
     * */
    fun getFixableBoardTotal(): Long{
        return boardRepository.countByPrefixCategoryAndUseYn(ADVERTISEMENT, true)
    }
    /**
     * =============================================================================================
     *  고정 게시글 총 개수
     * =============================================================================================
     * */
    fun getFixedBoardList(): List<Board>{
        return boardRepository.findByFixedAndPrefixCategoryAndUseYn(true, ADVERTISEMENT, true)
    }
    /**
     * =============================================================================================
     *  영구삭제 탭에서의 게시물 조회
     *  @param id
     *  @param redirectAttributes
     * =============================================================================================
     * */
    fun getDeletableBoardDetail(id: Long, redirectAttributes: RedirectAttributes): String? {
        val findBoard = boardRepository.findById(id)
        if (findBoard.isPresent) {
            return findBoard.get().boardCode.code
        }
        redirectAttributes.addAttribute("not_found_board", "게시물을 찾을 수 없습니다")
        return null
    }
    /**
     * =============================================================================================
     *  영구삭제 탭에서의 게시물 목록 조회 ( 페이징 )
     *  @param adminBoardSearch
     *  @param pageable
     * =============================================================================================
     * */
    fun getSearchDeletableBoardResult(adminBoardSearch: AdminBoardSearch, pageable: Pageable): Page<Board> {
        return boardRepository.getDeletableBoardListWithPaging(adminBoardSearch, pageable)
    }
    /**
     * =============================================================================================
     *  영구삭제
     *  @param adminBoardDeleteList
     * =============================================================================================
     * */
    @Transactional
    fun deleteBoards(adminBoardDeleteList: AdminBoardDeleteList) {
        val requestBoards = adminBoardDeleteList.deleteBoardList?.let { boardRepository.findByIds(it) }
        requestBoards?.forEach { b -> boardRepository.delete(b) }
    }
    /**
     * =============================================================================================
     *  영구삭제 게시글 총 개수
     * =============================================================================================
     * */
    fun getDeletableBoardTotal(): Long {
        return boardRepository.countByUseYn(false)
    }
}