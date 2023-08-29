package com.ticketseller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @RequestMapping(path = "/")
    public String index() {
        return "redirect:/swagger-ui/index.html";
    }
}
