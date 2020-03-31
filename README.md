# cs48 s20 project-idea-reviewer

This app allows an instructor to set up the students
in a course so that each one can:
1. OK Enter one project idea
2. OK Once they have entered a project idea,
   rate other students project ideas.

Some rules that are enforced:

1. OK Each student must enter a project idea before they
   can see other students project ideas
2. OK Each student should rate at least five other
   project ideas.
3. IN PROGRESS (#14/#15) Each project idea should receive at least five ratings.

Instructor Features:

1. OK As an instructor, I can upload a CSV file of students
   in egrades format, and it will populate a students 
   table with
   first name, last name, email and perm number.
2. OK As an instructor, I can do delete on 
   the students table.
2. OK As an instructor, I can upload a CSV that will add
   a new students without deleting anyone else
   (MVP crud substitute).
3. OK As an instructor I can copy/paste project ideas 
   and/or ratings
   into a Google Sheet. 
4. NEED TO DO The index page for project ideas shows number of
   ratings and average rating.

Student Features, Project Entry
1. OK As a student, I can enter a project title, and a
   brief description.   There is a minimum and maximum
   word length.

Student Features, Project Rating
1. OK As a student, I can see how many additional ratings 
   I have to enter.
1. NICE TO HAVE As a student/instructor, I can see how many additional ratings 
   the class needs to enter to be done. 
   (Admin can do it by looking project index page,
   and sorting by #ratings.)
2. OK As a student, enter  ratings.
   I will not be shown my own project, nor any idea 
   I have already rated.
3. NEED LIMIT ON DETAIL FIELD When rating, I am shown the project idea, and can
   choose 1 through 5, and enter a comment.   The comment
   has a minimum and maximum word length.
4. NEEDS DONE When no more ratings are needed, I can continue
   to enter extra ratings.

# Configuration for OAuth

See instructions at <https://ucsb-cs48.github.io/topics/oauth_google_setup>

After configuring OAuth:
* Use `mvn spring-boot:run` to run on localhost

To run tests: `mvn test`

Note that for Heroku, you will need to login with the `heroku login` command line tool
in order to be able to run the script that setup up Heroku credentials.



