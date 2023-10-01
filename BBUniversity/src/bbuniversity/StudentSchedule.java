/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bbuniversity;
import javafx.beans.property.SimpleStringProperty;
import java.util.ArrayList;

/**
 *
 * @author mak22
 */
public class StudentSchedule {
   // private String ssn, fName, mi, LName; 
    //private int numCred, numNC; // number of credit and NC courses
    //private double priceCred, priceNC, fees, totalPrice; // price paid for credit, nc, fees, total
    
    //private final SimpleStringProperty ssn, fName, mi, LName, matYr, courseNum, 
         //   courseName, day, time, room;
    
    private final Student student; 
    private final ArrayList<Course> course;
    

    public StudentSchedule(Student student, ArrayList course){
        this.student = student;
        this.course = course;
    }
    
    public Student getStudent() {
        return student;
    }

    public ArrayList getCourseArray() {
        return course;
    }
    
    public Course getCourse(int i) {
        return course.get(i);
    }
    public int getCourseArraySize() {
        return course.size();
    }
    
    public boolean isAlreadyRegistered(Course testInput){
        for (Course c: course){
            if(testInput.getCourseNum().equals(c.getCourseNum())){
                return true;
            }          
        }
        return false; // return false if the for loop completes without finding a match
    }
    
    /*
    
    public Schedule(String ssn, String fName, String mi, String LName, String matYr,
            String courseNum, String courseName, String day, String time, String room) {
        this.ssn = new SimpleStringProperty(ssn);
        this.fName = new SimpleStringProperty(fName);
        this.mi = new SimpleStringProperty(mi);
        this.LName = new SimpleStringProperty(LName);   
       
    }
    
    

    public String getSsn() {
        return ssn.get();
    }

    public String getfName() {
        return fName.get();
    }

    public String getMi() {
        return mi.get();
    }

    public String getLName() {
        return LName.get();
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

    
    
    public void setSsn(String ssn) {
        this.ssn.set(ssn);
    }

    public void getfName(String fName) {
        this.fName.set(fName);
    }

    public void getMi(String mi) {
        this.mi.set(mi);
    }

    public void getLName(String LName) {
        this.LName.set(LName);
    }

   
     public void setCourseNum(String courseNumber) {
        this.courseNum.set(courseNumber);
    }

    public void getCourseName(String courseName) {
        this.courseName.set(courseName);
    }

    public void getDay(String day) {
        this.day.set(day);
    }

    public void getTime(String time) {
        this.time.set(time);
    }

    public void getRoom(String room) {
        this.room.set(room);
    }    */

   
    

}
