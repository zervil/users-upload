package com.demo.usersupload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InitializeData {

    private PersonRepository personRepository;

    @Autowired
    public InitializeData(PersonRepository personRepository) {
        this.personRepository = personRepository;
        LoadPersons();
    }

    private void LoadPersons() {
        Person p1 = new Person();
        p1.setName("John");
        p1.setSalary(3500.05);
        personRepository.save(p1);
        Person p2 = new Person();
        p2.setName("Mary Posa");
        p2.setSalary(2000);
        personRepository.save(p2);
        Person p3 = new Person();
        p3.setName("Jack");
        p3.setSalary(4000);
        personRepository.save(p3);
        Person p4 = new Person();
        p4.setName("Rick");
        p4.setSalary(6000);
        personRepository.save(p4);
    }
}