package com.study.myproject01.members.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/members")
public class MembersController {

    @GetMapping("/hello")
    public String getHello(){
        return "Hello World! ";
    }

    @PostMapping("/hi")
    public String getHi(){
        return "Hi World! ";
    }

    @GetMapping("/hello2")
    public String getHello2(@RequestParam String msg){
        return  msg + "님 Hello World! ";
    }

    @PostMapping("/hi2")
    public String getHi2(@RequestBody Map<String, String> body){
        return body.get("msg")+"님 Hi World! ";
    }
}
