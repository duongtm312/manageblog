package com.blog.controller;

import com.blog.model.Account;
import com.blog.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {
    @Autowired
    AccountService accountService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String loginHome(@RequestParam String mail, @RequestParam String password) {
        Account account = accountService.findByMail(mail);
        if (account != null) {
            if (account.getPassWord().equals(password)) {
                return "redirect:/blog";
            }
        }
        return "login";
    }

    @PostMapping("/signup")
    public String sigup(@ModelAttribute Account account, @RequestParam String passWordCheck) {
        if (account.getPassWord().equals(passWordCheck)) {
            if (accountService.findByMail(account.getMail()) == null) {
                accountService.save(account);
                return "redirect:/login";
            }
        }
        return "/login";
    }
}
