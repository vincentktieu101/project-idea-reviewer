INSERT INTO project_idea (title, details) VALUES ('Cat Wheel', 'An app for your cats to play with digital mice');
INSERT INTO project_idea (title, details) VALUES ('Smart Stapler', 'A bluetooth electronic stapler to make stapling papers as complicated as possible');


INSERT INTO student (email, fname, lname, perm,  project_idea_id) VALUES ('pconrad.cis@gmail.com','Phill','Conrad', 123456, 1);
INSERT INTO student (email, fname, lname, perm) VALUES ('tehchowster@gmail.com','Scott','Chow', 222222);
INSERT INTO student (email, fname, lname, perm) VALUES ('scottpchow@gmail.com','Scott','Chow', 232323);
INSERT INTO student (email, fname, lname, perm) VALUES ('andrewgege@gmail.co','Andrew','Lu', 3333);
INSERT INTO student (email, fname, lname, perm, project_idea_id) VALUES ('colebergmann@gmail.com','Cole','Bergmann', 44444, 2);
INSERT INTO student (email, fname, lname, perm) VALUES ('bryan.terce@gmail.com','Bryan','Terce', 566555);
INSERT INTO student (email, fname, lname, perm) VALUES ('andylord288@gmail.com','Andrew','Lu', 777777);
INSERT INTO student (email, fname, lname, perm) VALUES ('jacquimai27@gmail.com','Jacqui','Mai', 123456);


INSERT INTO review (details, rating, idea_id, student_id) VALUES ('test details', 5, 1, 1)