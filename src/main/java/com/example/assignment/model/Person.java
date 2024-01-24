package com.example.assignment.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
@Entity
public class Person  implements Serializable {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern ="dd/MM/yyyy")
    private Date DOB;
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getDOB() {
        return DOB;
    }
}
