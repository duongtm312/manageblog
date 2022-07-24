package com.blog.service;

import com.blog.model.Blog;
import com.blog.repositori.IBlogRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BlogService {
@Autowired
    IBlogRepo iBlogRepo;
    public Page<Blog> getAll(Pageable pageable) {
        return iBlogRepo.findAll(pageable);
    }
    public void save(Blog blog) {
        iBlogRepo.save(blog);
    }

    public void delete(int id) {
        iBlogRepo.deleteById(id);
    }

    public Blog findById(int id) {
        Optional<Blog> optional = iBlogRepo.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        return new Blog();
    }
}
