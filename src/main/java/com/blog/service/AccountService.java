package com.blog.service;

import com.blog.model.Account;
import com.blog.model.Blog;
import com.blog.repositori.IAccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    IAccountRepo iAccountRepo;
    public Account findByMail(String mail){
      return  iAccountRepo.findByMail(mail);
    }
    public void save(Account account){
        iAccountRepo.save(account);
    }
}
