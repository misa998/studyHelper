package com.studyhelper.db.model;

import com.studyhelper.db.entity.Pomodoro;

public interface PomodoroService {
    Pomodoro setup(Pomodoro pmd);
    boolean updateSettings();
}
