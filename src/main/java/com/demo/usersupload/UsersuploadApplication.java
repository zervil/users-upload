package com.demo.usersupload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import com.demo.usersupload.limitOffset.OffsetBasedPageRequest;
import com.demo.usersupload.csv.helper.CSVHelper;
import com.demo.usersupload.csv.message.ResponseMessage;

@CrossOrigin("http://localhost:8080	")
@SpringBootApplication
@RestController
public class UsersuploadApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsersuploadApplication.class, args);
	}
	
	//Created by KOK CHUAN YONG
	//upload endpoint
	//Method: POST
	//● Content type:multipart/form-data
	//● Form field name: file
	//● Contents: CSV data.
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

	//Created by KOK CHUAN YONG
	//users endpoint
	//Method: GET
	//Params: 
	//● min - minimum salary. Optional,
	// defaults to 0.0..
	// ● max - maximum salary. Optional,
	// defaults to 4000.0.
	// ● offset - first result among set to be
	// returned. Optional, defaults to 0.
	// ● limit - number of results to include.
	// Optional, defaults to no limit.
	// ● sort - NAME or SALARY, non-case
	// sensitive. Optional, defaults to no
	// sorting. Sort only in ascending
	// sequence.
	@Autowired PersonRepository personRepository;
	@GetMapping("/users")
		public Iterable<Person> users(
			@RequestParam(required = false, value = "min", defaultValue = "0") Double min,
			@RequestParam(required = false, value = "max", defaultValue = "4000") Double max,
			@RequestParam(required = false, value = "offset", defaultValue = "0") Integer offset,
			@RequestParam(required = false, value = "limit", defaultValue = "999999999") Integer limit,
			@RequestParam(required = false, value = "sort") String sort) {
				if(sort != null){
					sort = sort.toLowerCase();
					Sort newSort = Sort.by(Sort.Direction.ASC, sort);
					Pageable page = new OffsetBasedPageRequest(offset, limit, newSort);
					return personRepository.findByParamsLimitSort(min, max, page);
				}
				else{
					Pageable page = new OffsetBasedPageRequest(offset, limit);
					return personRepository.findByParamsLimitSort(min, max, page);
				}
			}

	

}
