package com.example.assignment.controller;

import com.example.assignment.exception.ApiInternalServerException;
import com.example.assignment.exception.ApiRequestException;
import com.example.assignment.model.Person;
import com.example.assignment.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class PersonController {
    @Autowired
    PersonService personService;

    @PostMapping("/save")
    public ResponseEntity save(@RequestBody Person person) {
        ResponseEntity res = personService.save(person).join();
        if (res.getStatusCode().isError()) {
            throw new ApiRequestException("Cannot persist data");
        }
        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/getAllPersons")
    public ResponseEntity getAllPersons() {
        return ResponseEntity.ok().body(personService.getAllPerson());
    }

    @GetMapping("/getPerson/{id}")
    public ResponseEntity getPersons(@PathVariable int id) {
        if(id == 3)
        {
            throw new ApiInternalServerException("root user cant be deleted");
        }
        return ResponseEntity.ok().body(personService.getPerson(id));
    }

    @PutMapping("/updatePerson")
    public ResponseEntity updatePerson(@RequestBody Person person) {
        Person res = personService.updatePerson(person);
        if (null != res)
            return ResponseEntity.ok().body(res);
        return ResponseEntity.ok().body("Error");
    }

    @DeleteMapping("/deletePerson")
    public ResponseEntity deletePerson(@RequestParam(name = "id") int id) {
        String res = personService.deletePerson(id);
        return ResponseEntity.ok().body(res);
    }
    @PostMapping("/uploadFile")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        personService.store(file);
        return file.getOriginalFilename();
    }
}
