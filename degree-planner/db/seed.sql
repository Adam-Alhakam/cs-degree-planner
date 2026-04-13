SET FOREIGN_KEY_CHECKS = 0;

-- Clear data in dependency order
DELETE FROM advisor_reviews;
DELETE FROM plan_courses;
DELETE FROM prerequisites;
DELETE FROM plans;
DELETE FROM courses;
DELETE FROM users;

SET FOREIGN_KEY_CHECKS = 1;

-- =========================================================
-- USERS
-- Using plain text in password_hash for quick demo login
-- because PasswordService currently compares plain text.
-- =========================================================
INSERT INTO users (id, email, password_hash, role, linked_student_user_id)
VALUES
  (1, 'student@stan.edu', 'test123', 'STUDENT', NULL),
  (2, 'advisor@stan.edu', 'test123', 'ADVISOR', NULL),
  (3, 'parent@stan.edu',  'test123', 'PARENT', 1);

-- =========================================================
-- COURSES
-- Matches your actual schema: code, title, units, days,
-- start_time, end_time
-- =========================================================
INSERT INTO courses (id, code, title, units, days, start_time, end_time) VALUES
  (101, 'CS1500',   'Computer Programming I',                             3, 'MWF', '09:00:00', '09:50:00'),
  (102, 'CS2500',   'Computer Programming II',                            3, 'MWF', '10:00:00', '10:50:00'),
  (103, 'CS2700',   'Assembly Language and Computer Architecture',        3, 'TR',  '11:00:00', '12:15:00'),
  (104, 'MATH1410', 'Calculus I',                                         4, 'MWF', '12:00:00', '12:50:00'),
  (105, 'MATH1420', 'Calculus II',                                        4, 'MWF', '13:00:00', '13:50:00'),
  (106, 'MATH1620', 'Probability and Statistics',                         4, 'TR',  '09:00:00', '10:15:00'),
  (107, 'MATH1600', 'Statistics',                                         4, 'TR',  '10:30:00', '11:45:00'),
  (108, 'MATH2300', 'Discrete Structures',                                3, 'MWF', '11:00:00', '11:50:00'),

  (109, 'PHYS2250', 'General Physics I',                                  4, 'TR',  '13:00:00', '14:15:00'),
  (110, 'PHYS2252', 'General Physics Laboratory I',                       1, 'F',   '14:00:00', '16:50:00'),
  (111, 'PHYS2260', 'General Physics II',                                 4, 'TR',  '14:30:00', '15:45:00'),
  (112, 'PHYS2262', 'General Physics Laboratory II',                      1, 'F',   '09:00:00', '11:50:00'),

  (113, 'CHEM1100', 'General Chemistry I',                                4, 'MWF', '14:00:00', '14:50:00'),
  (114, 'CHEM1102', 'General Chemistry I Laboratory',                     1, 'F',   '12:00:00', '14:50:00'),
  (115, 'CHEM1110', 'General Chemistry II',                               4, 'MWF', '15:00:00', '15:50:00'),
  (116, 'CHEM1112', 'General Chemistry II Laboratory',                    1, 'F',   '15:00:00', '17:50:00'),

  (117, 'BIOL1050', 'General Biology I',                                  4, 'TR',  '08:00:00', '09:15:00'),
  (118, 'BIOL1150', 'General Biology II',                                 4, 'TR',  '09:30:00', '10:45:00'),

  (119, 'CS3100',   'Data Structures and Algorithms',                     3, 'MWF', '10:00:00', '10:50:00'),
  (120, 'CS3740',   'Computer Organization',                              3, 'TR',  '11:30:00', '12:45:00'),
  (121, 'CS3750',   'Operating Systems I',                                3, 'MWF', '11:00:00', '11:50:00'),
  (122, 'CS4100',   'Programming Languages (WP)',                         3, 'TR',  '13:00:00', '14:15:00'),
  (123, 'CS4960',   'Seminar in Computer Science',                        1, 'F',   '10:00:00', '10:50:00'),

  (124, 'CS4300',   'Compiler Theory',                                    3, 'MWF', '12:00:00', '12:50:00'),
  (125, 'CS4410',   'Automata, Computability, and Formal Languages',      3, 'MWF', '13:00:00', '13:50:00'),
  (126, 'CS4440',   'Theory of Algorithms',                               3, 'MWF', '11:00:00', '11:50:00'),
  (127, 'CS4450',   'Coding and Information Theory',                      3, 'TR',  '14:30:00', '15:45:00'),

  (128, 'CS4250',   'Database Management Systems',                        3, 'MWF', '14:00:00', '14:50:00'),
  (129, 'CS4270',   'Ecommerce Systems Design',                           3, 'TR',  '15:00:00', '16:15:00'),
  (130, 'CS4800',   'Software Engineering',                               3, 'MWF', '15:00:00', '15:50:00'),

  (131, 'CS3000',   'Communication Networks',                             3, 'TR',  '08:00:00', '09:15:00'),
  (132, 'CS3150',   'Nonlinear Systems and Chaos',                        3, 'TR',  '09:30:00', '10:45:00'),
  (133, 'CS3200',   'Computer Simulation Techniques',                     3, 'TR',  '11:00:00', '12:15:00'),
  (134, 'CS3500',   'Human-Centered Design',                              3, 'MWF', '09:00:00', '09:50:00'),
  (135, 'CS3600',   'Computer Graphics I',                                3, 'MWF', '16:00:00', '16:50:00'),
  (136, 'CS4480',   'Artificial Intelligence',                            3, 'TR',  '12:30:00', '13:45:00'),
  (137, 'CS4600',   'Computer Graphics II',                               3, 'MWF', '08:00:00', '08:50:00'),
  (138, 'CS4750',   'Operating Systems II',                               3, 'TR',  '16:30:00', '17:45:00'),
  (139, 'CS4840',   'Computer Security and Cryptography',                 3, 'MWF', '10:00:00', '10:50:00'),
  (140, 'CS4980',   'Individual Study',                                   1, 'ARR', '00:00:00', '00:00:00'),
  (141, 'PHIL4401', 'Professional Ethics',                                3, 'TR',  '12:00:00', '13:15:00');

-- =========================================================
-- PREREQUISITES
-- Uses your actual schema:
-- prerequisites(course_id, prereq_course_id)
-- =========================================================
INSERT INTO prerequisites (course_id, prereq_course_id)
SELECT c.id, p.id
FROM courses c
JOIN courses p
WHERE c.code = 'CS2500' AND p.code = 'CS1500';

INSERT INTO prerequisites (course_id, prereq_course_id)
SELECT c.id, p.id
FROM courses c
JOIN courses p
WHERE c.code = 'CS2700' AND p.code = 'CS2500';

INSERT INTO prerequisites (course_id, prereq_course_id)
SELECT c.id, p.id
FROM courses c
JOIN courses p
WHERE c.code = 'MATH1420' AND p.code = 'MATH1410';

INSERT INTO prerequisites (course_id, prereq_course_id)
SELECT c.id, p.id
FROM courses c
JOIN courses p
WHERE c.code = 'MATH1620' AND p.code = 'MATH1410';

INSERT INTO prerequisites (course_id, prereq_course_id)
SELECT c.id, p.id
FROM courses c
JOIN courses p
WHERE c.code = 'PHYS2250' AND p.code = 'MATH1410';

INSERT INTO prerequisites (course_id, prereq_course_id)
SELECT c.id, p.id
FROM courses c
JOIN courses p
WHERE c.code = 'PHYS2252' AND p.code = 'PHYS2250';

INSERT INTO prerequisites (course_id, prereq_course_id)
SELECT c.id, p.id
FROM courses c
JOIN courses p
WHERE c.code = 'PHYS2260' AND p.code = 'PHYS2250';

INSERT INTO prerequisites (course_id, prereq_course_id)
SELECT c.id, p.id
FROM courses c
JOIN courses p
WHERE c.code = 'PHYS2260' AND p.code = 'MATH1420';

INSERT INTO prerequisites (course_id, prereq_course_id)
SELECT c.id, p.id
FROM courses c
JOIN courses p
WHERE c.code = 'PHYS2262' AND p.code = 'PHYS2260';

INSERT INTO prerequisites (course_id, prereq_course_id)
SELECT c.id, p.id
FROM courses c
JOIN courses p
WHERE c.code = 'CHEM1110' AND p.code = 'CHEM1100';

INSERT INTO prerequisites (course_id, prereq_course_id)
SELECT c.id, p.id
FROM courses c
JOIN courses p
WHERE c.code = 'CHEM1110' AND p.code = 'CHEM1102';

INSERT INTO prerequisites (course_id, prereq_course_id)
SELECT c.id, p.id
FROM courses c
JOIN courses p
WHERE c.code = 'CHEM1112' AND p.code = 'CHEM1100';

INSERT INTO prerequisites (course_id, prereq_course_id)
SELECT c.id, p.id
FROM courses c
JOIN courses p
WHERE c.code = 'CHEM1112' AND p.code = 'CHEM1102';

INSERT INTO prerequisites (course_id, prereq_course_id)
SELECT c.id, p.id
FROM courses c
JOIN courses p
WHERE c.code = 'BIOL1150' AND p.code = 'BIOL1050';

INSERT INTO prerequisites (course_id, prereq_course_id)
SELECT c.id, p.id
FROM courses c
JOIN courses p
WHERE c.code = 'CS3100' AND p.code = 'CS2500';

INSERT INTO prerequisites (course_id, prereq_course_id)
SELECT c.id, p.id
FROM courses c
JOIN courses p
WHERE c.code = 'CS3740' AND p.code = 'CS2700';

INSERT INTO prerequisites (course_id, prereq_course_id)
SELECT c.id, p.id
FROM courses c
JOIN courses p
WHERE c.code = 'CS3750' AND p.code = 'CS3100';

INSERT INTO prerequisites (course_id, prereq_course_id)
SELECT c.id, p.id
FROM courses c
JOIN courses p
WHERE c.code = 'CS3750' AND p.code = 'CS3740';

INSERT INTO prerequisites (course_id, prereq_course_id)
SELECT c.id, p.id
FROM courses c
JOIN courses p
WHERE c.code = 'CS4100' AND p.code = 'CS3100';

INSERT INTO prerequisites (course_id, prereq_course_id)
SELECT c.id, p.id
FROM courses c
JOIN courses p
WHERE c.code = 'CS4300' AND p.code = 'CS4100';

INSERT INTO prerequisites (course_id, prereq_course_id)
SELECT c.id, p.id
FROM courses c
JOIN courses p
WHERE c.code = 'CS4410' AND p.code = 'CS4100';

INSERT INTO prerequisites (course_id, prereq_course_id)
SELECT c.id, p.id
FROM courses c
JOIN courses p
WHERE c.code = 'CS4440' AND p.code = 'CS3100';

INSERT INTO prerequisites (course_id, prereq_course_id)
SELECT c.id, p.id
FROM courses c
JOIN courses p
WHERE c.code = 'CS4440' AND p.code = 'MATH2300';

INSERT INTO prerequisites (course_id, prereq_course_id)
SELECT c.id, p.id
FROM courses c
JOIN courses p
WHERE c.code = 'CS4450' AND p.code = 'CS3100';

INSERT INTO prerequisites (course_id, prereq_course_id)
SELECT c.id, p.id
FROM courses c
JOIN courses p
WHERE c.code = 'CS4250' AND p.code = 'CS3100';

INSERT INTO prerequisites (course_id, prereq_course_id)
SELECT c.id, p.id
FROM courses c
JOIN courses p
WHERE c.code = 'CS4270' AND p.code = 'CS3100';

INSERT INTO prerequisites (course_id, prereq_course_id)
SELECT c.id, p.id
FROM courses c
JOIN courses p
WHERE c.code = 'CS4800' AND p.code = 'CS3100';

INSERT INTO prerequisites (course_id, prereq_course_id)
SELECT c.id, p.id
FROM courses c
JOIN courses p
WHERE c.code = 'CS3000' AND p.code = 'CS2500';

INSERT INTO prerequisites (course_id, prereq_course_id)
SELECT c.id, p.id
FROM courses c
JOIN courses p
WHERE c.code = 'CS3150' AND p.code = 'CS1500';

INSERT INTO prerequisites (course_id, prereq_course_id)
SELECT c.id, p.id
FROM courses c
JOIN courses p
WHERE c.code = 'CS3200' AND p.code = 'MATH1600';

INSERT INTO prerequisites (course_id, prereq_course_id)
SELECT c.id, p.id
FROM courses c
JOIN courses p
WHERE c.code = 'CS3200' AND p.code = 'CS1500';

INSERT INTO prerequisites (course_id, prereq_course_id)
SELECT c.id, p.id
FROM courses c
JOIN courses p
WHERE c.code = 'CS3600' AND p.code = 'CS2500';

INSERT INTO prerequisites (course_id, prereq_course_id)
SELECT c.id, p.id
FROM courses c
JOIN courses p
WHERE c.code = 'CS4480' AND p.code = 'CS3100';

INSERT INTO prerequisites (course_id, prereq_course_id)
SELECT c.id, p.id
FROM courses c
JOIN courses p
WHERE c.code = 'CS4600' AND p.code = 'CS3600';

INSERT INTO prerequisites (course_id, prereq_course_id)
SELECT c.id, p.id
FROM courses c
JOIN courses p
WHERE c.code = 'CS4750' AND p.code = 'CS3750';

INSERT INTO prerequisites (course_id, prereq_course_id)
SELECT c.id, p.id
FROM courses c
JOIN courses p
WHERE c.code = 'CS4840' AND p.code = 'CS3100';