package com.example.assignment.service;
import com.example.assignment.dao.PersonDaoImpl;
import com.example.assignment.exception.ApiRequestException;
import com.example.assignment.helper.CVSHelper;
import com.example.assignment.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class PersonServiceImpl implements PersonService {
    PersonDaoImpl personDao;
    RestTemplate restTemplate;

    @Autowired
    public PersonServiceImpl(PersonDaoImpl personDao
            , RestTemplate restTemplate) {
        this.personDao = personDao;
        this.restTemplate = restTemplate;
    }

    @Async
    @Override
    public CompletableFuture<ResponseEntity> save(Person person) {
        int id = personDao.savePerson(person);
//        if(id != 0){
//            throw new ApiRequestException("can not persist data");
//        }
        return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.CREATED).body(id))
                .handle((result, ex) -> {
            if (ex == null)
                return result;
            else
                throw new ApiRequestException("can not persist data");
        });
    }

    @Override
    public ResponseEntity saveAllFromCSV(List<Person> person) {

        int i = 0;
        for (Person p : person) {
            personDao.savePerson(p);
            i++;
        }
        if (i != person.size()) {
            return ResponseEntity.badRequest()
                    .body("bad data found on line number : " + i + " in ");
        }
        return ResponseEntity.ok(i);
    }

    @Override
    public List<Person> getAllPerson() {
        return personDao.getPerson();
    }

    @Override
    @CachePut(cacheNames = "persons", key = "#person.id")
    public Person updatePerson(Person person) {
        return personDao.updatePerson(person);
    }

    @Override
    @CacheEvict(cacheNames = "persons", key = "#id")
    public String deletePerson(int id) {
        personDao.deletePerson(id);
        return "Deleted";
    }


    @Override
    @Cacheable(cacheNames = "persons", key = "#id")
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
        } catch (Exception e) {
            throw new IOException("Failed to store file.", e);
        }
    }

    private void exchangeWithAllTypesOfParameter(Person person) {
        String uri = "http://localhost:2424/customer/{account}/fetch/{xyz}";
        // for Path Params
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("account", "saving");
        pathParams.put("xyz", "abc");

        //For Query Params and Path Params
        UriComponents uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(uri).
                queryParam("id", person.getId()).
                queryParam("name", person.getName())
                .buildAndExpand(pathParams);

        //For Header + request body
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        // with request Body
        HttpEntity<Person> httpEntity = new HttpEntity<>(person, headers);
        // without request Body
        //HttpEntity httpEntity = new HttpEntity(headers);

        //calling service with exchange
        ResponseEntity<Person> p = restTemplate.exchange(
                uriComponentsBuilder.toUriString(),
                HttpMethod.GET,
                httpEntity,
                Person.class);
        //calling service with getForEntity
        ResponseEntity<Person> getPerson = restTemplate.getForEntity(
                uriComponentsBuilder.toUriString(),
                Person.class,
                httpEntity);

        ResponseEntity<Person> postPerson = restTemplate.postForEntity(
                uriComponentsBuilder.toUriString(),
                httpEntity,
                Person.class);
        //calling service with getForObject
        Person getPerson1 = restTemplate.getForObject(
                uriComponentsBuilder.toUriString(),
                Person.class,
                httpEntity);

        Person postPerson1 = restTemplate.postForObject(
                uriComponentsBuilder.toUriString(),
                httpEntity,
                Person.class);
    }
    public ByteArrayInputStream getAllPersonInCSV() {
        List<Person> tutorials = personDao.getPerson();

        ByteArrayInputStream in = CVSHelper.tutorialsToCSV(tutorials);
        return in;
    }
}