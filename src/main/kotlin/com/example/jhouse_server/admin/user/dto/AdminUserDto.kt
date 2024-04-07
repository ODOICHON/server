package com.example.jhouse_server.admin.user.dto

import com.example.jhouse_server.domain.user.entity.Age
import com.example.jhouse_server.domain.user.entity.JoinPath
import com.example.jhouse_server.domain.user.entity.Term
import com.example.jhouse_server.domain.user.entity.UserType
import com.querydsl.core.annotations.QueryProjection
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime

/**
 * ============================================================================================
 * AdminAgentSearch               -- 공인중개사 검색 조건
 * filter                         -- 검색 필터 enum class
 * keyword                        -- 검색 키워드
 * ============================================================================================
 * */
data class AdminAgentSearch(
    val filter: AgentSearchFilter?,
    val keyword: String?
)

/**
 * ============================================================================================
 * AdminJoinAgentList               -- 공인중개사 가입 승인 대상 VO
 * joinAgentIds                     -- 공인중개사 UserID 리스트 ( 다중 선택으로 일괄 가입 승인 처리를 위함 )
 * ============================================================================================
 * */
data class AdminJoinAgentList(
        val joinAgentIds: List<Long>?
)

/**
 * ============================================================================================
 * AdminUserWithdrawalSearch      -- 사용자 회원 탈퇴 ( 데이터 영구 삭제 )
 * filter                         -- 검색 필터 enum class
 * keyword                        -- 검색 키워드
 * ============================================================================================
 * */
data class AdminUserWithdrawalSearch(
    val filter: UserSearchFilter?,
    val keyword: String?
)

/**
 * ============================================================================================
 * AdminWithdrawalList              -- 사용자 회원 탈퇴 대상 VO
 * withdrawalIds                    -- 사용자 UserID 리스트 ( 다중 선택으로 일괄 영구 삭제 )
 * ============================================================================================
 * */
data class AdminWithdrawalList(
        val withdrawalIds: List<Long>?
)

/**
 * ============================================================================================
 * ENUM CLASS
 * ============================================================================================
 * */
enum class AgentSearchFilter(val value: String) {
    ESTATE("부동산");
}

enum class UserSearchFilter(val value: String){
    NICKNAME("닉네임")
}

data class AdminUserList @QueryProjection constructor(
    val id: Long,
    val nickName: String,
    val email: String,
    val userType: UserType,
    val phoneNum : String,
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val createdAt : LocalDateTime,
    val age: String,
    val joinPath: Set<String>,
    val term : Set<String>,
){
    fun getAgeValues() : String{
        return Age.valueOf(age).value ?: ""
    }
    fun getJoinPathValues(): Set<String> {
        return joinPath.map { JoinPath.valueOf(it)?.value }.toSet() as Set<String>
    }

    fun getTermValues(): Set<String> {
        return term.map { Term.valueOf(it)?.value }.toSet() as Set<String>
    }

}
    data class AdminUserQueryResult(
        val id: Long,
        val nickName: String,
        val email: String,
        val userType: UserType,
        val phoneNum : String,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        val createdAt : LocalDateTime,
        val age: String,
    )
