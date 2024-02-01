package com.example.assignment.service;

import com.example.assignment.model.Person;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface PersonService {
    public CompletableFuture<ResponseEntity> save(Person person);
    public ResponseEntity saveAllFromCSV(List<Person> person);
    public List getAllPerson();
    public Person updatePerson(Person person);
    public String deletePerson(int id);
    public Person getPerson(int id);
    public void store(MultipartFile file) throws IOException;
    public ByteArrayInputStream getAllPersonInCSV();
}
