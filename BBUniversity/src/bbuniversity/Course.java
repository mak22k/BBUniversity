/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bbuniversity;
import javafx.beans.property.SimpleStringProperty;

public class Course {

    private final SimpleStringProperty courseNum, courseName, day, time, room, 
            credits;
    
    public Course (){
        this.courseNum = new SimpleStringProperty("");
        this.courseName = new SimpleStringProperty("");
        this.day = new SimpleStringProperty("");
        this.time = new SimpleStringProperty("");   
        this.room = new SimpleStringProperty("");
        this.credits = new SimpleStringProperty("");
        
    }
    public Course(String courseNum, String courseName,
            String day, String time, 
            String room, String credits) {
        this.courseNum = new SimpleStringProperty(courseNum);
        this.courseName = new SimpleStringProperty(courseName);
        this.day = new SimpleStringProperty(day);
        this.time = new SimpleStringProperty(time);   
        this.room = new SimpleStringProperty(room);
        this.credits = new SimpleStringProperty(credits);
    }
    
    

    public String getCourseNum() {
        return courseNum.get();
    }

    public String getCourseName() {
        return courseName.get();
    }

    public String getDay() {
        return day.get();
    }

    public String getTime() {
        return time.get();
    }

    public String getRoom() {
        return room.get();
    }

    public String getCredits() {
        return credits.get();
    }

    
    
    
    public void setCourseNum(String courseNumber) {
        this.courseNum.set(courseNumber);
    }

    public void setCourseName(String courseName) {
        this.courseName.set(courseName);
    }

    public void setDay(String day) {
        this.day.set(day);
    }

    public void setTime(String time) {
        this.time.set(time);
    }

    public void setRoom(String room) {
        this.room.set(room);
    }

    public void setCredits(String credits) {
        this.credits.set(credits);
    }

    
    

}