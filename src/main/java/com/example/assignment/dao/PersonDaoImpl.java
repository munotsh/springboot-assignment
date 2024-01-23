package com.example.assignment.dao;

import com.example.assignment.exception.ApiInternalServerException;
import com.example.assignment.model.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Repository
public class PersonDaoImpl implements PersonDao{
    SessionFactory factory;
    @Autowired
    public PersonDaoImpl(SessionFactory factory){
        this.factory = factory;
    }
    public Session getSession() {
        Session session = factory.getCurrentSession();
        if (session == null) {
            session = factory.openSession();
        }
        return session;
    }
    @SuppressWarnings("unchecked")
    @Override
    public List<Person> getPerson() {
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        return getSession().createQuery("from Person").list();
    }

    @Override
    public Person getPersonWithId(int id) {
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        return getSession().get(Person.class,id);
    }

    @Override
    public int savePerson(Person person) {
        return (int)getSession().save(person);
    }

    @Override
    public Person updatePerson(Person person) {
        getSession().update(person);
        return person;
    }

    @Override
    public void deletePerson(int id) {
        Session session = getSession();
        Person person = session.find(Person.class,id);
        session.delete(person);
    }

}
