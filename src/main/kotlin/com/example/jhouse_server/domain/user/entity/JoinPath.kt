package com.example.jhouse_server.domain.user.entity

enum class JoinPath {
    INTRODUCTION("지인 소개"),
    NAVER_CAFE("네이버 카페"),
    NAVER_BLOG("네이버 블로그"),
    NAVER_BAND("네이버 밴드"),
    FACEBOOK("페이스북"),
    INSTAGRAM("인스타그램"),
    SEARCH("검색"),
    ETC("기타");

    private val value: String

    constructor(value: String) {
        this.value = value
    }

    companion object {
        fun getJoinPath(value: String): JoinPath? {
            for (joinPath in JoinPath.values()) {
                if (joinPath.value == value) {
                    return joinPath
                }
            }
            return null
        }
    }
}