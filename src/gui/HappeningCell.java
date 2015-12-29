package gui;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import model.Happening;
import model.Task;

public class HappeningCell extends ListCell<Happening>{
	
	private VBox box;
	private Label type;
	private Label taskInfo;
	
	public HappeningCell(){
		box = new VBox();
		type = new Label();
		taskInfo = new Label();
		box.getChildren().add(type);
		box.getChildren().add(taskInfo);
	}
	
	 @Override
     protected void updateItem(Happening item, boolean empty) {
         super.updateItem(item, empty);
         setText(null);  // No text in label of super class
         if (empty) {
             setGraphic(null);
         } else {
             type.setText(item!=null ? item.getType().toString() : "<null>");
             Task t = item.getTask();
             taskInfo.setText("TASK "+t.getID()+" ("+t.getPriority()+" PRIO, "+t.getDirection()+")");
             setGraphic(box);
         }
     }
	 
}
