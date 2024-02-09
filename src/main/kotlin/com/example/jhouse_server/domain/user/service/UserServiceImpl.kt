package com.example.jhouse_server.domain.user.service

import com.example.jhouse_server.domain.house.repository.ReportRepository
import com.example.jhouse_server.domain.user.dto.*
import com.example.jhouse_server.domain.user.entity.*
import com.example.jhouse_server.domain.user.entity.WithdrawalStatus.WAIT
import com.example.jhouse_server.domain.user.repository.UserRepository
import com.example.jhouse_server.domain.user.repository.WithdrawalRepository
import com.example.jhouse_server.domain.user.service.common.UserServiceCommonMethod
import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode.*
import com.example.jhouse_server.global.jwt.TokenDto
import com.example.jhouse_server.global.jwt.TokenProvider
import com.example.jhouse_server.global.util.EmailUtil
import com.example.jhouse_server.global.util.RedisUtil
import com.example.jhouse_server.global.util.SmsUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils
import java.util.*

@Service
@Transactional(readOnly = true)
class UserServiceImpl (
        val userRepository: UserRepository,
        val withdrawalRepository: WithdrawalRepository,
        val tokenProvider: TokenProvider,
        val redisUtil: RedisUtil,
        val smsUtil: SmsUtil,
        val userServiceCommonMethod: UserServiceCommonMethod,
        val emailUtil: EmailUtil,
        val reportRepository: ReportRepository
): UserService {
    private val CONFIRM_CODE_EXPIRE_TIME: Long = 60 * 3  //3분

    override fun findUserById(userId: Long): UserResDto {
        // 신고 유저이면, 신고 정보 반환
        val findUser = userRepository.findById(userId).orElseThrow{ApplicationException(NOT_FOUND_EXCEPTION)}
        val reported = reportRepository.findByOwner(findUser).lastOrNull()
        return toDto(findUser, reported)
    }

    override fun checkUserName(userName: String): Boolean {
        return !userRepository.existsByUserName(userName)
    }

    override fun checkNickName(nickName: String): Boolean {
        return !userRepository.existsByNickName(nickName)
    }

    override fun sendSmsCode(phoneNum: String) {
        if (userRepository.existsByPhoneNum(phoneNum)) {
            throw ApplicationException(EXIST_PHONE_NUM)
        }
        val code = createCode()
        smsUtil.sendMessage(phoneNum, code)
        redisUtil.setValuesExpired(phoneNum, code, CONFIRM_CODE_EXPIRE_TIME)
    }

    override fun checkSmsCode(checkSmsReqDto: CheckSmsReqDto): Boolean {
        return checkCode(checkSmsReqDto.phoneNum, checkSmsReqDto.code)
    }

    @Transactional
    override fun signUp(userSignUpReqDto: UserSignUpReqDto) {
        userServiceCommonMethod.validateDuplicate(userSignUpReqDto.userName, userSignUpReqDto.nickName, userSignUpReqDto.phoneNum)

        val age: Age = Age.getAge(userSignUpReqDto.age)!!
        val joinPaths: MutableList<JoinPath> = mutableListOf()
        for(joinPath in userSignUpReqDto.joinPaths) {
            joinPaths.add(JoinPath.getJoinPath(joinPath)!!)
        }

        val terms: MutableList<Term> = mutableListOf()
        for(term in userSignUpReqDto.terms) {
            terms.add(Term.getTerm(term)!!)
        }

        if(!terms.containsAll(listOf(Term.SERVICE_USED_AGREE))) {
            throw ApplicationException(DISAGREE_TERM)
        }

        val user = User(userSignUpReqDto.email, userSignUpReqDto.userName, userServiceCommonMethod.encodePassword(userSignUpReqDto.password),
                        userSignUpReqDto.nickName, userSignUpReqDto.phoneNum, DefaultUser().profileImageUrl,
                        Authority.USER, age, UserType.NONE, null, null)
        userRepository.save(user)

        for(joinPath in joinPaths) {
            userServiceCommonMethod.saveUserJoinPath(joinPath, user)
        }

        for(term in terms) {
            userServiceCommonMethod.saveUserTerm(term, user)
        }
    }

    override fun signIn(userSignInReqDto: UserSignInReqDto): TokenDto {
        val user = userRepository.findByUserNameAndSuspension(userSignInReqDto.userName, false)
                .orElseThrow{ ApplicationException(DONT_EXIST_USERNAME) }
        if (user.password != userServiceCommonMethod.encodePassword(userSignInReqDto.password)) {
            throw ApplicationException(DONT_MATCH_PASSWORD)
        }

        return tokenProvider.createTokenResponse(user)
    }

    override fun reissue(bearerToken: String, refreshToken: String): TokenDto {tokenProvider.validateToken(refreshToken, false)

        val accessToken = tokenProvider.resolveToken(bearerToken).toString()
        val userName = tokenProvider.getSubject(accessToken)
        val user = userRepository.findByUserNameAndSuspension(userName, false)
                .orElseThrow{ ApplicationException(DONT_EXIST_USERNAME) }
        return tokenProvider.createTokenResponse(user)
    }

    override fun logout(token: String) {
        redisUtil.deleteValues(token)
    }

    @Transactional
    override fun update(user: User, userUpdateReqDto: UserUpdateReqDto) {
        if(user.password != userServiceCommonMethod.encodePassword(userUpdateReqDto.password)) {
            throw ApplicationException(DONT_MATCH_PASSWORD)
        }
        var nickName: String? = null
        var password: String? = null
        var phoneNum: String? = null
        if(userUpdateReqDto.nickName != null && StringUtils.hasText(userUpdateReqDto.nickName)) {
            userServiceCommonMethod.validateNickName(userUpdateReqDto.nickName)
            nickName = userUpdateReqDto.nickName
        }
        if(userUpdateReqDto.newPassword != null && StringUtils.hasText(userUpdateReqDto.newPassword)) {
            userServiceCommonMethod.validatePassword(userUpdateReqDto.newPassword)
            password = userServiceCommonMethod.encodePassword(userUpdateReqDto.newPassword)
        }
        if(userUpdateReqDto.phoneNum != null && StringUtils.hasText(userUpdateReqDto.phoneNum)) {
            userServiceCommonMethod.validatePhoneNum(userUpdateReqDto.phoneNum)
            phoneNum = userUpdateReqDto.phoneNum
        }
        user.update(nickName, password, phoneNum)
    }

    @Transactional
    override fun updateNickName(user: User, nickName: String) {
        if (userRepository.existsByNickName(nickName)) {
            throw ApplicationException(EXIST_NICK_NAME)
        }

        user.updateNickName(nickName)
    }

    @Transactional
    override fun updatePassword(user: User, password: String) {
        val encodePassword = userServiceCommonMethod.encodePassword(password)
        if (user.password == encodePassword) {
            throw ApplicationException(SAME_PASSWORD)
        }

        user.updatePassword(encodePassword)
    }

    @Transactional
    override fun withdrawal(user: User, withdrawalUserReqDto: WithdrawalUserReqDto) {
        if(user.withdrawal != null) {
            throw ApplicationException(WITHDRAWAL_WAIT)
        }
        val withdrawalReasons = withdrawalUserReqDto.reason.map { WithdrawalReason.getReasonByValue(it) }.toList()
        val withdrawal = Withdrawal(withdrawalReasons, withdrawalUserReqDto.content)

        withdrawalRepository.save(withdrawal)
        user.updateWithdrawal(withdrawal)
        user.updateWithdrawalStatus(WAIT)
    }

    override fun sendEmailCode(email: String) {
        if(userRepository.findByEmail(email).isPresent) throw ApplicationException(EXIST_EMAIL)
        val code = createCode()
        emailUtil.sendMessage(email, code)
        redisUtil.setValuesExpired(email, code, CONFIRM_CODE_EXPIRE_TIME)
    }

    override fun checkEmailCode(checkEmailReqDto: CheckEmailReqDto): Boolean {
        return checkCode(checkEmailReqDto.email, checkEmailReqDto.code)
    }

    @Transactional
    override fun updateEmail(user: User, email: String) {
        if(userRepository.findByEmail(email).isPresent) throw ApplicationException(EXIST_EMAIL)
        user.updateEmail(email)
    }

    @Transactional
    override fun updatePhoneNum(user: User, phoneNum: String) {
        if(userRepository.existsByPhoneNum(phoneNum)) throw ApplicationException(EXIST_PHONE_NUM)
        user.updatePhoneNum(phoneNum)
    }

    override fun checkPassword(user: User, password: PasswordReqDto): Boolean {
        val user = userRepository.findById(user.id)
                .orElseThrow{ ApplicationException(DONT_EXIST_USERNAME) }
        return user.password == userServiceCommonMethod.encodePassword(password.password)
    }

    private fun createCode(): String {
        val random: Random = Random()

        return String.format("%04d", random.nextInt(10000))
    }

    private fun checkCode(key: String, code: String) : Boolean {
        val savedCode : String = redisUtil.getValues(key).toString()

        if(savedCode == code) {
            redisUtil.deleteValues(key)
            return true
        }
        return false
    }
}