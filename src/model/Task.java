package model;
import java.util.LinkedList;
import java.util.List;


public class Task implements Comparable<Task>{
	
	private static List<Task> tasks = new LinkedList<Task>();

	private Priority priority;
	private Direction direction;
	private int id;
	
	public Task(int id, int prio, int dir){
		this(id, prio==0?Priority.NORMAL:Priority.HIGH, dir==0?Direction.DOWN:Direction.UP);
	}
	
	public Task(int id, Priority prio, Direction dir){
		this.priority = prio;
		this.direction = dir;
		this.id = id;
		tasks.add(this);
	}
	
	public Priority getPriority(){
		return priority;
	}
	
	public Direction getDirection(){
		return direction;
	}
	
	public int getID(){
		return id;
	}

	@Override
	public boolean equals(Object obj){
		return obj instanceof Task && ((Task)obj).getID() == id;
	}
	
	@Override
	public int compareTo(Task t2) {
		return this.getID()-t2.getID();		//Smaller ID --> first in list
	}
	
	@Override
	public String toString(){
		return "Task:{id:"+id+", prio:"+priority.toString()+", dir:"+direction.toString()+"}";
	}
	
	public static Task findTask(int id){
		for(Task t : tasks){
			if(t.getID() == id){
				return t;
			}
		}
		
		return null;
	}
	
	public enum Priority{
		HIGH(1),
		NORMAL(0);
		
		private int value;
		private Priority(int value){
			this.value = value;
		}
		
		public int getIntegerValue(){
			return this.value;
		}
	}
	
	public enum Direction{
		UP(1),
		DOWN(0),
		NONE(-1);
		
		private int value;
		private Direction(int value){
			this.value = value;
		}
		
		public int getIntegerValue(){
			return this.value;
		}
	}
}
