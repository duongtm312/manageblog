package com.blog.controller;

import com.blog.model.Comment;
import com.blog.model.Likes;
import com.blog.repositori.BlogDao;
import com.blog.model.Blog;
import com.blog.service.BlogService;
import com.blog.service.CommentService;
import com.blog.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/blog")

public class BlogController {
    @Value("${file-upload}")
    private String fileUpload;
    @Autowired
    BlogService blogService;
    @Autowired
    CommentService commentService;
    @Autowired
    LikeService likeService;

    @GetMapping("")
    public ModelAndView showBlog(@RequestParam(defaultValue = "0") int page) {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("blog", blogService.getAll(PageRequest.of(page, 3, Sort.by("idBlog"))));
        return modelAndView;
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Blog blog, @RequestParam(value = "file", required = false) MultipartFile file) {
        if (file.getSize() != 0) {
            String nameImg = file.getOriginalFilename();
            try {
                FileCopyUtils.copy(file.getBytes(), new File(fileUpload + "img/" + nameImg));
            } catch (IOException e) {
                e.printStackTrace();
            }
            blog.setImgSrc("/img/" + nameImg);
            blogService.save(blog);
        }
        return "redirect:/blog";
    }

    @GetMapping("/post/{id}")
    public String showPost(@PathVariable int id, Model model) {
        Blog blog = blogService.findById(id);
        model.addAttribute("blog", blog);
        model.addAttribute("likes", likeService.getSizeLike(id));
        model.addAttribute("dislikes", likeService.getSizeDisLike(id));
        List<Comment> comments = commentService.getAll(blogService.findById(id));
        model.addAttribute("comment", comments);
        return "single-post";
    }

    @PostMapping("/post/{id}")
    public String comment(@PathVariable int id, Model model, @RequestParam("nameComment") String nameComment,
                          @RequestParam("contentComment") String contentComment) {
        Blog blog = blogService.findById(id);
        Comment comment = new Comment();
        comment.setBlog(blog);
        comment.setContentComment(contentComment);
        comment.setNameComment(nameComment);
        commentService.save(comment);

        model.addAttribute("blog", blogService.findById(id));
        List<Comment> comments = commentService.getAll(blogService.findById(id));
        model.addAttribute("comment", comments);
        return "single-post";
    }

    @GetMapping("/like/{idBlog}")
    private ModelAndView like(@PathVariable int idBlog) {
        Likes likes = new Likes();
        likes.setStatus(true);
        likes.setBlog(blogService.findById(idBlog));
        likeService.save(likes);
        ModelAndView modelAndView = new ModelAndView("redirect:/blog/post/"+idBlog);
        return modelAndView;
    }
    @GetMapping("/dislikes/{idBlog}")
    private ModelAndView dislike(@PathVariable int idBlog) {
        Likes likes = new Likes();
        likes.setStatus(false);
        likes.setBlog(blogService.findById(idBlog));
        likeService.save(likes);
        ModelAndView modelAndView = new ModelAndView("redirect:/blog/post/"+idBlog);
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public String showEdit(@PathVariable int id, Model model) {

        model.addAttribute("blog", blogService.findById(id));
        return "edit";
    }

    @PostMapping("/edit")
    public String editPost(@ModelAttribute Blog blog, @RequestParam(value = "file", required = false) MultipartFile file) {
        if (file.getSize() != 0) {
            try {
                File file1 = new File(fileUpload + blog.getImgSrc());
                file1.deleteOnExit();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String nameImg = file.getOriginalFilename();
            try {
                FileCopyUtils.copy(file.getBytes(), new File(fileUpload + "img/" + nameImg));
            } catch (IOException e) {
                e.printStackTrace();
            }
            blog.setImgSrc("/img/" + nameImg);

        }
        blogService.save(blog);
        return "single-post";
    }

}
