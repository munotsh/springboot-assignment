package com.example.assignment.helper;

import com.example.assignment.model.Person;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CVSHelper {
    public static List<Person> cvsToPerson(InputStream inputStream) {
        List<Person> list = new ArrayList<>();
        try (BufferedReader fileReader = new
                BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
            list = csvParser.getRecords().stream().map(cvs -> {
                Person person = new Person();
                person.setDOB(LocalDate.parse(cvs.get("DOB")));
                person.setName(cvs.get("name"));
                return person;
            }).collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;

    }

    public static ByteArrayInputStream tutorialsToCSV(List<Person> persons) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
            List<String> data = Arrays.asList("id,DOB,name");
            csvPrinter.printRecord(data);
            for (Person person : persons) {
                data = Arrays.asList(person.toString());

                csvPrinter.printRecord(data);
            }

            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
        }
    }
}