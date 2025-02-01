package com.tripPlanner.project.domain.login.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("error")
public class ErrorController {

    @GetMapping("/unauthorized")
    public String unauthorized(){
        return "error/unauthorized";
    }

    @GetMapping("/forbidden")
    public String forbidden() {
        return "error/forbidden"; // 🚀 forbidden.html로 이동
    }
}
