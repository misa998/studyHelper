package com.studyhelper.controller.Courses;

import com.studyhelper.db.entity.Course;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/*
*   class that holds all the components for course list
*   in courseOverview controller
 */
public class VBoxForCourse {
    private VBox vBox;
    private VBox vBoxModel;
    private Course course;
    private TextField nameTextField;
    private Label labelForHours;
    private Label labelForDue;

    public VBoxForCourse(Course course, VBox vBoxToCopyFrom) {
        this.course = course;
        this.vBoxModel = vBoxToCopyFrom;
        this.vBox = new VBox();
    }

    public VBoxForCourse() {
        this.vBox = new VBox();
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public TextField getNameTextField() {
        return nameTextField;
    }

    public void setNameTextField(TextField nameTextField) {
        this.nameTextField = nameTextField;
    }

    public Label getLabelForHours() {
        return labelForHours;
    }

    public void setLabelForHours(Label labelForHours) {
        this.labelForHours = labelForHours;
    }

    public Label getLabelForDue() {
        return labelForDue;
    }

    public void setLabelForDue(Label labelForDue) {
        this.labelForDue = labelForDue;
    }

    public VBox getvBox() {
        return vBox;
    }

    public void setvBox(VBox vBox) {
        this.vBox = vBox;
    }

    public VBox getvBoxModel() {
        return vBoxModel;
    }

    public void setvBoxModel(VBox vBoxModel) {
        this.vBoxModel = vBoxModel;
    }

    @Override
    public String toString() {
        return "VBoxForCourse{" +
                "vBox=" + vBox +
                ", vBoxModel=" + vBoxModel +
                ", course=" + course +
                ", nameTextField=" + nameTextField +
                ", labelForHours=" + labelForHours +
                ", labelForDue=" + labelForDue +
                '}';
    }
}
