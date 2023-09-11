package com.example.jhouse_server.domain.user.entity

enum class Term(val value : String) {
    SERVICE_USED_AGREE("서비스 이용 약관에 대한 동의"),
    PERSONAL_INFO_NOTI("개인정보 수집 이용에 대한 안내"),
    PERSONAL_INFO_USED_AGREE("개인정보 수집 및 이용 동의"), // 선택
    MARKETING_ADVERTISEMENT_AGREE("마케팅 활용 및 광고성 정보 수신 동의"), // 선택
    ;

    companion object {
        fun getTerm(name : String) : Term? {
            for(term in Term.values()) {
                if(term.name == name) {
                    return term
                }
            }
            return null
        }
    }
}