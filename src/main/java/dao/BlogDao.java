package dao;

import model.Blog;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

public class BlogDao {
    @Autowired
    EntityManager entityManager;
    public List<Blog> getList(){
        String queryStr = "SELECT b FROM Blog b";
        TypedQuery<Blog> query = entityManager.createQuery(queryStr, Blog.class);
        return (List<Blog>) query.getResultList();
    }
    public void save(Blog blog){
        EntityTransaction txn = entityManager.getTransaction();
        txn.begin();
        entityManager.persist(blog);
        txn.commit();
    }
    public void edit(Blog blog) {
        EntityTransaction txn = entityManager.getTransaction();
        txn.begin();
        entityManager.merge(blog);
        txn.commit();
    }
    public void delete(int id){
        EntityTransaction txn = entityManager.getTransaction();
        txn.begin();
        entityManager.remove(findById(id));
        txn.commit();
    }
    public Blog findById(int id){
        String query = "SELECT b FROM Blog b where b.idBlog=:id";
        Blog blog = entityManager.createQuery(query,Blog.class).setParameter("id",id).getSingleResult();
        return blog;
    }
}
