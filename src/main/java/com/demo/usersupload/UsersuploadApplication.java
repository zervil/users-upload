package com.demo.usersupload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import com.demo.usersupload.csv.service.CSVService;
import com.demo.usersupload.csv.helper.CSVHelper;
import com.demo.usersupload.csv.message.ResponseMessage;

@CrossOrigin("http://localhost:8080	")
@SpringBootApplication
@RestController
public class UsersuploadApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsersuploadApplication.class, args);
	}

	@GetMapping("/")
	public String index() {
		return "Index page";
	}

	@Autowired
	CSVService fileService;
	@PostMapping("/upload")
	public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
		String message = "";
		if (CSVHelper.hasCSVFormat(file)) {
		try {
			fileService.save(file);
			message = "success: 1";
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		} catch (Exception e) {
			message = "error: " + e;
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}
		}
		message = "Upload CSV file";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
	}

	@Autowired PersonRepository personRepository;
	@GetMapping("/users")
		public Iterable<Person> users(
			@RequestParam(value = "min", defaultValue = "0") Double min,
			@RequestParam(value = "max", defaultValue = "4000") Double max,
			@RequestParam(value = "offset", defaultValue = "0") Integer offset,
			@RequestParam(value = "limit", defaultValue = "999999999") Integer limit,
			@RequestParam(value = "sort", defaultValue = "0") String sort) {
				if(sort != "0"){
					return personRepository.findByParamsLimitSort(min, max, limit, offset, sort);
				}
				else{
					return personRepository.findByParamsLimit(min, max, limit, offset);
				}
			}

	

}
