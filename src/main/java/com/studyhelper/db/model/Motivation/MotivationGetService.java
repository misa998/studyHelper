package com.studyhelper.db.model.Motivation;

import com.studyhelper.db.entity.Motivation;

public interface MotivationGetService {

    Motivation byId(int id);
    Motivation byTitle(String title);
    Motivation byBody(String body);

}
