package edu.stanstate.degreeplanner.dao;

import edu.stanstate.degreeplanner.models.Course;
import edu.stanstate.degreeplanner.models.Plan;
import edu.stanstate.degreeplanner.models.PlanCourse;
import edu.stanstate.degreeplanner.utils.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PlanDao {

    public Plan getPlanForStudent(long studentUserId) throws SQLException {
        String sql = "SELECT * FROM plans WHERE student_user_id=? ORDER BY id DESC LIMIT 1";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, studentUserId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new Plan(
                        rs.getLong("id"),
                        rs.getLong("student_user_id"),
                        rs.getString("status"),
                        rs.getString("advisor_comment"),
                        rs.getInt("total_completed_units"),
                        rs.getInt("total_required_units"),
                        rs.getTimestamp("updated_at")
                );
            }
        }
    }

    public Plan ensurePlanForStudent(long studentUserId) throws SQLException {
        Plan plan = getPlanForStudent(studentUserId);
        if (plan != null) {
            return plan;
        }

        String insertSql = "INSERT INTO plans(student_user_id, status, total_completed_units, total_required_units) VALUES(?, 'DRAFT', 0, 120)";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(insertSql)) {
            ps.setLong(1, studentUserId);
            ps.executeUpdate();
        }

        return getPlanForStudent(studentUserId);
    }

    public List<PlanCourse> listPlanCourses(long planId) throws SQLException {
        String sql = """
            SELECT pc.id as pc_id, pc.plan_id, pc.term, pc.year, pc.status as pc_status,
                   c.*
            FROM plan_courses pc
            JOIN courses c ON c.id = pc.course_id
            WHERE pc.plan_id=?
            ORDER BY pc.year,
                     CASE pc.term
                         WHEN 'SPRING' THEN 1
                         WHEN 'SUMMER' THEN 2
                         WHEN 'FALL' THEN 3
                         WHEN 'WINTER' THEN 4
                         ELSE 5
                     END,
                     c.code
            """;
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, planId);
            try (ResultSet rs = ps.executeQuery()) {
                List<PlanCourse> out = new ArrayList<>();
                while (rs.next()) {
                    Course course = new Course(
                            rs.getLong("id"),
                            rs.getString("code"),
                            rs.getString("title"),
                            rs.getInt("units"),
                            rs.getString("days"),
                            rs.getTime("start_time").toLocalTime(),
                            rs.getTime("end_time").toLocalTime()
                    );
                    out.add(new PlanCourse(
                            rs.getLong("pc_id"),
                            rs.getLong("plan_id"),
                            rs.getString("term"),
                            rs.getInt("year"),
                            course,
                            rs.getString("pc_status")
                    ));
                }
                return out;
            }
        }
    }

    public void addCourse(long planId, String term, int year, long courseId) throws SQLException {
        String sql = "INSERT IGNORE INTO plan_courses(plan_id, term, year, course_id, status) VALUES(?,?,?,?, 'PLANNED')";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, planId);
            ps.setString(2, term);
            ps.setInt(3, year);
            ps.setLong(4, courseId);
            ps.executeUpdate();
        }
    }

    public void removeCourse(long planCourseId) throws SQLException {
        String sql = "DELETE FROM plan_courses WHERE id=?";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, planCourseId);
            ps.executeUpdate();
        }
    }

    public boolean updateStatusWithOptimisticLock(long planId, String newStatus, String comment, Timestamp expectedUpdatedAt) throws SQLException {
        String sql = "UPDATE plans SET status=?, advisor_comment=? WHERE id=? AND updated_at=?";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setString(2, comment);
            ps.setLong(3, planId);
            ps.setTimestamp(4, expectedUpdatedAt);
            return ps.executeUpdate() == 1;
        }
    }

    public void updateCompletedUnits(long planId, int completedUnits) throws SQLException {
        String sql = "UPDATE plans SET total_completed_units=? WHERE id=?";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, completedUnits);
            ps.setLong(2, planId);
            ps.executeUpdate();
        }
    }

    public List<Plan> listSubmittedPlans() throws SQLException {
        String sql = "SELECT * FROM plans WHERE status='SUBMITTED' ORDER BY updated_at DESC";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Plan> out = new ArrayList<>();
            while (rs.next()) {
                out.add(new Plan(
                        rs.getLong("id"),
                        rs.getLong("student_user_id"),
                        rs.getString("status"),
                        rs.getString("advisor_comment"),
                        rs.getInt("total_completed_units"),
                        rs.getInt("total_required_units"),
                        rs.getTimestamp("updated_at")
                ));
            }
            return out;
        }
    }

    public Plan getPlanById(long planId) throws SQLException {
        String sql = "SELECT * FROM plans WHERE id=?";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, planId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new Plan(
                        rs.getLong("id"),
                        rs.getLong("student_user_id"),
                        rs.getString("status"),
                        rs.getString("advisor_comment"),
                        rs.getInt("total_completed_units"),
                        rs.getInt("total_required_units"),
                        rs.getTimestamp("updated_at")
                );
            }
        }
    }
}