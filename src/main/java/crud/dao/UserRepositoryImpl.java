package crud.dao;

import crud.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository{

    EntityManagerFactory emf;

    @PersistenceUnit
    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public User findById(long id) {
        EntityManager em = emf.createEntityManager();
        User user = em.find(User.class, id);
        em.detach(user);
        return user;
    }

    @Override
    public void save(User user) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(user);
        em.getTransaction().commit();
        em.close();

    }

    @Override
    public void deleteById(long id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        User user = em.find(User.class, id);
        em.remove(user);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public List<User> findAll() {
        EntityManager em = emf.createEntityManager();
        return em.createQuery("SELECT u from User u").getResultList();
    }
}
