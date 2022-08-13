package com.demo.usersupload.csv.service;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.demo.usersupload.csv.helper.CSVHelper;
import com.demo.usersupload.Person;
import com.demo.usersupload.PersonRepository;
@Service
public class CSVService {
  @Autowired
  PersonRepository repository;
  public void save(MultipartFile file) {
    try {
      List<Person> persons = CSVHelper.csvToPersons(file.getInputStream(), repository);
      repository.saveAll(persons);
    } catch (IOException e) {
      throw new RuntimeException("fail to store csv data: " + e.getMessage());
    }
  }
}