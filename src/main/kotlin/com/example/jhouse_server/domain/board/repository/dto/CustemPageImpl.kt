package com.example.jhouse_server.domain.board.repository.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

@JsonIgnoreProperties(ignoreUnknown = true)
class CustomPageImpl<T> : PageImpl<T> {

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    constructor(@JsonProperty("content") content: List<T>, @JsonProperty("page") page: Int, @JsonProperty("size") size: Int, @JsonProperty("totalElements") total: Long) :
            super(content, PageRequest.of(page, size), total) {
    }

    @JsonIgnore
    override fun getPageable(): Pageable {
        return super<PageImpl>.getPageable()
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class CustomMyPageImpl<T> : PageImpl<T> {
    var cntAll: Long
    var cntApply : Long
    var cntOngoing : Long
    var cntCompleted : Long

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    constructor(
        @JsonProperty("content") content: List<T>,
        @JsonProperty("page") page: Int,
        @JsonProperty("size") size : Int,
        @JsonProperty("totalElements") total: Long,
        @JsonProperty("cntAll") cntAll : Long,
        @JsonProperty("cntApply") cntApply: Long,
        @JsonProperty("cntOngoing") cntOngoing : Long,
        @JsonProperty("cntCompleted") cntCompleted : Long
    ) : super(content, PageRequest.of(page, size), total) {
        this.cntAll = cntAll
        this.cntApply = cntApply
        this.cntOngoing = cntOngoing
        this.cntCompleted = cntCompleted
    }
    @JsonIgnore
    override fun getPageable(): Pageable {
        return super<PageImpl>.getPageable()
    }
}