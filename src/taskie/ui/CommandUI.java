//@author A0121555M
package taskie.ui;

import static org.fusesource.jansi.Ansi.ansi;

import java.io.File;
import java.time.LocalDate;
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
import taskie.models.ViewType;

public class CommandUI implements UI {
	private static int TASKS_PER_PAGE = 20;

	private Scanner _scanner;
	private Task[] _currentTaskList;
	private static boolean _isInitialized;
	private static boolean _isUIRunning;

	public CommandUI() {
		_scanner = new Scanner(System.in);
		_isInitialized = false;
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
		
		int numTasks = tasks.length;
		int characters = String.valueOf(numTasks).length();

		for (int x = 0; x < numTasks; x++) {
			Task task = tasks[x];
			
			System.out.print(String.format("#%1$" + characters + "s: ", (x + 1)));
			if (task.isDeadlined()) {
				System.out.println(task.getTitle() + " -- Complete by "
						+ task.getEndDateTime());
			} else if (task.isTimed()) {
				System.out.println(task.getTitle() + " -- Do between "
						+ task.getStartDateTime() + " to "
						+ task.getEndDateTime());
			} else {
				System.out.println(task.getTitle());
			}
		}
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
		this.display(ansi().fg(Color.RED).bg(Color.WHITE).a(Messages.UI_WELCOME_MESSAGE).reset());
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
		if ( returnVal == JFileChooser.APPROVE_OPTION )  {
			File file = fileChooser.getSelectedFile();
			return file.getAbsolutePath();
		}
		
		return null;
	}
	
	private void printDateHeader(LocalDate date) {
		this.display(ansi().a(date.format(DateTimeFormatter.ofPattern("dd MM yyyy"))));
	}
}
