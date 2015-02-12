package taskie;

public interface Command {
	String keyword=null;
	String details=null; 
	
	public String getKeyword();
	public String getDetails();
}
