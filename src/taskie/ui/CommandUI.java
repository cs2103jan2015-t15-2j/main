//@author A0121555M
package taskie.ui;

import static org.fusesource.jansi.Ansi.ansi;

import java.io.File;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;

import javax.swing.JFileChooser;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Color;
import org.fusesource.jansi.AnsiConsole;

import taskie.Controller;
import taskie.commands.ViewCommand;
import taskie.exceptions.InvalidTaskException;
import taskie.models.DisplayType;
import taskie.models.Messages;
import taskie.models.Task;
import taskie.models.TaskType;
import taskie.models.ViewType;

public class CommandUI implements UI {
	private Controller _controller;
	private Scanner _scanner;
	private Task[] _currentTaskList;
	private static boolean _isInitialized;
	private static boolean _isUIRunning;
	private LocalDateTime _now;

	public CommandUI(Controller controller) {
		_controller = controller;
		_scanner = new Scanner(System.in);
		_isInitialized = false;
		_now = LocalDateTime.now();
	}

	public String readInput() {
		this.display(DisplayType.DEFAULT, Messages.UI_REQUEST_INPUT);
		String input = null;
		if (_scanner.hasNextLine()) {
			input = _scanner.nextLine();

			if (input == null) {
				return null;
			}
		}
		return input;
	}

	public void display(Task task) {
		this.display(new Task[] { task });
	}

	public void display(Task[] tasks) {
		_currentTaskList = tasks;
		_now = LocalDateTime.now();

		int numTasks = tasks.length;
		int characters = String.valueOf(numTasks).length();
		int shown = 0;
		
		this.printHeader(Messages.UI_HEADER);
		
		for (int x = 0; x < numTasks; x++) {
			Task task = tasks[x];
			display(ansi().fg(Color.CYAN).a("#" + padLeft(String.valueOf(x + 1), characters, "0") + ": ").reset());
			this.printTask(task);
			shown++;
		}

		display(ansi().a(String.format("---%nShowing %d out of %d tasks%n", shown, numTasks, 0)).newline().reset());
	}

	public void display(DisplayType type, String message) {
		if ( type == DisplayType.ERROR ) {
			this.display(ansi().fg(Color.RED).bold().a(message).reset());
		} else if ( type == DisplayType.SUCCESS ) {
			this.display(ansi().fg(Color.GREEN).bold().a(message).reset());
		} else {
			this.display(ansi().a(message));
		}
	}

	public void display(Ansi message) {
		System.out.print(message);
		System.out.flush();
	}

	public Task getTask(int index) throws InvalidTaskException {
		index = index - 1;

		if (_currentTaskList == null) {
			throw new InvalidTaskException();
		}

		assert _currentTaskList != null;

		if (_currentTaskList.length > index && _currentTaskList[index] != null) {
			assert _currentTaskList[index] != null;
			return _currentTaskList[index];
		}

		throw new InvalidTaskException();
	}

	public Task[] getCurrentTaskList() throws InvalidTaskException {
		if (_currentTaskList == null) {
			throw new InvalidTaskException();
		}

		assert _currentTaskList != null;
		return _currentTaskList;
	}
	
	private void printTask(Task task) {
		int numTasks = _currentTaskList.length;
		int characters = String.valueOf(numTasks).length();
		
		if (task.isDone()) {
			this.printSuccessMessage(String.format("[DONE] %s%n", task.getTitle()));
		} else {
			if (task.getTaskType() == TaskType.DEADLINE) {
				if (task.getEndDateTime().isBefore(_now)) {
					// Task is OVERDUE
					this.printCriticalMessage(String.format("[!!!] %s [Overdue by %s]%n", task.getTitle(), prettyDates(task.getEndDateTime())));
					this.printCriticalMessage(String.format(padLeft("", characters+3, " ") + "%s%n", formatDateTime(task.getEndDateTime())));
				} else {
					this.printInfoMessage(String.format("%s [Due in %s]%n", task.getTitle(), prettyDates(task.getEndDateTime())));
					this.printInfoMessage(String.format(padLeft("", characters+3, " ") + "%s%n", formatDateTime(task.getEndDateTime())));
				}
			} else if (task.getTaskType() == TaskType.TIMED) {
				if (task.getEndDateTime().isBefore(_now)) {
					// Task is OVERDUE
					this.printCriticalMessage(String.format("[!!!] %s [Started %s]%n", task.getTitle(), prettyDates(task.getStartDateTime())));
					this.printCriticalMessage(String.format(padLeft("", characters+3, " ") + "%s%n", formatDateTime(task.getStartDateTime(), task.getEndDateTime())));
				} else {
					this.printInfoMessage(String.format("%s [Starts in %s]%n", task.getTitle(), prettyDates(task.getStartDateTime())));
					this.printInfoMessage(String.format(padLeft("", characters+3, " ") + "%s%n", formatDateTime(task.getStartDateTime(), task.getEndDateTime())));
				}
			} else {
				this.printInfoMessage(String.format("%s%n", task.getTitle()));
			}

			display(DisplayType.DEFAULT, Messages.NEWLINE);
		}

	}

	private void printWelcomeMessage() {
		String padding = new String(new char[Messages.UI_WELCOME_MESSAGE.length() + 2]).replace("\0", "=");
		this.display(DisplayType.DEFAULT, ansi().fg(Color.RED).bg(Color.WHITE).a(padding).reset() + Messages.NEWLINE);
		this.display(DisplayType.DEFAULT, ansi().fg(Color.RED).bg(Color.WHITE).a(" " + Messages.UI_WELCOME_MESSAGE + " ").reset() + Messages.NEWLINE);
		this.display(DisplayType.DEFAULT, ansi().fg(Color.RED).bg(Color.WHITE).a(padding).reset() + Messages.NEWLINE);
		_controller.executeCommand(new ViewCommand(ViewType.UPCOMING));
	}
	
	private void printHeader(String header) {
		String padding = new String(new char[header.length() + 2]).replace("\0", "=");
		this.display(DisplayType.DEFAULT, Messages.NEWLINE + ansi().fg(Color.RED).bg(Color.WHITE).a(padding).reset() + Messages.NEWLINE);
		this.display(DisplayType.DEFAULT, ansi().fg(Color.RED).bg(Color.WHITE).a(" " + header + " ").reset() + Messages.NEWLINE);
		this.display(DisplayType.DEFAULT, ansi().fg(Color.RED).bg(Color.WHITE).a(padding).reset() + Messages.NEWLINE + Messages.NEWLINE);
	}
	
	private void printCriticalMessage(String msg) {
		display(ansi().fg(Color.RED).bold().a(msg).reset());
	}
	
	private void printSuccessMessage(String msg) {
		display(ansi().fg(Color.RED).bold().a(msg).reset());
	}
	
	private void printInfoMessage(String msg) {
		display(ansi().fg(Color.DEFAULT).a(msg).reset());
	}

	public void run() {
		if (!_isInitialized) {
			AnsiConsole.systemInstall();
			_isInitialized = true;
			this.printWelcomeMessage();
			_isUIRunning = true;
		}
	}

	public void exit() {
		AnsiConsole.systemUninstall();
		_isUIRunning = false;
	}

	public boolean isUIRunning() {
		return _isUIRunning;
	}

	public String formatDateTime(LocalDateTime dateTime) {
		if ( dateTime.toLocalTime().equals(LocalTime.MAX) ) {
			return dateTime.format(Messages.DATE_FORMAT);
		} else {
			return dateTime.format(Messages.DATETIME_FORMAT);
		}
	}
	
	public String formatDateTime(LocalDateTime startDateTime, LocalDateTime endDateTime) {
		if ( startDateTime.toLocalDate().equals(endDateTime.toLocalDate()) ) {
			return startDateTime.format(Messages.DATETIME_FORMAT) + " to " + endDateTime.format(Messages.TIME_FORMAT);
		} else {
			return formatDateTime(startDateTime) + " to " + formatDateTime(endDateTime);
		}
	}
	
	public String loadSelectDirectoryDialog(String currentDirectory) {
		JFileChooser fileChooser = new JFileChooser((currentDirectory == null) ? "." : currentDirectory);
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setVisible(true);
		int returnVal = fileChooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			return file.getAbsolutePath();
		}

		return null;
	}

	private String padLeft(String str, int length, String padding) {
		return String.format("%1$" + length + "s", str).replace(" ", padding);
	}

	private String prettyDates(LocalDateTime dateTime) {
		StringBuffer sb = new StringBuffer();
		RelativeDate relative = new RelativeDate(_now);
		relative.calculate(dateTime);
		
		if (relative.getYears() > 0 ) {
			sb.append(String.format("%d %s", relative.getYears(), relative.getYears() > 1 ? "years" : "year") + " ");
		} else if (relative.getMonths() > 0 ) {
			sb.append(String.format("%d %s", relative.getMonths(), relative.getMonths() > 1 ? "months" : "month") + " ");
		} else if (relative.getDays() > 0) {
			sb.append(String.format("%d %s", relative.getDays(), relative.getDays() > 1 ? "days" : "day") + " ");
		} else if (relative.getHours() > 0) {
			sb.append(String.format("%d %s", relative.getHours(), relative.getHours() > 1 ? "hours" : "hour") + " ");
		} else if (relative.getMinutes() > 0) {
			sb.append(String.format("%d %s", relative.getMinutes(), relative.getMinutes() > 1 ? "minutes" : "minute") + " ");
		} else {
			sb.append(String.format("%d %s", relative.getSeconds(), relative.getSeconds() > 1 ? "seconds" : "second") + " ");
		}

		return "about " + sb.toString().trim();
	}
}
