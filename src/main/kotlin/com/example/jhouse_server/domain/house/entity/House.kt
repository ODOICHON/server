package com.example.jhouse_server.domain.house.entity

import com.example.jhouse_server.domain.board.entity.*
import com.example.jhouse_server.domain.scrap.entity.Scrap
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.entity.BaseEntity
import javax.persistence.*

@Entity
class House(
    @Convert(converter = RentalTypeConverter::class)
    var houseType: RentalType, // 매물유형

    @Embedded
    var address: Address, // 주소

    var size: String, // 집 크기( m2 )

    var purpose: String, // 용도 ( 예: 주택 )

    @Column(nullable = true)
    var floorNum : Int, // 층수 ( 다가구인 경우에만 )

    var contact : String, // 바로 연락 가능한 연락처

    var createdDate : String, // 준공연도,

    var price: Int, // 매물가격

    @Column(nullable = true)
    var monthlyPrice : Double, // 월세의 경우,

    var agentName: String, // 공인중개사명

    var title: String, // 게시글 제목

    @Column(length = Int.MAX_VALUE)
    var content : String, // 게시글 내용

    @Column(length = Int.MAX_VALUE)
    var code : String, // 게시글 코드

    @Column(length = Int.MAX_VALUE)
    @Convert(converter = BoardImageUrlConverter::class) // 이미지 url ","로 슬라이싱
    var imageUrls : List<String>,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user : User,

    var useYn : Boolean = true, // 삭제여부 ( 미삭제 : true, 삭제 : false )

    var reported : Boolean = false, // 신고여부 ( 신고: true, 미신고 : false )

    @Column(nullable = true, length = 101)
    var reportReason : String? = null, // 신고 사유 ( 100자 )

    @Convert(converter = HouseReviewStatusConverter::class)
    var applied : HouseReviewStatus? = null, // 신청여부 ( 신청 : true, 미신청 : false )

    @Column(nullable = true)
    var rejectReason: String? = null, // 관리자가 게시글을 반려한 이유

    @OneToMany(mappedBy = "house", cascade = [CascadeType.ALL], orphanRemoval = true)
    var scrap: MutableList<Scrap> = mutableListOf(),

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
) : BaseEntity() {

    fun updateEntity(
         rentalType: RentalType,
         size: String,
         purpose: String,
         floorNum: Int,
         contact: String,
         createdDate: String,
         price: Int,
         monthlyPrice: Double,
         agentName : String,
         title: String,
         code: String,
         content : String,
         imageUrls: List<String>
    ) : House {
        this.houseType = rentalType
        this.size = size
        this.purpose = purpose
        this.floorNum = floorNum
        this.contact = contact
        this.createdDate = createdDate
        this.price = price
        this.monthlyPrice = monthlyPrice
        this.agentName = agentName
        this.title = title
        this.code = code
        this.content = content
        this.imageUrls = imageUrls
        this.applied = HouseReviewStatus.APPROVE
        return this
    }
    fun deleteEntity() {
        this.useYn = false
    }

    fun reportEntity(reportReason: String) {
        this.reported = true
        this.reportReason = reportReason
    }

    fun applyEntity() {
        this.applied = HouseReviewStatus.APPLY
    }

    fun approveEntity() {
        this.applied = HouseReviewStatus.APPROVE
    }

    fun rejectEntity(rejectReason: String) {
        this.applied = HouseReviewStatus.REJECT
        this.rejectReason = rejectReason
    }

    fun addScrap(scrap: Scrap) : House {
        this.scrap.add(scrap)
        return this
    }

    fun deleteScrap(scrap: Scrap) {
        this.scrap.remove(scrap)
    }
}