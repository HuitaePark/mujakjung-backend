package com.mujakjung.page;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @Value("${kakao.map.Key}")
    private String key;
    //메인페이지
    @GetMapping("/")
    public String mujakjung(Model model){
        model.addAttribute("appKey",key);
        return "mujakjung";
    }
    //회원가입 성공시 로그인페이지로 이동
    @GetMapping("/login-page")
    public String login(){
        return "login";
    }
    //회원가입 페이지
    @GetMapping("/join")
    public String joinProc(){
        return "join";
    }
    //마이페이지
    @GetMapping("/myPage")
    public String myPage(){
        return "myPage";
    }


}
