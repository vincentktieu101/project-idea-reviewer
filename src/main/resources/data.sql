/* Add some ideas */
INSERT INTO project_idea (title, details) VALUES ('Cat Wheel', 'An app for your cats to play with digital mice');
INSERT INTO project_idea (title, details) VALUES ('Smart Stapler', 'A bluetooth electronic stapler to make stapling papers as complicated as possible');
INSERT INTO project_idea (title, details) VALUES ('Idea3', 'Idea description');
INSERT INTO project_idea (title, details) VALUES ('Idea4', 'Idea description');
INSERT INTO project_idea (title, details) VALUES ('Idea5', 'Idea description');
INSERT INTO project_idea (title, details) VALUES ('Idea6', 'Idea description');

/* Add and assign some students to those ideas */
INSERT INTO student (email, fname, lname, perm,  project_idea_id) VALUES ('pconrad.cis@gmail.com','Phill','Conrad', 123456, 1);
INSERT INTO student (email, fname, lname, perm, project_idea_id) VALUES ('s1@ucsb.edu','Student','Student', 4554534, 2);
INSERT INTO student (email, fname, lname, perm, project_idea_id) VALUES ('s2@ucsb.edu','Student','Student', 4554535, 3);
INSERT INTO student (email, fname, lname, perm, project_idea_id) VALUES ('s3@ucsb.edu','Student','Student', 4554536, 4);
INSERT INTO student (email, fname, lname, perm, project_idea_id) VALUES ('s4@ucsb.edu','Student','Student', 4554537, 5);
INSERT INTO student (email, fname, lname, perm, project_idea_id) VALUES ('s4@ucsb.edu','Student','Student', 4554537, 6);

/* Add some other students for testing */
INSERT INTO student (email, fname, lname, perm) VALUES ('colebergmann@gmail.com','Cole','Bergmann', 44444);
INSERT INTO student (email, fname, lname, perm) VALUES ('tehchowster@gmail.com','Scott','Chow', 222222);
INSERT INTO student (email, fname, lname, perm) VALUES ('scottpchow@gmail.com','Scott','Chow', 232323);
INSERT INTO student (email, fname, lname, perm) VALUES ('andrewgege@gmail.com','Andrew','Lu', 3333);
INSERT INTO student (email, fname, lname, perm) VALUES ('bryan.terce@gmail.com','Bryan','Terce', 566555);
INSERT INTO student (email, fname, lname, perm) VALUES ('andylord288@gmail.com','Andrew','Lu', 777777);
INSERT INTO student (email, fname, lname, perm) VALUES ('jacquimai27@gmail.com','Jacqui','Mai', 123456);

/* Add student data for end2end test */
INSERT INTO student (email, fname, lname, perm) VALUES ('joe@ucsb.edu','Joe','Gacho', 555123);

/* Add reviews */
INSERT INTO review (details, rating, idea_id, student_id) VALUES ('test details', 5, 1, 1)