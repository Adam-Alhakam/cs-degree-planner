package edu.stanstate.degreeplanner.dao;

import edu.stanstate.degreeplanner.models.Course;
import edu.stanstate.degreeplanner.utils.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDao {

    public List<Course> listAll() throws SQLException {
        String sql = "SELECT * FROM courses ORDER BY code";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Course> out = new ArrayList<>();
            while (rs.next()) {
                out.add(new Course(
                        rs.getLong("id"),
                        rs.getString("code"),
                        rs.getString("title"),
                        rs.getInt("units"),
                        rs.getString("days"),
                        rs.getTime("start_time").toLocalTime(),
                        rs.getTime("end_time").toLocalTime()
                ));
            }
            return out;
        }
    }

    public Course getById(long courseId) throws SQLException {
        String sql = "SELECT * FROM courses WHERE id=?";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, courseId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                return new Course(
                        rs.getLong("id"),
                        rs.getString("code"),
                        rs.getString("title"),
                        rs.getInt("units"),
                        rs.getString("days"),
                        rs.getTime("start_time").toLocalTime(),
                        rs.getTime("end_time").toLocalTime()
                );
            }
        }
    }

    public List<Long> getPrerequisiteIds(long courseId) throws SQLException {
        String sql = "SELECT prereq_course_id FROM prerequisites WHERE course_id=?";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, courseId);

            try (ResultSet rs = ps.executeQuery()) {
                List<Long> ids = new ArrayList<>();
                while (rs.next()) {
                    ids.add(rs.getLong("prereq_course_id"));
                }
                return ids;
            }
        }
    }
}