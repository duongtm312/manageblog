package com.blog.repositori;

import com.blog.model.Likes;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ILikeRepo extends CrudRepository<Likes, Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM Likes where blog_idBlog =:id and status =1;")
    List<Likes> findAllByIdBlogLike(@Param("id") int id);

    @Query(nativeQuery = true, value = "SELECT * FROM Likes where blog_idBlog =:id and status =0;")
    List<Likes> findAllByIdBlogDisLike(@Param("id") int id);

    @Query(nativeQuery = true, value = "SELECT * FROM quanlyblog.likes where blog_idBlog =:blog and account_idAccount =:idAcc and status=:status")
    Likes findLike(@Param("blog") int blog, @Param("idAcc") int idAcc, @Param("status") boolean status);

}
