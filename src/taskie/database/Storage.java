package taskie.database;

import java.util.ArrayList;
import java.util.HashMap;

import taskie.models.Task;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.StringTokenizer;
import java.time.format.DateTimeFormatter;


public class Storage implements IStorage {
	
	public static final String DEADLINED_TASKNAME = "deadlined";
	public static final String TIMED_TASKNAME = "timed";
	public static final String FLOATING_TASKNAME = "floating";
	
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
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		for (int i = 0; i < strTaskList.size(); i++) {
			Task tempTask;
			String tempStr = strTaskList.get(i);
			StringTokenizer tokenizer = new StringTokenizer(tempStr, SEPARATOR);
			if (tokenizer.countTokens() == FIELD_QTY_FOR_DEADLINED_WITH_ENDTIME) {
				tempTask = tokenizeStrOfDeadlinedTaskWithEndtime(dateFormatter, timeFormatter,tokenizer);
			} else {
				tempTask = tokenizeStrOfDeadlinedTaskWithoutEndtime(dateFormatter, timeFormatter,tokenizer);
			}
			taskList.add(tempTask);
		}
		return taskList;
	}

	private Task tokenizeStrOfDeadlinedTaskWithEndtime(DateTimeFormatter dateFormatter, 
			DateTimeFormatter timeFormatter, StringTokenizer tokenizer) {
		Task tempTask;
		String title = tokenizer.nextToken().trim();
		String strEndDate = tokenizer.nextToken().trim();
		LocalDate endDate = LocalDate.parse(strEndDate, dateFormatter);
		String strEndTime = tokenizer.nextToken().trim();
		LocalTime time = LocalTime.parse(strEndTime, timeFormatter);
		tempTask = new Task(title, endDate, time);
		return tempTask;
	}
	
	private Task tokenizeStrOfDeadlinedTaskWithoutEndtime(DateTimeFormatter dateFormatter, 
			DateTimeFormatter timeFormatter, StringTokenizer tokenizer) {
		Task tempTask;
		String title = tokenizer.nextToken().trim();
		String strEndDate = tokenizer.nextToken().trim();
		LocalDate endDate = LocalDate.parse(strEndDate, dateFormatter);
		tempTask = new Task(title, endDate);
		return tempTask;
	}
	
	private ArrayList<Task> readTimedTasks(ArrayList<String> strTaskList) {
		ArrayList<Task> taskList = new ArrayList<Task>();
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		for (int i = 0; i < strTaskList.size(); i++) {
			Task tempTask;
			String tempStr = strTaskList.get(i);
			StringTokenizer tokenizer = new StringTokenizer(tempStr, SEPARATOR);
			if (tokenizer.countTokens() == FIELD_QTY_FOR_TIMED_WITH_TIME) {
				tempTask = tokenizeStrOfTimedTaskWithTime(dateFormatter, timeFormatter, tokenizer);
			} else {
				tempTask = tokenizeStrOfTimedTaskWithoutTime(dateFormatter, timeFormatter, tokenizer);
			}
			taskList.add(tempTask);
		}
		return taskList;
	}
	
	private Task tokenizeStrOfTimedTaskWithTime(DateTimeFormatter dateFormatter, 
			DateTimeFormatter timeFormatter, StringTokenizer tokenizer) {
		Task tempTask;
		String title = tokenizer.nextToken().trim();
		String strStartDate = tokenizer.nextToken().trim();
		LocalDate startDate = LocalDate.parse(strStartDate, dateFormatter);
		String strStartTime = tokenizer.nextToken().trim();
		LocalTime startTime = LocalTime.parse(strStartTime, timeFormatter);
		String strEndDate = tokenizer.nextToken().trim();
		LocalDate endDate = LocalDate.parse(strEndDate, dateFormatter);
		String strEndTime = tokenizer.nextToken().trim();
		LocalTime endTime = LocalTime.parse(strEndTime, timeFormatter);
		tempTask = new Task(title, startDate, startTime, endDate, endTime);
		return tempTask;
	}
	
	private Task tokenizeStrOfTimedTaskWithoutTime(DateTimeFormatter dateFormatter, 
			DateTimeFormatter timeFormatter, StringTokenizer tokenizer) {
		Task tempTask;
		String title = tokenizer.nextToken().trim();
		String strStartDate = tokenizer.nextToken().trim();
		LocalDate startDate = LocalDate.parse(strStartDate, dateFormatter);
		String strEndDate = tokenizer.nextToken().trim();
		LocalDate endDate = LocalDate.parse(strEndDate, dateFormatter);
		tempTask = new Task(title, startDate, endDate);
		return tempTask;
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
	
	public void storeTaskMap(HashMap<String, ArrayList<Task>> hm) {
		if (hm.containsKey(DEADLINED_TASKNAME)) {
			storeDeadlinedTasks(hm.get(DEADLINED_TASKNAME));
		} 
		if (hm.containsKey(TIMED_TASKNAME)) {
			storeTimedTasks(hm.get(TIMED_TASKNAME));
		} 
		if (hm.containsKey(FLOATING_TASKNAME)) {
			storeFloatingTasks(hm.get(FLOATING_TASKNAME));
		}
	}
	
	private void storeDeadlinedTasks(ArrayList<Task> taskList) {
		ArrayList<String> strTaskList = new ArrayList<String>();
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		for (int i = 0; i < taskList.size(); i++) {
			Task tempTask = taskList.get(i);
			StringBuilder sb = new StringBuilder();
			sb = appendTaskTitle(tempTask, sb);
			sb.append(SEPARATOR);
			sb = appendStrEndDate(dateFormatter, tempTask, sb);
			if (tempTask.getEndTime() != null) {
				sb.append(SEPARATOR);
				sb = appendStrEndTime(timeFormatter, tempTask, sb);
			}
			strTaskList.add(sb.toString());
		}
		writeFile(DEADLINED_TASKS_FILENAME, strTaskList);
	}
	
	private void storeTimedTasks(ArrayList<Task> taskList) {
		ArrayList<String> strTaskList = new ArrayList<String>();
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		for (int i = 0; i < taskList.size(); i++) {
			Task tempTask = taskList.get(i);
			StringBuilder sb = new StringBuilder();
			sb = appendTaskTitle(tempTask, sb);
			sb.append(SEPARATOR);
			sb = appendStrStartDate(dateFormatter, tempTask, sb);
			sb.append(SEPARATOR);
			if (tempTask.getStartTime() == null) {
				sb = appendStrEndDate(dateFormatter, tempTask, sb);
			} else {
				sb = appendStrStartTime(timeFormatter, tempTask, sb);
				sb.append(SEPARATOR);
				sb = appendStrEndDate(dateFormatter, tempTask, sb);
				sb.append(SEPARATOR);
				sb = appendStrEndTime(timeFormatter, tempTask, sb);
			}
			strTaskList.add(sb.toString());
		}
		writeFile(TIMED_TASKS_FILENAME, strTaskList);
	}
	
	private void storeFloatingTasks(ArrayList<Task> taskList) {
		ArrayList<String> strTaskList = new ArrayList<String>();
		for (int i = 0; i < taskList.size(); i++) {
			Task tempTask = taskList.get(i);
			StringBuilder sb = new StringBuilder();
			sb = appendTaskTitle(tempTask, sb);
			strTaskList.add(sb.toString());
		}
		writeFile(FLOATING_TASKS_FILENAME, strTaskList);
	}
	
	private StringBuilder appendStrStartTime(DateTimeFormatter timeFormatter, Task tempTask, StringBuilder sb) {
		LocalTime startTime = tempTask.getStartTime();
		String strStartTime = startTime.format(timeFormatter);
		sb.append(strStartTime);
		return sb;
	}

	private StringBuilder appendStrEndTime(DateTimeFormatter timeFormatter, Task tempTask, StringBuilder sb) {
		LocalTime endTime = tempTask.getEndTime();
		String strEndTime = endTime.format(timeFormatter);
		sb.append(strEndTime);
		return sb;
	}

	private StringBuilder appendTaskTitle(Task tempTask, StringBuilder sb) {
		sb.append(tempTask.getTitle().trim());
		return sb;
	}
	
	private StringBuilder appendStrStartDate(DateTimeFormatter dateFormatter, Task tempTask, StringBuilder sb) {
		LocalDate startDate = tempTask.getStartDate();
		String strStartDate = startDate.format(dateFormatter);
		sb.append(strStartDate);
		return sb;
	}

	private StringBuilder appendStrEndDate(DateTimeFormatter dateFormatter, Task tempTask, StringBuilder sb) {
		LocalDate endDate = tempTask.getEndDate();
		String strEndDate = endDate.format(dateFormatter);
		sb.append(strEndDate);
		return sb;
	}
	
	private void writeFile(String fileName, ArrayList<String> strList) {
		try {
			PrintWriter out = new PrintWriter(new FileWriter(fileName));
			for (int i = 0; i < strList.size(); i++) {
				out.println(strList.get(i));
			}
			out.close();
		} catch (IOException io) {
			io.printStackTrace();
		}
	}
	
	public void addFloatingTask(Task taskToAdd){
		ArrayList<Task> floatingTasks = readTaskList(FLOATING_TASKS_FILENAME);
		floatingTasks.add(taskToAdd);
		storeFloatingTasks(floatingTasks);
	}
	
	public void addDeadlinedTask(Task taskToAdd){
		ArrayList<Task> deadlinedTasks = readTaskList(DEADLINED_TASKS_FILENAME);
		deadlinedTasks.add(taskToAdd);
		storeDeadlinedTasks(deadlinedTasks);
	}
	
	public void addTimedTask(Task taskToAdd){
		ArrayList<Task> timedTasks = readTaskList(TIMED_TASKS_FILENAME);
		timedTasks.add(taskToAdd);
		storeTimedTasks(timedTasks);
	}
	
	
	
	
	
}
