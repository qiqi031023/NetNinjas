package com.tvpss.repository;

import com.tvpss.model.CrewApplication;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class CrewApplicationDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public void save(CrewApplication application) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(application);
    }

    @Transactional
    public CrewApplication getById(String id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(CrewApplication.class, id);
    }

    @Transactional
    public List<CrewApplication> getAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from CrewApplication", CrewApplication.class).list();
    }

    @Transactional
    public void delete(String id) {
        Session session = sessionFactory.getCurrentSession();
        CrewApplication application = session.get(CrewApplication.class, id);
        if (application != null) {
            session.delete(application);
        }
    }
}
