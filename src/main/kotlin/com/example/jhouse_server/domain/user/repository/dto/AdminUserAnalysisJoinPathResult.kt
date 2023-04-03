package com.example.jhouse_server.domain.user.repository.dto

import com.example.jhouse_server.domain.user.entity.JoinPath
import com.querydsl.core.annotations.QueryProjection

data class AdminUserAnalysisJoinPathResult @QueryProjection constructor(
        val joinPath : JoinPath,
        val count : Long
)
