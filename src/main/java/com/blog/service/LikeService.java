package com.blog.service;

import com.blog.model.Likes;
import com.blog.repositori.ILikeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    ILikeRepo iLikeRepo;
    public int getSizeLike(int idBlog){
        return iLikeRepo.findAllByIdBlogLike(idBlog).size();
    }
    public int getSizeDisLike(int idBlog){
        return iLikeRepo.findAllByIdBlogDisLike(idBlog).size();
    }
    public void save(Likes like){
        iLikeRepo.save(like);
    }
}
