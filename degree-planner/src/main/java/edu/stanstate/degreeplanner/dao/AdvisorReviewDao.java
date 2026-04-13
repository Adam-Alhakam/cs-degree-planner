package edu.stanstate.degreeplanner.dao;

import edu.stanstate.degreeplanner.utils.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdvisorReviewDao {
  public void create(long planId, long advisorUserId, String decision, String comment) throws SQLException {
    String sql = "INSERT INTO advisor_reviews(plan_id, advisor_user_id, decision, comment) VALUES(?,?,?,?)";
    try (Connection c = DB.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setLong(1, planId);
      ps.setLong(2, advisorUserId);
      ps.setString(3, decision);
      ps.setString(4, comment);
      ps.executeUpdate();
    }
  }
}