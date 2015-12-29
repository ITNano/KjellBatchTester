package model;
import java.io.File;
import java.io.FileNotFoundException;


public class Main {
	
	private static final String SOURCE_FILE = "data/log.txt";

	public static void main(String[] args){
		File source = new File(SOURCE_FILE);
		TestRun test = null;
		try {
			test = TestRun.createTestRun(source);
		} catch (FileNotFoundException e) {
			System.out.println("Could not find source file at '"+SOURCE_FILE+"'. Closing...");
			System.exit(1);
		}
		
		System.out.println("Loaded happenings. Printing...");
		for(Happening h : test.getHappenings()){
			System.out.println(h.toString());
		}
	}
	
}
