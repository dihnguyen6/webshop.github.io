package com.mrKhoai.webshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FragmentsController {

    @GetMapping("/footer")
    public String getFooter() {
        return "fragments/footer";
    }

    @GetMapping("/headerfiles")
    public String getHeaderFiles() {
        return "fragments/headerfiles";
    }

    @GetMapping("/navbar")
    public String getNavbar() {
        return "fragments/navbar";
    }
}