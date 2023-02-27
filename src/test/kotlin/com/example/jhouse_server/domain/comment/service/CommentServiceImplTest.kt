//package com.example.jhouse_server.domain.comment.service // ktlint-disable package-name
//
//import com.example.jhouse_server.domain.comment.entity.Comment
//import com.example.jhouse_server.domain.comment.repository.CommentRepository
//import com.example.jhouse_server.domain.post.entity.Post
//import com.example.jhouse_server.domain.post.repository.PostRepository
//import com.example.jhouse_server.domain.user.entity.User
//import com.example.jhouse_server.domain.user.repository.UserRepository
//import com.example.jhouse_server.global.util.MockEntity
//import org.assertj.core.api.Assertions.assertThat
//import org.junit.jupiter.api.AfterAll
//import org.junit.jupiter.api.BeforeAll
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.TestInstance
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.transaction.annotation.Transactional
//
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@Transactional
//@SpringBootTest
//internal class CommentServiceImplTest @Autowired constructor(
//        val commentServiceImpl: CommentServiceImpl,
//        val userRepository: UserRepository,
//        val postRepository: PostRepository,
//        val commentRepository: CommentRepository,
//) {
//    lateinit var user: User
//    lateinit var post: Post
//    var comments: MutableList<Comment> = mutableListOf()
//
//    @BeforeAll
//    fun setUp() {
//        initCommentData()
//    }
//
//    @AfterAll
//    fun clear() {
//        clearCommentData()
//    }
//
//    fun clearCommentData() {
//        commentRepository.deleteAll(comments)
//        postRepository.delete(post)
//        userRepository.delete(user)
//    }
//
//    @Test
//    fun `댓글 조회 테스트`() {
//        // when
//        val comments = commentServiceImpl.getCommentAll(post.id)
//
//        // then
//        assertThat(comments.size).isEqualTo(10)
//    }
//    fun initCommentData() {
//        user = userRepository.save(
//            MockEntity.testUser1(),
//        )
//        post = postRepository.save(
//            MockEntity.testPost1(user),
//        )
//        for (i in 1..10) {
//            comments.add(Comment(post, "Comment $i", user))
//        }
//        commentRepository.saveAll(comments)
//    }
//}
