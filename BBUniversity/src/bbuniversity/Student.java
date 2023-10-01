/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bbuniversity;
import java.time.LocalDate;

/**
 *
 * @author mak22
 */
public class Student {
    
    private String ssn, fName, mi, LName, address, city, state, zip, matYr,
            degree, matriculated, HSdiploma, immunization, dateCreated;
    private LocalDate date;

    public Student(){}; //default/empty constructor
    public Student(String ssn, String fName, String mi, String LName, 
            String address, String city, String state, String zip, String matYr, 
            String degree, String matriculated, String HSdiploma, 
            String immunization) { 

        this.ssn = ssn;
        this.fName = fName;
        this.mi = mi;
        this.LName = LName;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.matYr = matYr;
        this.degree = degree;
        this.matriculated = matriculated;
        this.HSdiploma = HSdiploma;
        this.immunization = immunization;
        date = LocalDate.now();
        dateCreated = String.format("%s/%s/%s", date.getMonthValue(), 
                date.getDayOfMonth(), date.getYear());
    }

       
    
    
    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public void setMi(String mi) {
        this.mi = mi;
    }

    public void setLName(String LName) {
        this.LName = LName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public void setMatYr(String matYr) {
        this.matYr = matYr;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public void setMatriculated(String matriculated) {
        this.matriculated = matriculated;
    }

    public void setHSdiploma(String HSdiploma) {
        this.HSdiploma = HSdiploma;
    }

    public void setImmunization(String immunization) {
        this.immunization = immunization;
    }

    public void setDateCreated(String dateC){
        this.dateCreated = dateC;
    }
    
    
    
    
    public String getSsn() {
        return ssn;
    }

    public String getfName() {
        return fName;
    }

    public String getMi() {
        return mi;
    }

    public String getLName() {
        return LName;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }

    public String getMatYr() {
        return matYr;
    }

    public String getDegree() {
        return degree;
    }

    public String getMatriculationStatus() {
        return matriculated;
    }

    public String getHSdiplomaStatus() {
        return HSdiploma;
    }

    public String hasImmunization() {
        return immunization;
    }
    
    public String getDateCreated() {
        return dateCreated;
    }
    
    
    
    
}
