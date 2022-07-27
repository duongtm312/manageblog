package com.blog.controller;

import com.blog.model.Account;
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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
    @Autowired
    HttpSession httpSession;

    @GetMapping("")
    public ModelAndView showBlog(@RequestParam(defaultValue = "0") int page, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("index");
        Account account = (Account) httpSession.getAttribute("account");
        if (account == null) {
            return new ModelAndView("/login");
        } else {
            modelAndView.addObject("account", account);
        }
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

    @PostMapping("/repy/{id}/{idcom}")
    public ModelAndView repy(@PathVariable int id, @PathVariable int idcom, @RequestParam("nameComment") String nameComment,
                             @RequestParam("contentComment") String contentComment) {

        Comment comment = new Comment();
        comment.setComment(commentService.finById(idcom));
        comment.setContentComment(contentComment);
        comment.setNameComment(nameComment);
        comment.setBlog(blogService.findById(id));
        commentService.save(comment);
        ModelAndView modelAndView = new ModelAndView("redirect:/blog/post/" + id);
        return modelAndView;

    }

    @GetMapping("/like/{idBlog}")
    private ModelAndView like(@PathVariable int idBlog) {
        Account account = (Account) httpSession.getAttribute("account");
        Likes disLike = likeService.findLike(idBlog, account.getIdAccount(),false);
        if (disLike!=null){
            likeService.delete(disLike.getIdLike());
        }
        Likes like = likeService.findLike(idBlog, account.getIdAccount(),true);
        if (like != null) {
            likeService.delete(like.getIdLike());
        } else {
            Likes likes = new Likes();
            likes.setStatus(true);
            likes.setAccount(account);
            likes.setBlog(blogService.findById(idBlog));
            likeService.save(likes);
        }
        ModelAndView modelAndView = new ModelAndView("redirect:/blog/post/" + idBlog);
        return modelAndView;
    }

    @GetMapping("/dislikes/{idBlog}")
    private ModelAndView dislike(@PathVariable int idBlog) {
        Account account = (Account) httpSession.getAttribute("account");
        Likes like = likeService.findLike(idBlog, account.getIdAccount(),true);
        if (like!=null){
            likeService.delete(like.getIdLike());
        }
        Likes disLike = likeService.findLike(idBlog, account.getIdAccount(),false);
        if (disLike != null) {
            likeService.delete(disLike.getIdLike());
        } else {
            Likes likes = new Likes();
            likes.setStatus(false);
            likes.setAccount(account);
            likes.setBlog(blogService.findById(idBlog));
            likeService.save(likes);
        }
        ModelAndView modelAndView = new ModelAndView("redirect:/blog/post/" + idBlog);
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
