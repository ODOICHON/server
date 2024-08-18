package com.example.jhouse_server.domain.house.entity

import com.example.jhouse_server.domain.board.entity.BoardImageUrlConverter
import com.example.jhouse_server.domain.scrap.entity.Scrap
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.entity.BaseEntity
import javax.persistence.*

@Table(
    name = "house",
    indexes = [Index(
        name = "idx__rental_type",
        columnList = "rentalType, useYn, tmpYn, reported, applied"
    ),
        Index(name = "idx__title", columnList = "title, useYn, tmpYn, reported, applied")
    ]
)
@Entity
class House(

    var agentDetail: String?,

    @Enumerated(EnumType.STRING)
    @Column(name = "house_type")
    var houseType: HouseType, // 매물타입

    @Convert(converter = RentalTypeConverter::class)
    @Column(length = 40)
    var rentalType: RentalType, // 매물유형

    @Embedded
    var address: Address, // 주소

    @Column(length = 10)
    var size: String, // 집 크기( m2 )

    @Column(length = 100)
    var purpose: String?, // 용도 ( 예: 주택 )

    @Column(nullable = true)
    var floorNum: Int, // 층수 ( 다가구인 경우에만 )

    @Column(length = 13)
    var contact: String, // 바로 연락 가능한 연락처

    @Column(length = 5)
    var createdDate: String?, // 준공연도,

    var price: Int, // 매물가격

    @Column(nullable = true)
    var monthlyPrice: Double, // 월세의 경우,

    @Column(length = 20)
    var agentName: String, // 공인중개사명

    @Column(length = 50)
    var title: String, // 게시글 제목

    @Column(length = Int.MAX_VALUE)
    var content: String, // 게시글 내용

    @Column(length = Int.MAX_VALUE)
    var code: String, // 게시글 코드

    @Column(length = Int.MAX_VALUE)
    @Convert(converter = BoardImageUrlConverter::class) // 이미지 url ","로 슬라이싱
    var imageUrls: List<String>,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    var useYn: Boolean = true, // 삭제여부 ( 미삭제 : true, 삭제 : false )

    var tmpYn: Boolean = false, // 임시저장여부 ( 임시저장: true, 저장 : false )

    var reported: Boolean = false, // 신고여부 ( 신고: true, 미신고 : false )

    @Convert(converter = HouseReviewStatusConverter::class)
    @Column(length = 10)
    var applied: HouseReviewStatus? = null, // 신청여부

    @Column(nullable = true, length = 200)
    var rejectReason: String? = null, // 관리자가 게시글을 반려한 이유

    @Column(length = 10)
    var dealState: DealState = DealState.APPLYING, // 판매상태 ( 기본값 : 승인중 )

    @OneToMany(mappedBy = "house", cascade = [CascadeType.ALL], orphanRemoval = true)
    var scrap: MutableList<Scrap> = mutableListOf(),

    @OneToMany(mappedBy = "house", cascade = [CascadeType.ALL], orphanRemoval = true)
    var houseTag: MutableList<HouseTag> = mutableListOf(),

    @OneToMany(mappedBy = "house", cascade = [CascadeType.ALL])
    var reports: MutableList<Report> = mutableListOf(),

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
) : BaseEntity() {

    @OneToOne
    lateinit var deal: Deal

    fun updateEntity(
        agentDetail: String?,
        houseType: HouseType,
        rentalType: RentalType,
        size: String,
        purpose: String?,
        floorNum: Int,
        contact: String,
        createdDate: String?,
        price: Int,
        monthlyPrice: Double,
        agentName: String,
        title: String,
        content: String,
        code: String,
        imageUrls: List<String>
    ): House {
        this.agentDetail = agentDetail
        this.houseType = houseType
        this.rentalType = rentalType
        this.size = size
        this.purpose = purpose
        this.floorNum = floorNum
        this.contact = contact
        this.createdDate = createdDate
        this.price = price
        this.monthlyPrice = monthlyPrice
        this.agentName = agentName
        this.title = title
        this.content = content
        this.code = code
        this.imageUrls = imageUrls
        return this
    }

    fun deleteEntity() {
        this.useYn = false
    }

    fun reportEntity() {
        this.reported = true
        this.dealState = DealState.REJECTED
    }

    fun applyEntity() {
        this.applied = HouseReviewStatus.APPLY
    }

    fun approveEntity() {
        this.applied = HouseReviewStatus.APPROVE
        this.dealState = DealState.ONGOING
    }

    fun rejectEntity(rejectReason: String) {
        this.applied = HouseReviewStatus.REJECT
        this.rejectReason = rejectReason
    }

    fun addScrap(scrap: Scrap): House {
        this.scrap.add(scrap)
        return this
    }

    fun deleteScrap(scrap: Scrap) {
        this.scrap.remove(scrap)
    }

    // 임시저장
    fun tmpSaveEntity() {
        this.tmpYn = true
    }

    fun saveEntity() {
        this.tmpYn = false
    }

    fun updateDealStatus() {
        this.dealState = DealState.COMPLETED
    }

    fun addHouseTag(houseTag: HouseTag): House {
        this.houseTag.add(houseTag)
        return this
    }

    fun deleteHouseTag(houseTag: HouseTag) {
        this.houseTag.remove(houseTag)
    }
}