package com.lyj.securitydomo.controller;

import com.lyj.securitydomo.config.auth.PrincipalDetails;
import com.lyj.securitydomo.domain.User;
import com.lyj.securitydomo.repository.UserRepository;
import com.lyj.securitydomo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Log4j2
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserService userService;

    @GetMapping("/join")
    public void join() {
    }

    @PostMapping("/register")
    public String register(User user, RedirectAttributes redirectAttributes) {
        log.info("회원가입 진행 : " + user);
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        user.setRole("USER");
        userRepository.save(user);

        // 회원가입 완료 메시지 추가
        redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다.");

        return "redirect:/user/join";
    }

    @GetMapping("/login")
    public void login() {
    }

    // 마이페이지 정보 조회
    @GetMapping("/mypage")
    public String getMyPage(@AuthenticationPrincipal PrincipalDetails principal, Model model) {
        log.info("mypage");

        User user = principal.getUser();
        log.info("user: " + user);
        model.addAttribute("user", user);

        return "/user/mypage";
    }

    // 사용자 정보 수정
    @PostMapping("/update")
    public String updateUser(@ModelAttribute User user) {
        User existingUser = userRepository.findById(user.getUserId()).orElseThrow();

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword(existingUser.getPassword());
        } else {
            String encPassword = bCryptPasswordEncoder.encode(user.getPassword());
            user.setPassword(encPassword);
        }
        user.setRole(existingUser.getRole());

        userService.save(user);

        return "redirect:/user/mypage";
    }

    // 마이페이지 읽기
    @GetMapping("/readmypage")
    public String readMyPage(Model model, @AuthenticationPrincipal PrincipalDetails principal) {
        return "user/readmypage";
    }

    // 회원 탈퇴 기능 추가
    @PostMapping("/delete")
    public String deleteUser(@AuthenticationPrincipal PrincipalDetails principal) {
        User user = principal.getUser();
        userService.deleteUser(user.getUserId()); // 필드명이 userId일 경우
        return "redirect:/user/logout"; // 로그아웃 후 메인 페이지로 이동
    }
}