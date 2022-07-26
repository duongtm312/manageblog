package com.blog.controller;

import com.blog.model.Account;
import com.blog.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    @Autowired
    AccountService accountService;
    @Autowired
    HttpSession httpSession;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String loginHome(@RequestParam String mail, @RequestParam String password) {
        Account account = accountService.findByMail(mail);
        if (account != null) {
            if (account.getPassWord().equals(password)) {
                httpSession.setAttribute("account", account);
                return "redirect:/blog";
            }
        }
        return "login";
    }

    @PostMapping("/signup")
    public String sigup(@ModelAttribute Account account, @RequestParam String passWordCheck, Model model) {
        if (account.getPassWord().equals(passWordCheck)) {
            if (accountService.findByMail(account.getMail()) == null) {
                accountService.save(account);
                model.addAttribute("messages","Tạo tài khoản thành công");
                return "login";
            }
        }
        model.addAttribute("messages","Tạo tài khoản không thành công");
        return "login";
    }
}
