package com.example.assignment.controller;

import com.example.assignment.exception.ApiInternalServerException;
import com.example.assignment.exception.ApiRequestException;
import com.example.assignment.exception.DataException;
import com.example.assignment.helper.CVSHelper;
import com.example.assignment.model.Person;
import com.example.assignment.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
public class PersonController {
    @Autowired
    PersonService personService;


    @PostMapping(value = "/save")
    public ResponseEntity save(@RequestBody Person person) {
        ResponseEntity res = personService.save(person).join();
        if (res.getStatusCode().isError()) {
            throw new ApiRequestException("Cannot persist data");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping(value = "/getAllPersons")
    public ResponseEntity getAllPersons() {
        return ResponseEntity.ok().body(personService.getAllPerson());
    }

    @GetMapping(value = "/getPerson/{id}")
    public ResponseEntity getPersons(@PathVariable int id) {
        if (id == 3) {
            throw new ApiInternalServerException("root user cant be deleted");
        }
        return ResponseEntity.ok().body(personService.getPerson(id));
    }

    @PutMapping(value = "/updatePerson")
    public ResponseEntity updatePerson(@RequestBody Person person) {
        Person res = personService.updatePerson(person);
        if (null == res)
            throw new ApiRequestException("Can not update person : " + person.getName());
        return ResponseEntity.ok().body(res);
    }

    @DeleteMapping(value = "/deletePerson")
    public ResponseEntity deletePerson(@RequestParam(name = "id") int id,
                                       @RequestParam(name = "name", required = false) String name) {
        String res = personService.deletePerson(id);
        if (StringUtils.isEmpty(res) && !res.equals("Deleted"))
            throw new ApiRequestException("Can not update person : " + id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(res);
    }

    @PostMapping("/uploadFile")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        personService.store(file);
        return file.getOriginalFilename();
    }

    @PostMapping("/csv/uploadCSVFile")
    public ResponseEntity uploadCSVFile(@RequestParam("file") MultipartFile file) throws IOException, DataException {
        log.info("Upload API call for file name : {}", file.getName());
        List<Person> list = CVSHelper.cvsToPerson(file.getInputStream());
        ResponseEntity response = personService.saveAllFromCSV(list);
        if (response.getStatusCode().is4xxClientError()) {
            throw new DataException(response.getBody() + " in file : " + file.getName());
        }
        return response;
    }

    @GetMapping("csv/download")
    public ResponseEntity getFile() {
        String filename = "persons.csv";
        InputStreamResource file = new InputStreamResource(personService.getAllPersonInCSV());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }

}
