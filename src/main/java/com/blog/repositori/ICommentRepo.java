package com.blog.repositori;

import com.blog.model.Blog;
import com.blog.model.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICommentRepo extends CrudRepository<Comment,Integer> {
    List<Comment> findByBlog(Blog blog);
}
