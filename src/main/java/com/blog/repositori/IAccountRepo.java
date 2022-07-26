package com.blog.repositori;

import com.blog.model.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IAccountRepo extends CrudRepository<Account,Integer> {
//    @Query(nativeQuery = true,value = "SELECT * FROM quanlyblog.account where mail =:mail;")
    Account findByMail( String mail);
}
