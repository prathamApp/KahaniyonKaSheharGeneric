package com.example.pefpr.kahaniyonkashehar.database;

/**
 * Created by Pravin 17/1/2018.
 */

public final class DatabaseInitialization {

//    public static final String CreateAserTable = "CREATE TABLE Aser(StudentId TEXT NOT NULL, ChildID text, TestType int, TestDate text, Lang int, Num int, OAdd boolean, OSub boolean, OMul boolean, ODiv boolean, WAdd boolean, WSub boolean, CreatedBy text, CreatedDate text, DeviceId text, FLAG boolean, GroupID TEXT );";
    public static final String CreateCRLTable = "CREATE TABLE CRL(CRLID TEXT PRIMARY KEY, FirstName TEXT NOT NULL, LastName TEXT NOT NULL, UserName TEXT NOT NULL, PassWord TEXT NOT NULL, ProgramId INTEGER NOT NULL, Mobile TEXT NOT NULL, State TEXT NOT NULL, Email TEXT NOT NULL , CreatedBy text, NewFlag boolean);";
//    public static final String CreateGroupTable = "CREATE TABLE Groups (GroupID text PRIMARY KEY,GroupCode text, GroupName text, UnitNumber text, DeviceID text, Responsible text, ResponsibleMobile text, VillageID integer, ProgramId integer, CreatedBy text, NewFlag boolean, VillageName text, SchoolName text);";
//    public static final String CreateVillageTable = "CREATE TABLE Village (VillageID integer PRIMARY KEY, VillageCode text, VillageName text, Block text, District text, State text, CRLID TEXT);";
    public static final String CreateStudentTable = "CREATE TABLE Student (StudentID text PRIMARY KEY, StudentUID text, FirstName text, MiddleName text, LastName text, Gender text, Age int, regDate text, villageName text, NewFlag int, DeviceId text);";
    public static final String CreateLevelTable = "CREATE TABLE Level ( StudentID text PRIMARY KEY, BaseLevel text , CurrentLevel text, UpdatedDate text);";
    public static final String CreateScoreTable = "CREATE TABLE Scores (SessionID TEXT NOT NULL, StudentID TEXT, DeviceID TEXT NOT NULL, ResourceID text NOT NULL, QuestionID INTEGER NOT NULL, ScoredMarks integer NOT NULL, TotalMarks integer NOT NULL, StartDateTime TEXT NOT NULL, EndDateTime TEXT, Level INTEGER DEFAULT 1);";
    public static final String CreateSessionTable = "CREATE TABLE Session(SessionID TEXT PRIMARY KEY NOT NULL, fromDate text, toDate text);";
    public static final String CreateAttendanceTable = "CREATE TABLE Attendance(SessionID TEXT NOT NULL, StudentID text)";
    public static final String CreateStatusTable = "CREATE TABLE Status(key TEXT, value TEXT NOT NULL, description text default '')";

    // to prevent instantiation of a static class
    private DatabaseInitialization() {
    }

}
