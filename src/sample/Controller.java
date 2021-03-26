package sample;

import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.time.LocalDate;
import java.util.List;

public class Controller {

    @FXML
    private Pane coursePanel;
    @FXML
    private Button button;
    @FXML
    private VBox vBox;
    @FXML
    private StackedBarChart<String, Double> stackedBarChart;
    /*@FXML
    private StackedBarChart<Integer, Double> stackedBarChart;*/
    @FXML
    private NumberAxis yAxis;
    @FXML
    private CategoryAxis xAxis;

    public void initialize(){

    }

    @FXML
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

    @FXML
    public void addCourse(){
        DataSource.getInstance().openConnection();
        DataSource.getInstance().insertCourse(new Course(0, "art", "description", LocalDate.now().plusMonths(2), 1));
        DataSource.getInstance().closeConnection();
    }

    @FXML
    public void dashboardTabChanged(){
        DataSource.getInstance().openConnection();
        Course course = DataSource.getInstance().getCourseByName("math");
        DataSource.getInstance().closeConnection();

        // prepare
        XYChart.Series<String, Double> seriesMath = new XYChart.Series<String, Double>();
        // set displayable name
        seriesMath.setName("Math");
        // add data
        seriesMath.getData().add(new XYChart.Data<String, Double>(String.valueOf(LocalDate.now().minusDays(1).getDayOfMonth()), 5.0));
        seriesMath.getData().add(new XYChart.Data<String, Double>(String.valueOf(LocalDate.now().getDayOfMonth()), 2.0));

        stackedBarChart.getData().add(seriesMath);

        // prepare
        XYChart.Series<String, Double> seriesArt = new XYChart.Series<String, Double>();
        // set displayable name
        seriesArt.setName("Art");
        // add data
        seriesArt.getData().add(new XYChart.Data<String, Double>(String.valueOf(LocalDate.now().minusDays(1).getDayOfMonth()), 1.5));
        seriesArt.getData().add(new XYChart.Data<String, Double>(String.valueOf(LocalDate.now().getDayOfMonth()), 2.5));

        stackedBarChart.getData().add(seriesArt);

    }

}
