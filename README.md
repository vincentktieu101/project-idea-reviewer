# cs48 s20 project-idea-reviewer

This app allows an instructor to set up the students
in a course so that each one can:
1. Enter one project idea
2. Once they have entered a project idea,
   rate other students project ideas.

Some rules that are enforced:

1. Each student must enter a project idea before they
   can see other students project ideas
2. Each student should rate at least five other
   project ideas.
3. Each project idea should receive at least five ratings.

Instructor Features:

1. As an instructor, I can upload a CSV file of students
   in egrades format, and it will populate a students 
   table with
   first name, last name, email and perm number.
2. As an instructor, I can do basic CRUD operations on 
   the students table.
3. As an instructor I can download a CSV of all project
   ideas.   The student's perm, first name, last name, email, project title, project idea, and average project rating is listed.
4. As an instructor I can download a CSV of all ratings.
   Ratings show the project id, the title, the numeric rating,

Student Features, Project Entry
1. As a student, I can enter a project title, and a
   brief description.   There is a minimum and maximum
   word length.

Student Features, Project Rating
1. As a student, I can see how many additional ratings 
   I have to enter, and how many ratings the class needs to enter to be done. 
2. As a student, I can ask to enter another rating.
   I will not be shown my own project, nor any idea 
   I have already rated.
3. When rating, I am shown the project idea, and can
   choose 1 through 5, and enter a comment.   The comment
   has a minimum and maximum word length.
4. When no more ratings are needed, I cannot enter
   another rating.

# Configuration for OAuth

See instructions at <https://ucsb-cs48.github.io/topics/oauth_google_setup>

After configuring OAuth:
* Use `mvn spring-boot:run` to run on localhost

To run tests: `mvn test`

Note that for Heroku, you will need to login with the `heroku login` command line tool
in order to be able to run the script that setup up Heroku credentials.



