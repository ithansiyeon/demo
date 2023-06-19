package com.example.demo.controller.user;

import com.example.demo.dto.user.UserLogDto;
import com.example.demo.entity.user.UserLog;
import com.example.demo.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import static com.example.demo.utils.CoreUtil.getClientIP;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/userLog")
    public String userLog(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        List<UserLogDto> userLogList = userService.getUserLog((String) session.getAttribute("userId"));
        model.addAttribute("userLogList",userLogList);
        model.addAttribute("currentIp", getClientIP(request));
        return "/user/userLog";
    }
}
