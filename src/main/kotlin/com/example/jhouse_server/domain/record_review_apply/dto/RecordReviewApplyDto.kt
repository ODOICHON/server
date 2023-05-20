package com.example.jhouse_server.domain.record_review_apply.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class RecordReviewApplyResDto(
    val status: String,
    @JsonProperty("nick_name")
    val nickName: String
)
