package com.studyhelper.controller;

import com.studyhelper.db.entity.Course;
import com.studyhelper.db.entity.Time;
import com.studyhelper.db.entity.Todo;
import com.studyhelper.db.model.CourseServiceImpl;
import com.studyhelper.db.model.TimeServiceImpl;
import com.studyhelper.db.model.TodoServiceImpl;
import com.studyhelper.db.properties.UiProperties;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.util.Callback;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CourseOverviewController {
    @FXML
    private VBox vBoxCourses;
    @FXML
    private VBox vBoxEachCourse;
    @FXML
    private TextField todoTextField;
    @FXML
    private TableView<Todo> todoTableView;
    @FXML
    private TableColumn<Todo, String> itemTableColumn;
    @FXML
    private TableColumn<Todo, Boolean> selectTableColumn;
    @FXML
    private ProgressIndicator todoProgressIndicator;
    @FXML
    private TextArea courseDescription;
    @FXML
    private Button addCourseButton;
    @FXML
    private Button addTodo;
    @FXML
    private Button deleteSelectedTodo;
    @FXML
    private Button deleteCourse;
    @FXML
    private AnchorPane courseOverviewAnchorPane;

    private IntegerProperty selectedCourse = new SimpleIntegerProperty(0);

    private static final Logger logger = Logger.getLogger(CourseOverviewController.class.getName());

    public void initialize(){
        setupEverythingForCourseTabChange();
    }

    private void setupEverythingForCourseTabChange() {
        fillTheListOfCourses();
        todoTableViewSetup();
        setupCourseDescription();
        setupTodoButtons();
        setupSelectedCourseHandler();
        setupDeleteCourseButton();
    }

    private void setupDeleteCourseButton() {
        deleteCourse.disableProperty().bind(
                selectedCourse.lessThan(1)
        );
    }

    private void setupSelectedCourseHandler() {
        selectedCourse.addListener(selectedCourseListener());
    }

    private ChangeListener<Number> selectedCourseListener(){
        return new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                todoTableViewRefresh();
                courseDescriptionRefresh();
            }
        };
    }

    private void setupTodoButtons() {
        addTodo.disableProperty().bind(
                Bindings.isEmpty(todoTextField.textProperty())
                        .or(selectedCourse.lessThan(1))
        );
        todoTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            deleteSelectedTodo.setDisable(newSelection == null);
        });
    }

    private void setupCourseDescription() {
        courseDescription.disableProperty().bind(selectedCourse.lessThan(1));
        courseDescription.focusedProperty().addListener(focusChangeListenerForDescription());
        courseDescriptionRefresh();
    }

    private void courseDescriptionRefresh(){
        Course course = new CourseServiceImpl().getCourseById(selectedCourse.get());
        courseDescription.setText(course == null ? "" : course.getDescription());
    }

    private ChangeListener<Boolean> focusChangeListenerForDescription(){
        return new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
                if(oldValue)
                    new CourseServiceImpl().updateCourseDescription(courseDescription.getText(), selectedCourse.get());
            }
        };
    }

    @FXML
    private void vboxCoursesOnAction(MouseEvent mouseEvent){
        VBox vbox = (VBox) mouseEvent.getSource();
        Course course = new CourseServiceImpl().getCourseByName(vbox.getId());
        if(course == null)
            return;
        selectedCourse.setValue(course.getId());
    }

    @FXML
    private void onActionDeleteSelectedCourse(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Todo item");
        String name = new CourseServiceImpl().getCourseById(selectedCourse.get()).getName();
        if(name == null)
            return;
        alert.setHeaderText("Delete item: " + name);
        alert.setContentText("Are you sure?");
        alert.initOwner(courseOverviewAnchorPane.getScene().getWindow());
        alert.showingProperty().addListener(e -> updateBlurEffect());

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && (result.get() == ButtonType.OK)){
            new CourseServiceImpl().deleteAllDataAboutCourse(selectedCourse.get());
            fillTheListOfCourses();
        }
    }

    private void updateBlurEffect(){
        if(courseOverviewAnchorPane.getEffect() != null)
            courseOverviewAnchorPane.setEffect(null);
        else
            courseOverviewAnchorPane.setEffect(getBlurEffect());
    }

    private Effect getBlurEffect(){
        ColorAdjust adj = new ColorAdjust(0, 0, 0, 0);
        GaussianBlur blur = new GaussianBlur(55);
        adj.setInput(blur);

        return adj;
    }

    private void fillTheListOfCourses(){
        cleanOldDataForCourseList();

        ObservableList<Course> courseList = new CourseServiceImpl().getAllCourses();
        courseList.sort(compareCoursesByDue());

        for (Course course : courseList) {
            VBox vbox = createVBox(course);

            vBoxCourses.getChildren().add(vbox);
        }
    }

    private void cleanOldDataForCourseList(){
        vBoxCourses.getChildren().clear();
        vBoxEachCourse.getChildren().clear();
        selectedCourse.setValue(0);
    }

    private VBox createVBox(Course course) {
        VBox vbox = setCourseListVBox(course.getName());
        TextField textField = setCourseListTextField(course.getName());
        Label labelForHours = setCourseListLabelHours(course.getId());
        Label labelForDue = setCourseListLabelDue(course);

        vbox.getChildren().addAll(textField, labelForHours, labelForDue);
        return vbox;
    }

    private Comparator<Course> compareCoursesByDue(){
        return Comparator.comparing((Course c) -> c.getDue().isAfter(LocalDate.now()))
                .thenComparing(c -> c.getDue().isBefore(LocalDate.now()))
                .thenComparing(c -> c.getDue().isBefore(LocalDate.now().plusDays(10)))
                .reversed()
                ;
    }

    private TextField setCourseListTextField(String courseName){
        TextField textField = new TextField();
        textField.setStyle("-fx-background-color : transparent; -fx-text-fill : #cdcdcd; -fx-alignment : center;");
        textField.setText(courseName);
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldName, String newName) {
                selectedCourse.setValue(new CourseServiceImpl().getCourseByName(oldName).getId());
                new CourseServiceImpl().updateCourseName(newName, selectedCourse.get());
            }
        });

        return textField;
    }

    private Label setCourseListLabelHours(int id){
        Label label3 = new Label();
        label3.setTextFill(Paint.valueOf("#cdcdcd"));
        label3.setWrapText(true);
        Time time = new TimeServiceImpl().getTimeByCourse_id(id);
        if(time == null)
            label3.setText("N/A");
        else
            label3.setText(time.getDuration().toHours() + " hours studied");

        return label3;
    }

    private VBox setCourseListVBox(String courseName){
        VBox vbox = new VBox();
        vbox.setId(courseName);
        vbox.setStyle(vBoxEachCourse.getStyle());
        vbox.setEffect(vBoxEachCourse.getEffect());
        vbox.setOnMouseClicked(vBoxEachCourse.getOnMouseClicked());
        vbox.setPrefSize(vBoxEachCourse.getPrefWidth(), vBoxEachCourse.getPrefHeight());
        vbox.setSpacing(10);
        vbox.setAlignment(vBoxEachCourse.getAlignment());

        return vbox;
    }

    private Label setCourseListLabelDue(Course course) {
        Label label2 = new Label();
        label2.setWrapText(true);
        long daysLeft = Duration.between(LocalDate.now().atStartOfDay(), course.getDue().atStartOfDay()).toDays();
        String daysLeftString;
        if(daysLeft <= 0){
            daysLeftString = "expired";
            label2.setTextFill(Paint.valueOf("#cdcdcd"));
        } else if(daysLeft < 10){
            daysLeftString = daysLeft + " days left";
            label2.setTextFill(Paint.valueOf("#bf7878"));
        }
        else{
            daysLeftString = daysLeft + " days left";
            label2.setTextFill(Paint.valueOf("#8dae72"));
        }
        label2.setText(daysLeftString);

        return label2;
    }

    private void todoTableViewSetup() {
        todoTableViewRefresh();
        itemTableColumnSetup();
        selectTableColumnSetup();
    }

    private void todoTableViewRefresh(){
        todoTableView.getItems().clear();

        final ObservableList<Todo> data = new TodoServiceImpl().getAllTodoByCourseId(selectedCourse.get());
        if(data == null)
            return;
        todoTableView.setItems(data);

        setTodoProgressIndicator(countDoneTodos(data), data.size());
    }

    private void setTodoProgressIndicator(int doneTodos, int size){
        DoubleProperty done = new SimpleDoubleProperty(0.0);
        todoProgressIndicator.progressProperty().bind(done);
        done.setValue((double)doneTodos / (double) size);
    }

    private int countDoneTodos(ObservableList<Todo> data){
        int doneCount = 0;
        for(Todo todo : data)
            if(todo.getCompletedProperty().get())
                doneCount++;

        return doneCount;
    }

    private void itemTableColumnSetup(){
        itemTableColumn.setCellValueFactory(new PropertyValueFactory<>("item"));
        itemTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    private void selectTableColumnSetup(){
        selectTableColumn.setCellValueFactory(new PropertyValueFactory<>("completed"));
        selectTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Todo, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Todo, Boolean> param) {
                if (param.getValue() != null)
                    return param.getValue().getCompletedSBP();
                else
                    return null;
            }
        });
        selectTableColumn.setCellFactory(checkboxCellSetup());
    }

    /*
     * how checkbox cell will be setup
     */
    private Callback<TableColumn<Todo, Boolean>, TableCell<Todo, Boolean>> checkboxCellSetup() {
        return new Callback<TableColumn<Todo, Boolean>, TableCell<Todo, Boolean>>() {
            @Override
            public TableCell<Todo, Boolean> call(TableColumn<Todo, Boolean> todoBooleanTableColumn) {
                CheckBoxTableCell<Todo, Boolean> cell = new CheckBoxTableCell<>();
                cell.setSelectedStateCallback(new Callback<Integer, ObservableValue<Boolean>>() {
                    @Override
                    public ObservableValue<Boolean> call(Integer index) {
                        return onCheckBoxActionSelectTableColumn(index);
                    }
                });

                return cell;
            }
        };
    }

    /*
     * what will happen on checkbox select
     */
    private ObservableValue<Boolean> onCheckBoxActionSelectTableColumn(Integer index){
        BooleanProperty selected = new SimpleBooleanProperty(todoTableView.getItems().get(index).getCompletedProperty().get());
        selected.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                onActionCheckBoxUpdate(selected.get(), index);
                todoTableViewRefresh();
            }
        });
        return selected;
    }

    private void onActionCheckBoxUpdate(boolean isSelected, int rowIndex){
        Todo todoSelected = todoTableView.getItems().get(rowIndex);
        todoSelected.setCompleted(isSelected);
        new TodoServiceImpl().updateTodo(todoSelected);
    }

    @FXML
    private void itemCellOnEditCommit(TableColumn.CellEditEvent<Todo, String> cellEditEvent){
        Todo todoSelected = todoTableView.getSelectionModel().getSelectedItem();
        todoSelected.setItem(cellEditEvent.getNewValue());
        new TodoServiceImpl().updateTodo(todoSelected);
    }

    @FXML
    private void addTodoOnAction(){
        new TodoServiceImpl().insertTodo(new Todo(0, false, todoTextField.getText(), selectedCourse.get()));
        todoTextField.setText("");

        todoTableViewRefresh();
    }

    @FXML
    private void onActionDeleteSelectedTodo(){
        if(todoTableView.getSelectionModel().getSelectedItem() != null)
            new TodoServiceImpl().deleteTodo(todoTableView.getSelectionModel().getSelectedItem());

        todoTableViewRefresh();
    }

    @FXML
    public void addCourseButtonAction() {
        try {
            loadEditPane();
        } catch (IOException e){
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    private void loadEditPane() throws IOException {
        Dialog<Course> addCourseDialog = configureDialog();
        addCourseDialog.showAndWait();
    }

    private Dialog<Course> configureDialog() throws IOException {
        Dialog<Course> addCourseDialog = new Dialog<>();
        addCourseDialog.initOwner(courseOverviewAnchorPane.getScene().getWindow());
        addCourseDialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        Node close = addCourseDialog.getDialogPane().lookupButton(ButtonType.CLOSE);
        close.managedProperty().bind(close.visibleProperty());
        close.setVisible(false);
        addCourseDialog.showingProperty().addListener(dialogShowingProperty());
        addCourseDialog.setTitle("Add new course");
        addCourseDialog.getDialogPane().setContent(getLoader().load());

        return addCourseDialog;
    }

    private ChangeListener<? super Boolean> dialogShowingProperty() {
        return (ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            updateBlurEffect();
            if (oldValue)
                fillTheListOfCourses();
        };
    }

    private FXMLLoader getLoader(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(new UiProperties().getAddCourseFXMLPath());
        return fxmlLoader;
    }
}
