/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bbuniversity;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author mak22
 */
public class Receivables {
   // private String ssn, fName, mi, LName; 
    private int numCred, numNC, numTimesRegistered; // number of credit and NC courses
    private double priceAllCred, priceAllNC, fees, totalPrice; // price paid for credit, nc, fees, total
        
    private final SimpleStringProperty ssn, isFT;//, fName, mi, LName;
    
    
    public Receivables (){
        this.ssn = new SimpleStringProperty("");
        this.numCred = 0;
        this.numNC = 0;
        this.numTimesRegistered = 0;        
        this.isFT = new SimpleStringProperty("n");
        
        this.priceAllCred = 0;
        this.priceAllNC = 0;
        this.fees = 0;
        this.totalPrice = 0;    
        
    }
    
    public Receivables(String ssn, int numCred, int numNC, int numTimesRegistered, String isFT) {
        this.ssn = new SimpleStringProperty(ssn); 
        this.numCred = numCred;
        this.numNC = numNC;
        this.numTimesRegistered = numTimesRegistered;
        this.isFT = new SimpleStringProperty(isFT);
        
        this.priceAllCred = calcCreditCourseTuition(numCred, isFT);
        this.priceAllNC = calcNCCourseTuition(numNC);
        this.fees = calcRegistrationFees(numTimesRegistered);
        this.totalPrice = this.priceAllCred + this.priceAllNC + this.fees;
    }
    

    public double calcCreditCourseTuition(int numCreditCourses, String isFT) {
        double creditTuition = 0;
        if (isFT.equals("y") && numCreditCourses >= 3) {
            creditTuition = (3.0 * 285.0);
            creditTuition += (double) (numCreditCourses - 3) * 265.0;
        } 
        else if (isFT.equals("n") || numCreditCourses < 3) {
            creditTuition = (double) (numCreditCourses) * 300.0;
        }
        return creditTuition;
    }

    public double calcNCCourseTuition(int numNC) {
        double ncTuition = (double) (numNC) * 150.0;
        return ncTuition;
    }

    public double calcRegistrationFees(int numTimesRegistered) {
        double calcFees = (double) (numTimesRegistered) * 5.00;
        return calcFees;
    }

    public String getSsn() {
        return ssn.get();
    }

    public String getIsFT() {
        return isFT.get();
    }

    public int getNumCred() {
        return numCred;//.get();
    }

    public int getNumNC() {
        return numNC;//.get();
    }
    
    public int getNumTimesRegistered() {
        return numTimesRegistered;//.get();
    }
    
    public double getPriceAllCred(){
        return calcCreditCourseTuition(this.numCred, isFT.get());
    }
    
    public double getPriceAllNC(){
        return calcNCCourseTuition(this.numNC);
    } 
    
    public double getFees(){
        return calcRegistrationFees(this.numTimesRegistered);
    } 
    
    public double getTotalPrice(){
        return getPriceAllCred() + getPriceAllNC() + getFees();
    } 
        
           
    
    

    public void setSsn(String ssn) {
        this.ssn.set(ssn);
    }

    public void setIsFT(String isFT) {
        this.isFT.set(isFT);
    }

    
    public void setNumCred(int numCred) {
        this.numCred = numCred;//.set(room);
    }

    public void setNumNC(int numNC) {
        this.numNC = numNC;//.set(credits);
    }

    public void setNumTimesRegistered(int numTimes){
        this.numTimesRegistered = numTimes;
    }
    
    // set priceAllCred, priceAllNC, fees, totalPrice all calculated using member
    // functions; use these to "set" these values
       

}


   
   /* public Receivables(String ssn, String fName, String mi, String LName, 
            int numCred, int numNC) {
        this.ssn = new SimpleStringProperty(ssn);
        this.fName = new SimpleStringProperty(fName);
        this.mi = new SimpleStringProperty(mi);
        this.LName = new SimpleStringProperty(LName);   
        this.numCred = numCred;
        this.numNC = numNC;
    }*/