package taskie.database;

import java.util.ArrayList;
import java.util.HashMap;

import taskie.models.Task;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.StringTokenizer;
import java.time.format.DateTimeFormatter;

public class Storage implements IStorage {
	
	private static final String DEADLINED_TASKNAME = "deadlined";
	private static final String TIMED_TASKNAME = "timed";
	private static final String FLOATING_TASKNAME = "floating";
	
	private static final String DEADLINED_TASKS_FILENAME = "deadlined tasks.txt";
	private static final String TIMED_TASKS_FILENAME = "timed tasks.txt";
	private static final String FLOATING_TASKS_FILENAME = "floating tasks.txt";
	
	private static final String SEPARATOR = "||";
	

	private static final int FIELD_QTY_FOR_DEADLINED_WITH_ENDTIME = 3;
	private static final int FIELD_QTY_FOR_TIMED_WITH_TIME = 5;
	
	private static final int NUMBER_OF_TASKTYPES = 3;
	
	private String _storageLocation;
	
	public void setStorageLocation(String fileDir) {
		_storageLocation = fileDir;
	}
	
	public HashMap<String, ArrayList<Task>> retrieveTaskMap() {	
		HashMap<String, ArrayList<Task>> taskLists = new HashMap<String, ArrayList<Task>>(NUMBER_OF_TASKTYPES);
		
		ArrayList<Task> deadlinedTasks = readTaskList(DEADLINED_TASKS_FILENAME);
		ArrayList<Task> timedTasks = readTaskList(TIMED_TASKS_FILENAME);
		ArrayList<Task> floatingTasks = readTaskList(FLOATING_TASKS_FILENAME);
		
		taskLists.put(DEADLINED_TASKNAME, deadlinedTasks);
		taskLists.put(TIMED_TASKNAME, timedTasks);
		taskLists.put(FLOATING_TASKNAME, floatingTasks);
		
		return taskLists;
	}
	
	private ArrayList<Task> readTaskList(String fileName) {
		ArrayList<String> strTaskList = readFile(fileName);
		ArrayList<Task> taskList;
		
		if (fileName == DEADLINED_TASKS_FILENAME) {
			taskList = readDeadlinedTasks(strTaskList);
		} else if (fileName == TIMED_TASKS_FILENAME) {
			taskList = readTimedTasks(strTaskList);
		} else {
			taskList = readFloatingTasks(strTaskList);
		}
		
		return taskList;
	}
	
	private ArrayList<String> readFile(String fileName) {
		try {
			ArrayList<String> taskList = new ArrayList<String>();
			Scanner scanner = new Scanner(new FileInputStream(fileName));
			while (scanner.hasNextLine()) {
				taskList.add(scanner.nextLine());
			}
			scanner.close();
			return taskList;
		} catch (IOException io) {
			io.printStackTrace();
			return null;
		}
	}
	
	private ArrayList<Task> readDeadlinedTasks(ArrayList<String> strTaskList) {
		ArrayList<Task> taskList = new ArrayList<Task>();
		Task tempTask;
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		for (int i = 0; i < strTaskList.size(); i++) {
			String tempStr = strTaskList.get(i);
			StringTokenizer tokenizer = new StringTokenizer(tempStr, SEPARATOR);
			String title = tokenizer.nextToken().trim();
			String strEndDate = tokenizer.nextToken().trim();
			LocalDate date = LocalDate.parse(strEndDate, dateFormatter);
			if (tokenizer.countTokens() == FIELD_QTY_FOR_DEADLINED_WITH_ENDTIME) {
				String strEndTime = tokenizer.nextToken().trim();
				LocalTime time = LocalTime.parse(strEndTime, timeFormatter);
				tempTask = new Task(title, date, time);
			} else {
				tempTask = new Task(title, date);
			}
			taskList.add(tempTask);
		}
		return taskList;
	}
	
	private ArrayList<Task> readTimedTasks(ArrayList<String> strTaskList) {
		ArrayList<Task> taskList = new ArrayList<Task>();
		Task tempTask;
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		for (int i = 0; i < strTaskList.size(); i++) {
			String tempStr = strTaskList.get(i);
			StringTokenizer tokenizer = new StringTokenizer(tempStr, SEPARATOR);
			String title = tokenizer.nextToken().trim();
			String strStartDate = tokenizer.nextToken().trim();
			LocalDate startDate = LocalDate.parse(strStartDate, dateFormatter);
			if (tokenizer.countTokens() == FIELD_QTY_FOR_TIMED_WITH_TIME) {
				String strStartTime = tokenizer.nextToken().trim();
				LocalTime startTime = LocalTime.parse(strStartTime, timeFormatter);
				String strEndDate = tokenizer.nextToken().trim();
				LocalDate endDate = LocalDate.parse(strEndDate, dateFormatter);
				String strEndTime = tokenizer.nextToken().trim();
				LocalTime endTime = LocalTime.parse(strEndTime, timeFormatter);
				tempTask = new Task(title, startDate, startTime, endDate, endTime);
			} else {
				String strEndDate = tokenizer.nextToken().trim();
				LocalDate endDate = LocalDate.parse(strEndDate, dateFormatter);
				tempTask = new Task(title, startDate, endDate);
			}
			taskList.add(tempTask);
		}
		return taskList;
	}

	private ArrayList<Task> readFloatingTasks(ArrayList<String> strTaskList) {
		ArrayList<Task> taskList = new ArrayList<Task>();
		Task tempTask;
		for (int i = 0; i < strTaskList.size(); i++) {
			String tempStr = strTaskList.get(i);
			String title = tempStr.trim();
			tempTask = new Task(title);
			taskList.add(tempTask);
		}
		return taskList;
	}
	

	
}
