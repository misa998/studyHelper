package sample;

import java.time.LocalDate;

public class Course {

    private int id;
    private String name;
    private String description;
    private LocalDate due;
    private int student_id;

    public Course(){}

    public Course(int id, String name, String description, LocalDate due, int student_id) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.due = due;
        this.student_id = student_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDue() {
        return due;
    }

    public void setDue(LocalDate due) {
        this.due = due;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", due=" + due +
                ", student_id=" + student_id +
                '}';
    }
}
