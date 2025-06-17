package com.mujakjung.page;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @Value("${kakao.map.Key}")
    private String key;

    @GetMapping("/")
    public String mujakjung(Model model){
        model.addAttribute("appKey",key);
        return "mujakjung";
    }

}
