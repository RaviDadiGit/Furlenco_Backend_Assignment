package com.furlenco.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

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
				student.setAdmissionYear(rs.getInt("admissionyear"));
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
			Integer admissionYearBefore,Integer admissionYearAfter,Integer id) {
		StringBuilder query = new StringBuilder("SELECT * FROM STUDENT");
		if (admissionYearAfter != null) {
			if (!query.toString().contains(Where_Condition)) {
				query.append(Where_Condition);
			}
			query.append("ADMISSIONYEAR >=");
			query.append(admissionYearAfter);
		}
		if (admissionYearBefore != null) {
			if (!query.toString().contains(Where_Condition)) {
				query.append(Where_Condition);
			}else {
				query.append(" AND ");
			}
			query.append("ADMISSIONYEAR <=");
			query.append(admissionYearBefore);
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
						query.append("CLASS IN ( ");
					}
					query.append(classSplit[i]);
					query.append(", ");
				}
				query.deleteCharAt(query.length() - 2);
				query.append(")");
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

	public static void insertStudents(List<Student> students) {
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		try {
			connection = getDBConnection();
			connection.setAutoCommit(false);
			preparedStatement = connection
					.prepareStatement("INSERT INTO STUDENT(ID,NAME,CLASS,ADMISSIONYEAR,ACTIVE) VALUES(?,?,?,?,?);");
			for (Student student : students) {
				preparedStatement.setInt(1, student.getId());
				preparedStatement.setString(2, student.getName());
				preparedStatement.setInt(3, student.getClassNumber());
				preparedStatement.setInt(4, student.getAdmissionYear());
				preparedStatement.setBoolean(5, student.isActive());
				preparedStatement.executeUpdate();
			}
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void inactiveStudent(int id) {
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		try {
			connection = getDBConnection();
			//connection.setAutoCommit(false);
			preparedStatement = connection
					.prepareStatement("UPDATE STUDENT set ACTIVE = ? where ID="
							+ id);
			preparedStatement.setBoolean(1, false);
			preparedStatement.executeUpdate();
			//connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				preparedStatement.close();
				//connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public static void updateStudent(int id) {
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		try {
			List<Student> students = getStudents(buildQuery(null, false, null,null, id));
			int classNumber  = students.get(0).getClassNumber();
			connection = getDBConnection();
			//connection.setAutoCommit(false);
			preparedStatement = connection
					.prepareStatement("UPDATE STUDENT set CLASS = ? where ID="
							+ id);
			preparedStatement.setInt(1, classNumber+1);
			preparedStatement.executeUpdate();
			//connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				preparedStatement.close();
				//connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
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
			//System.out.println(getStudents(buildQuery("1,2", false, null,null, 0)));
		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}
}
