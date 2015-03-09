/**
 * Class to call methods within each command to achieve functionality. Interface between front and back end.
 * Contains methods commonly used by individual command classes.
 * Still under development
 * Bugs: none known
 *
 */
//@author A0097582N

package taskie.controller;

import java.time.LocalDate;
import java.time.LocalTime;

import taskie.*;
import taskie.commands.ICommand;
import taskie.models.Task;

public class Controller {
	
	private static final String DEADLINED_TASKNAME = "deadlined";
	private static final String TIMED_TASKNAME = "timed";
	private static final String FLOATING_TASKNAME = "floating";
	public Controller() {
		
	}
	
	public void run(){
			boolean isRunning = true;
			Taskie.UI.printWelcomeMessage();
			while (isRunning) {
				String string = Taskie.UI.readInput();
				Taskie.Parser.parse(string);
				
			}
	}

	public void executeCommand(ICommand command) {
		command.execute();
		

	}
	public String formatTime(LocalDate startDate, LocalTime startTime) {
		String string = "";
		if(startDate!=null){
		string = string.concat(startDate.toString());
		}
		
		if(startTime!=null){
		string = string.concat(" " + startTime.getHour() + " "
				+ startTime.getMinute());
		}
		return string;
	}
	
	public String determineTaskType(Task task) {
		if (task.getStartDate() == null && task.getEndDate() == null) {
			return FLOATING_TASKNAME;
		} else if (task.getStartDate() == null && task.getEndDate() != null) {
			return DEADLINED_TASKNAME;
		} else {
			return TIMED_TASKNAME;
		}
	}

}