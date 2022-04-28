package application;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class MainController implements Initializable {

	//***********************************
	//**************GOMBOK***************
   
	@FXML
    private Button btnDelete;

    @FXML
    private Button btnNew;

    @FXML
    private Button btnUpdate;
    
    @FXML
    private Button btnToTracks;
    
    @FXML
    private Button btnClear;

    //***********************************
    //*************TÁBLÁZAT**************
   
    @FXML
    private TableView<Driver> tableviewOfDrivers;

    @FXML
    private TableColumn<Driver, String> columnCountry;

    @FXML
    private TableColumn<Driver, Date> columnDOB;

    @FXML
    private TableColumn<Driver, Integer> columnID;

    @FXML
    private TableColumn<Driver, String> columnName;

    @FXML
    private TableColumn<Driver, String> columnTeam;

    //***************************************
    //*************TEXTFIELDEK***************
    @FXML
    private TextField tfCountry;

    @FXML
    private TextField tfDOB;

    @FXML
    private TextField tfID;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfTeam;
    
    @FXML
    private TextField tfNumOfDrivers;
    
    @FXML
    private TextField tfSearchDriver;
    
    //****************************************
    //************TF KITÖLTÕ******************
    
    @FXML
    private void handleMouseAction(MouseEvent event) {
    	try {
    		Driver driver = tableviewOfDrivers.getSelectionModel().getSelectedItem();
        	tfID.setText("" + driver.getId());
        	tfName.setText(driver.getName());
        	tfDOB.setText(""+driver.getDob());
        	tfCountry.setText(driver.getCountry());
        	tfTeam.setText(driver.getTeam());
    	} catch (Exception e) {
    		
    	}
    	
    }
    
    //**********************************************
    //**********GOMBOK VEZÉRLÉSE********************
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
    	if(event.getSource() == btnNew) {
    		newDriver();
    	} else if (event.getSource() == btnDelete ) {
    		deleteDriver();
    	} else if (event.getSource() == btnUpdate) {
    		updateDriver();
    	} else if (event.getSource() == btnToTracks) {
    		toTrackWindow(event);
    	} else if (event.getSource() == btnClear) {
    		clearFields();
    	}
    }
    
    
    
  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	showDriverData();    	
   }

    //******************************************************
    //*******************Adat beolvasás*********************
    
    public ObservableList<Driver> getDriversList(){
    	
    	ObservableList<Driver> driverData = FXCollections.observableArrayList();
    	
    	try {    		     
    		BufferedReader csvReader = new BufferedReader(new FileReader("src/racers.csv"));
    		String line = csvReader.readLine();
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    		Driver driver;
    		
    		while(line != null) {
    			String[] st = line.split(";");
    			driver = new Driver(Integer.parseInt(st[0]), st[1], sdf.parse(st[2]), st[3], st[4]);
    			driverData.add(driver);
    			line = csvReader.readLine();    			
    		}
    		
    		csvReader.close();	
    		
    	} catch(IOException ioe) {
    		Alert.displayAlert("Error", "Error with the file.");
    	} catch (NumberFormatException e) {
			Alert.displayAlert("Error", "Number format exception.");
			System.out.println(e);
		} catch (ParseException e) {
			Alert.displayAlert("Error", "Parse exception.");
			System.out.println(e);
		}
    	
    	return driverData;
    	
    }
    
    //*************************************************************
    //*********************ADAT ÁBRÁZOLÁS**************************
    
    public void showDriverData() {
    	
    	ObservableList<Driver> list = getDriversList();
    	
    	columnID.setCellValueFactory(new PropertyValueFactory<Driver, Integer>("Id"));
    	//columnID.setCellValueFactory(data -> data.getValue().getId()); EZ MÉR NEM JÓ?!?!?!
    	columnName.setCellValueFactory(new PropertyValueFactory<Driver, String>("Name"));
    	columnDOB.setCellValueFactory(new PropertyValueFactory<Driver, Date>("Dob"));
    	columnCountry.setCellValueFactory(new PropertyValueFactory<Driver, String>("Country"));
    	columnTeam.setCellValueFactory(new PropertyValueFactory<Driver, String>("Team"));
    	
    	tableviewOfDrivers.setItems(list);
    	tfNumOfDrivers.setText(""+list.size());
    	
    	//**************************************************************
    	//***************************KERESÉS****************************
    	FilteredList<Driver> filteredDrivers = new FilteredList<>(list, b -> true);
    	tfSearchDriver.textProperty().addListener((observable, oldValue, newValue) -> {
    		filteredDrivers.setPredicate(Driver -> {
    			if(newValue.isEmpty() || newValue.isBlank() || newValue == null) {
    				return true;
    			}
    			
    			String searchKeyword = newValue.toLowerCase();
    			if(Driver.getName().toLowerCase().indexOf(searchKeyword) > -1) {
    				return true;
    			} else {
    				return false;
    			}
    			
    		});
    	});
    	
    	SortedList<Driver> sortedData = new SortedList <>(filteredDrivers);
    	tableviewOfDrivers.setItems(sortedData);
    	sortedData.comparatorProperty().bind(tableviewOfDrivers.comparatorProperty());
    }
    
    //***************************************************************
    //**************************ÚJ BEJEGYZÉS*************************
    
    private void newDriver() {
    	
    	
    	if(isInt(tfID.getText()) == false) {    		
    		Alert.displayAlert("Error!", "Wrong ID input!");   		
    	} else if (checkID(getDriversList(), tfID.getText() )) {
    		Alert.displayAlert("Error", "ID Already exists!");
    	} else if (isName(tfName.getText()) == false) {    		
    		Alert.displayAlert("Error!", "Wrong Name input!");	
    	} else if (isValidDate(tfDOB.getText()) == false) {
    		Alert.displayAlert("Error!", "Wrong Date input!");
    	} else if (isName(tfCountry.getText()) == false) {
    		Alert.displayAlert("Error!", "Wrong Country input!");
    	} else if (isName(tfTeam.getText()) == false) {
    		Alert.displayAlert("Error", "Wrong Team input!");
    	} else {
    		String driver = tfID.getText() + ";" + tfName.getText() + ";" + tfDOB.getText() + ";" + tfCountry.getText() + ";" + tfTeam.getText();   	     
    	        	try {
    	        		PrintStream out = new PrintStream(new FileOutputStream("src/racers.csv", true));
    	        		out.println(driver);
    	        		out.close();
    	        	} catch (IOException ioe) {
    	        		Alert.displayAlert("Error!", "Something went wrong while writing to your file.");;
    	        	}
    	}
    	showDriverData();
    }
    
    
    //*******************************************************************
    //************************MÓDOSÍTÁS**********************************
    
    private void updateDriver() {
    	String tempFile = "src/temp.csv";
    	String file = "src/racers.csv";
    	File oldFile = new File(file);
    	File newFile = new File(tempFile);
    	String currentLine;
    	String data[];

    	if(isInt(tfID.getText()) == false) {    		
    		Alert.displayAlert("Error!", "Wrong ID input!");   		
    	} else if (isName(tfName.getText()) == false) {    		
    		Alert.displayAlert("Error!", "Wrong Name input!");	
    	} else if (isValidDate(tfDOB.getText()) == false) {
    		Alert.displayAlert("Error!", "Wrong Date input!");
    	} else if (isName(tfCountry.getText()) == false) {
    		Alert.displayAlert("Error!", "Wrong Country input!");
    	} else if (isName(tfTeam.getText()) == false) {
    		Alert.displayAlert("Error", "Wrong Team input!");
    	} else {

    		try {
    			FileWriter fw = new FileWriter(tempFile, true);
    			BufferedWriter bw = new BufferedWriter(fw);
    			PrintWriter pw = new PrintWriter(bw);
    			FileReader fr = new FileReader(file);
    			BufferedReader br = new BufferedReader(fr);

    			while((currentLine = br.readLine()) != null) {
    				data = currentLine.split(";");
    				if(!(data[0].equalsIgnoreCase(tfID.getText()))) {
    					pw.println(currentLine);
    				} else {
    					data[1] = tfName.getText();
    					data[2] = tfDOB.getText();
    					data[3] = tfCountry.getText();
    					data[4] = tfTeam.getText();
    					pw.println(data[0] + ";" + data[1] + ";" + data[2] + ";" + data[3] + ";" + data[4]);
    				}
    			}

    			pw.flush();
    			pw.close();
    			fr.close();
    			br.close();
    			bw.close();
    			fw.close();

    			oldFile.delete();
    			File dump = new File(file);
    			newFile.renameTo(dump);
    		} catch (IOException ioe){

    		}
    		showDriverData();
    	}}
    
    //***************************************************************
    //*************************TÖRLÉS********************************
    
    private void deleteDriver() {
    	String tempFile = "src/temp.csv";
    	String file = "src/racers.csv";
    	File oldFile = new File(file);
    	File newFile = new File(tempFile);
    	String currentLine;
    	String data[];

    	if(tfID.getText().equals("")) {
    		Alert.displayAlert("Error", "Select a driver!");
    	} else {

    		try {
    			FileWriter fw = new FileWriter(tempFile, true);
    			BufferedWriter bw = new BufferedWriter(fw);
    			PrintWriter pw = new PrintWriter(bw);
    			FileReader fr = new FileReader(file);
    			BufferedReader br = new BufferedReader(fr);

    			while((currentLine = br.readLine()) != null) {
    				data = currentLine.split(";");
    				if(!(data[0].equalsIgnoreCase(tfID.getText()))) {
    					pw.println(currentLine);
    				}
    			}

    			pw.flush();
    			pw.close();
    			fr.close();
    			br.close();
    			bw.close();
    			fw.close();

    			oldFile.delete();
    			File dump = new File(file);
    			newFile.renameTo(dump);


    		} catch (IOException ioe) {
    			Alert.displayAlert("Error!", "Error while deleting driver.");
    		}
    		showDriverData();
    	}}
    
    //************************************************
    //******************MEZÕK ÜRÍTÉSE*****************
    
    public void clearFields() {
    	tfID.clear();
    	tfName.clear();
    	tfDOB.clear();
    	tfCountry.clear();
    	tfTeam.clear();
    }
    
    //*************************************************
    //******************ABLAKVÁLTÁS********************
    
    public void toTrackWindow(Event event) {
    	try {
			Parent root = FXMLLoader.load(getClass().getResource("/application/TrackScene.fxml"));
    		Scene TrackScene = new Scene(root);
    		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
    		window.setScene(TrackScene);
    		window.show();
		} catch (IOException e){
		System.out.println(e);
	}
    }
    
    //*************************************************
    //***************SZÁMCSEKK*************************
    
    private boolean isInt(String s) {
    	try {
    		int number = Integer.parseInt(s);
    		return true;
    	} catch (NumberFormatException e) {
    		return false;
    	}
    	
    }
    
    //*************************************************
    //****************SZTRINGCSEKK*********************
       
    private boolean isName(String s) {
    	return Pattern.matches("^[\\p{L} .'-]+$", s);
    }
    
    //*************************************************
    //*****************DÁTUMCSEKK**********************
    
    private boolean isValidDate(String s) {
    	return s.matches("(\\d{4})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])");
    }
    
    //*************************************************
    //*****************ID-CSEKK************************
    
    private boolean checkID(ObservableList<Driver> list ,String s) {
    	boolean taken = false;
    	for(Driver d : list) {
    		if(d.getId() == Integer.parseInt(s)) {
    			taken = true;
    		}
    	}
    	return taken;
    }
    
    

}