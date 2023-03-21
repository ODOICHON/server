package com.example.jhouse_server.admin.user.controller

import com.example.jhouse_server.admin.user.SessionConst
import com.example.jhouse_server.admin.user.dto.LoginForm
import com.example.jhouse_server.admin.user.service.AdminUserService
import com.example.jhouse_server.domain.user.repository.UserRepository
import com.example.jhouse_server.domain.user.service.UserService
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

@Controller
@RequestMapping("/admin")
class AdminUserController (
        var userRepository: UserRepository,
        var userService: UserService
        ){

        // 메인 페이지 - 로그인 화면
        @GetMapping
        fun getSignIn(@ModelAttribute("loginForm") loginForm: LoginForm) : String {
                return "login"
        }

        @GetMapping("/main")
        fun getMain() : String{
                return "main"
        }

        @GetMapping("/test")
        fun getTest() : String{
                return "test"
        }


        @PostMapping
        fun signIn(@Validated @ModelAttribute("loginForm") loginForm: LoginForm,
                                                bindingResult: BindingResult,
                                                @RequestParam("redirectURI", defaultValue = "/admin/main") redirectURI : String,
                                                request: HttpServletRequest) : String {
                val findUser = userRepository.findByEmail(loginForm.email)
                if (findUser.isEmpty){
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

        private fun encodePassword(password: String?): String {
                val messageDigest = MessageDigest.getInstance("SHA-512")
                messageDigest.reset()
                if (password != null) {
                        messageDigest.update(password.toByteArray(StandardCharsets.UTF_8))
                }
                return String.format("%0128x", BigInteger(1, messageDigest.digest()))
        }



}