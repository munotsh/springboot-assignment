package com.example.assignment.dao;

import com.example.assignment.model.Person;

import java.util.List;

public interface PersonDao {
    public List<Person> getPerson();
    public Person getPersonWithId(int id);
    public int savePerson(Person person);
    public Person updatePerson(Person person);
    public void deletePerson(int id);
}
