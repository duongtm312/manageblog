package controller;

import dao.BlogDao;
import model.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/blog")

public class BlogController {
    @Value("${file-upload}")
    private String fileUpload;
    @Autowired
    BlogDao blogDao;

    @GetMapping("")
    public ModelAndView showBlog() {
        ModelAndView modelAndView = new ModelAndView("index");
        List<Blog> blogs = blogDao.getList();
        modelAndView.addObject("blog", blogs);
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
            blogDao.save(blog);
        }
        return "redirect:/blog";
    }

    @GetMapping("/post/{id}")
    public String showPost(@PathVariable int id,Model model) {

      model.addAttribute("blog",blogDao.findById(id));
        return "single-post";
    }
    @GetMapping("/edit/{id}")
    public String showEdit(@PathVariable int id,Model model) {

        model.addAttribute("blog",blogDao.findById(id));
        return "edit";
    }
    @PostMapping("/edit")
    public String editPost(@ModelAttribute Blog blog, @RequestParam(value = "file", required = false) MultipartFile file){
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
        blogDao.edit(blog);
        return "single-post";
    }
}
