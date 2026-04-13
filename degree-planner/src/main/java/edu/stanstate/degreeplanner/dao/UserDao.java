package edu.stanstate.degreeplanner.dao;

import edu.stanstate.degreeplanner.models.User;
import edu.stanstate.degreeplanner.utils.DB;

import java.sql.*;

public class UserDao {

  public User findByEmail(String email) throws SQLException {
    String sql = "SELECT id, email, role, linked_student_user_id FROM users WHERE email=?";
    try (Connection c = DB.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setString(1, email);
      try (ResultSet rs = ps.executeQuery()) {
        if (!rs.next()) return null;
        Long linked = rs.getObject("linked_student_user_id") == null ? null : rs.getLong("linked_student_user_id");
        return new User(rs.getLong("id"), rs.getString("email"), rs.getString("role"), linked);
      }
    }
  }

  public String getPasswordHashByEmail(String email) throws SQLException {
    String sql = "SELECT password_hash FROM users WHERE email=?";
    try (Connection c = DB.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setString(1, email);
      try (ResultSet rs = ps.executeQuery()) {
        return rs.next() ? rs.getString("password_hash") : null;
      }
    }
  }
}