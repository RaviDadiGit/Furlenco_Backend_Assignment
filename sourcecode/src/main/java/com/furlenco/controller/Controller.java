package com.furlenco.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.furlenco.model.Student;
import com.furlenco.utility.DBUtility;

@RestController
public class Controller {

	@RequestMapping(value = "/students", method = RequestMethod.GET)
	public ResponseEntity<List<Student>> getStudentsService(
			@RequestParam(value = "id", required = false) Integer id,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "classNumber", required = false) String classNumber,
			@RequestParam(value = "active", required = false) boolean active,
			@RequestParam(value = "admissionYear", required =false) String admissionYear) {
		List<Student> students = DBUtility.getStudents(DBUtility.buildQuery(classNumber, active, admissionYear, id));
		return new ResponseEntity<List<Student>>(students, HttpStatus.OK);
	}
	@RequestMapping(value = "/students", method = RequestMethod.POST)
	public ResponseEntity<String> getStudentsService(
			@RequestBody List<Student> students) {
//		Connection connection = DBUtility.getDBConnection();
//		for(Student student:students){
//		System.out.println(student);
//		}
		return new ResponseEntity<String>("Success", HttpStatus.OK);
	}
}
