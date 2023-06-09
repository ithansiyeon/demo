package com.example.demo.controller.user;

import com.example.demo.dto.user.AutoLoginForm;
import com.example.demo.dto.user.UserDto;
import com.example.demo.dto.user.UserLogDto;
import com.example.demo.entity.user.User;
import com.example.demo.entity.user.UserLog;
import com.example.demo.service.user.UserService;
import com.example.demo.springsecurity.AutoLoginUtil;
import com.example.demo.springsecurity.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.utils.CoreUtil.getClientIP;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserDetailsServiceImpl userDetailsService;

    @GetMapping("/userLog")
    public String userLog(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        List<UserLogDto> userLogList = userService.getUserLog((String) session.getAttribute("loginId"));
        model.addAttribute("userLogList",userLogList);
        model.addAttribute("currentIp", getClientIP(request));
        return "user/userLog";
    }

    @GetMapping("/user/userList")
    public String userList(Model model, @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by("id").descending());
        Page<UserDto> userList = userService.getUserList(pageRequest);
        model.addAttribute("startPage", Math.floor(userList.getNumber() / userList.getSize()) * userList.getSize() + 1);
        model.addAttribute("userList", userList);
        model.addAttribute("count", userList.getTotalElements());
        return "user/userList";
    }

    @GetMapping("/user/autoLogin/{userId}")
    public String autoLogin(@PathVariable(value = "userId", required = true) String userId, Model model) {
        model.addAttribute("userId",userId);
        return "user/autoLoginForm";
    }

    @PostMapping("/user/autoLogin/{userId}")
    public ResponseEntity<String> autoLoginProc(@PathVariable(value = "userId", required = true) String userId, @ModelAttribute AutoLoginForm autoLoginForm, HttpServletRequest request) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
        User user = userService.getUserByUserId(userId);
        AutoLoginUtil.autoLogin(request, userDetails, userId);
        UserLog userLog = UserLog.builder().ip(getClientIP(request)).isLoginSuccess("Y").user(user).autoYn("Y").reasonType(autoLoginForm.getReasonType()).otherReason(autoLoginForm.getOtherReason()).build();
        userService.insertUserLog(userLog);
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    @GetMapping("/user/userRegister")
    public String userRegister(Model model) {
        return "user/userRegister";
    }
}
