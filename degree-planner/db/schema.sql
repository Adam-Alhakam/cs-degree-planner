CREATE DATABASE IF NOT EXISTS degree_planner;
USE degree_planner;

DROP TABLE IF EXISTS advisor_reviews;
DROP TABLE IF EXISTS plan_courses;
DROP TABLE IF EXISTS plans;
DROP TABLE IF EXISTS prerequisites;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  email VARCHAR(255) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  role ENUM('STUDENT','ADVISOR','PARENT') NOT NULL,
  linked_student_user_id BIGINT NULL,  -- for parent
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (linked_student_user_id) REFERENCES users(id)
);

CREATE TABLE courses (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(20) NOT NULL UNIQUE,
  title VARCHAR(255) NOT NULL,
  units INT NOT NULL,
  days VARCHAR(10) NOT NULL,     -- e.g., "MWF", "TR"
  start_time TIME NOT NULL,
  end_time TIME NOT NULL
);

CREATE TABLE prerequisites (
  course_id BIGINT NOT NULL,
  prereq_course_id BIGINT NOT NULL,
  PRIMARY KEY (course_id, prereq_course_id),
  FOREIGN KEY (course_id) REFERENCES courses(id),
  FOREIGN KEY (prereq_course_id) REFERENCES courses(id)
);

CREATE TABLE plans (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  student_user_id BIGINT NOT NULL,
  status ENUM('DRAFT','SUBMITTED','APPROVED','DENIED') NOT NULL DEFAULT 'DRAFT',
  advisor_comment TEXT NULL,
  total_completed_units INT NOT NULL DEFAULT 0,
  total_required_units INT NOT NULL DEFAULT 120, -- simple default
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (student_user_id) REFERENCES users(id)
);

CREATE TABLE plan_courses (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  plan_id BIGINT NOT NULL,
  term ENUM('SPRING','SUMMER','FALL','WINTER') NOT NULL,
  year INT NOT NULL,
  course_id BIGINT NOT NULL,
  status ENUM('PLANNED','COMPLETED') NOT NULL DEFAULT 'PLANNED',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (plan_id) REFERENCES plans(id),
  FOREIGN KEY (course_id) REFERENCES courses(id),
  UNIQUE(plan_id, term, year, course_id)
);

CREATE TABLE advisor_reviews (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  plan_id BIGINT NOT NULL,
  advisor_user_id BIGINT NOT NULL,
  decision ENUM('APPROVED','DENIED') NOT NULL,
  comment TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (plan_id) REFERENCES plans(id),
  FOREIGN KEY (advisor_user_id) REFERENCES users(id)
);