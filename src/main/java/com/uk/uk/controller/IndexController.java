package com.uk.uk.controller;

import org.springframework.web.bind.annotation.RequestMapping;

public class IndexController {
    @RequestMapping(value = "/{path:[^\\.]*}")
    public String redirect() {
        return "forward:/index.html";
    }
}
