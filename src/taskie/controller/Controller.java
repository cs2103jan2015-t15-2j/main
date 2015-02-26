/**
 * Class to perform tasks needed to achieve functionality. Interface between front and back end.
 * Still under development
 * Bugs: none known
 *
 */
//@author       A0097582N

package taskie.controller;

import java.time.LocalDate;
import java.time.LocalTime;

import taskie.*;
import taskie.commands.ICommand;

public class Controller {

	public Controller() {
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
		

}