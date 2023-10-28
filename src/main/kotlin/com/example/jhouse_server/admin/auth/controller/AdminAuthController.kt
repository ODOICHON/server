package com.example.jhouse_server.admin.auth.controller

import com.example.jhouse_server.admin.auth.SessionConst
import com.example.jhouse_server.admin.auth.dto.LoginForm
import com.example.jhouse_server.domain.user.entity.Authority
import com.example.jhouse_server.domain.user.repository.UserRepository
import com.example.jhouse_server.domain.user.service.UserService
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("/admin")
class AdminAuthController(
    /**
     * =============================================================================================
     *  DI for Repository
     * =============================================================================================
     * */
    val userRepository: UserRepository,
    val userService: UserService
) {
    /**
     * =============================================================================================
     * 최초 진입 시, 로그인 페이지
     *
     * @author YoonTaeminnnn
     * @param loginForm
     * @return login 로그인페이지
     * =============================================================================================
     * */
    @GetMapping
    fun getSignIn(@ModelAttribute("loginForm") loginForm: LoginForm): String {
        return "login"
    }

    /**
     * =============================================================================================
     * 로그인
     *
     * @author YoonTaeminnnn
     * @param loginForm
     * @param bindingResult
     * @param redirectURI
     * @param request
     * @return login || redirect:redirectURI
     * =============================================================================================
     * */
    @PostMapping
    fun signIn(
        @Validated @ModelAttribute("loginForm") loginForm: LoginForm,
        bindingResult: BindingResult,
        @RequestParam(
            "redirectURI",
            defaultValue = "/admin/analysis/join-path"
        ) redirectURI: String,
        request: HttpServletRequest
    ): String {
        val findUser = userRepository.findByUserNameAndAuthority(loginForm.email!!, Authority.ADMIN)
        if (findUser.isEmpty) {
            bindingResult.reject("emailNotFound", "존재하지 않는 아이디입니다")
            return "login"
        }
        val user = findUser.get()
        if (user.password != encodePassword(loginForm.password)) {
            bindingResult.reject("passwordNotMatch", "비밀번호가 일치하지 않습니다")
            return "login"
        }
        val session = request.getSession(true)
        session.setAttribute(SessionConst.LOGINUSER, user)
        return "redirect:$redirectURI"
    }

    /**
     * =============================================================================================
     * 로그아웃
     *
     * @author YoonTaeminnnn
     * @param httpServletRequest
     * @return redirect:/admin
     * =============================================================================================
     * */
    @PostMapping("/logout")
    fun logout(httpServletRequest: HttpServletRequest): String {
        httpServletRequest.getSession(false)?.invalidate()
        return "redirect:/admin"
    }

    /**
     * =============================================================================================
     *  PRIVATE FUNCTION
     * =============================================================================================
     * */

    /**
     * =============================================================================================
     *  비밀번호 암호화
     * =============================================================================================
     * */
    private fun encodePassword(password: String?): String {
        val messageDigest = MessageDigest.getInstance("SHA-512")
        messageDigest.reset()
        if (password != null) {
            messageDigest.update(password.toByteArray(StandardCharsets.UTF_8))
        }
        return String.format("%0128x", BigInteger(1, messageDigest.digest()))
    }


}