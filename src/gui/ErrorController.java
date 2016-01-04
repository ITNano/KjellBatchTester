package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ErrorController {
	
	private boolean closed = false;

	@FXML
	private Label errorLabel;
	
	public void setErrorMessage(String errorMessage){
		if(errorLabel != null){
			errorLabel.setText(errorMessage);
		}
	}
	
	public void close(){
		if(!closed){
			if(errorLabel.getScene() != null && errorLabel.getScene().getWindow() != null){
				((Stage)errorLabel.getScene().getWindow()).close();
			}
			closed = true;
		}
	}
	
}
