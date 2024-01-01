/**
 * 
 */
package com.usermnagement.Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.usermangement.model.User;

//This  Dao provieds CRUD database operation for the table users in the databse.
public class UserDao {
	private String jdbcURL = "jdbc:mysql://localhost:3306/demo?useSSL=false";
	private String jdbcUserName = "root";
	private String jdbcPassword = "root";

	private static final String INSERT_USERS_SQL = "INSERT INTO users" + "(name,email,country)VALUES" + "(?,?,?);";
	private static final String UPDATE_USERS_SQL = "update users set name=?,email=?,country=? where id=?;";
	private static final String SELECT_USER_BY_ID = "select id,name,email,country from users where id=?";
	private static final String SELECT_ALL_USERS = "select * from users";
	private static final String DELETE_USERS_SQL = "delete from users where id=?;";
	

	protected Connection getConnection() throws SQLException {
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcURL, jdbcUserName, jdbcPassword);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new SQLException("Database driver not found", e);
		}
		return connection;
	}
	// create or insert user

	public void insertUser(User user) throws SQLException {
		try (Connection connection = getConnection();
				PreparedStatement preparedstatement = connection.prepareStatement(INSERT_USERS_SQL)) {
			preparedstatement.setString(1, user.getName());
			preparedstatement.setString(2, user.getEmail());
			preparedstatement.setString(3, user.getCountry());
			preparedstatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	// update user

	public boolean updateUser(User user) throws SQLException {
		boolean rowUpdated;
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE_USERS_SQL);) {
			statement.setString(1, user.getName());
			statement.setString(2, user.getEmail());
			statement.setString(3, user.getCountry());
			statement.setInt(4, user.getId());

			rowUpdated = statement.executeUpdate() > 0;
		}
		return rowUpdated;
	}

	/* select user by id
	 * **********************************
	 */

//	public User selectUser(int id) throws SQLException {
//		User user = null;
//
//		// Step 1 : Establishing a connection
//		try (Connection connection = getConnection();
//				// Step 2 :Create statement using connection object
//				PreparedStatement statement = connection.prepareStatement(SELECT_USER_BY_ID);) {
//			statement.setInt(1, id);
//			System.out.println(statement);
//
//			// Step 3 : Execute the query
//			ResultSet rs = statement.executeQuery();
//
//			// Step 4 : Process the ResultSet Object
//			while (rs.next()) {
//				String name = rs.getString("name");
//				String email = rs.getString("email");
//				String country = rs.getString("country");
//				user = new User(id, name, email, country);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//
//		return user;
//
//	}
	
	public User selectUser(int id) throws SQLException {
        User user = null;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_USER_BY_ID)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("name");
                    String email = rs.getString("email");
                    String country = rs.getString("country");
                    user = new User(id, name, email, country);
                }
            }
        }
        return user;
    }

	/* select all users
	 * **************************
	 
	public List<User> selectAllUser() throws SQLException {
		List<User> users = new ArrayList<>();
		// Step 1 : Establishing a connection
		try (Connection connection = getConnection();
				// Step 2 :Create statement using connection object
				PreparedStatement statement = connection.prepareStatement(SELECT_ALL_USERS);) {

			System.out.println(statement);

			// Step 3 : Execute the query
			ResultSet rs = statement.executeQuery();

			// Step 4 : Process the ResultSet Object
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String email = rs.getString("email");
				String country = rs.getString("country");
				users.add(new User(id, name, email, country));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return users;

	}
	*/
	public List<User> selectAllUser() throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_USERS);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String country = rs.getString("country");
                users.add(new User(id, name, email, country));
            }
        }
        return users;
    }

	// delete user
	public boolean deleteUser(int id) throws SQLException {
		boolean rowDeleted;
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_USERS_SQL);) {
			statement.setInt(1, id);
			rowDeleted = statement.executeUpdate() > 0;
		}
		return rowDeleted;
	}

}
