package taskie.commands;

public interface ICommand {
	String _keyword=null;
	String _details=null; 
	
	public String getKeyword();
	public String getDetails();
}
