package com.studyhelper.controller.Courses;

import com.studyhelper.db.entity.Course;
import com.studyhelper.db.entity.Todo;
import com.studyhelper.db.model.Course.CourseServiceImpl;
import com.studyhelper.db.model.Todo.TodoServiceImpl;
import com.studyhelper.db.properties.I18N;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
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
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
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
    @FXML
    private Label selectedCourseNameLabel;
    @FXML
    private DatePicker dueDatePicker;

    private IntegerProperty selectedCourse = new SimpleIntegerProperty(0);
    private Course course;

    private static final Logger logger = Logger.getLogger(CourseOverviewController.class.getName());

    public void initialize(){
        setupEverythingForCourseTabChange();
    }

    private void setupEverythingForCourseTabChange() {
        fillTheListOfCourses();
        todoTableViewSetup();
        setupCourseDescription();
        setupCourseDue();
        setupTodoButtons();
        setupSelectedCourseHandler();
        setupSelectedCourseNameHandler();
        setupDeleteCourseButton();
    }

    private void setupCourseDue() {
        dueDatePicker.disableProperty().bind(selectedCourse.lessThan(1));
        dueDatePicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable,
                                LocalDate oldValue, LocalDate newValue) {
                if(oldValue != null)
                    new CourseServiceImpl().update().due(newValue, selectedCourse.get());

                fillTheListOfCourses();
            }
        });
    }

    private void setupSelectedCourseNameHandler() {
        selectedCourseNameLabel.visibleProperty().bind(
                Bindings.not(selectedCourse.lessThan(1))
        );
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
            public void changed(ObservableValue<? extends Number> observable,
                                Number oldValue, Number newValue) {
                todoTableViewRefresh();
                courseDescriptionRefresh();
                dueDatePickerRefresh();
            }
        };
    }

    private void dueDatePickerRefresh() {
        dueDatePicker.setValue(course.getDue());
    }

    private void setupTodoButtons() {
        todoTextField.disableProperty().bind(selectedCourse.lessThan(1));
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
        Course course = new CourseServiceImpl().get().byId(selectedCourse.get());
        courseDescription.setText(course == null ? "" : course.getDescription());
    }

    private ChangeListener<Boolean> focusChangeListenerForDescription(){
        return new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue,
                                Boolean oldValue, Boolean newValue) {
                if(oldValue)
                    new CourseServiceImpl().update().description(
                            courseDescription.getText(), selectedCourse.get());
            }
        };
    }

    @FXML
    private void vboxCoursesOnAction(MouseEvent mouseEvent){
        VBox vbox = (VBox) mouseEvent.getSource();

        course = new CourseServiceImpl().get().byId(Integer.parseInt(vbox.getId()));
        if(course == null)
            return;
        selectedCourse.setValue(course.getId());
        selectedCourseNameLabel.setText(course.getName());
    }

    @FXML
    private void onActionDeleteSelectedCourse(){
        Alert alert = deleteCourseAlertDialogConfig();
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && (result.get() == ButtonType.OK)){
            new CourseServiceImpl().delete().byId(selectedCourse.get());

            fillTheListOfCourses();
        }
    }

    private Alert deleteCourseAlertDialogConfig() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(I18N.getString("alert.title"));
        String name = course.getName();
        if(name == null)
            return alert;
        alert.setHeaderText(I18N.getString("alert.header") + " " + name);
        alert.setContentText(I18N.getString("alert.message"));
        alert.initOwner(courseOverviewAnchorPane.getScene().getWindow());
        alert.showingProperty().addListener(e -> updateBlurEffect());

        return alert;
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

        VBoxForCourse vBoxForCourse = new VBoxForCourse();
        vBoxForCourse.setvBoxModel(vBoxEachCourse);
        VBoxForCourseFactory vboxFactory = new VBoxForCourseFactory(vBoxForCourse);
        vboxFactory.setListenerForTextField(listenerForTextFieldInVBoxCourse());
        GetVBoxesForCourses vboxes = new GetVBoxesForCourses(vboxFactory);

        vBoxCourses.getChildren().addAll(vboxes.getAll());
    }

    private ChangeListener<String> listenerForTextFieldInVBoxCourse() {
        return (observableValue, oldName, newName) -> {
            selectedCourse.setValue(new CourseServiceImpl().get().byName(oldName).getId());
            new CourseServiceImpl().update().name(newName, selectedCourse.get());
        };
    }

    private void cleanOldDataForCourseList(){
        vBoxCourses.getChildren().clear();
        vBoxEachCourse.getChildren().clear();
    }

    private void todoTableViewSetup() {
        todoTableViewRefresh();
        itemTableColumnSetup();
        selectTableColumnSetup();
    }

    private void todoTableViewRefresh(){
        todoTableView.getItems().clear();

        final ObservableList<Todo> data = new TodoServiceImpl()
                .getList().byCourseId(selectedCourse.get());
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
        BooleanProperty selected = new SimpleBooleanProperty(
                todoTableView.getItems().get(index).getCompletedProperty().get());
        selected.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue,
                                Boolean aBoolean, Boolean t1) {
                onActionCheckBoxUpdate(selected.get(), index);
                todoTableViewRefresh();
            }
        });
        return selected;
    }

    private void onActionCheckBoxUpdate(boolean isSelected, int rowIndex){
        Todo todoSelected = todoTableView.getItems().get(rowIndex);
        todoSelected.setCompleted(isSelected);
        new TodoServiceImpl().update().byId(todoSelected);
    }

    @FXML
    private void itemCellOnEditCommit(TableColumn.CellEditEvent<Todo, String> cellEditEvent){
        Todo todoSelected = todoTableView.getSelectionModel().getSelectedItem();
        todoSelected.setItem(cellEditEvent.getNewValue());
        new TodoServiceImpl().update().byId(todoSelected);
    }

    @FXML
    private void addTodoOnAction(){
        new TodoServiceImpl().insert().add(new Todo(0, false, todoTextField.getText(), selectedCourse.get()));
        todoTextField.setText("");

        todoTableViewRefresh();
    }

    @FXML
    private void onActionDeleteSelectedTodo(){
        if(todoTableView.getSelectionModel().getSelectedItem() != null)
            new TodoServiceImpl().delete().byId(
                    todoTableView.getSelectionModel().getSelectedItem().getId());
    }

    @FXML
    public void addCourseButtonAction() {
        try {
            configureDialog().showAndWait();
        } catch (IOException e){
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    private Dialog<Course> configureDialog() throws IOException {
        GetDialogForAddingCourses dialog = new GetDialogForAddingCourses();
        dialog.configureDialog();
        dialog.setInitOwner(courseOverviewAnchorPane.getScene().getWindow());
        dialog.getInsertDialog().showingProperty().addListener(dialogShowingProperty());

        return dialog.getInsertDialog();
    }

    private ChangeListener<? super Boolean> dialogShowingProperty() {
        return (ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            updateBlurEffect();
            if (oldValue)
                fillTheListOfCourses();
        };
    }
}