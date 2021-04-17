package com.studyhelper.controller;

import com.studyhelper.db.model.*;
import com.studyhelper.db.source.DataSource;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebErrorEvent;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import com.studyhelper.db.entity.*;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainController {

	// https://edencoding.com/scene-background/
    @FXML
    private StackedBarChart<String, Double> stackedBarChart;
    @FXML
    private WebView webView;
    @FXML
    private Pane editCoursesPane;
    @FXML
    private Label studyTimerLabel;
    @FXML
    private Button startTimerBtn;
    @FXML
    private Button pauseTimerBtn;
    @FXML
    private Button endTimerBtn;
    @FXML
    private TextField studySessionTimeTextField;
    @FXML
    private TextField largePauseTimeTextField;
    @FXML
    private TextField miniPauseTimeTextField;
    @FXML
    private ChoiceBox<String> courseChoiceBox;
    @FXML
    private Button skipPauseBtn;
    @FXML
    private ProgressBar timerProgress;
    @FXML
    private Slider daysSliderForChart;
    @FXML
    private Label unableToLoadWebViewLabel;
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
    private PieChart coursePieChart;
    @FXML
    private Button closeEditPaneBtn;
    @FXML
    private Button addCourseButton;
    @FXML
    private Button addTodo;
    @FXML
    private Button deleteSelectedTodo;
    @FXML
    private AnchorPane coursesAnchorPane;
    @FXML
    private Button deleteCourse;
    @FXML
    private Label studyStateLabel;

    private IntegerProperty selectedCourse = new SimpleIntegerProperty(0);

    private final TrayIconController trayIconController = new TrayIconController();
    private static final Logger logger = Logger.getLogger(DataSource.class.getName());

    public void initialize(){
        trayIconController.showTray();
        setupRadio();
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
        new TodoServiceImpl().insertTodo(new Todo(false, todoTextField.getText(), selectedCourse.get()));
        todoTextField.setText("");

        todoTableViewRefresh();
    }

    @FXML
    private void onActionDeleteSelectedTodo(){
        if(todoTableView.getSelectionModel().getSelectedItem() != null)
            new TodoServiceImpl().deleteTodo(todoTableView.getSelectionModel().getSelectedItem());

        todoTableViewRefresh();
    }

    private void fillTheListOfCourses(){
        List<Course> courseList = new CourseServiceImpl().getAllCourses();
        if(courseList == null) {
            return;
        }

        for (Course course : courseList) {
            VBox vbox = new VBox();
            vbox.setId(course.getName());
            vbox.setStyle(vBoxEachCourse.getStyle());
            vbox.setEffect(vBoxEachCourse.getEffect());
            vbox.setOnMouseClicked(vBoxEachCourse.getOnMouseClicked());
            vbox.setPrefSize(vBoxEachCourse.getPrefWidth(), vBoxEachCourse.getPrefHeight());
            vbox.setSpacing(10);

            vbox.setAlignment(vBoxEachCourse.getAlignment());

            String paint = "#cdcdcd";

            TextField textField = new TextField();
            textField.setStyle("-fx-background-color : transparent; -fx-text-fill : #cdcdcd; -fx-alignment : center;");
            textField.setText(course.getName());
            textField.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String oldName, String newName) {
                    selectedCourse.setValue(new CourseServiceImpl().getCourseByName(oldName).getId());
                    new CourseServiceImpl().updateCourseName(newName, selectedCourse.get());
                }
            });

            vbox.getChildren().add(textField);

            Label label3 = new Label();
            label3.setTextFill(Paint.valueOf(paint));
            label3.setWrapText(true);
            Time time = new TimeServiceImpl().getTimeByCourse_id(course.getId());
            if(time == null)
                label3.setText("N/A");
            else
                label3.setText(time.getDuration().toHours() + " hours studied");
            vbox.getChildren().add(label3);

            Label label2 = new Label();
            label2.setTextFill(Paint.valueOf(paint));
            label2.setWrapText(true);
            long daysLeft = Duration.between(LocalDate.now().atStartOfDay(), course.getDue().atStartOfDay()).toDays();
            String daysLeftString;
            if(daysLeft <= 0){
                daysLeftString = "expired";
                label2.setTextFill(Paint.valueOf(paint));
            } else if(daysLeft < 10){
                daysLeftString = daysLeft + " days left";
                label2.setTextFill(Paint.valueOf("#bf7878"));
            }
            else{
                daysLeftString = daysLeft + " days left";
                label2.setTextFill(Paint.valueOf("#8dae72"));
            }
            label2.setText(daysLeftString);
            vbox.getChildren().add(label2);

            vBoxCourses.getChildren().add(vbox);
        }
    }

    @FXML
    public void courseTabChange(){
        vBoxCourses.getChildren().clear();
        vBoxEachCourse.getChildren().clear();

        setupEverythingForCourseTabChange();
        fillTheListOfCourses();
    }

    private void setupEverythingForCourseTabChange() {
        todoTableViewSetup();
        setupCourseDescription();
        pieChartSetup();
        editPaneSetup();
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

    private void pieChartSetup() {
        coursePieChart.getData().clear();

        final ObservableList<Course> data = new CourseServiceImpl().getAllCourses();
        if(data == null)
            return;

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for(Course course : data){
            Time time = new TimeServiceImpl().getTimeByCourse_id(course.getId());
            long period;
            try{
                period = time.getDuration().toHours();
            } catch (NullPointerException e){
                period = 0;
            }
            pieChartData.add(new PieChart.Data(course.getName(), period));
        }

        /*
        * this fixes the bug in pie chart, where it does not update labels properly
         */
        coursesAnchorPane.layout();

        coursePieChart.getData().addAll(pieChartData);
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
    public void addCourseButtonAction() {
        try {
            loadEditPane();
        } catch (IOException e){
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    @FXML
    private void onActionCloseEditPane(){
        editCoursesPane.getChildren().clear();
    }

    private void loadEditPane() throws IOException {
        GridPane gridPane = FXMLLoader.load(getClass().getResource("/ui/editPaneView.fxml"));
        gridPane.setMaxWidth(editCoursesPane.getMaxWidth());
        gridPane.setMaxHeight(editCoursesPane.getMaxHeight());

        editCoursesPane.getChildren().setAll(gridPane);
    }

    private void editPaneSetup(){
        editCoursesPane.getChildren().addListener(new ListChangeListener<Node>() {
            @Override
            public void onChanged(Change<? extends Node> change) {
                closeEditPaneBtn.setVisible(editCoursesPane.getChildren().size() >= 1);
            }
        });
        addCourseButton.disableProperty().bind(
                Bindings.selectBoolean(closeEditPaneBtn.visibleProperty())
        );
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
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && (result.get() == ButtonType.OK)){
            new CourseServiceImpl().deleteAllDataAboutCourse(selectedCourse.get());
            selectedCourse.setValue(0);
            courseTabChange();
        }
    }

    private int daysToShowInChart = 5;

    @FXML
    private void onScrollFinishedForChart(){
        daysToShowInChart = (int) Math.round(daysSliderForChart.getValue());
        dashboardTabChanged();
    }

    @FXML
    private AnchorPane dashboardTab;

    @FXML
    public void dashboardTabChanged(){
        // clear the chart, because of tab refresh
        stackedBarChart.getData().clear();

        dashboardTab.layout();

        // get courses
        List<Course> coursesList = new CourseServiceImpl().getAllCourses();
        if(coursesList == null)
            return;
        Course[] courses = new Course[coursesList.size()];
        coursesList.toArray(courses);
        coursesList = null;

        // get timePerDay for certain day and course_id
        //int daysToShow = 5;
        // for each course
        DataSource.getInstance().openConnection();
        for(int i=0; i<courses.length; i++){
            XYChart.Series<String, Double> series = new XYChart.Series<String, Double>();
            series.setName(courses[i].getName());

            // for each day A course

            for(int j = 0; j< daysToShowInChart; j++) {
                double minutes = 0.0;
                LocalDate localDate = LocalDate.now().minusDays(daysToShowInChart -j);
                List<TimePerDay> timePerDayForDayAndCourseId = new TimePerDayServiceImpl().getTimeByDateAndCourse_id(courses[i].getId(), localDate);

                // for each hour A day
                if(timePerDayForDayAndCourseId.size() > 0) {
                    for (TimePerDay tpd : timePerDayForDayAndCourseId) {
                        minutes += tpd.getDuration().toMinutes();
                    }
                }
                series.getData().add(new XYChart.Data<>(String.valueOf(LocalDate.now().minusDays(daysToShowInChart -j).getDayOfMonth()), minutes));
            }
            stackedBarChart.getData().add(series);
        }

        DataSource.getInstance().closeConnection();


        /*

        DataSource.getInstance().openConnection();
        // get courses
        List<Course> coursesList = DataSource.getInstance().getAllCourses();
        Course[] courses = new Course[coursesList.size()];
        coursesList.toArray(courses);
        // get hours
        List<TimePerDay> timePerDay = DataSource.getInstance().getAllTimePerDay();
        TimePerDay[] timePerDays = new TimePerDay[timePerDay.size()];
        timePerDay.toArray(timePerDays);
        timePerDay = null;
        coursesList = null;
        DataSource.getInstance().closeConnection();

        int daysToShow = 5;
        // iteracija kroz curseve
        for(int i=0; i<courses.length; i++){
            XYChart.Series<String, Double> series = new XYChart.Series<String, Double>();
            series.setName(courses[i].getName());

            // iteracija kroz tabelu, trazeci konkretan dan
            int daysToSearch = daysToShow;
            int daysToAdd = daysToShow;
            double hours = 0.0;
            for(int k=0; k<timePerDays.length; k++){

                for(int j=0; j<timePerDays.length; j++){
                    LocalDate day = LocalDate.now().minusDays(daysToSearch);
                    if(timePerDays[j].getDate().equals(day)) {
                        if (timePerDays[j].getCourse_id() == courses[i].getId()) {
                            System.out.println("###" + timePerDays[j] + "###" + courses[i]);
                            hours += timePerDays[j].getHours().getHour();
                        } else{
                            System.out.println(timePerDays[j] + " / " + courses[i]);
                        }
                    }
                }
                series.getData().add(new XYChart.Data<>(String.valueOf(LocalDate.now().minusDays(daysToAdd--).getDayOfMonth()), hours));
                daysToSearch--;
            }

            stackedBarChart.getData().add(series);
            //daysToShow--;
        }
        */



        /*
            1) broj predmeta
            2) for petlja
            3) new XYChart.Series
            4) iteracija i <= brojPredmeta
            5) .setName(course.name)
            - iteracija za dane unazad?
            6) izvuci podatke za prethodna pet dana
            7) uzeti sate
            8) .add(series)
         */

        /*
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
        */
    }

    private String RADIO_URL = "http://tunein.com/popout/player/s288329";

    @FXML
    private void setupRadio(){
        unableToLoadWebViewLabel.setVisible(false);
        WebEngine webEngine = webView.getEngine();

        webEngine.setOnError(new EventHandler<WebErrorEvent>() {
            @Override
            public void handle(WebErrorEvent webErrorEvent) {
                logger.log(Level.WARNING, webErrorEvent.getMessage() + " \n " + "Another instance of an app may be running in the background.");
            }
        });
        webEngine.getLoadWorker().stateProperty()
                .addListener(new ChangeListener<Worker.State>() {
                    @Override
                    public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {
                        if (newState == Worker.State.FAILED) {
                            unableToLoadWebViewLabel.setVisible(true);
                        }
                    }
                });
        //webEngine.getLoadWorker().exceptionProperty().addListener((obs, oldExc, newExc) -> { if (newExc != null) { newExc.printStackTrace();}});
        webEngine.load(RADIO_URL);
    }

    @FXML
    public void pomodoroTabChange() {
        choiceBoxFillWithCourses();
        // creating new instances on every tab change results in multiple animations that can not be stopped
        if(timelineForStudyTime == null)
            setupTimelineForStudying();

        //studyStateLabelSetup();
        setupPomodoroTimerButtonBindings();
    }

    private ObjectProperty<PomodoroServiceImpl.StudyState> studyStateObjectProperty = new SimpleObjectProperty<>();
    private void studyStateLabelSetup() {
        studyStateObjectProperty.set(PomodoroServiceImpl.getInstance().getStudyState());
        studyStateLabel.textProperty().bind(studyStateObjectProperty.asString());
    }

    private void progressBarSetup() {
        DoubleProperty studySessions = new SimpleDoubleProperty();
        timerProgress.progressProperty().bind(studySessions);
        studySessions.set((PomodoroServiceImpl.getInstance().getStudySessionCounter())/4.0);
    }

    private void setupPomodoroTimerButtonBindings() {
        startTimerBtn.disableProperty().bind(
                Bindings.isEmpty(studySessionTimeTextField.textProperty())
                .or(Bindings.isEmpty(miniPauseTimeTextField.textProperty()))
                .or(Bindings.isEmpty(largePauseTimeTextField.textProperty()))
                .or(Bindings.isNull(courseChoiceBox.valueProperty()))
                .or(Bindings.not(pauseTimerBtn.disabledProperty()))
                .or(Bindings.selectBoolean(skipPauseBtn.visibleProperty()))
        );
        endTimerBtn.disableProperty().bind(
                Bindings.selectBoolean(pauseTimerBtn.disabledProperty())
        );
    }

    /*
    * steps to run pomodoro
    * 1. setup timeline (increment(timer+1), isTimeOver?)
    * 2. start (labelReset(timerReset, label=currentTime, color), checkFields, disable buttons,
    * notify, sessionCounter++, timeline.play, state.Study, setup
    * 3. setup (getDataFromFields, new Pomodoro instance)
    * 4. pause (timeline.pause, buttons disable)
    * 5. end (isSessionOver ? (largePause : miniPause), addTimePerDay, disable buttons, timeline.stop,
    * labelClear, startPausetimer
     */

    private Timeline timelineForStudyTime = null;

    private void setupTimelineForStudying(){
        timelineForStudyTime = new Timeline(new KeyFrame(javafx.util.Duration.millis(1000), actionEvent -> {
            incrementTimeForStudyTimeline();
            trayIconNotificationForTimePassed();
        }));
        timelineForStudyTime.setCycleCount(Animation.INDEFINITE);
    }

    private void incrementTimeForStudyTimeline() {
        PomodoroServiceImpl.getInstance().incrementTime();
        studyTimerLabel.setText(PomodoroServiceImpl.getInstance().getCurrentStudyTimeString());
    }

    private void setupPomodoro(){
        LocalTime studySessionTime = LocalTime.of(0, Integer.parseInt(studySessionTimeTextField.getText()), 1);
        LocalTime miniPauseTime = LocalTime.of(0, Integer.parseInt(miniPauseTimeTextField.getText()), 1);
        LocalTime largePauseTime = LocalTime.of(0, Integer.parseInt(largePauseTimeTextField.getText()), 1);

        PomodoroServiceImpl.getInstance().setup(new Pomodoro(studySessionTime, miniPauseTime, largePauseTime, new CourseServiceImpl().getCourseByName(courseChoiceBox.getValue()).getId()));
    }

    public void trayIconNotificationForTimePassed() {
        switch (PomodoroServiceImpl.studyState) {
            case STUDY:
                if (PomodoroServiceImpl.getInstance().isStudyTimerOver())
                        trayIconController.displayMessage(PomodoroServiceImpl.getInstance().getCurrentStudyTime().getMinute() + " minutes passed in study session. " + PomodoroServiceImpl.getInstance().getStudySessionCounter() + " session over.");
                break;
            case MINIPAUSE:
                if (PomodoroServiceImpl.getInstance().isMiniPauseTimerOver())
                        trayIconController.displayMessage(PomodoroServiceImpl.getInstance().getCurrentStudyTime().getMinute() + " minutes passed in mini pause.");
                break;
            case LARGEPAUSE:
                if (PomodoroServiceImpl.getInstance().isLargePauseTimerOver())
                        trayIconController.displayMessage(PomodoroServiceImpl.getInstance().getCurrentStudyTime().getMinute() + " minutes passed in large pause.");
                break;
        }
    }

    private void choiceBoxFillWithCourses() {
        String choice = null;
        if(courseChoiceBox.getItems().size() > 0) {
            choice = courseChoiceBox.getValue();
            courseChoiceBox.getItems().clear();
        }
        else
            courseChoiceBox.getItems().clear();

        List<Course> courseList = new CourseServiceImpl().getAllCourses();
        if(courseList == null)
            return;
        for(Course course : courseList)
            courseChoiceBox.getItems().add(course.getName());

        if(courseChoiceBox.getItems().contains(choice))
            courseChoiceBox.setValue(choice);
    }

    private void timerLabelCleanUp(){
        PomodoroServiceImpl.getInstance().timerReset();
        studyTimerLabel.setText(PomodoroServiceImpl.getInstance().getCurrentStudyTimeString());
        studyTimerLabel.setTextFill(Paint.valueOf("#404040"));
    }

    @FXML
    private void startTimer(ActionEvent event) {
        //studyStateLabelSetup();

        pauseTimerBtn.setDisable(false);

        trayIconController.displayMessage("studying session started");

        if(timelineForStudyTime.getStatus().equals(Animation.Status.STOPPED)) {
            timerLabelCleanUp();
            studyTimerLabel.setTextFill(Paint.valueOf("#0d6300"));
            setupPomodoro();
            PomodoroServiceImpl.getInstance().startTimer();
        }

        timelineForStudyTime.play();
        progressBarSetup();
    }

    @FXML
    private void onPauseTimer(ActionEvent event) {
        timelineForStudyTime.pause();
        pauseTimerBtn.setDisable(true);
    }

    @FXML
    private void onEndTimer(ActionEvent event) {
        if(PomodoroServiceImpl.getInstance().isStudySessionOver()){
            PomodoroServiceImpl.getInstance().studySessionCounterReset();

            trayIconController.displayMessage("Study sessions over. Big pause!");

            PomodoroServiceImpl.getInstance().setStudyState(PomodoroServiceImpl.StudyState.LARGEPAUSE);
        } else {
            PomodoroServiceImpl.getInstance().setStudyState(PomodoroServiceImpl.StudyState.MINIPAUSE);
        }

        PomodoroServiceImpl.getInstance().endStudySession();

        pauseTimerBtn.setDisable(true);

        timelineForStudyTime.stop();
        timerLabelCleanUp();

        startPauseTimer();
    }

    private void startPauseTimer(){
        skipPauseBtn.setVisible(true);
        studyTimerLabel.setTextFill(Paint.valueOf("#993000"));
        timelineForStudyTime.play();
    }

    @FXML
    private void onSkipPauseButtonAction(){
        timerLabelCleanUp();
        timelineForStudyTime.stop();
        PomodoroServiceImpl.getInstance().setStudyState(PomodoroServiceImpl.StudyState.STOPPED);
        skipPauseBtn.setVisible(false);
    }
}