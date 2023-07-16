package com.example.jhouse_server.domain.user.entity

import com.example.jhouse_server.domain.board.entity.Board
import com.example.jhouse_server.domain.scrap.entity.Scrap
import com.example.jhouse_server.domain.comment.entity.Comment
import com.example.jhouse_server.domain.house.entity.House
import com.example.jhouse_server.domain.record.entity.Record
import com.example.jhouse_server.domain.record_comment.entity.RecordComment
import com.example.jhouse_server.domain.record_review.entity.RecordReview
import com.example.jhouse_server.domain.record_review_apply.entity.RecordReviewApply
import com.example.jhouse_server.domain.user.DefaultUser
import com.example.jhouse_server.domain.user.UserUpdateReqDto
import com.example.jhouse_server.domain.user.WithdrawalUser
import com.example.jhouse_server.domain.user.entity.WithdrawalStatus.*
import com.example.jhouse_server.global.entity.BaseEntity
import org.springframework.util.StringUtils
import javax.persistence.*

@Entity
@DiscriminatorValue("U")
class User(
        @Convert(converter = CryptoConverter::class)
        var email: String,

        @Convert(converter = CryptoConverter::class)
        var password: String,

        var nickName: String,

        @Convert(converter = CryptoConverter::class)
        var phoneNum: String,

        var profileImageUrl: String,

        @Convert(converter = CryptoConverter::class)
        @Enumerated(EnumType.STRING)
        var authority: Authority,

        @Convert(converter = CryptoConverter::class)
        @Enumerated(EnumType.STRING)
        var age: Age,

        @Convert(converter = CryptoConverter::class)
        @Enumerated(EnumType.STRING)
        var userType: UserType,

        @Convert(converter = CryptoConverter::class)
        @Enumerated(EnumType.STRING)
        var withdrawalStatus: WithdrawalStatus?,

        @OneToOne(cascade = [CascadeType.PERSIST], orphanRemoval = true)
        @JoinColumn(name = "withdrawal_id")
        var withdrawal: Withdrawal?,

        @OneToMany(mappedBy = "user")
        val joinPaths: MutableList<UserJoinPath> = mutableListOf(),

        @OneToMany(mappedBy = "user")
        val comments: MutableList<Comment> = mutableListOf(),

        @OneToMany(mappedBy = "user")
        val boards: MutableList<Board> = mutableListOf(),

        @OneToMany(mappedBy = "subscriber")
        val scraps: MutableList<Scrap> = mutableListOf(),

        @OneToMany(mappedBy = "user")
        val houses: MutableList<House> = mutableListOf(),

        @OneToMany(mappedBy = "user")
        val records: MutableList<Record> = mutableListOf(),

        @OneToMany(mappedBy = "reviewer")
        val applies: MutableList<RecordReviewApply> = mutableListOf(),

        @OneToMany(mappedBy = "reviewer")
        val reviews: MutableList<RecordReview> = mutableListOf(),

        @OneToMany(mappedBy = "user")
        val recordComments: MutableList<RecordComment> = mutableListOf(),

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0L,
): BaseEntity() {

    fun update(nickName: String?, password: String?, phoneNum: String?) {
        if(nickName != null && StringUtils.hasText(nickName)) this.nickName = nickName
        if(password != null && StringUtils.hasText(password)) this.password = password
        if(phoneNum != null && StringUtils.hasText(phoneNum)) this.phoneNum = phoneNum
    }

    fun updateNickName(nickName: String) {
        this.nickName = nickName
    }

    fun updatePassword(password: String) {
        this.password = password
    }

    fun updateAuthority(authority: Authority) {
        this.authority = authority
    }

    fun updateUserType(userType: UserType) {
        this.userType = userType
    }

    fun withdrawalUser() {
        this.withdrawalStatus = APPROVE
        this.nickName = WithdrawalUser().nickname
        this.email = ""
        this.password = ""
        this.phoneNum = ""
    }

    fun updateWithdrawalStatus(withdrawalStatus: WithdrawalStatus) {
        this.withdrawalStatus = withdrawalStatus
    }

    fun updateWithdrawal(withdrawal: Withdrawal) {
        this.withdrawal = withdrawal
    }
}