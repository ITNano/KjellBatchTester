package gui;

import util.Log;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GUIMain extends Application{
	
	private static Application currentApplication;
	
	public GUIMain(){
		super();
		currentApplication = this;
	}

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Fleetspeak server");
        primaryStage.getIcons().add(ImageLoader.loadImage("logo.gif"));
        primaryStage.setOnCloseRequest((WindowEvent event) -> {
        	System.out.println("Closing...");
        });
        
        FXUtil.switchLayout(primaryStage, "main");
    }

    public static Application getCurrentApplication(){
    	return currentApplication;
    }
    
    public static void main(String[] args) {
    	Log.start();
    	launch();
    }
}
