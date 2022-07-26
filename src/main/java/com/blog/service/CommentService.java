package com.blog.service;

import com.blog.model.Blog;
import com.blog.model.Comment;
import com.blog.repositori.ICommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    ICommentRepo iCommentRepo;
    public List<Comment> getAll(Blog blog) {
        return iCommentRepo.findByBlog(blog);
    }
    public void save(Comment comment){
        iCommentRepo.save(comment);
    }
    public Comment finById(int id){
        return iCommentRepo.findById(id).get();
    }
}
