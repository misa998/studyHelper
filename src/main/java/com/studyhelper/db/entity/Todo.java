package com.studyhelper.db.entity;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;

public class Todo {
    private final BooleanProperty completedSBP = new SimpleBooleanProperty();
    private final SimpleStringProperty item;
    private final SimpleIntegerProperty course_id;
    private final CheckBox completed;
    private int id;

    public Todo(){
        this.id = 0;
        this.completed = new CheckBox();
        this.completed.setSelected(false);
        this.completedSBP.setValue(false);
        this.item = new SimpleStringProperty("");
        this.course_id = new SimpleIntegerProperty(0);
    }

    public Todo(boolean selected, String item, int course_id) {
        this.id = 0;
        this.completed = new CheckBox();
        this.completed.setSelected(selected);
        this.completedSBP.setValue(selected);
        this.item = new SimpleStringProperty(item);
        this.course_id = new SimpleIntegerProperty(course_id);
    }

    public CheckBox getCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completedSBP.setValue(completed);
        this.completed.selectedProperty().setValue(completed);
        this.completed.setSelected(completed);
    }

    public BooleanProperty getCompletedProperty(){
        return this.completed.selectedProperty();
    }

    public void setCompletedSBP(boolean completedSBP){
        this.completed.selectedProperty().setValue(completedSBP);
        this.completedSBP.setValue(completedSBP);
        this.completed.setSelected(completedSBP);
    }

    public ObservableValue<Boolean> getCompletedSBP(){
        return this.completedSBP;
    }

    public String getItem() {
        return item.get();
    }

    public SimpleStringProperty itemProperty() {
        return item;
    }

    public void setItem(String item) {
        this.item.set(item);
    }

    public int getCourse_id() {
        return course_id.get();
    }

    public SimpleIntegerProperty course_idProperty() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id.set(course_id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "completedSBP=" + completedSBP +
                ", item=" + item +
                ", course_id=" + course_id +
                ", completed=" + completed +
                ", id=" + id +
                '}';
    }
}
