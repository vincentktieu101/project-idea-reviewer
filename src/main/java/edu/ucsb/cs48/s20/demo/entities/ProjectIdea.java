package edu.ucsb.cs48.s20.demo.entities;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;

@Entity
public class ProjectIdea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @NotBlank
    private String title;

    @NotBlank
    @Lob
    private String details;

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Student getStudent() {
        return this.student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return this.details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ProjectIdea)) {
            return false;
        }
        ProjectIdea projectIdea = (ProjectIdea) o;
        return id == projectIdea.id && Objects.equals(student, projectIdea.student)
                && Objects.equals(title, projectIdea.title) && Objects.equals(details, projectIdea.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, student, title, details);
    }

    @Override
    public String toString() {
        return "{" + " id='" + getId() + "'" + ", student='" + getStudent() + "'" + ", title='" + getTitle() + "'"
                + ", details='" + getDetails() + "'" + "}";
    }

}