package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ErrorController {

	@FXML
	private Label errorLabel;
	
	public void setErrorMessage(String errorMessage){
		if(errorLabel != null){
			errorLabel.setText(errorMessage);
		}
	}
	
	public void close(){
		((Stage)errorLabel.getScene().getWindow()).close();
	}
	
}
