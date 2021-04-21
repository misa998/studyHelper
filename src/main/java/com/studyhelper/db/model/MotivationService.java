package com.studyhelper.db.model;

import com.studyhelper.db.entity.Motivation;
import javafx.collections.ObservableList;

public interface MotivationService {
    ObservableList<Motivation> getAllMotivation();
    Motivation getMotivationById(int id);
    Motivation getMotivationByTitle(String title);
    Motivation getMotivationByBody(String body);
    void updateMotivationTitle(String title, int id);
    void updateMotivationBody(String body, int id);
    int insertMotivation(Motivation motivation);
    void deleteMotivationById(int id);

}
