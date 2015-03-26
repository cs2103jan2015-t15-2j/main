//@author A0135137L
package taskie.database;

import java.util.ArrayList;
import java.util.HashMap;

import taskie.exceptions.TaskModificationFailedException;
import taskie.exceptions.TaskTypeNotSupportedException;
import taskie.models.Task;
import taskie.models.TaskType;

import java.io.File;
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
	public static final String DEFAULT_LOCATION = "C:\\Users\\Haihui\\Desktop\\Taskie";
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
	
	public HashMap<TaskType, ArrayList<Task>> retrieveTaskMap() {
		HashMap<TaskType, ArrayList<Task>> taskMap;
		if (_storageLocation == null) {
			taskMap = retrieveTaskMapFromDefaultLocation();
		} else {
			taskMap = retrieveTaskMapFromSetLocation();
		}
		return taskMap;
	}

	private HashMap<TaskType, ArrayList<Task>> retrieveTaskMapFromDefaultLocation() {
		
		HashMap<TaskType, ArrayList<Task>> taskMap = new HashMap<TaskType, ArrayList<Task>>(NUMBER_OF_TASKTYPES);
		File deadlinedFile = new File(DEFAULT_LOCATION, DEADLINED_TASKS_FILENAME);
		File timedFile = new File(DEFAULT_LOCATION, TIMED_TASKS_FILENAME);
		File floatingFile = new File(DEFAULT_LOCATION, FLOATING_TASKS_FILENAME);
		ArrayList<Task> deadlinedTasks = readTaskList(deadlinedFile);
		ArrayList<Task> timedTasks = readTaskList(timedFile);
		ArrayList<Task> floatingTasks = readTaskList(floatingFile);
		if (deadlinedTasks == null) {
			deadlinedTasks = new ArrayList<Task>();
		}
		taskMap.put(TaskType.DEADLINE, deadlinedTasks);
		if (timedTasks == null) {
			timedTasks = new ArrayList<Task>();
		}
		taskMap.put(TaskType.TIMED, timedTasks);
		if (floatingTasks == null) {
			floatingTasks = new ArrayList<Task>();
		}
		taskMap.put(TaskType.FLOATING, floatingTasks);
		return taskMap;
	}
	
	private HashMap<TaskType, ArrayList<Task>> retrieveTaskMapFromSetLocation() {
		HashMap<TaskType, ArrayList<Task>> taskMap = new HashMap<TaskType, ArrayList<Task>>(NUMBER_OF_TASKTYPES);
		File deadlinedFile = new File(_storageLocation, DEADLINED_TASKS_FILENAME);
		File timedFile = new File(_storageLocation, TIMED_TASKS_FILENAME);
		File floatingFile = new File(_storageLocation, FLOATING_TASKS_FILENAME);
		ArrayList<Task> deadlinedTasks = readTaskList(deadlinedFile);
		ArrayList<Task> timedTasks = readTaskList(timedFile);
		ArrayList<Task> floatingTasks = readTaskList(floatingFile);
		
		if (deadlinedTasks == null) {
			deadlinedTasks = new ArrayList<Task>();
		}
		taskMap.put(TaskType.DEADLINE, deadlinedTasks);
		if (timedTasks == null) {
			timedTasks = new ArrayList<Task>();
		}
		taskMap.put(TaskType.TIMED, timedTasks);
		if (floatingTasks == null) {
			floatingTasks = new ArrayList<Task>();
		}
		taskMap.put(TaskType.FLOATING, floatingTasks);
		
		return taskMap;
	}
	
	private ArrayList<Task> readTaskList(File file) {
		if (file.exists() == false) {
			return null;
		}
		ArrayList<String> strTaskList = readFile(file);
		ArrayList<Task> taskList;
		if (file.getName().equals(DEADLINED_TASKS_FILENAME)) {
			taskList = readDeadlinedTasks(strTaskList);
		} else if (file.getName().equals(TIMED_TASKS_FILENAME)) {
			taskList = readTimedTasks(strTaskList);
		} else {
			taskList = readFloatingTasks(strTaskList);
		}
		
		return taskList;
	}
	
	private ArrayList<String> readFile(File file) {
		try {
			ArrayList<String> taskList = new ArrayList<String>();
			Scanner scanner = new Scanner(new FileInputStream(file));
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
	
	public void storeTaskMap(HashMap<TaskType, ArrayList<Task>> hm) {
		if (_storageLocation == null) {
			storeTaskMapToDefaultLocation(hm);
		} else {
			storeTaskMapToSetLocation(hm);
		}

	}
	
	private void storeTaskMapToDefaultLocation(HashMap<TaskType, ArrayList<Task>> hm) {
		if (hm.containsKey(TaskType.DEADLINE)) {
			storeDeadlinedTasks(hm.get(TaskType.DEADLINE), DEFAULT_LOCATION);
		}
		if (hm.containsKey(TaskType.TIMED)) {
			storeTimedTasks(hm.get(TaskType.TIMED), DEFAULT_LOCATION);
		}
		if (hm.containsKey(TaskType.FLOATING)) {
			storeFloatingTasks(hm.get(TaskType.FLOATING), DEFAULT_LOCATION);
		}
	}
	
	private void storeTaskMapToSetLocation(HashMap<TaskType, ArrayList<Task>> hm) {
		if (hm.containsKey(TaskType.DEADLINE)) {
			storeDeadlinedTasks(hm.get(TaskType.DEADLINE), _storageLocation);
		}
		if (hm.containsKey(TaskType.TIMED)) {
			storeTimedTasks(hm.get(TaskType.TIMED),  _storageLocation);
		}
		if (hm.containsKey(TaskType.FLOATING)) {
			storeFloatingTasks(hm.get(TaskType.FLOATING),  _storageLocation);
		}
	}
	
	
	private void storeDeadlinedTasks(ArrayList<Task> taskList, String pathName) {
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
		writeFile(new File(pathName, DEADLINED_TASKS_FILENAME), strTaskList);
	}
	
	private void storeTimedTasks(ArrayList<Task> taskList, String pathName) {
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
		writeFile(new File(pathName, TIMED_TASKS_FILENAME), strTaskList);
	}
	
	private void storeFloatingTasks(ArrayList<Task> taskList, String pathName) {
		ArrayList<String> strTaskList = new ArrayList<String>();
		for (int i = 0; i < taskList.size(); i++) {
			Task tempTask = taskList.get(i);
			StringBuilder sb = new StringBuilder();
			sb = appendTaskTitle(tempTask, sb);
			strTaskList.add(sb.toString());
		}
		writeFile(new File(pathName,FLOATING_TASKS_FILENAME), strTaskList);
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
	
	private void writeFile(File file, ArrayList<String> strList) {
		try {
			if (file.exists() == false) {
				File fileParent = file.getParentFile();
				fileParent.mkdirs();
				file.createNewFile();
			}
			PrintWriter out = new PrintWriter(new FileWriter(file));
			for (int i = 0; i < strList.size(); i++) {
				out.println(strList.get(i));
			}
			out.close();
		} catch (IOException io) {
			io.printStackTrace();
		}
	}
	
	public void addFloatingTask(Task taskToAdd){
		if (_storageLocation == null) {
			addFloatingTaskToDefaultLocation(taskToAdd);
		} else {
			addFloatingTaskToSetLocation(taskToAdd);
		}
	}
	
	private void addFloatingTaskToDefaultLocation(Task taskToAdd){
		File floatingFile = new File(DEFAULT_LOCATION, FLOATING_TASKS_FILENAME);
		ArrayList<Task> floatingTasks = readTaskList(floatingFile);
		if (floatingTasks == null)
			floatingTasks = new ArrayList<Task>();
		floatingTasks.add(taskToAdd);
		storeFloatingTasks(floatingTasks, DEFAULT_LOCATION);
	}
	
	private void addFloatingTaskToSetLocation(Task taskToAdd){
		File floatingFile = new File(_storageLocation, FLOATING_TASKS_FILENAME);
		ArrayList<Task> floatingTasks = readTaskList(floatingFile);
		if (floatingTasks == null)
			floatingTasks = new ArrayList<Task>();
		floatingTasks.add(taskToAdd);
		storeFloatingTasks(floatingTasks, _storageLocation);
	}
	
	public void addDeadlinedTask(Task taskToAdd){
		if (_storageLocation == null) {
			addDeadlinedTaskToDefaultLocation(taskToAdd);
		} else {
			addDeadlinedTaskToSetLocation(taskToAdd);
		}
	}
	
	private void addDeadlinedTaskToDefaultLocation(Task taskToAdd){
		File deadlinedFile = new File(DEFAULT_LOCATION, DEADLINED_TASKS_FILENAME);
		ArrayList<Task> deadlinedTasks = readTaskList(deadlinedFile);
		if (deadlinedTasks == null)
			deadlinedTasks = new ArrayList<Task>();
		deadlinedTasks.add(taskToAdd);
		storeDeadlinedTasks(deadlinedTasks, DEFAULT_LOCATION);
	}
	
	private void addDeadlinedTaskToSetLocation(Task taskToAdd){
		File deadlinedFile = new File(_storageLocation, DEADLINED_TASKS_FILENAME);
		ArrayList<Task> deadlinedTasks = readTaskList(deadlinedFile);
		if (deadlinedTasks == null)
			deadlinedTasks = new ArrayList<Task>();
		deadlinedTasks.add(taskToAdd);
		storeDeadlinedTasks(deadlinedTasks, _storageLocation);
	}
	
	public void addTimedTask(Task taskToAdd){
		if (_storageLocation == null) {
			addTimedTaskToDefaultLocation(taskToAdd);
		} else {
			addTimedTaskToSetLocation(taskToAdd);
		}
	}
	
	private void addTimedTaskToDefaultLocation(Task taskToAdd) {
		File timedFile = new File(DEFAULT_LOCATION, TIMED_TASKS_FILENAME);
		ArrayList<Task> timedTasks = readTaskList(timedFile);
		if (timedTasks == null)
			timedTasks = new ArrayList<Task>();
		timedTasks.add(taskToAdd);
		storeTimedTasks(timedTasks, DEFAULT_LOCATION);
	}
	
	private void addTimedTaskToSetLocation(Task taskToAdd) {
		File timedFile = new File(_storageLocation, TIMED_TASKS_FILENAME);
		ArrayList<Task> timedTasks = readTaskList(timedFile);
		if (timedTasks == null)
			timedTasks = new ArrayList<Task>();
		timedTasks.add(taskToAdd);
		storeTimedTasks(timedTasks, _storageLocation);
	}
	
	public void deleteFloatingTask(Task taskToDelete) {
		if (_storageLocation == null) {
			deleteFloatingTaskFromDefaultLocation(taskToDelete);
		} else {
			deleteFloatingTaskFromSetLocation(taskToDelete);
		}
	}
	
	private void deleteFloatingTaskFromDefaultLocation(Task taskToDelete) {
		File floatingFile = new File(DEFAULT_LOCATION, FLOATING_TASKS_FILENAME);
		ArrayList<Task> floatingTasks = readTaskList(floatingFile);
		Task tempTask = null;
		for (Task tsk : floatingTasks) {
			if (tsk.equals(taskToDelete)) {
				tempTask = tsk;
				break;
			}
		}
		floatingTasks.remove(tempTask);
		storeFloatingTasks(floatingTasks, DEFAULT_LOCATION);
	}
	
	private void deleteFloatingTaskFromSetLocation(Task taskToDelete) {
		File floatingFile = new File(_storageLocation, FLOATING_TASKS_FILENAME);
		ArrayList<Task> floatingTasks = readTaskList(floatingFile);
		Task tempTask = null;
		for (Task tsk : floatingTasks) {
			if (tsk.equals(taskToDelete)) {
				tempTask = tsk;
				break;
			}
		}
		floatingTasks.remove(tempTask);
		storeFloatingTasks(floatingTasks, _storageLocation);
	}
	
	public void deleteDeadlinedTask(Task taskToDelete) {
		if (_storageLocation == null) {
			deleteDeadlinedTaskFromDefaultLocation(taskToDelete);
		} else {
			deleteDeadlinedTaskFromSetLocation(taskToDelete);
		}
	}
	
	private void deleteDeadlinedTaskFromDefaultLocation(Task taskToDelete) {
		File deadlinedFile = new File(DEFAULT_LOCATION, DEADLINED_TASKS_FILENAME);
		ArrayList<Task> deadlinedTasks = readTaskList(deadlinedFile);
		Task tempTask = null;
		for (Task tsk : deadlinedTasks) {
			if (tsk.equals(taskToDelete)) {
				tempTask = tsk;
				break;
			}
		}
		deadlinedTasks.remove(tempTask);
		storeDeadlinedTasks(deadlinedTasks, DEFAULT_LOCATION);
	}
	
	private void deleteDeadlinedTaskFromSetLocation(Task taskToDelete) {
		File deadlinedFile = new File(_storageLocation, DEADLINED_TASKS_FILENAME);
		ArrayList<Task> deadlinedTasks = readTaskList(deadlinedFile);
		Task tempTask = null;
		for (Task tsk : deadlinedTasks) {
			if (tsk.equals(taskToDelete)) {
				tempTask = tsk;
				break;
			}
		}
		deadlinedTasks.remove(tempTask);
		storeDeadlinedTasks(deadlinedTasks, _storageLocation);
	}
	
	public void deleteTimedTask(Task taskToDelete) {
		if (_storageLocation == null) {
			deleteTimedTaskFromDefaultLocation(taskToDelete);
		} else {
			deleteTimedTaskFromSetLocation(taskToDelete);
		}
	}
	
	private void deleteTimedTaskFromDefaultLocation(Task taskToDelete) {
		File timedFile = new File(DEFAULT_LOCATION, TIMED_TASKS_FILENAME);
		ArrayList<Task> timedTasks = readTaskList(timedFile);
		Task tempTask = null;
		for (Task tsk : timedTasks) {
			if (tsk.equals(taskToDelete)) {
				tempTask = tsk;
				break;
			}
		}
		timedTasks.remove(tempTask);
		storeTimedTasks(timedTasks, DEFAULT_LOCATION);
	}
	
	private void deleteTimedTaskFromSetLocation(Task taskToDelete) {
		File timedFile = new File(_storageLocation, TIMED_TASKS_FILENAME);
		ArrayList<Task> timedTasks = readTaskList(timedFile);
		Task tempTask = null;
		for (Task tsk : timedTasks) {
			if (tsk.equals(taskToDelete)) {
				tempTask = tsk;
				break;
			}
		}
		timedTasks.remove(tempTask);
		storeTimedTasks(timedTasks, _storageLocation);
	}
	

	public void updateTask(Task oldTask, Task newTask) {
		if (_storageLocation == null) {
			updateTaskFromDefaultLocation(oldTask, newTask);
		} else {
			updateTaskFromSetLocation(oldTask, newTask);
		}
	}
	
	private void updateTaskFromDefaultLocation(Task oldTask, Task newTask) {
		if (oldTask.getTaskType() == TaskType.DEADLINE) {
			deleteDeadlinedTaskFromDefaultLocation(oldTask);
		} else if (oldTask.getTaskType() == TaskType.TIMED) {
			deleteTimedTaskFromDefaultLocation(oldTask);
		} else {
			deleteFloatingTaskFromDefaultLocation(oldTask);
		}
	
		if (newTask.getTaskType() == TaskType.DEADLINE) {
			addDeadlinedTaskToDefaultLocation(newTask);
		} else if (newTask.getTaskType() == TaskType.TIMED) {
			addTimedTaskToDefaultLocation(newTask);
		} else {
			addFloatingTaskToDefaultLocation(newTask);
		}
	}
	
	private void updateTaskFromSetLocation(Task oldTask, Task newTask) {
		if (oldTask.getTaskType() == TaskType.DEADLINE) {
			deleteDeadlinedTaskFromSetLocation(oldTask);
		} else if (oldTask.getTaskType() == TaskType.TIMED) {
			deleteTimedTaskFromSetLocation(oldTask);
		} else {
			deleteFloatingTaskFromSetLocation(oldTask);
		}
	
		if (newTask.getTaskType() == TaskType.DEADLINE) {
			addDeadlinedTaskToSetLocation(newTask);
		} else if (newTask.getTaskType() == TaskType.TIMED) {
			addTimedTaskToSetLocation(newTask);
		} else {
			addFloatingTaskToSetLocation(newTask);
		}
	}

	public String getStorageLocation() {
		return _storageLocation;
	}
	
	
	
	//@author A0121555M
	public void addTask(Task task) throws TaskTypeNotSupportedException, TaskModificationFailedException {
		if ( task.getTaskType() == TaskType.FLOATING ) {
			this.addFloatingTask(task);
		} else if ( task.getTaskType() == TaskType.DEADLINE ) {
			this.addDeadlinedTask(task);
		} else if ( task.getTaskType() == TaskType.TIMED ){
			this.addTimedTask(task);
		} else {
			throw new TaskTypeNotSupportedException();
		}
	}

	@Override
	public void deleteTask(Task task) throws TaskTypeNotSupportedException, TaskModificationFailedException {
		if ( task.getTaskType() == TaskType.FLOATING ) {
			this.deleteFloatingTask(task);
		} else if ( task.getTaskType() == TaskType.DEADLINE ) {
			this.deleteDeadlinedTask(task);
		} else if ( task.getTaskType() == TaskType.TIMED ){
			this.deleteTimedTask(task);
		} else {
			throw new TaskTypeNotSupportedException();
		}
	}
	

	
	public void close() throws IOException {		
	}
}
