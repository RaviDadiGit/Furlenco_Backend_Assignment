package com.furlenco.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.furlenco.model.Student;

public class DBUtility {
	private static Connection connection;
	private static String Where_Condition = " WHERE ";

	public static Connection getConnection() {
		return connection;
	}

	public static void setConnection(Connection connection) {
		DBUtility.connection = connection;
	}

	public static Connection getDBConnection() {
		Connection connection = null;
		try {
			if (getConnection() == null) {
				Class.forName("org.postgresql.Driver");
				connection = DriverManager
						.getConnection(
								"jdbc:postgresql://ravidb.cjqzbq5pdqys.eu-west-1.rds.amazonaws.com:8080/raviDB",
								"raviDB", "raviDB1!");
				setConnection(connection);
			} else {
				return getConnection();
			}
		} catch (Exception exception) {
			System.out.println(exception);
		}
		return getConnection();
	}

	public static List<Student> getStudents(String query) {
		Statement statement = null;
		ResultSet rs = null;
		List<Student> students = new ArrayList<Student>();
		try {
			statement = getDBConnection().createStatement();
			rs = statement.executeQuery(query);
			while (rs.next()) {
				Student student = new Student();
				student.setId(rs.getInt("id"));
				student.setName(rs.getString("name"));
				student.setClassNumber(rs.getInt("class"));
				student.setAdmissionYear(rs.getString("admissionyear"));
				student.setActive(rs.getBoolean("active"));
				students.add(student);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			try {
				statement.close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return students;
	}

	public static String buildQuery(String classNumber, boolean active,
			String admissionYear, Integer id) {
		StringBuilder query = new StringBuilder("SELECT * FROM STUDENT");
		if (admissionYear != null && !admissionYear.isEmpty()) {
			if (!query.toString().contains(Where_Condition)) {
				query.append(Where_Condition);
			}
			query.append("ADMISSIONYEAR=");
			query.append(admissionYear);
		}
		if (active) {
			if (!query.toString().contains(Where_Condition)) {
				query.append(Where_Condition);
			} else {
				query.append(" AND ");
			}
			query.append("ACTIVE=");
			query.append(active);
		}
		if (classNumber != null && !classNumber.isEmpty()) {
			if (classNumber.contains(",")) {
				String classSplit[] = classNumber.split(",");
				for (int i = 0; i < classSplit.length; i++) {
					if (!query.toString().contains(Where_Condition)) {
						query.append(Where_Condition);
					} else {
						query.append(" AND ");
					}				
					query.append("CLASS=");
					query.append(classSplit[i]);
				}
			} else {
				if (!query.toString().contains(Where_Condition)) {
					query.append(Where_Condition);
				} else {
					query.append(" AND ");
				}

				query.append("CLASS=");
				query.append(classNumber);
			}
		}
			if (id != null && id != 0) {
				if (!query.toString().contains(Where_Condition)) {
					query.append(Where_Condition);
				} else {
					query.append(" AND ");
				}
				query.append("ID=");
				query.append(id);
			}
			query.append(";");
			return query.toString();		
	}

	public static void main(String[] args) {
		try {

			// Connection connection = getDBConnection();
			// Statement statement = connection.createStatement();
			// String sql = "CREATE TABLE STUDENT " +
			// "(ID INT PRIMARY KEY NOT NULL," +
			// " NAME             TEXT    NOT NULL, " +
			// " CLASS            INT     NOT NULL, " +
			// " ADMISSIONYEAR    TEXT    NOT NULL, " +
			// " ACTIVE         BOOLEAN)";
			// statement.executeUpdate(sql);
			// String sql1 =
			// "INSERT INTO STUDENT (ID,NAME,CLASS,ADMISSIONYEAR,ACTIVE) "
			// + "VALUES (1, 'varma', '1', '1995', TRUE );";
			// String sql2 =
			// "INSERT INTO STUDENT (ID,NAME,CLASS,ADMISSIONYEAR,ACTIVE) "
			// + "VALUES (2, 'RAVI', '1', '1996', TRUE );";
			// String sql3 =
			// "INSERT INTO STUDENT (ID,NAME,CLASS,ADMISSIONYEAR,ACTIVE) "
			// + "VALUES (3, 'TRINS', '2', '1999', TRUE );";
			// // String sql = "DELETE from STUDENT where ID = 6;";
			// statement.executeUpdate(sql1);
			// statement.executeUpdate(sql2);
			// statement.executeUpdate(sql3);
			// System.out.println("EXECUTED");
			// System.out.println(getStudents(buildQuery(null, false, null,
			// 0)));
		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}
}