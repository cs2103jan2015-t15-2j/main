//@author A0121555M
package taskie.ui;

import static org.fusesource.jansi.Ansi.ansi;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import javax.swing.JFileChooser;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Color;
import org.fusesource.jansi.AnsiConsole;

import taskie.Taskie;
import taskie.commands.ViewCommand;
import taskie.exceptions.InvalidTaskException;
import taskie.models.Messages;
import taskie.models.Task;
import taskie.models.TaskType;
import taskie.models.ViewType;

public class CommandUI implements UI {
	private static int TASKS_PER_PAGE = 20;

	private Scanner _scanner;
	private Task[] _currentTaskList;
	private static boolean _isInitialized;
	private static boolean _isUIRunning;
	private LocalDateTime _now;

	public CommandUI() {
		_scanner = new Scanner(System.in);
		_isInitialized = false;
		_now = LocalDateTime.now();
	}

	public String readInput() {
		this.display(Messages.UI_REQUEST_INPUT);
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

		for (int x = 0; x < numTasks; x++) {
			Task task = tasks[x];

			display(ansi().fg(Color.CYAN).a("#" + padLeft(String.valueOf(x + 1), characters, "0") + ": ").reset());
			if (task.isDone()) {
				display(ansi().fg(Color.GREEN).a(String.format("[DONE] %s%n", task.getTitle())).reset());
			} else {
				if (task.getTaskType() == TaskType.DEADLINE) {
					if (task.getEndDateTime().isBefore(_now)) {
						// Task is OVERDUE
						display(ansi().fg(Color.RED).bold().a(String.format("[!!!] %s [Overdue by %s]%n", task.getTitle(), prettyDates(task.getEndDateTime()))).reset());
					} else {
						display(ansi().fg(Color.DEFAULT).a(String.format("%s [Due in %s]%n", task.getTitle(), prettyDates(task.getEndDateTime()))).reset());
					}
				} else if (task.getTaskType() == TaskType.TIMED) {
					if (task.getEndDateTime().isBefore(_now)) {
						// Task is OVERDUE
						display(ansi().fg(Color.RED).bold().a(String.format("[!!!] %s [Started %s]%n", task.getTitle(), prettyDates(task.getStartDateTime()))).reset());
					} else {
						display(ansi().fg(Color.DEFAULT).a(String.format("%s [Starts in %s]%n", task.getTitle(), prettyDates(task.getStartDateTime()))).reset());
					}
				} else {
					display(String.format("%s%n", task.getTitle()));
				}
			}

			shown++;
		}

		display(ansi().a(String.format("---%nShowing %d out of %d tasks%n", shown, numTasks, 0)).newline().reset());
	}

	public void display(String message) {
		this.display(ansi().a(message));
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

	private void printWelcomeMessage() {
		String padding = new String(new char[Messages.UI_WELCOME_MESSAGE.length() + 2]).replace("\0", "=");
		this.display(ansi().fg(Color.RED).bg(Color.WHITE).a(padding).reset() + Messages.NEWLINE);
		this.display(ansi().fg(Color.RED).bg(Color.WHITE).a(" " + Messages.UI_WELCOME_MESSAGE + " ").reset() + Messages.NEWLINE);
		this.display(ansi().fg(Color.RED).bg(Color.WHITE).a(padding).reset() + Messages.NEWLINE);
		Taskie.Controller.executeCommand(new ViewCommand(ViewType.ALL));
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

	private void printDateHeader(LocalDate date) {
		this.display(ansi().a(date.format(DateTimeFormatter.ofPattern("dd MM yyyy"))));
	}

	private String padLeft(String str, int length, String padding) {
		return String.format("%1$" + length + "s", str).replace(" ", padding);
	}

	private String padRight(String str, int length, String padding) {
		return String.format("%1$-" + length + "s", str).replace(" ", padding);
	}

	private String prettyDates(LocalDateTime dateTime) {
		StringBuffer sb = new StringBuffer();
		RelativeDate relative = new RelativeDate(_now);
		relative.calculate(dateTime);

		if (relative.getDays() > 0) {
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
