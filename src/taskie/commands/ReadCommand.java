package taskie.commands;

import java.util.Calendar;

public class ReadCommand implements ICommand {
	private Calendar _readDate;
	
	public ReadCommand(){
		_readDate=null;
	}
	
	public ReadCommand(Calendar readDate){
		_readDate=readDate;
	}
	
	public void setReadDate(Calendar readDate){
		_readDate=readDate;
	}
	
	//return null if no read date is specified.
	public Calendar getReadDate(){
		return _readDate;
	}
}
