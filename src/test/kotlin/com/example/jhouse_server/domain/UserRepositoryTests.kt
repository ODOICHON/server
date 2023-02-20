package com.example.jhouse_server.domain

import com.example.jhouse_server.domain.user.User
import com.example.jhouse_server.domain.user.UserRepository
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner

//@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTests @Autowired constructor(
        val entityManager: TestEntityManager,
        val userRepository: UserRepository
) {

    @Test
    fun 유저단일조회() {
        // given
        val newUser = User("userId1", "010-1234-5678")
        entityManager.persist(newUser)
        entityManager.flush()

        // when
        val found = userRepository.findById(newUser.id!!)

        // then
        assertThat(found.get()).isEqualTo(newUser)
    }
}