package com.furlenco.model;

import java.util.Date;

public class Student {
	private int id;
	private String name;
	private int classNumber;
	private boolean active;
	private String admissionYear;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getClassNumber() {
		return classNumber;
	}

	public void setClassNumber(int classNumber) {
		this.classNumber = classNumber;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getAdmissionYear() {
		return admissionYear;
	}

	public void setAdmissionYear(String admissionYear) {
		this.admissionYear = admissionYear;
	}

	@Override
	public String toString() {
		return "Student [id=" + id + ", name=" + name + ", classNumber="
				+ classNumber + ", active=" + active + ", admissionYear="
				+ admissionYear + "]";

	}
}
