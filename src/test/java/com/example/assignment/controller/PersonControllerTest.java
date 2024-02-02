package com.example.assignment.controller;

import com.example.assignment.model.Person;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonControllerTest {
    @Autowired
    private MockMvc mockMvc;
    ObjectMapper mapper;

    @BeforeEach
    public void init() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(
           SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Test
    public void testGetPersonAPI() throws Exception {
        ResultActions resultActions = this.mockMvc.
                perform(MockMvcRequestBuilders.get("/getAllPersons")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status()
                        .isOk());
        ArrayList<Person> list = mapper.readerForListOf(Person.class).
                readValue(resultActions.andReturn()
                        .getResponse().getContentAsString());
        Assertions.assertEquals(list.size(), 3);
        // .andDo(MockMvcResultHandlers.print()).
        // andExpect(MockMvcResultMatchers.status().isOk())
        //.andExpect(MockMvcResultMatchers.content()
        // .string(Matchers.containsString("3")));
        //.andExpect(MockMvcResultMatchers.jsonPath("$.content.size()",
        // CoreMatchers.is(responseDto.getContent().size())));
    }

    @Test
    public void testSavePersonAPI() throws Exception {
        Person p = Person.builder().DOB(LocalDate.now()).name("xyz").build();
        ResultActions resultActions = this.mockMvc.perform(
                MockMvcRequestBuilders.post("/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(p)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content()
                .string(Matchers.containsString("1")));

    }

    @Test
    public void testDeletePersonAPI() throws Exception {
        ResultActions resultActions = this.mockMvc.
                perform(MockMvcRequestBuilders.delete("/deletePerson")
                .param("id", "40")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content()
                .string(Matchers.containsString("Deleted")));

    }

    @Test
    public void testUpdatePersonAPI() throws Exception {
        Person p = Person.builder().DOB(LocalDate.now()).name("Pritam").id(39).build();
        ResultActions resultActions = this.mockMvc.
                perform(MockMvcRequestBuilders.put("/updatePerson")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(p)))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Person result = mapper.
                readValue(resultActions.andReturn()
                .getResponse().getContentAsString(),Person.class);
        Assertions.assertEquals(result.getName(), "Pritam");

    }
}
