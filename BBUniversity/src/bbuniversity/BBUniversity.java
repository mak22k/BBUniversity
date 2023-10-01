/* Boola Boola University Student Portal - Project 1
 *  CIT-285 Fall 2019
 *
 *  Author: Marisha Kulseng
 *  Date last modified: 10/21/2019
 *
 *  This project consists of a GUI where students can apply to attend the school,
 *  register for a variety of classes, and receive reports about their course
 *  selections and tuition bill. Matriculated students must have a HS diploma and
 *  immunization records in order to register for classes, while non-matriculated
 *  students only need to present immunization records if they register for nine
 *  or more credits. Error messages appear if these conditions are violated.
 *  
 *  University image sourced from DrStockPhoto
 *  https://www.drstockphoto.com/photos/568-old-college-building
 */
package bbuniversity;

import java.net.*;
import java.io.*;
import java.time.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.shape.Line;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javafx.scene.paint.Color;
import javafx.geometry.*;
import java.util.ArrayList;
import javafx.event.EventType;
import javafx.scene.input.KeyCombination;


//import javafx.beans.property.SimpleStringProperty;



public class BBUniversity extends Application {
    
    // set custom fonts/sizes
    Font uniNameFont = new Font("Tahoma", 48);
    Font titleFont = new Font("Tahoma", 28);
    Font timeFont = new Font("Tahoma", 22);
    Font basicFont = new Font("Tahoma", 18);
    
    // this section declares our most commonly used sql queries as strings


    // this section declares variables that are used throughout the class
    static final String DRIVER = "com.mysql.jdbc.Driver";
    static final String DATABASE_URL = "jdbc:mysql://localhost/bbu";
    
    Student studentEntry;
    String matYrString = "", degreeString = "", hasHSdiploma, hasImmunization,
            matriculated;
    boolean needsImmunization, needsHSdiploma, isFT, isPT, isNC;
    int numCredCourses = 0, numNCcourses = 0;
    static String name=""; // query DB to get user's first name, import SSN
    static String ssnLoggedIn, loginEntry;
 
    
    Connection connection = null;
    Statement statement = null;
    ResultSet resultSet = null;
 
    public double tuition = 0; 
    public Label ssn, fName, mi, LName, stAddress, city, state, zip, 
            dateToday, matricYr, degree, docsPresented;
    
    public Label errorLabel = new Label();
    public Label tuitionLabel = new Label(); 
    public Label registrationCompleteMessage = new Label();
    public Label reportsNumCredCourse = new Label();
    public Label reportsNumNCcourse = new Label();
    public Label reportsRegFees = new Label();
    public Label reportsCredDollars = new Label();
    public Label reportsNCDollars = new Label();
    public Label reportsGrandTotal = new Label();
    public Label droppedMessage = new Label();
    public Label reportsHeaderLabel = new Label();
    public Label registrationHeaderLabel = new Label();
    public Label admissionsHeaderLabel = new Label();
    
    public Label uniName = new Label("Boola Boola University\n\n\n");
        
    @Override
    public void start(Stage primaryStage) throws IOException {
       
       registrationHeaderLabel.setFont(uniNameFont);
       admissionsHeaderLabel.setFont(uniNameFont);
        
       VBox menuAdmissions = new VBox(15);
       VBox menuRegistration = new VBox(15);
       VBox menuReports = new VBox(15);       
       BorderPane primaryPane = new BorderPane();
       GridPane demographicsPane = new GridPane();
       GridPane reportsHeaderPane = new GridPane();
       GridPane receivablesPane = new GridPane();
       GridPane reportsPane = new GridPane();
          reportsPane.setVgap(100);
       //StackPane registrationPane = new StackPane();
       VBox registrationPane = new VBox();
          registrationPane.setTranslateY(100);
       StackPane quitDialogue = new StackPane();       
       StackPane schedulePane = new StackPane();
       StackPane errorPane = new StackPane();
         errorPane.getChildren().add(errorLabel);
         //primaryPane.setBottom(errorPane);
         errorLabel.setFont(titleFont);
         errorLabel.setTranslateY(40); // -130
         errorLabel.setTextFill(Color.RED);
       StackPane tuitionBillPane = new StackPane();
         tuitionBillPane.getChildren().addAll(tuitionLabel,registrationCompleteMessage);
         tuitionBillPane.setTranslateX(-30);
         tuitionBillPane.setTranslateY(-100);
         tuitionLabel.setTranslateY(-100);
         registrationCompleteMessage.setTranslateX(200);
         registrationCompleteMessage.setTranslateY(0);
         
       MenuBar mb = new MenuBar();
       Menu meAdmissions = new Menu("Admissions");
         MenuItem miMatriculated = new MenuItem("Matriculated");
           miMatriculated.setAccelerator(KeyCombination.keyCombination("Ctrl+M"));
         MenuItem miNonMatriculated = new MenuItem("Non-Matriculated");
           miNonMatriculated.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
         MenuItem miQuit = new MenuItem("Quit");
           miQuit.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
         meAdmissions.getItems().addAll(miMatriculated, miNonMatriculated,
                 new SeparatorMenuItem(), miQuit);
         
       Menu meRegistration = new Menu("Registration");
         MenuItem miFullTime = new MenuItem("Full-time");
           miFullTime.setAccelerator(KeyCombination.keyCombination("Ctrl+F"));
         MenuItem miPartTime = new MenuItem("Part-time");
           miPartTime.setAccelerator(KeyCombination.keyCombination("Ctrl+P"));
         MenuItem miNC = new MenuItem("Non-credit");
           miNC.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
         meRegistration.getItems().addAll(miFullTime, miPartTime, miNC);
         
       Menu meReports = new Menu("Reports");
         MenuItem miReceivables = new MenuItem("Receivables");
           miReceivables.setAccelerator(KeyCombination.keyCombination("Ctrl+R"));
         MenuItem miClassSchedule = new MenuItem("Class Schedule");
           miClassSchedule.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
         meReports.getItems().addAll(miReceivables, miClassSchedule);
       
         mb.getMenus().addAll(meAdmissions, meRegistration, meReports);
         primaryPane.setTop(mb);

       
        // Demographics section //
        ssn = new Label(" SSN: ");
        fName = new Label(" First Name: ");
        mi = new Label(" M.I.: ");
        LName = new Label(" Last Name: ");
        stAddress = new Label(" Street Address: ");
        city = new Label(" City: ");
        state = new Label(" State: ");
        zip = new Label(" Zip: ");
        matricYr = new Label(" Matriculation Year: ");
        degree = new Label(" Degree Program: ");
        docsPresented = new Label(" Documents Presented: ");
     
        // Demographics text fields
        TextField ssnInputField = new TextField();
        TextField fNameField = new TextField();
        TextField miField = new TextField();
        miField.setMaxWidth(50);
        TextField LNameField = new TextField();
        TextField stAddressField = new TextField();
        TextField cityField = new TextField();
        TextField stateField = new TextField();
        stateField.setMaxWidth(50);
        TextField zipField = new TextField();
        //TextField matricYrField = new TextField();
        
        // Demographics radio buttons   
        RadioButton matYrFR = new RadioButton();
        matYrFR.setText("Freshman");
        RadioButton matYrSO = new RadioButton();
        matYrSO.setText("Sophomore");
        RadioButton matYrJR = new RadioButton();
        matYrJR.setText("Junior");
        RadioButton matYrSR = new RadioButton();
        matYrSR.setText("Senior");
        ToggleGroup matYrGroup = new ToggleGroup();
        matYrFR.setToggleGroup(matYrGroup);
        matYrSO.setToggleGroup(matYrGroup);
        matYrJR.setToggleGroup(matYrGroup);
        matYrSR.setToggleGroup(matYrGroup);
               
        RadioButton asDegree = new RadioButton();
        asDegree.setText("Associate of Science in Computer Programming");
        RadioButton aaDegree = new RadioButton();
        aaDegree.setText("Associate of Arts in Humanities");
        ToggleGroup degreeGroup = new ToggleGroup();
        asDegree.setToggleGroup(degreeGroup);
        aaDegree.setToggleGroup(degreeGroup);
        
        // Demographics check boxes
        CheckBox HSdiploma = new CheckBox("HS Diploma");
        CheckBox immunization = new CheckBox("Immunization Records");

        
        // reports label
        Label matClassReportsLabel = new Label();
        
        // Add all demographics elements to demographics grid pane
        // GridPane demographicsPane = new GridPane();
        demographicsPane.setTranslateY(30);
        demographicsPane.setTranslateX(40);
        demographicsPane.add(ssn, 0, 2);
        demographicsPane.add(ssnInputField, 1, 2);
        demographicsPane.add(fName, 0, 3);
        demographicsPane.add(fNameField, 1, 3);
        demographicsPane.add(mi, 2, 3);
        demographicsPane.add(miField, 3, 3);
        demographicsPane.add(LName, 4,3);
        demographicsPane.add(LNameField, 5, 3);
        demographicsPane.add(stAddress, 0, 4);
        demographicsPane.add(stAddressField, 1, 4);
        demographicsPane.add(city, 0, 5);
        demographicsPane.add(cityField, 1, 5);
        demographicsPane.add(state, 2, 5);
        demographicsPane.add(stateField, 3, 5);
        demographicsPane.add(zip, 4, 5);
        demographicsPane.add(zipField, 5, 5);
        demographicsPane.add(matricYr, 0, 6);
        demographicsPane.add(matYrFR, 1, 6);
        demographicsPane.add(matYrSO, 1, 7);
        demographicsPane.add(matYrJR, 3, 6);
        demographicsPane.add(matYrSR, 3, 7);
        matYrJR.setTranslateX(-300);
        matYrSR.setTranslateX(-300);
        // dividing line between mat yr and other questions     
        Line line2 = new Line(-100, 10, 250, 10);
        demographicsPane.add(line2,1,9);
       // Line line3 = new Line(-50, 10, 0, 10);
        //demographicsPane.add(line3,2,9);
       // Line line4 = new Line(0, 10, 65, 10);
        //demographicsPane.add(line4,3,9);
        // radio buttons
        demographicsPane.add(degree, 0, 10);
        demographicsPane.add(asDegree, 1, 10);
        demographicsPane.add(aaDegree, 1, 11);
        // check boxes    
        demographicsPane.add(docsPresented, 0, 12);
        demographicsPane.add(HSdiploma, 1, 12); //////
        demographicsPane.add(immunization, 1, 13);
        
        // buttons to submit
        Button btSubmitDemographics = new Button("Submit");
        Button btClearDemographics = new Button("Clear form");
        demographicsPane.add(btClearDemographics, 1, 14);
        demographicsPane.add(btSubmitDemographics, 3, 14);
        
        btSubmitDemographics.setTranslateY(40);
        btClearDemographics.setTranslateY(40);
        
        btClearDemographics.setOnAction(e-> {
                errorLabel.setText("");
                ssnInputField.clear();        fNameField.clear();
                miField.clear();              LNameField.clear();
                stAddressField.clear();       cityField.clear();
                stateField.clear();           zipField.clear();
            
                matYrFR.setSelected(false);   matYrJR.setSelected(false);
                matYrSO.setSelected(false);   matYrSR.setSelected(false);                              
                asDegree.setSelected(false);  aaDegree.setSelected(false);
                               
                HSdiploma.setSelected(false); immunization.setSelected(false);                         
        });
        
        
        btSubmitDemographics.setOnAction(e-> {
            if(matYrFR.isSelected()) matYrString = "Freshman";
            else if(matYrSO.isSelected()) matYrString = "Sophomore";
            else if(matYrJR.isSelected()) matYrString = "Junior";
            else if(matYrJR.isSelected()) matYrString = "Senior";
            else matYrString = "NM";
            if(asDegree.isSelected()) 
                degreeString = "Associate of Science in Computer Programming";
            else if(aaDegree.isSelected()) 
                degreeString = "Associate of Arts in Humanities";
            else degreeString = "NA";
            
            if(HSdiploma.isSelected()) hasHSdiploma = "y";
            else hasHSdiploma ="n";
            
            if(immunization.isSelected()) hasImmunization = "y";
            else hasImmunization = "n";
            
            if((needsHSdiploma && hasHSdiploma.equals("n")) || 
                    needsImmunization && hasImmunization.equals("n")){
           

               String errorMsg = "WARNING: Class registration not allowed! \nYou need " + 
                    ((needsHSdiploma && hasHSdiploma.equals("n")) ? "a HS diploma" : "")
                    +(((needsHSdiploma && hasHSdiploma.equals("n")) && 
                    (needsImmunization && hasImmunization.equals("n"))) ? " and " : " ")
                    + ((needsImmunization && hasImmunization.equals("n")) ? "immunization records" : "") 
                    + " in order to register for classes.";
            
                 errorLabel.setText(errorMsg);
            
                 errorPane.setVisible(true);
                 uniName.setVisible(false);
            
               
            }
            else {
                errorPane.setVisible(false);
                uniName.setVisible(true);
            }
                                        
        
            studentEntry = new Student(processRawSSN(ssnInputField.getText()), 
                    fNameField.getText(), miField.getText(), LNameField.getText(), 
                    stAddressField.getText(), cityField.getText(), 
                    stateField.getText(), zipField.getText(), matYrString, 
                    degreeString, matriculated, hasHSdiploma, hasImmunization);
            
            storeStudentData(studentEntry);
        
            
            
            // once we save input, clear input
            ssnInputField.clear();        fNameField.clear();
            miField.clear();              LNameField.clear();
            stAddressField.clear();       cityField.clear();
            stateField.clear();           zipField.clear();
            matYrFR.setSelected(false);   matYrJR.setSelected(false);
            matYrSO.setSelected(false);   matYrSR.setSelected(false);                              
            asDegree.setSelected(false);  aaDegree.setSelected(false);                  
            HSdiploma.setSelected(false); immunization.setSelected(false);
                           
        });
        //demographicsPane.setHgap(10);
        
        

       
       /* StackPane pane = new StackPane();
        pane.getChildren().add(btRefresh);*/
       
       /// Class registration section  ///
       Course cCMP100 =new Course("CMP100", "Introduction to Computers", "M", "6:30-8:45",
               "D211", "3");
       Course cOIM220 = new Course("OIM220", "Keyboarding I", "Tu", "5:00-6:15",
               "B101", "3");
       Course cENG111 = new Course("ENG111", "English I", "W", "8:30-11:45",
               "C202", "3");
       Course cCMP545 = new Course("CMP545", "Web Programming", "F", "6:30-9:15",
               "D117", "3");
       Course cCMP237 = new Course("CMP237", "C++ Programming", "Th", "6:30-9:15",
               "D121", "3");
       Course cNC100 = new Course("NC100", "Basic Cookie Baking", "M", "6:00-8:00",
               "E415", "NC");
       Course cNC200 = new Course("NC200", "Advanced Tire Inflation", "W", "6:00-8:00",
               "D100", "NC");
       Course cNC300 = new Course("NC300", "Intro to Sinusitis", "W", "6:00-8:00",
               "B345", "NC");
       Course cNC400 = new Course("NC400", "Shoe Polish and You", "Tu", "6:15-8:15",
               "C202", "NC");
       Course cNC500 = new Course("NC500", "Gout for Fun and Profit", "F", "7:00-9:00",
               "A300", "NC");
       
       TableView<Course> registrationTable = new TableView<>();
       ObservableList<Course> courses = 
               FXCollections.observableArrayList(cCMP100, cOIM220, cENG111, 
                       cCMP545, cCMP237, cNC100, cNC200, cNC300, cNC400, cNC500);
       
      // registrationTable.setPrefSize(500,500); // width, height
       registrationTable.setMaxWidth(760);
       registrationTable.setMaxHeight(340);
       registrationTable.setItems(courses);
       
       TableColumn cNumber = new TableColumn("Course Number");
       cNumber.setMinWidth(120);
       cNumber.setCellValueFactory(
               new PropertyValueFactory<Course, String>("courseNum"));
       
       TableColumn cName = new TableColumn("Course Name");
       cName.setMinWidth(200);
       cName.setCellValueFactory(
               new PropertyValueFactory<Course, String>("courseName"));
       
       TableColumn cDay = new TableColumn("Day of Week");
       cDay.setMinWidth(120);
       cDay.setCellValueFactory(
               new PropertyValueFactory<Course, String>("day"));
       
       TableColumn cTime = new TableColumn("Meeting Time");
       cTime.setMinWidth(120);
       cTime.setCellValueFactory(
               new PropertyValueFactory<Course, String>("time"));
       
       TableColumn cRoom = new TableColumn("Meeting Room");
       cRoom.setMinWidth(120);
       cRoom.setCellValueFactory(
               new PropertyValueFactory<Course, String>("room"));
       
       TableColumn cNumCredits = new TableColumn("Credits");
       cNumCredits.setMinWidth(75);
       cNumCredits.setCellValueFactory(
               new PropertyValueFactory<Course, String>("credits"));
       
       registrationTable.getColumns().addAll(cNumber, cName, cDay, cTime, 
               cRoom, cNumCredits);
       // end course display section
       GridPane registrationSelection = new GridPane(); //Gridpane trial
       VBox registrationCheckboxes = new VBox();
       Label registerNow = new Label(" Register: ");
       Label threeCredits = new Label("Three Credit Courses: ");
       registerNow.setFont(titleFont);
       threeCredits.setFont(timeFont);
       Line creditCoursesLine = new Line(0, 10, 100, 10);
       RadioButton CMP100 = new RadioButton("CMP 100:  6:30-8:45");
       RadioButton OIM220 = new RadioButton("OIM 220:  5:00-6:15");
       RadioButton ENG111 = new RadioButton("ENG 111:  8:30-11:45");
       RadioButton CMP545 = new RadioButton("CMP 545:  6:30-9:15");
       RadioButton CMP237 = new RadioButton("CMP 237:  6:30-9:15");
       Line ncCoursesLine = new Line(0, 10, 100, 10);
       Label noCredits = new Label("Non-Credit Courses: ");
       noCredits.setFont(timeFont);
       RadioButton NC100 = new RadioButton("NC 100:    6:00-8:00");
       RadioButton NC200 = new RadioButton("NC 200:    6:00-8:00");
       RadioButton NC300 = new RadioButton("NC 300:    6:00-8:00");
       RadioButton NC400 = new RadioButton("NC 400:    6:15-8:15");
       RadioButton NC500 = new RadioButton("NC 500:    7:00-9:00");
       
       //gridpane trial:
       Label monday = new Label("Monday:     ");
       Label tuesday = new Label("Tuesday:     ");
       Label wednesday = new Label("Wednesday:     ");
       Label thursday = new Label("Thursday:     ");
       Label friday = new Label("Friday:     ");
       // keep mutually exclusive courses separate
       // Monday:
        ToggleGroup mondayClasses = new ToggleGroup();
        CMP100.setToggleGroup(mondayClasses);
        NC100.setToggleGroup(mondayClasses);     
        // no Tuesday class conflicts
        // Wednesday:
        ToggleGroup wednesdayClasses = new ToggleGroup();
        NC200.setToggleGroup(wednesdayClasses);
        NC300.setToggleGroup(wednesdayClasses);
        // no Thursday class conflicts
        //Friday:
        ToggleGroup fridayClasses = new ToggleGroup();
        CMP545.setToggleGroup(fridayClasses);
        NC500.setToggleGroup(fridayClasses);
        Button btResetChoices = new Button("Reset Choices");
        Button btSubmitChoices = new Button("Submit");
        
        btResetChoices.setOnAction(e-> {
                CMP100.setSelected(false);
                OIM220.setSelected(false);
                ENG111.setSelected(false);
                CMP545.setSelected(false);
                CMP237.setSelected(false);
                NC100.setSelected(false);
                NC200.setSelected(false);
                NC300.setSelected(false);
                NC400.setSelected(false);
                NC500.setSelected(false);    
                tuitionBillPane.setVisible(false);
                errorPane.setVisible(false);
                registrationCompleteMessage.setVisible(false);
                droppedMessage.setVisible(false);
        });
        CMP100.setOnAction(e->{
            numCredCourses = 0; numNCcourses = 0; //reset accumulators
            numCredCourses++;
            if(NC400.isSelected())   numNCcourses++;
            if(NC200.isSelected() || NC300.isSelected())  numNCcourses++;
            if(NC500.isSelected())   numNCcourses++;
            
            if(OIM220.isSelected()) numCredCourses++;
            if(ENG111.isSelected()) numCredCourses++;
            if(CMP237.isSelected()) numCredCourses++;
            if(CMP545.isSelected()) numCredCourses++;
            tuition = calcTuition(numCredCourses, numNCcourses, isFT);
            tuitionLabel.setText(String.format("Tuition: $%.2f \t"
              +  "\tFees: $   5.00\t\t  Total: $%.2f\t", tuition, tuition + 5.00));
            tuitionBillPane.setVisible(true);
            registrationCompleteMessage.setVisible(false);
            });
        OIM220.setOnAction(e->{
            numCredCourses = 0; numNCcourses = 0; //reset accumulators
            if(NC100.isSelected()) numNCcourses++;
            if(NC400.isSelected()) numNCcourses++;
            if(NC200.isSelected() || NC300.isSelected())  numNCcourses++;
            if(NC500.isSelected()) numNCcourses++;
            
            if(CMP100.isSelected()) numCredCourses++;
            if(OIM220.isSelected()) numCredCourses++;
            if(ENG111.isSelected()) numCredCourses++;
            if(CMP237.isSelected()) numCredCourses++;
            if(CMP545.isSelected()) numCredCourses++;
            tuition = calcTuition(numCredCourses, numNCcourses, isFT);
            tuitionLabel.setText(String.format("Tuition: $%.2f \t"
              +  "\tFees: $   5.00\t\t  Total: $%.2f\t", tuition, tuition + 5.00));
            tuitionBillPane.setVisible(true);
            registrationCompleteMessage.setVisible(false);
        });
        ENG111.setOnAction(e->{
            numCredCourses = 0; numNCcourses = 0; //reset accumulators
            if(NC100.isSelected()) numNCcourses++;
            if(NC400.isSelected()) numNCcourses++;
            if(NC200.isSelected() || NC300.isSelected())  numNCcourses++;
            if(NC500.isSelected()) numNCcourses++;
            
            if(CMP100.isSelected()) numCredCourses++;
            if(OIM220.isSelected()) numCredCourses++;
            if(ENG111.isSelected()) numCredCourses++;
            if(CMP237.isSelected()) numCredCourses++;
            if(CMP545.isSelected()) numCredCourses++;
            tuition = calcTuition(numCredCourses, numNCcourses, isFT);
            tuitionLabel.setText(String.format("Tuition: $%.2f \t"
              +  "\tFees: $   5.00\t\t  Total: $%.2f\t", tuition, tuition + 5.00));
            tuitionBillPane.setVisible(true);
            registrationCompleteMessage.setVisible(false);
        });
        CMP545.setOnAction(e->{
            numCredCourses = 0; numNCcourses = 0; //reset accumulators
            numCredCourses++;
            if(NC100.isSelected()) numNCcourses++;
            if(NC400.isSelected()) numNCcourses++;
            if(NC200.isSelected() || NC300.isSelected())  numNCcourses++;
            
            if(CMP100.isSelected()) numCredCourses++;
            if(OIM220.isSelected()) numCredCourses++;
            if(CMP237.isSelected()) numCredCourses++;
            if(ENG111.isSelected()) numCredCourses++;
            tuition = calcTuition(numCredCourses, numNCcourses, isFT);
            tuitionLabel.setText(String.format("Tuition: $%.2f \t"
              +  "\tFees: $   5.00\t\t  Total: $%.2f\t", tuition, tuition + 5.00));
            tuitionBillPane.setVisible(true);
            registrationCompleteMessage.setVisible(false);
        });
        CMP237.setOnAction(e->{
            numCredCourses = 0; numNCcourses = 0; //reset accumulators
            if(NC100.isSelected()) numNCcourses++;
            if(NC400.isSelected()) numNCcourses++;
            if(NC200.isSelected() || NC300.isSelected())  numNCcourses++;
            if(NC500.isSelected()) numNCcourses++;
            
            if(CMP100.isSelected()) numCredCourses++;
            if(OIM220.isSelected()) numCredCourses++;
            if(ENG111.isSelected()) numCredCourses++;
            if(CMP545.isSelected()) numCredCourses++;
            if(CMP237.isSelected()) numCredCourses++;
            tuition = calcTuition(numCredCourses, numNCcourses, isFT);
            tuitionLabel.setText(String.format("Tuition: $%.2f \t"
              +  "\tFees: $   5.00\t\t  Total: $%.2f\t", tuition, tuition + 5.00));
            tuitionBillPane.setVisible(true);
            registrationCompleteMessage.setVisible(false);
        });
        NC100.setOnAction(e->{
            numCredCourses = 0; numNCcourses = 0; //reset accumulators
            numNCcourses++;
            if(NC400.isSelected()) numNCcourses++;
            if(NC200.isSelected() || NC300.isSelected())  numNCcourses++;
            if(NC500.isSelected()) numNCcourses++;
            
            if(OIM220.isSelected()) numCredCourses++;
            if(ENG111.isSelected()) numCredCourses++;
            if(CMP237.isSelected()) numCredCourses++;
            if(CMP545.isSelected()) numCredCourses++;
            tuition = calcTuition(numCredCourses, numNCcourses, isFT);
            tuitionLabel.setText(String.format("Tuition: $%.2f \t"
              +  "\tFees: $   5.00\t\t  Total: $%.2f\t", tuition, tuition + 5.00));
            tuitionBillPane.setVisible(true);
            registrationCompleteMessage.setVisible(false);
        });
        NC200.setOnAction(e->{
            numCredCourses = 0; numNCcourses = 0; //reset accumulators
            numNCcourses++;
            if(NC100.isSelected()) numNCcourses++;
            if(NC400.isSelected()) numNCcourses++;
            if(NC500.isSelected()) numNCcourses++;
            
            if(CMP100.isSelected()) numCredCourses++;
            if(OIM220.isSelected()) numCredCourses++;
            if(ENG111.isSelected()) numCredCourses++;
            if(CMP237.isSelected()) numCredCourses++;
            if(CMP545.isSelected()) numCredCourses++;
            tuition = calcTuition(numCredCourses, numNCcourses, isFT);
            tuitionLabel.setText(String.format("Tuition: $%.2f \t"
              +  "\tFees: $   5.00\t\t  Total: $%.2f\t", tuition, tuition + 5.00));
            tuitionBillPane.setVisible(true);
            registrationCompleteMessage.setVisible(false);
        });
        NC300.setOnAction(e->{
            numCredCourses = 0; numNCcourses = 0; //reset accumulators
            numNCcourses++;
            if(NC100.isSelected()) numNCcourses++;
            if(NC400.isSelected()) numNCcourses++;
            if(NC500.isSelected()) numNCcourses++;
            
            if(CMP100.isSelected()) numCredCourses++;
            if(OIM220.isSelected()) numCredCourses++;
            if(ENG111.isSelected()) numCredCourses++;
            if(CMP237.isSelected()) numCredCourses++;
            if(CMP545.isSelected()) numCredCourses++;
            tuition = calcTuition(numCredCourses, numNCcourses, isFT);
            tuitionLabel.setText(String.format("Tuition: $%.2f \t"
              +  "\tFees: $   5.00\t\t  Total: $%.2f\t", tuition, tuition + 5.00));
            tuitionBillPane.setVisible(true);
            registrationCompleteMessage.setVisible(false);
        });
        NC400.setOnAction(e->{
            numCredCourses = 0; numNCcourses = 0; //reset accumulators
            if(NC100.isSelected()) numNCcourses++;
            if(NC200.isSelected() || NC300.isSelected())  numNCcourses++;
            if(NC400.isSelected()) numNCcourses++;
            if(NC500.isSelected()) numNCcourses++;
            
            if(CMP100.isSelected()) numCredCourses++;
            if(OIM220.isSelected()) numCredCourses++;
            if(ENG111.isSelected()) numCredCourses++;
            if(CMP237.isSelected()) numCredCourses++;
            if(CMP545.isSelected()) numCredCourses++;
            tuition = calcTuition(numCredCourses, numNCcourses, isFT);
            tuitionLabel.setText(String.format("Tuition: $%.2f \t"
              +  "\tFees: $   5.00\t\t  Total: $%.2f\t", tuition, tuition + 5.00));
            tuitionBillPane.setVisible(true);
            registrationCompleteMessage.setVisible(false);
        });
        NC500.setOnAction(e->{
            numCredCourses = 0; numNCcourses = 0; //reset accumulators
            numNCcourses++;
            if(NC100.isSelected()) numNCcourses++;
            if(NC200.isSelected() || NC300.isSelected())  numNCcourses++;
            if(NC400.isSelected()) numNCcourses++;
            
            if(CMP100.isSelected()) numCredCourses++;
            if(OIM220.isSelected()) numCredCourses++;
            if(ENG111.isSelected()) numCredCourses++;
            if(CMP237.isSelected()) numCredCourses++;
            calcTuition(numCredCourses, numNCcourses, isFT);
            tuition = calcTuition(numCredCourses, numNCcourses, isFT);
            tuitionLabel.setText(String.format("Tuition: $%.2f \t"
              +  "\tFees: $   5.00\t\t  Total: $%.2f\t", tuition, tuition + 5.00));
            tuitionBillPane.setVisible(true);
            registrationCompleteMessage.setVisible(false);
        });

        
        
        
        TextField ssnLogin = new TextField();
        ssnLogin.maxWidth(2);
        Label ssnFieldLabel = new Label("Please enter your SSN: ");
        TextField ssnLoginField = new TextField();
        
        
        //gridpane populating
        registrationSelection.setHgap(10);
        registrationSelection.setVgap(10);
        registrationSelection.add(registerNow, 1,0);
        registrationSelection.add(monday, 1, 1);
        registrationSelection.add(CMP100, 1, 2);
        registrationSelection.add(NC100, 1, 3);
        registrationSelection.add(tuesday, 3, 1);
        registrationSelection.add(OIM220, 3, 2);
        registrationSelection.add(NC400, 3, 3);
        registrationSelection.add(wednesday, 4, 1);
        registrationSelection.add(ENG111, 4, 2);
        registrationSelection.add(NC200,  4, 3);
        registrationSelection.add(NC300, 4, 4);
        registrationSelection.add(thursday, 5, 1);
        registrationSelection.add(CMP237, 5, 2);
        registrationSelection.add(friday, 6, 1);
        registrationSelection.add(CMP545, 6, 2);
        registrationSelection.add(NC500, 6, 3);
        registrationSelection.add(btResetChoices, 3, 5);
        registrationSelection.add(btSubmitChoices, 5, 5);
        registrationSelection.add(creditCoursesLine, 4,6);
        registrationSelection.add(ssnFieldLabel, 3,0);
        registrationSelection.add(ssnLoginField, 4,0);
        
        registrationSelection.setTranslateY(600);
        registrationTable.setTranslateY(-100);
        
        Button btDropRecords = new Button("Drop All");
        registrationSelection.add(btDropRecords, 4, 5);
        droppedMessage.setFont(basicFont);
        droppedMessage.setText("All saved courses have been dropped.");
        droppedMessage.setVisible(false);
        tuitionBillPane.getChildren().add(droppedMessage);
        btDropRecords.setOnAction(e->{
            droppedMessage.setVisible(true);
            registrationCompleteMessage.setVisible(false);
            tuitionBillPane.setVisible(false);
            loginEntry = processRawSSN(ssnLoginField.getText()); 
            if(loginEntry.equals("")){
                droppedMessage.setText("You must log in as an admitted student first!");
            }
            else{
                droppedMessage.setText("All saved courses have been dropped.");
                loginEntry = processRawSSN(ssnLoginField.getText()); // given user ssn
                updateDB(String.format("DELETE FROM Registration WHERE ssn ='%s';", loginEntry));
            }
        });
       
        btSubmitChoices.setOnAction(e->{
            droppedMessage.setVisible(false);
            loginEntry = processRawSSN(ssnLoginField.getText()); // given user ssn
            
            StudentSchedule currentStuRegistration = loadScheduleReport(loginEntry); 
             
            // get info about the student
            String stuImmStatus = currentStuRegistration.getStudent().hasImmunization();
            String stuMatStatus = currentStuRegistration.getStudent().getMatriculationStatus();
            String stuHSDstatus = currentStuRegistration.getStudent().getHSdiplomaStatus();
            // get info about the number of credits the student has selected

            
            int numCreditCourses = Integer.valueOf(
                    retrieveFromDB(loginEntry + "' AND credits = '3", "ssn", "COUNT(*)", "Schedule"));
            numCreditCourses += (CMP100.isSelected() && 
                    !currentStuRegistration.isAlreadyRegistered(cCMP100) ? 1 : 0);
            numCreditCourses += (OIM220.isSelected() && 
                    !currentStuRegistration.isAlreadyRegistered(cOIM220)? 1 : 0);
            numCreditCourses += (ENG111.isSelected() && 
                    !currentStuRegistration.isAlreadyRegistered(cENG111)? 1 : 0);
            numCreditCourses += (CMP545.isSelected() && 
                    !currentStuRegistration.isAlreadyRegistered(cCMP545)? 1 : 0);
            numCreditCourses += (CMP237.isSelected() && 
                    !currentStuRegistration.isAlreadyRegistered(cCMP237)? 1 : 0);
            
            // prepare an error message to print if necessary
            String errorMsg = "";
            boolean hasError = false;
            String submitMessage = "";
                
            registrationCompleteMessage.setVisible(false);
            
            if(loginEntry.equals("")  || stuMatStatus == null){
                // print message "you must be admitted and log in first!"
                hasError = true;
                errorMsg += "You must log in as an admitted student first!";
            }
            else{ if(stuMatStatus.equals("y") && 
                    (stuImmStatus.equals("n") || stuHSDstatus.equals("n"))){
                hasError = true;
                errorMsg += "Matriculated students must have " +
                        (stuImmStatus.equals("n") ? "immunization records" : "") +
                        (stuImmStatus.equals("n") && stuHSDstatus.equals("n") ? " AND " : "") +
                        (stuHSDstatus.equals("n") ? "a HS diploma" : "") +
                        ". \n";
                System.out.println("HSD Status: " + stuHSDstatus);
                System.out.println("HSD Status n? :  " + (stuHSDstatus.equals("n")? "true" : "false"));
                // print error message indicating that matriculated students need
                // BOTH immunizations and hs degrees to register; tailor to
                // fit what the student is missing.
            }
            if(isFT && numCreditCourses < 3){
                hasError = true;
                errorMsg += "You have not registered enough credits to be full time. \n";
                // print error message saying that the student is not FT if 
                // they take this many credits, and should register as PT
            }
            if(isPT && numCreditCourses > 2){
                hasError = true;
                errorMsg += "Part-time means six or fewer credits: register as "
                        + "full time or take fewer courses. \n";
                // print error message saying that the student is not FT if 
                // they take this many credits, and should register as PT
            }
            System.out.printf("\nMat status: %s \n Imm Status: %s \n Num Cred Courses: %d\n", stuMatStatus, stuImmStatus, numCreditCourses);
            if(stuMatStatus.equals("n") &&
                    stuImmStatus.equals("n") && numCreditCourses >= 3){
                hasError = true;
                errorMsg += "You cannot register for more than 6 credits due to "
                        + "your immunization status. \n";
                // error message about how you cannot register for so many courses
                // without immunization records
            }
                    }
            if(hasError){
                errorLabel.setText(errorMsg);
                errorPane.setVisible(true);
                errorLabel.setVisible(true);
               // errorPane.toFront();
                
                
            }
            else{ // if we don't trigger errors, register the selected classes 
                errorPane.setVisible(false);                
                submitMessage += selectionAction(CMP100, cCMP100, currentStuRegistration.getStudent());
                submitMessage += selectionAction(OIM220, cOIM220, currentStuRegistration.getStudent());
                submitMessage += selectionAction(ENG111, cENG111, currentStuRegistration.getStudent());
                submitMessage += selectionAction(CMP545, cCMP545, currentStuRegistration.getStudent());
                submitMessage += selectionAction(CMP237, cCMP237, currentStuRegistration.getStudent());
                submitMessage += selectionAction(NC100, cNC100, currentStuRegistration.getStudent());
                submitMessage += selectionAction(NC200, cNC200, currentStuRegistration.getStudent());
                submitMessage += selectionAction(NC300, cNC300, currentStuRegistration.getStudent());
                submitMessage += selectionAction(NC400, cNC400, currentStuRegistration.getStudent());
                submitMessage += selectionAction(NC500, cNC500, currentStuRegistration.getStudent());
                // add something to display any of the submission messages under the tuition messages
                registrationCompleteMessage.setText(submitMessage 
                        + "\nRegistration complete.");
                registrationCompleteMessage.setVisible(true);
                registrationCompleteMessage.setTranslateY(40);
                registrationCompleteMessage.setTranslateX(-40);
                primaryPane.setRight(registrationCompleteMessage);
                updateDBNumTimesRegistered(loginEntry, isFT);
            }
                    
        });
        
      
       // end registration section 
       

        // Menu Navigation button actions
      
        miMatriculated.setOnAction(e-> {
                errorLabel.setText("");
                //primaryPane.setBottom(errorPane);
                primaryPane.setCenter(demographicsPane);
                menuAdmissions.setVisible(true);
                demographicsPane.setVisible(true);
                errorPane.setVisible(false);

                matriculated = "y";
                needsHSdiploma = true;       needsImmunization = true;
                
                matricYr.setVisible(true);   matYrFR.setVisible(true);
                matYrSO.setVisible(true);    matYrSO.setVisible(true);
                matYrJR.setVisible(true);    matYrSR.setVisible(true);
                degree.setVisible(true);     asDegree.setVisible(true);
                aaDegree.setVisible(true);   HSdiploma.setVisible(true);
                
                // clear any entries left over
                ssnInputField.clear();        fNameField.clear();
                miField.clear();              LNameField.clear();
                stAddressField.clear();       cityField.clear();
                stateField.clear();           zipField.clear();            
                matYrFR.setSelected(false);   matYrJR.setSelected(false);
                matYrSO.setSelected(false);   matYrSR.setSelected(false);                              
                asDegree.setSelected(false);  aaDegree.setSelected(false);                               
                HSdiploma.setSelected(false); immunization.setSelected(false);
        });
       
        miNonMatriculated.setOnAction(e-> {
                errorLabel.setText("");
                //primaryPane.setBottom(errorPane);
                primaryPane.setCenter(demographicsPane);
                menuAdmissions.setVisible(true);
                demographicsPane.setVisible(true);
                errorPane.setVisible(false);
                matriculated = "n";
                needsHSdiploma = false;       needsImmunization = false;

                matricYr.setVisible(false);   matYrFR.setVisible(false);
                matYrSO.setVisible(false);    matYrSO.setVisible(false);
                matYrJR.setVisible(false);    matYrSR.setVisible(false);
                degree.setVisible(false);     asDegree.setVisible(false);
                aaDegree.setVisible(false);   HSdiploma.setVisible(false);
                
                // clear any entries left over
                ssnInputField.clear();        fNameField.clear();
                miField.clear();              LNameField.clear();
                stAddressField.clear();       cityField.clear();
                stateField.clear();           zipField.clear();            
                matYrFR.setSelected(false);   matYrJR.setSelected(false);
                matYrSO.setSelected(false);   matYrSR.setSelected(false);                              
                asDegree.setSelected(false);  aaDegree.setSelected(false);                               
                HSdiploma.setSelected(false); immunization.setSelected(false);
                
        });                
        

        miFullTime.setOnAction(e-> {
               // primaryPane.setBottom(errorPane);
                uniName.setVisible(false);
                primaryPane.setCenter(registrationPane);              
                menuRegistration.setVisible(true);
                registrationPane.setVisible(true);
                CMP100.setVisible(true);    CMP237.setVisible(true);
                CMP545.setVisible(true);    OIM220.setVisible(true);
                ENG111.setVisible(true);    thursday.setVisible(true);
                errorPane.setVisible(true);
                errorLabel.setText("");
                tuitionBillPane.setVisible(false);
                isFT = true; isPT = false; isNC = false;
                registrationHeaderLabel.setText("Full-time Registration");
                registrationHeaderLabel.setVisible(true);
        });
        
        miPartTime.setOnAction(e-> {
               // primaryPane.setBottom(errorPane);
                primaryPane.setCenter(registrationPane);
                registrationPane.setVisible(true);
                //menuAdmissions.setVisible(true);
                CMP100.setVisible(true);    CMP237.setVisible(true);
                CMP545.setVisible(true);    OIM220.setVisible(true);
                ENG111.setVisible(true);    thursday.setVisible(true);
                errorPane.setVisible(false);
                tuitionBillPane.setVisible(false);
                errorLabel.setText("");
                isFT = false; isPT = true; isNC = false;
                registrationHeaderLabel.setText("Part-time Registration");
                registrationHeaderLabel.setVisible(true);
        });

        miNC.setOnAction(e-> {
                //primaryPane.setBottom(errorPane);
                primaryPane.setCenter(registrationPane);
                registrationPane.setVisible(true);
                CMP100.setVisible(false);    CMP237.setVisible(false);
                CMP545.setVisible(false);    OIM220.setVisible(false);
                ENG111.setVisible(false);    thursday.setVisible(false);
                errorPane.setVisible(false);
                tuitionBillPane.setVisible(false);
                errorLabel.setText("");
                isFT = false; isPT = false; isNC = true;
                registrationHeaderLabel.setText("Non-Credit Registration");
                registrationHeaderLabel.setVisible(true);
                //menuAdmissions.setVisible(true);
        });
        
        
        miReceivables.setOnAction(e-> {
           // primaryPane.setBottom(errorPane);
            schedulePane.setVisible(false);
            receivablesPane.setVisible(true);
            errorPane.setVisible(false);
            primaryPane.setCenter(reportsPane);
            reportsPane.setVisible(true);
            reportsHeaderPane.toFront();
            matClassReportsLabel.setVisible(false);
            errorLabel.setText("");
            reportsHeaderLabel.setText("Receivables");
        });

        miClassSchedule.setOnAction(e-> {        
           //primaryPane.setBottom(errorPane);
           schedulePane.setVisible(true);
           receivablesPane.setVisible(false);
           errorPane.setVisible(false);
           primaryPane.setCenter(reportsPane);
           reportsPane.setVisible(true);    
           reportsHeaderPane.toFront();
           matClassReportsLabel.setVisible(true);
           errorLabel.setText("");
           reportsHeaderLabel.setText("Schedule");
           
        });

      
        miQuit.setOnAction(e-> {
                primaryPane.setCenter(quitDialogue);
                quitDialogue.setVisible(true);
                errorPane.setVisible(false);
                errorLabel.setText("");
        });
        

        
        // print schedule: 
        
         Label ssnReportsLoginLabel = new Label("Please enter your SSN: ");
         TextField ssnReportsLoginField = new TextField();
         ssnReportsLoginField.setMaxWidth(150);
         Button btLogIn = new Button("Log In"); // button to query for ssn/name
         
         Label ssnReportsLabel = new Label();
         Label fNameReportsLabel = new Label();
         Label midInitReportsLabel = new Label();
         Label LNameReportsLabel = new Label();
         
         // set up table to display schedule
         TableView<Course> stuScheduleTable = new TableView<>();
         stuScheduleTable.setMaxWidth(800);
         stuScheduleTable.setMaxHeight(340);
         TableColumn stuCourseNum = new TableColumn("Course Number");
           stuCourseNum.setMinWidth(120);
         TableColumn stuCourseName = new TableColumn("Course Name");
           stuCourseName.setMinWidth(200);
         TableColumn stuCourseDay = new TableColumn("Day of Week");
           stuCourseDay.setMinWidth(120);
         TableColumn stuCourseTime = new TableColumn("Meeting Time");
           stuCourseTime.setMinWidth(120);
         TableColumn stuCourseRoom = new TableColumn("Meeting Room");
           stuCourseRoom.setMinWidth(120);
         stuScheduleTable.getColumns().addAll(stuCourseNum, stuCourseName, 
               stuCourseDay, stuCourseTime, stuCourseRoom);
         // end table setup for schedule
                           
         btLogIn.setOnAction(e->{ // this button loads all reports data for a student
            loginEntry = processRawSSN(ssnReportsLoginField.getText());  // query DB for student's reports
            if(loginEntry.equals("")){
                errorLabel.setText("You must log in as an admitted student first!");
                errorPane.setVisible(true);
            }
            else{
                errorPane.setVisible(false);
                stuScheduleTable.getItems().clear();
               // loginEntry = processRawSSN(ssnReportsLoginField.getText()); 
                StudentSchedule studentSchedule = loadScheduleReport(loginEntry); 
                // set up student information header
                ssnReportsLabel.setText(studentSchedule.getStudent().getSsn()); 
                fNameReportsLabel.setText(studentSchedule.getStudent().getfName());
                midInitReportsLabel.setText(studentSchedule.getStudent().getMi());
                LNameReportsLabel.setText(studentSchedule.getStudent().getLName());
                matClassReportsLabel.setText(studentSchedule.getStudent().getMatYr());
                // end student information header
           
                // print schedule report to the tableView
                 for (int i = 0; i < studentSchedule.getCourseArraySize(); i++){
                    System.out.println(studentSchedule.getCourse(i).getCourseNum());
                 }
            
                 ObservableList<Course> stuScheduleList =  
                         FXCollections.observableArrayList( 
                                 studentSchedule.getCourseArray());            
 
                  stuScheduleTable.setItems(stuScheduleList);
                  stuCourseNum.setCellValueFactory(
                          new PropertyValueFactory<Course, String>("courseNum"));
                  stuCourseName.setCellValueFactory(
                          new PropertyValueFactory<Course, String>("courseName"));
                  stuCourseDay.setCellValueFactory(
                          new PropertyValueFactory<Course, String>("day"));
                  stuCourseTime.setCellValueFactory(
                          new PropertyValueFactory<Course, String>("time"));  
                  stuCourseRoom.setCellValueFactory(
                          new PropertyValueFactory<Course, String>("room"));
                  // end print schedule report to tableView
             
                  // begin print receivables report
                  Receivables receivablesReport = retrieveReceivablesDataFromDB(loginEntry);
                  reportsNumCredCourse.setText(
                          String.format("Credit Courses: %d", receivablesReport.getNumCred()));
                  reportsNumNCcourse.setText(
                          String.format("Non-Credit Courses: %d", receivablesReport.getNumNC()));      
                  reportsCredDollars.setText(
                          String.format("Credit Course Tuition: $%.2f", receivablesReport.getPriceAllCred()));
                  reportsNCDollars.setText(
                          String.format("Non-Credit Course Tuition: $%.2f", receivablesReport.getPriceAllNC()));                     
                  reportsRegFees.setText(
                          String.format("Registration Fees: $%.2f", receivablesReport.getFees()));
                  reportsGrandTotal.setText(
                          String.format("Total Tuition: $%.2f", receivablesReport.getTotalPrice()));
            }
       });
       // end of table displaying selected courses 
       
     
       // oren***
       reportsHeaderPane.add(reportsHeaderLabel, 1, 0);
       reportsHeaderPane.add(ssnReportsLoginLabel, 0, 1);
       reportsHeaderPane.add(ssnReportsLoginField, 1, 1);
       reportsHeaderPane.add(btLogIn, 2, 1);
       reportsHeaderPane.add(ssnReportsLabel, 4, 3);
       reportsHeaderPane.add(fNameReportsLabel, 1, 3);
       reportsHeaderPane.add(midInitReportsLabel, 2, 3);
       reportsHeaderPane.add(LNameReportsLabel, 3, 3);
       reportsHeaderPane.add(matClassReportsLabel, 1, 4);
       reportsHeaderPane.setHgap(10);
       
       reportsHeaderLabel.setFont(uniNameFont);
       fNameReportsLabel.setFont(titleFont);
       midInitReportsLabel.setFont(titleFont);
       LNameReportsLabel.setFont(titleFont);
       matClassReportsLabel.setFont(basicFont);

       
        reportsHeaderPane.setTranslateY(100);
        reportsHeaderPane.setTranslateX(100);
       schedulePane.getChildren().addAll(stuScheduleTable);
         
        //reportsPane.getChildren().addAll(reportsHeaderPane, schedulePane, receivablesPane);
        reportsPane.add(reportsHeaderPane, 0, 0);
        reportsPane.add(schedulePane, 0, 1);
        reportsPane.add(receivablesPane, 0, 1);
        
        schedulePane.setTranslateX(150);
        schedulePane.setTranslateY(50);
        receivablesPane.setTranslateX(150);
        receivablesPane.setTranslateY(50);
        
        
        receivablesPane.add(reportsNumCredCourse, 0, 0);
        receivablesPane.add(reportsNumNCcourse, 0, 1);
        receivablesPane.add(reportsCredDollars, 1, 0);
        receivablesPane.add(reportsNCDollars, 1, 1);
        receivablesPane.add(reportsRegFees, 1, 2);
        receivablesPane.add(reportsGrandTotal, 1, 3);
        receivablesPane.setTranslateY(200);
        receivablesPane.setHgap(30);
        receivablesPane.setVgap(10);
        
        
 
       // reportsPane.setTop(reportsHeaderPane);
        //reportsPane.getChildren().add(reportsHeaderPane);
       
        
        //StackPane quitDialogue = new StackPane();
        Label quitMessage = new Label("Are you sure you want to quit?");
        Button btYes = new Button("Yes");
        Button btNo = new Button("No");
        quitDialogue.getChildren().addAll(quitMessage, btYes, btNo);
        quitDialogue.setAlignment(Pos.CENTER);
        btYes.setTranslateX(-30);
        btYes.setTranslateY(30);
        btNo.setTranslateX(40);
        btNo.setTranslateY(30);
        btYes.setOnAction(e-> { System.exit(0); });
        btNo.setOnAction(e-> {
               quitDialogue.setVisible(false);
        });
      

       
       registrationPane.getChildren().addAll(registrationHeaderLabel, registrationTable, 
               registrationSelection, tuitionBillPane);//, 
       registrationSelection.setTranslateY(-60);
       tuitionBillPane.setTranslateY(45);
       tuitionBillPane.setTranslateX(-50);
       registrationHeaderLabel.setTranslateY(-95);
       //registrationTable.setTranslateY(-70);
       //registrationTable.setTranslateX(-15);
       registrationPane.setAlignment(Pos.CENTER);


        // university front page info
        uniName.setFont(uniNameFont);
        errorPane.getChildren().add(uniName);
        uniName.setAlignment(Pos.CENTER);
        Image uniImage = new Image("file:src/college_stock_photo.png");
        ImageView uniImgDisplay = new ImageView(uniImage);
        primaryPane.setCenter(uniImgDisplay);
        //primaryPane.setRight(tuitionBillPane);
        primaryPane.setBottom(errorPane);
        


        
        
        Scene primaryScene = new Scene(primaryPane, 1040, 900);
        primaryStage.setTitle("Boola Boola University");
        primaryStage.setScene(primaryScene);
        primaryStage.show();
        

    }

    public static void main(String[] args) throws Exception {

        Application.launch(args);
    }


    
    private void updateDB(String queryString) { 
        try{
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(DATABASE_URL,
                    "marisha", "password");
            statement = connection.createStatement();
            
            statement.executeUpdate(queryString);
            
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        catch(ClassNotFoundException classNotFound){
            classNotFound.printStackTrace();
        }
        finally{
        
            try{
                statement.close();
                connection.close();
            }
            catch(Exception exception){
                exception.printStackTrace();
            }
        }
    }

   
    private void storeStudentData(Student student) { 
        String queryString = String.format("INSERT INTO Demographics VALUES"
                + " ( \'%s\', \'%s\', \'%s\', \'%s\', \'%s\', \'%s\', \'%s\', "
                + "\'%s\', \'%s\', \'%s\', \'%s\', \'%s\', \'%s\', \'%s\');",
                student.getSsn(), student.getfName(), student.getMi(),
                student.getLName(), student.getAddress(), student.getCity(),
                student.getState(), student.getZip(), student.getMatYr(),
                student.getDegree(), student.getHSdiplomaStatus(), 
                student.hasImmunization(), student.getDateCreated(), 
                student.getMatriculationStatus());
                       
        updateDB(queryString);
        
        }
    
    private void storeRegistrationData(String ssn, String courseNum) { 
        String queryString = String.format("INSERT INTO Registration VALUES"
                + " ( \'%s\', \'%s\');",
                ssn, courseNum);
                       
        updateDB(queryString);
        
        }
    

     private String retrieveFromDB(String userInput, String userInputType,
             String field, String tableName) {  //look at result set stuff from class handouts
       String output = "";
       try{
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(DATABASE_URL,
                    "marisha", "password");
            statement = connection.createStatement();
            
           resultSet = statement.executeQuery(String.format("SELECT %s FROM %s WHERE %s='%s';", 
                   field, tableName, userInputType, userInput));
           if(resultSet.next())
              output = resultSet.getString(field);
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
            output = "ERROR: Data retrieval not successful";
        }
        catch(ClassNotFoundException classNotFound){
            classNotFound.printStackTrace();
            output = "ERROR: Data retrieval not successful";
        }
        finally{
        
            try{
                resultSet.close();
                statement.close();
                connection.close();
            }
            catch(Exception exception){
                exception.printStackTrace();
            }
        }
       return output;
    }
    
      private Student retrieveStudentFromDB(String userInput) {  //look at result set stuff from class handouts
       Student output = new Student();
       try{
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(DATABASE_URL,
                    "marisha", "password");
            statement = connection.createStatement();
            
           resultSet = statement.executeQuery(String.format("SELECT * FROM Demographics WHERE ssn='%s';", userInput));
           if (resultSet.next()){
             output.setSsn(resultSet.getString("ssn"));
             output.setfName(resultSet.getString("fName"));
             output.setMi(resultSet.getString("mi"));
             output.setLName(resultSet.getString("LName"));
             output.setAddress(resultSet.getString("address"));
             output.setCity(resultSet.getString("city"));
             output.setState(resultSet.getString("state"));
             output.setZip(resultSet.getString("zip"));
             output.setMatYr(resultSet.getString("matriculationYr"));
             output.setDegree(resultSet.getString("degree"));           
             output.setHSdiploma(resultSet.getString("HSdiploma"));
             output.setImmunization(resultSet.getString("immunization"));
             output.setDateCreated(resultSet.getString("dateCreated"));           
             output.setMatriculated(resultSet.getString("isMatriculated"));
           }
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();   
        }
        catch(ClassNotFoundException classNotFound){
            classNotFound.printStackTrace();
        }
        finally{
        
            try{
                resultSet.close();
                statement.close();
                connection.close();
            }
            catch(Exception exception){
                exception.printStackTrace();
            }
        }
       return output;
    }
     

        
        public static double calcTuition(int numCreditCourses, int numNCcourses, boolean isFT){
            double tuition = 0;

            if (isFT && numCreditCourses >= 3){
                tuition = (3 * 285) + numNCcourses * 150;
                tuition += (numCreditCourses - 3) * 265;
            }
            else if(!isFT || numCreditCourses < 3){
                tuition = numCreditCourses * 300 + numNCcourses * 150;
            }
            
            return tuition;
        }
        
       private int retrieveNumTimesCourseRegistered(String ssn, String courseNum) {  //look at result set stuff from class handouts
       int output = -1; // if -1 is returned, there was an error.   
       try{
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(DATABASE_URL,
                    "marisha", "password");
            statement = connection.createStatement();
            
           resultSet = statement.executeQuery(String.format("SELECT COUNT(*) FROM Registration WHERE ssn='%s' and courseNumber='%s';", ssn, courseNum));
           if(resultSet.next())
              output = resultSet.getInt("COUNT(*)");
           
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();   
        }
        catch(ClassNotFoundException classNotFound){
            classNotFound.printStackTrace();
        }
        finally{
        
            try{
                resultSet.close();
                statement.close();
                connection.close();
            }
            catch(Exception exception){
                exception.printStackTrace();
            }
        }
       return output;
    }
    
       private int retrieveNumCoursesRegistered(String ssn) {  //look at result set stuff from class handouts
       int output = -1; // if -1 is returned, there was an error.   
       try{
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(DATABASE_URL,
                    "marisha", "password");
            statement = connection.createStatement();
            
           resultSet = statement.executeQuery(String.format("SELECT COUNT(*) FROM Registration WHERE ssn='%s';", ssn));
           if(resultSet.next())
              output = resultSet.getInt("COUNT(*)");
           
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();   
        }
        catch(ClassNotFoundException classNotFound){
            classNotFound.printStackTrace();
        }
        finally{
        
            try{
                resultSet.close();
                statement.close();
                connection.close();
            }
            catch(Exception exception){
                exception.printStackTrace();
            }
        }
       return output;
    }
       
       private Course retrieveCourseDataFromDB(String userInput) {  //look at result set stuff from class handouts
       Course output = new Course();
       try{
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(DATABASE_URL,
                    "marisha", "password");
            statement = connection.createStatement();
            
           resultSet = statement.executeQuery(String.format("SELECT * FROM Courses WHERE cNum='%s';", userInput));
           if(resultSet.next()){
             output.setCourseNum(resultSet.getString("cNum"));
             output.setCourseName(resultSet.getString("cName"));
             output.setDay(resultSet.getString("day"));
             output.setTime(resultSet.getString("time"));
             output.setCredits(resultSet.getString("credits"));
           }
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();   
        }
        catch(ClassNotFoundException classNotFound){
            classNotFound.printStackTrace();
        }
        finally{
        
            try{
                resultSet.close();
                statement.close();
                connection.close();
            }
            catch(Exception exception){
                exception.printStackTrace();
            }
        }
       return output;
    }
       
      
       
       private String selectionAction(RadioButton courseButton, Course course, Student student){//, StudentSchedule sch){ //String courseName as 2nd arg
            int numTimesCourseRegistered;
            String submitMessage = "";
            if(courseButton.isSelected()){ // swapped loginEntry for currentStudent.getSsn()
              numTimesCourseRegistered = retrieveNumTimesCourseRegistered(student.getSsn(), course.getCourseNum());
                   if(numTimesCourseRegistered == 0){
                      storeRegistrationData(student.getSsn(), course.getCourseNum());
                      submitMessage += "Registered " + course.getCourseNum() + "\n";
                   }
                   else if ( numTimesCourseRegistered > 0){
                       submitMessage += "Previously registered " + course.getCourseNum() + "\n";
                   }
                   else { // error occurred
                       System.out.printf("\nError checking duplicates for %n "
                               + "for student %s\n", course.getCourseNum(), student.getSsn()); 
                   }
                }
            return submitMessage;
       }
       

       
       private StudentSchedule loadScheduleReport(String ssn) {  //look at result set stuff from class handouts
       ArrayList<Course> courses = new ArrayList();       
       Student student = retrieveStudentFromDB(ssn);       
       //int i = 0;
       
       try{
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(DATABASE_URL,
                    "marisha", "password");
            statement = connection.createStatement();
            
           resultSet = statement.executeQuery(String.format("SELECT * FROM Schedule WHERE ssn='%s';", ssn));
           //resultSet.next();
          
           while(resultSet.next()){
               courses.add(new Course(resultSet.getString("courseNumber"), resultSet.getString("cName"),
                       resultSet.getString("day"), resultSet.getString("time"), resultSet.getString("room"), 
                       resultSet.getString("credits")));
              // i++;
           }
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();   
        }
        catch(ClassNotFoundException classNotFound){
            classNotFound.printStackTrace();
        }
        finally{
        
            try{
                resultSet.close();
                statement.close();
                connection.close();
            }
            catch(Exception exception){
                exception.printStackTrace();
            }
        }
       StudentSchedule schedule = new StudentSchedule(student, courses);
       return schedule;
    }
       
       
    private Receivables retrieveReceivablesDataFromDB(String ssn) {  //look at result set stuff from class handouts FIND ME***
       Receivables output = new Receivables();
       output.setSsn(ssn);
       try{
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(DATABASE_URL,
                    "marisha", "password");
            statement = connection.createStatement();
            
            // start by getting data about how many times registered/ whether full time student
           resultSet = statement.executeQuery(String.format("SELECT * FROM times_registered WHERE ssn='%s';", ssn));
           if (resultSet.next()){
              output.setIsFT(resultSet.getString("isFT"));
              output.setNumTimesRegistered(resultSet.getInt("numTimes"));
           }
           
           // create a second query, this time to the schedule view, for num credit courses
           resultSet = statement.executeQuery(String.format("SELECT COUNT(*) FROM Schedule WHERE ssn='%s' AND credits = '3';", ssn));
           if (resultSet.next())
              output.setNumCred(resultSet.getInt("count(*)"));
           
           // finally, query scheudle view to find num nc courses
           resultSet = statement.executeQuery(String.format("SELECT COUNT(*) FROM Schedule WHERE ssn='%s' AND credits != '3';", ssn));
           if(resultSet.next())
               output.setNumNC(resultSet.getInt("count(*)"));
           
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();   
        }
        catch(ClassNotFoundException classNotFound){
            classNotFound.printStackTrace();
        }
        finally{
        
            try{
                resultSet.close();
                statement.close();
                connection.close();
            }
            catch(Exception exception){
                exception.printStackTrace();
            }
        }
       return output;
    }
       
    private void updateDBNumTimesRegistered(String ssn, boolean isFT) {  //look at result set stuff from class handouts FIND ME***
      int numTimes = 0;
      String ftStatus = (isFT? "y" : "n"); // convert bool to string to update table
      boolean hasRegisteredBefore = false;
        try{
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(DATABASE_URL,
                    "marisha", "password");
            statement = connection.createStatement();
            
            // start by getting data about how many times registered/ whether full time student
           resultSet = statement.executeQuery(String.format("SELECT numTimes FROM times_registered WHERE ssn='%s';", ssn));
           //resultSet.next();          
           if(resultSet.next()){
             numTimes += resultSet.getInt("numTimes"); // get the value currently stored in DB, if it exists 
             hasRegisteredBefore = true;
           }
           numTimes++; // increment numTimes by one
           
           resultSet.close();
           statement.close();
           
           // next update the db with the current results
           statement = connection.createStatement();
           if(hasRegisteredBefore){
               statement.executeUpdate(
                   String.format("UPDATE times_registered SET isFT ='%s', numTimes = %d WHERE ssn='%s';", ftStatus, numTimes, ssn));  
           }
           else{
               statement.executeUpdate(
                   String.format("INSERT INTO times_registered values ('%s', '%s', %d);", ssn, ftStatus, numTimes));
           }
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();   
        }
        catch(ClassNotFoundException classNotFound){
            classNotFound.printStackTrace();
        }
        finally{
        
            try{
                resultSet.close();
                statement.close();
                connection.close();
            }
            catch(Exception exception){
                exception.printStackTrace();
            }
        }
    }
       
    String processRawSSN(String ssnInput){
        String output, temp;
        temp = ssnInput.replace("-", ""); // removes dashes
        output = temp.replace(" ", ""); // removes spaces
        return output;
    }
       
       
}

