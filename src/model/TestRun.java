package model;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


public class TestRun {

	private List<Happening> happenings;
	
	private TestRun(List<Happening> happenings){
		this.happenings = happenings;
	}
	
	public List<Happening> getHappenings(){
		return Collections.unmodifiableList(this.happenings);
	}
	
	public static TestRun createTestRun(File sourceFile) throws FileNotFoundException{
		return new TestRun(loadTestRun(sourceFile));
	}
	
	private static List<Happening> loadTestRun(File input) throws FileNotFoundException{
		List<Happening> happenings = new LinkedList<Happening>();
		Scanner sc = new Scanner(input);
		Scanner innerScanner;
		String line;
		int lineCount = 0;
		int skipCount = 0;
		while(sc.hasNextLine()){
			line = sc.nextLine();
			lineCount++;
			if(!line.trim().isEmpty()){
				try{
					innerScanner = new Scanner(line);
					int orderNum = innerScanner.nextInt();
					HappeningType type = HappeningType.getType(innerScanner.next());
					Task t = null;
					if(type == HappeningType.ARRIVE){
						t = new Task(innerScanner.nextInt(), innerScanner.nextInt(), innerScanner.nextInt());
					}else{
						t = Task.findTask(innerScanner.nextInt());
					}
					
					if(type != HappeningType.LEFT){
						Happening h = new Happening(orderNum-skipCount, type, t);
						happenings.add(h);
					}else{
						skipCount++;
					}
					innerScanner.close();
				}catch(InputMismatchException ime){
					System.out.println("Invalid data on line "+lineCount+": '"+line+"'");
				}
			}
		}
		sc.close();
		
		Collections.sort(happenings);
		return happenings;
	}
	
}
