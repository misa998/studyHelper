package com.studyhelper.db.model.Time;

import com.studyhelper.db.entity.Time;

public interface TimeGetService {
    Time byId(int id);
    Time byCourseId(int id);
}
