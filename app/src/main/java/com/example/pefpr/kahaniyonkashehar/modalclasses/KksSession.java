package com.example.pefpr.kahaniyonkashehar.modalclasses;

/**
 * Created by Ameya on 16-Feb-18.
 */

public class KksSession {
    public String SessionID;
    public String fromDate;
    public String toDate;

    public String getSessionID() {
        return SessionID;
    }

    public void setSessionID(String sessionID) {
        SessionID = sessionID;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
}
