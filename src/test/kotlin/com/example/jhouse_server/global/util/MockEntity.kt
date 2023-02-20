package com.example.jhouse_server.global.util

import com.example.jhouse_server.domain.post.entity.Post
import com.example.jhouse_server.domain.post.entity.PostCategory
import com.example.jhouse_server.domain.user.entity.Authority
import com.example.jhouse_server.domain.user.entity.User

class MockEntity {
    companion object {
        fun testUser1() = User(
                email = "ZZang9@test.com",
                password = "password123!",
                nickName = "짱구",
                phoneNum = "010-1234-5678",
                authority = Authority.USER
        )

        fun testUser2() = User(
                email = "IronSu@test.com",
                password = "password123!",
                nickName = "철수",
                phoneNum = "010-9876-5432",
                authority = Authority.USER
        )

        fun testPost1(writer : User) = Post(
                code = "<html></html>",
                title = "짱구는 못말려",
                category = PostCategory.DAILY,
                content = "짱구가 작성한 철수 관찰 일기",
                imageUrls = emptyList(),
                address = "떡잎마을",
                isSaved = true,
                user = writer,
                love = 1
        )
    }
}