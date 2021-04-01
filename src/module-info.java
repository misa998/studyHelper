module StudyHelper {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires javafx.web;
    requires java.desktop;

    exports com.studyhelper.controller to javafx.graphics, javafx.fxml;
    opens com.studyhelper.controller to javafx.fxml;
    opens com.studyhelper;
}