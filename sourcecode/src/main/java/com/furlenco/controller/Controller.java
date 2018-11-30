package com.furlenco.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
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
			@RequestParam(value = "admissionYearBefore", required = false) Integer admissionYearBefore,
			@RequestParam(value = "classes", required = false) String classNumber,
			@RequestParam(value = "active", required = false) boolean active,
			@RequestParam(value = "admissionYearAfter", required = false) Integer admissionYearAfter) {
		List<Student> students = DBUtility.getStudents(DBUtility.buildQuery(
				classNumber, active, admissionYearBefore, admissionYearAfter,
				id));
		return new ResponseEntity<List<Student>>(students, HttpStatus.OK);
	}

	@RequestMapping(value = "/students/{id}", method = RequestMethod.GET)
	public ResponseEntity<List<Student>> getStudentsWithID(
			@PathVariable("id") Integer id) {
		List<Student> students = DBUtility.getStudents(DBUtility.buildQuery(
				null, false, null, null, id));
		if (students.isEmpty()) {
			return new ResponseEntity<List<Student>>(students,
					HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<List<Student>>(students, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/students", method = RequestMethod.POST)
	public ResponseEntity<String> postStudentsService(
			@RequestBody List<Student> students) {
		try {
			DBUtility.insertStudents(students);
		} catch (SQLException e) {
			return new ResponseEntity<String>("Student ID already exists",
					HttpStatus.ALREADY_REPORTED);
		}
		return new ResponseEntity<String>("Created", HttpStatus.CREATED);
	}

	@RequestMapping(value = "/students/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> inactiveStudent(@PathVariable("id") int id) {
		DBUtility.inactiveStudent(id);
		return new ResponseEntity<String>("Inacivated Student",
				HttpStatus.CREATED);
	}

	@RequestMapping(value = "/students/{id}", method = RequestMethod.PATCH)
	public ResponseEntity<String> updateStudent(@PathVariable("id") int id) {
		DBUtility.updateStudent(id);
		return new ResponseEntity<String>("Updated Student", HttpStatus.CREATED);
	}
}
