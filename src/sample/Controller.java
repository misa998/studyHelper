package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.List;

public class Controller {

    @FXML
    private Pane coursePanel;
    @FXML
    private Button button;
    @FXML
    private VBox vBox;

    public void initialize(){

    }

    public void courseTabChange(){
        vBox.getChildren().clear();

        DataSource.getInstance().openConnection();
        DataSource.getInstance().getAllTime();
        List<Course> courseList = DataSource.getInstance().getAllCourses();
        if(courseList == null) {
            System.out.println("Nulcina");
            return;
        }

        int i = 1;

        for (Course course : courseList) {
            Button button2 = new Button();
            button2.setText(course.getName());
            button2.setId("button" + i++);
            button2.setPrefWidth(button.getPrefWidth());
            button2.setPrefHeight(button.getPrefHeight());
            vBox.getChildren().add(button2);
        }

        DataSource.getInstance().closeConnection();
    }

    public void addCourse(){
        DataSource.getInstance().openConnection();
        DataSource.getInstance().insertCourse(new Course(0, "art", "description", LocalDate.now().plusMonths(2), 1));
        DataSource.getInstance().closeConnection();
    }
}
