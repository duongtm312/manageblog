package com.blog.model;

import javax.persistence.*;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idComment;
    @Column(length = 1000)
    private String contentComment;
    private String nameComment;
    @ManyToOne
    private Blog blog;


    public Comment(int idComment, String contentComment, String nameComment, Blog blog) {
        this.idComment = idComment;
        this.contentComment = contentComment;
        this.nameComment = nameComment;
        this.blog = blog;
    }

    public String getNameComment() {
        return nameComment;
    }

    public void setNameComment(String nameComment) {
        this.nameComment = nameComment;
    }

    public Comment() {
    }

    public int getIdComment() {
        return idComment;
    }

    public void setIdComment(int idComment) {
        this.idComment = idComment;
    }

    public String getContentComment() {
        return contentComment;
    }

    public void setContentComment(String contentComment) {
        this.contentComment = contentComment;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }
}
