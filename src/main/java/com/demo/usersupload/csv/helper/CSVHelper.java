package com.demo.usersupload.csv.helper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;
import com.demo.usersupload.Person;
import com.demo.usersupload.PersonRepository;

public class CSVHelper {
  public static String TYPE = "text/csv";
  static String[] HEADERs = { "name", "salary" };
  public static boolean hasCSVFormat(MultipartFile file) {
    if (!TYPE.equals(file.getContentType())) {
      return false;
    }
    return true;
  }

  public static List<Person> csvToPersons(InputStream is, PersonRepository repository) {
    try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        CSVParser csvParser = new CSVParser(fileReader,
            CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
      List<Person> persons = new ArrayList<Person>();
      Iterable<CSVRecord> csvRecords = csvParser.getRecords();
      for (CSVRecord csvRecord : csvRecords) {
        Double salary = Double.parseDouble(csvRecord.get("salary"));
        String name = csvRecord.get("name");
        if(salary >= 0.0){
            
            Person existingPerson = repository.findByName(name);
            if(existingPerson != null){
                existingPerson.setSalary(salary);
                repository.save(existingPerson);
            }
            else{
                Person person = new Person();
                person.setName(name);
                person.setSalary(salary);
                persons.add(person);
            }
        }
      }
      return persons;
    } catch (IOException e) {
      throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
    }
  }
}