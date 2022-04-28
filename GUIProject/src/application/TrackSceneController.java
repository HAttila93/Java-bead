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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class TrackSceneController implements Initializable {

	
		//**************GOMBOK*********************
	 	@FXML
	    private Button btnDeleteTrack;

	    @FXML
	    private Button btnNewTrack;

	    @FXML
	    private Button btnUpdateTrack;
	    
	    @FXML
	    private Button btnDrivers;
	    
	    
	    //**************TÁBLÁZAT************************
	    @FXML
	    private TableView<Track> tableviewOfTracks;

	    @FXML
	    private TableColumn<Track, String> columnTrackCountry;

	    @FXML
	    private TableColumn<Track, Integer> columnTrackID;

	    @FXML
	    private TableColumn<Track, Integer> columnTrackLength;

	    @FXML
	    private TableColumn<Track, String> columnTrackName;

	   // @FXML
	    //private Label trackID;
	    
	    //**************TEXTFIELDEK*******************

	    @FXML
	    private TextField tracktfCountry;

	    @FXML
	    private TextField tracktfID;

	    @FXML
	    private TextField tracktfLength;

	    @FXML
	    private TextField tracktfName;
	    
	    @FXML
	    private TextField tracktfAvgLength;
	    
	  //************TF KITÖLTÕ******************
	    @FXML
	    private void handleMouseAction(MouseEvent event) {
	    	try {
	    		Track track = tableviewOfTracks.getSelectionModel().getSelectedItem();
	        	tracktfID.setText("" + track.getId());
	        	tracktfName.setText(track.getName());
	        	tracktfLength.setText(""+track.getLength());
	        	tracktfCountry.setText(track.getCountry());
	        	
	    	} catch (Exception e) {
	    		
	    	}
	    	
	    }

	    //***********GOMBVEZÉRLÕ**************************
	    @FXML
	    void handleButtonAction2(ActionEvent event) {
	    	if(event.getSource() == btnDrivers) {
	    		try {
	    			Parent root = FXMLLoader.load(getClass().getResource("/application/Main.fxml"));
	        		Scene DriverScene = new Scene(root);
	        		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
	        		window.setScene(DriverScene);
	        		window.show();
	    		} catch (IOException e) {
	    			System.out.println(e);
	    		}
	    	} else if(event.getSource() == btnNewTrack) {
	    		newTrack();
	    	} else if(event.getSource() == btnDeleteTrack) {
	    		deleteTrack();
	    	} else if(event.getSource() == btnUpdateTrack) {
	    		updateTrack();
	    	}
	    }
	    
	    @Override
	    public void initialize(URL url, ResourceBundle rb) {
	    	showTrackData();
	    	
	   }
	    
	    
	    public ObservableList<Track> getTrackList(){
	    	
	    	ObservableList<Track> trackData = FXCollections.observableArrayList();
	    	
	    	try {
	    		BufferedReader txtReader = new BufferedReader(new FileReader("src/application/tracks.txt"));
	    		String line = txtReader.readLine();
	    		Track track;
	    		
	    		while(line != null) {
	    			String[] st = line.split("-");
	    			track = new Track(Integer.parseInt(st[0]), st[1],(st[2]), Integer.parseInt(st[3]));
	    			trackData.add(track);
	    			line = txtReader.readLine();	
	    		}
	    		txtReader.close();		
	    	} catch(IOException ioe) {
	    		
	    		System.out.println(ioe);
	    	} catch (NumberFormatException e) {
				e.printStackTrace();
			}
	    	
	    	return trackData;
	    	
	    }
	    
	    public void showTrackData() {
	    	
	    	ObservableList<Track> list = getTrackList();
	    	
	    	
	    	columnTrackID.setCellValueFactory(new PropertyValueFactory<Track, Integer>("Id"));
	    	columnTrackName.setCellValueFactory(new PropertyValueFactory<Track, String>("Name"));
	    	columnTrackCountry.setCellValueFactory(new PropertyValueFactory<Track, String>("Country"));
	    	columnTrackLength.setCellValueFactory(new PropertyValueFactory<Track, Integer>("Length"));
	   	
	    	tableviewOfTracks.setItems(list);  	
	    	
	    	int sumOfLength = 0;
	    	for(Track t : list) {
	    		sumOfLength += t.getLength();
	    	}
	    	tracktfAvgLength.setText(""+sumOfLength/list.size());
	    }
	    
	    private void newTrack() {
	    	
	    	if(isInt(tracktfID.getText()) == false) {    		
	    		Alert.displayAlert("Error!", "Wrong ID input!");   		
	    	} else if (checkID(getTrackList(), tracktfID.getText() )) {
	    		Alert.displayAlert("Error", "ID Already exists!");
	    	} else if (isName(tracktfName.getText()) == false) {    		
	    		Alert.displayAlert("Error!", "Wrong Name input!");	
	    	} else if (isName(tracktfCountry.getText()) == false) {
	    		Alert.displayAlert("Error!", "Wrong Country input!");
	    	} else if (isInt(tracktfLength.getText()) == false) {
	    		Alert.displayAlert("Error!", "Wrong Length input!");
	    	} else {
	    	
	    	String track = tracktfID.getText() + "-" + tracktfName.getText() + "-" + tracktfCountry.getText() + "-" + tracktfLength.getText();
	  
	    	
	    	try {
	    		PrintStream out = new PrintStream(new FileOutputStream("src/application/tracks.txt", true));
	    		
	    		out.println(track);
	    		out.close();
	    	} catch (IOException ioe) {
	    		Alert.displayAlert("Error!", "Something went wrong while writing the file!");;
	    	}
	    	showTrackData();
	    }
	    }
	    
	    private void deleteTrack() {
	    	String tempFile = "src/application/temp.txt";
	    	String file = "src/application/tracks.txt";
	    	File oldFile = new File(file);
	    	File newFile = new File(tempFile);
	    	String currentLine;
	    	String data[];

	    	if(tracktfID.getText().equals("")) {
	    		Alert.displayAlert("Error", "Select a track!");
	    	} else {

	    		try {
	    			FileWriter fw = new FileWriter(tempFile, true);
	    			BufferedWriter bw = new BufferedWriter(fw);
	    			PrintWriter pw = new PrintWriter(bw);
	    			FileReader fr = new FileReader(file);
	    			BufferedReader br = new BufferedReader(fr);

	    			while((currentLine = br.readLine()) != null) {
	    				data = currentLine.split("-");
	    				if(!(data[0].equalsIgnoreCase(tracktfID.getText()))) {
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
	    		showTrackData();
	    	}}
	    
	    
	    private void updateTrack() {
	    	String tempFile = "src/application/temp.txt";
	    	String file = "src/application/tracks.txt";
	    	File oldFile = new File(file);
	    	File newFile = new File(tempFile);
	    	String currentLine;
	    	String data[];

	    	if(isInt(tracktfID.getText()) == false) {    		
	    		Alert.displayAlert("Error!", "Wrong ID input!");   		
	    	} else if (isName(tracktfName.getText()) == false) {    		
	    		Alert.displayAlert("Error!", "Wrong Name input!");	
	    	} else if (isName(tracktfCountry.getText()) == false) {
	    		Alert.displayAlert("Error!", "Wrong Country input!");
	    	} else if (isInt(tracktfLength.getText()) == false) {
	    		Alert.displayAlert("Error", "Wrong Length input!");
	    	} else {

	    		try {
	    			FileWriter fw = new FileWriter(tempFile, true);
	    			BufferedWriter bw = new BufferedWriter(fw);
	    			PrintWriter pw = new PrintWriter(bw);
	    			FileReader fr = new FileReader(file);
	    			BufferedReader br = new BufferedReader(fr);

	    			while((currentLine = br.readLine()) != null) {
	    				data = currentLine.split("-");
	    				if(!(data[0].equalsIgnoreCase(tracktfID.getText()))) {
	    					pw.println(currentLine);
	    				} else {
	    					data[1] = tracktfName.getText();
	    					data[2] = tracktfCountry.getText();
	    					data[3] = tracktfLength.getText();
	    					
	    					pw.println(data[0] + "-" + data[1] + "-" + data[2] + "-" + data[3]);
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
	    		showTrackData();
	    	}}
	    
	    
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
	    //*****************ID-CSEKK************************
	    
	    private boolean checkID(ObservableList<Track> list ,String s) {
	    	boolean taken = false;
	    	for(Track d : list) {
	    		if(d.getId() == Integer.parseInt(s)) {
	    			taken = true;
	    		}
	    	}
	    	return taken;
	    }
	
}
