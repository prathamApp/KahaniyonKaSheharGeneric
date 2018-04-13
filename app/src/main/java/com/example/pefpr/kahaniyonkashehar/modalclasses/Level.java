package com.example.pefpr.kahaniyonkashehar.modalclasses;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

/**
 * Created by PEF on 05/06/2017.
 */

public class Level {
    public String StudentID;
    public String BaseLevel;
    public String CurrentLevel;
    public String UpdateDate;

    public void setStudentID(String studentID) {
        StudentID = studentID;
    }

    public void setBaseLevel(String baseLevel) {
        BaseLevel = baseLevel;
    }

    public void setCurrentLevel(String currentLevel) {
        CurrentLevel = currentLevel;
    }

    @Override
    public String toString() {

        return "Level{" +
                "StudentID='" + StudentID + '\'' +
                ", BaseLevel=" + BaseLevel +
                ", CurrentLevel=" + CurrentLevel +
                ", UpdateDate=" + UpdateDate+
                '}';
    }

    public String getUpdateDate() {
        return UpdateDate;
    }

    public void setUpdateDate(String updateDate) {
        UpdateDate = updateDate;
    }

    public String getStudentID() {
        return StudentID;
    }

    public String getBaseLevel() {
        return BaseLevel;
    }

    public String getCurrentLevel() {
        return CurrentLevel;
    }
}
