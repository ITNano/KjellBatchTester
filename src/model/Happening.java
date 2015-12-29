package model;

public class Happening implements Comparable<Happening>{

	private int orderNum;
	private HappeningType type;
	private Task task;
	
	public Happening(int orderNum, HappeningType type, Task task){
		this.orderNum = orderNum;
		this.type = type;
		this.task = task;
	}
	
	public int getOrderNum(){
		return orderNum;
	}
	
	public HappeningType getType(){
		return type;
	}
	
	public Task getTask(){
		return task;
	}

	@Override
	public int compareTo(Happening h2) {
		return this.orderNum-h2.orderNum;			//Lower orderNum --> earlier
	}
	
	@Override
	public String toString(){
		return "Happening:{id:"+orderNum+",type="+type.toString()+",task:"+task+"}";
	}
}
