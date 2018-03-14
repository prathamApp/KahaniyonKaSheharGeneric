package com.example.pefpr.kahaniyonkashehar.modalclasses;

/**
 * Created by Ameya on 28-Nov-17.
 */

public class AttendenceView {

    public String attendenceId;
    public String name;
    public int attendencethumbnail;

    public AttendenceView(String name, String attendenceId, int attendencethumbnail) {
        this.name = name;
        this.attendenceId = attendenceId;
        this.attendencethumbnail = attendencethumbnail;
    }


}
