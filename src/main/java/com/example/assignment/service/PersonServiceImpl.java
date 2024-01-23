package com.example.assignment.service;

import com.example.assignment.dao.PersonDaoImpl;
import com.example.assignment.exception.ApiInternalServerException;
import com.example.assignment.exception.ApiRequestException;
import com.example.assignment.model.Person;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class PersonServiceImpl implements PersonService{
    PersonDaoImpl personDao;
    @Autowired
    public PersonServiceImpl(PersonDaoImpl personDao){
        this.personDao = personDao;
    }
    @Async
    @Override
    public CompletableFuture<ResponseEntity> save(Person person){
        int id = personDao.savePerson(person);
//        if(id != 0){
//            throw new ApiRequestException("can not persist data");
//        }
        return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.CREATED).body(id)).handle((result,ex) ->{
                    if(ex == null)
                        return result;
                    else
                        throw new ApiRequestException("can not persist data");
        });
    }
    @Override
    public List<Person> getAllPerson() {
        return personDao.getPerson();
    }

    @Override
    @CachePut(cacheNames = "persons",key= "#person.id")
    public Person updatePerson(Person person) {
        return personDao.updatePerson(person);
//        return "Updated";
    }

    @Override
    @CacheEvict(cacheNames = "person", key="#id")
    public String deletePerson(int id) {
        personDao.deletePerson(id);
        return "Updated";
    }


    @Override
    @Cacheable(cacheNames = "persons", key="#id")
    public Person getPerson(int id) {
        System.out.println("from DB");
        return personDao.getPersonWithId(id);
    }
    @Override
    public void store(MultipartFile file) throws IOException {
        try {
            if (file.isEmpty()) {
                throw new Exception("Failed to store empty file.");
            }
            Path destinationPath = Paths.get(URI.create("file:///Users/joe/FileTest.java"));
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationPath,
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (Exception e) {
            throw new IOException("Failed to store file.", e);
        }
    }
}
