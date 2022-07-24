package com.blog.repositori;

import com.blog.model.Blog;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBlogRepo extends PagingAndSortingRepository<Blog,Integer> {
}
