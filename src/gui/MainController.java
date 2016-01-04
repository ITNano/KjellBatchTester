package gui;

import gui.FXUtil.Function;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import javafx.util.converter.BooleanStringConverter;
import javafx.util.converter.NumberStringConverter;

import javax.swing.Timer;

import model.Happening;
import model.Task;
import model.Task.Direction;
import model.TestRun;

public class MainController {

	private static final String SOURCE_FILE = "data/log.txt";
	
	@FXML
	private AnchorPane root;
	
	//Side menu
	@FXML
	private ListView<Happening> taskList;
	@FXML
	private ImageView fastForwardButton;
	
	//Counters
	@FXML
	private Label count_lowprio_up;
	@FXML
	private Label count_highprio_up;
	@FXML
	private Label count_lowprio_down;
	@FXML
	private Label count_highprio_down;
	
	//Information
	@FXML
	private Label info_curr_dir;
	@FXML
	private Label info_bus_count;
	@FXML
	private Label info_buses_empty;
	
	//Data storage
	private List<Happening> happenings;
	private int currentPosition = 0;
	private Timer fastForwardTimer;
	private ErrorController errorController;
	
	//Other graphical variables.
	private Bus[] busses;
	private IntegerProperty[][] queueCount;
	private StringProperty currentDirection;
	private IntegerProperty busesUsed;
	
	public void initialize(){
		//Init data
		try {
			TestRun testRun = TestRun.createTestRun(new File(SOURCE_FILE));
			happenings = testRun.getHappenings();
		} catch (FileNotFoundException e) {
			System.out.println("Failed to retrive any data. Closing...");
			System.exit(2);
		}
		
		//Init side menu
		taskList.setCellFactory(new Callback<ListView<Happening>, ListCell<Happening>>() {
            @Override
            public ListCell<Happening> call(ListView<Happening> param) {
                return new HappeningCell();
            }
        });
		taskList.setItems(FXCollections.observableArrayList(happenings));
		taskList.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent me) {
				Happening h = taskList.getSelectionModel().getSelectedItem();
				if(h != null){
					int stepReached = gotoStep(h.getOrderNum());
					currentPosition = stepReached+1;
					if(stepReached != h.getOrderNum()){
						taskList.getSelectionModel().select(stepReached);
						taskList.scrollTo(stepReached);
					}
				}
			}
		});
		
		//Init busses
		busses = new Bus[3];
		for(int i = 0; i<busses.length; i++){
			busses[i] = new Bus(root, "bus"+(i+1));
		}
		
		//Init counters
		queueCount = new IntegerProperty[2][2];
		for(int i = 0; i<queueCount.length; i++){
			for(int j = 0; j<queueCount[i].length; j++){
				queueCount[i][j] = new SimpleIntegerProperty(0);
			}
		}
		count_lowprio_down.textProperty().bindBidirectional(queueCount[0][0], new NumberStringConverter());
		count_highprio_down.textProperty().bindBidirectional(queueCount[1][0], new NumberStringConverter());
		count_lowprio_up.textProperty().bindBidirectional(queueCount[0][1], new NumberStringConverter());
		count_highprio_up.textProperty().bindBidirectional(queueCount[1][1], new NumberStringConverter());
		
		//Init info
		currentDirection = new SimpleStringProperty(Direction.NONE.toString());
		info_curr_dir.textProperty().bind(currentDirection);
		busesUsed = new SimpleIntegerProperty(0);
		info_bus_count.textProperty().bindBidirectional(busesUsed, new NumberStringConverter());
		BooleanProperty busesEmpty = new SimpleBooleanProperty();
		busesEmpty.bind(busesUsed.isEqualTo(0));
		busesEmpty.addListener(new ChangeListener<Boolean>(){

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0,
					Boolean arg1, Boolean arg2) {
				//Do nothing at this time. Perhaps more later.
			}
			
		});
		info_buses_empty.textProperty().bindBidirectional(busesEmpty, new BooleanStringConverter());
	}
	
	public void fastForward(){
		if(fastForwardTimer == null){
			fastForwardTimer = new Timer(100, new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					Platform.runLater(() -> {
						if(currentPosition<happenings.size() && fastForwardTimer != null){
							if(!next()){
								fastForwardTimer.stop();
								fastForwardTimer = null;
								fastForwardButton.setImage(ImageLoader.loadImage("forward.png"));
							}
						}else{
							if(fastForwardTimer.isRunning()){
								fastForwardTimer.stop();
							}
						}
					});
				}
			});
			fastForwardTimer.setRepeats(true);
			fastForwardTimer.start();
			fastForwardButton.setImage(ImageLoader.loadImage("stopButton.png"));
		}else{
			fastForwardTimer.stop();
			fastForwardTimer = null;
			fastForwardButton.setImage(ImageLoader.loadImage("forward.png"));
		}
	}
	
	public boolean prev(){
		if(currentPosition>0){
			int stepReached = gotoStep(currentPosition-2);
			if(stepReached == currentPosition-2){
				currentPosition--;
				taskList.getSelectionModel().selectPrevious();
				taskList.scrollTo(taskList.getSelectionModel().getSelectedIndex());
				if(currentPosition == 0){
					taskList.getSelectionModel().clearSelection();
				}
				return true;
			}
		}
		
		return false;
	}
	
	public boolean next(){
		if(currentPosition<happenings.size()){
			int stepReached = gotoStep(currentPosition);
			if(stepReached == currentPosition){
				currentPosition++;
				taskList.getSelectionModel().selectNext();
				taskList.scrollTo(taskList.getSelectionModel().getSelectedIndex());
				return true;
			}
		}
		
		return false;
	}
	
	public int gotoStep(int step){
		//Clear all stuff.
		for(int i = 0; i<queueCount.length; i++){
			for(int j = 0; j<queueCount[i].length; j++){
				queueCount[i][j].set(0);
			}
		}
		clearBuses();
		busesUsed.set(0);
		currentDirection.set(Direction.NONE.toString());
		
		System.out.println("Going to step "+step);
		
		//Recalculate.
		for(int i = 0; i<=step&&i<happenings.size(); i++){
			Happening h = happenings.get(i);
			switch(h.getType()){
				case ARRIVE:	IntegerProperty p = queueCount[h.getTask().getPriority().getIntegerValue()]
															  [h.getTask().getDirection().getIntegerValue()];
								p.set(p.get()+1);
								break;
				case TRANSFER:	Bus freeBus = null;
								try{
									freeBus = getFreeBus();
									freeBus.enterBus(h.getTask());
								}catch(IllegalStateException ise){
									showError("Got an error during transfer: "+ise.getMessage()+". Invalid action selected");
									return i;
								}
								
								if(busesUsed.get()==0){
									currentDirection.set(h.getTask().getDirection().toString());
								}
								busesUsed.set(busesUsed.get()+1);
								IntegerProperty p2 = queueCount[h.getTask().getPriority().getIntegerValue()]
										  					  [h.getTask().getDirection().getIntegerValue()];
								p2.set(p2.get()-1);
								break;
				case LEAVE:		Bus bus = null;
								try{
									bus = getBus(h.getTask());
									bus.leaveBus(h.getTask());
								}catch(IllegalStateException ise){
									showError("Got an error during leave: "+ise.getMessage()+". Invalid action selected");
									return i;
								}
								
								busesUsed.set(busesUsed.get()-1);
								if(busesUsed.get()==0){
									currentDirection.set(Direction.NONE.toString());
								}
								break;
				case LEFT:		break;
			}
		}
		
		return step;
	}
	
	private void showError(String msg){
		if(errorController != null){
			errorController.close();
		}
		
		System.out.println(msg);
		FXUtil.showModalWindow(root.getScene().getWindow(), (Parent)FXUtil.getNode("error", new Function(){
			@Override
			public <T> void perform(T controller) {
				if(controller instanceof ErrorController){
					errorController = (ErrorController)controller;
					errorController.setErrorMessage(msg);
				}
			}
		}), "Oops, an error occured!", ImageLoader.loadImage("error.png"));
	}
	
	private Bus getFreeBus() throws IllegalStateException{
		for(Bus b : busses){
			if(b.isEmpty()){
				return b;
			}
		}
		
		throw new IllegalStateException("No bus is free!");
	}
	
	private Bus getBus(Task task) throws IllegalStateException{
		for(Bus b : busses){
			if(b.hasTask(task)){
				return b;
			}
		}
		
		throw new IllegalStateException("Trying to fetch bus of task that has not entered yet.");
	}
	
	private void clearBuses(){
		for(Bus b : busses){
			b.clear();
		}
	}

	
	
	
	public class Bus{
		
		private Task currentTask;
		private Pane rootPane;
		private Label idLabel;
		private Label prioLabel;
		private Label dirLabel;
		private Label emptyLabel;
		
		public Bus(Node view, String name){
			idLabel = (Label)view.lookup("#"+name+"_id");
			prioLabel = (Label)view.lookup("#"+name+"_prio");
			dirLabel = (Label)view.lookup("#"+name+"_dir");
			emptyLabel = (Label)view.lookup("#"+name+"_empty");
			rootPane = (Pane)idLabel.getParent();
		}
		
		public boolean isEmpty(){
			return emptyLabel.isVisible();
		}
		
		public boolean hasTask(Task task){
			return currentTask != null && currentTask.equals(task);
		}
		
		public void enterBus(Task task){
			if(isEmpty() && task != null){
				emptyLabel.setVisible(false);
				idLabel.setText(task.getID()+"");
				prioLabel.setText(task.getPriority().toString());
				dirLabel.setText(task.getDirection().toString());
				currentTask = task;
				FXUtil.replaceClassNames(rootPane, "nonfree", "free", "pending");
			}else{
				throw new IllegalStateException("Cannot enter bus when occupied!");
			}
		}
		
		public void prepareLeave(Task task){
			if(hasTask(task)){
				FXUtil.replaceClassNames(rootPane, "pending", "nonfree", "free");
			}
		}
		
		public void leaveBus(Task task){
			if(task != null && hasTask(task)){
				FXUtil.replaceClassNames(rootPane, "free", "nonfree", "pending");
				emptyLabel.setVisible(true);
				currentTask = null;
			}else{
				if(!(currentTask == null && task == null)){
					throw new IllegalStateException("Cannot leave bus since it is not occupied by the given task!");
				}
			}
		}
		
		public void clear(){
			leaveBus(currentTask);
		}
		
	}
}
