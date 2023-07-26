package com.example.jhouse_server.admin.user.dto


data class AdminAgentSearch(
    val filter: AgentSearchFilter?,
    val keyword: String?
)

data class AdminJoinAgentList(
        val joinAgentIds: List<Long>?
)

data class AdminUserWithdrawalSearch(
    val filter: UserSearchFilter?,
    val keyword: String?
)

data class AdminWithdrawalList(
        val withdrawalIds: List<Long>?
)

enum class AgentSearchFilter(val value: String) {
    ESTATE("부동산");
}

enum class UserSearchFilter(val value: String){
    NICKNAME("이름")
}