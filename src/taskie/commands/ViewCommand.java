package taskie.commands;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import taskie.Taskie;
import taskie.exceptions.TaskRetrievalFailedException;
import taskie.exceptions.UndoNotSupportedException;
import taskie.models.CommandType;
import taskie.models.Task;
import taskie.models.TaskType;
import taskie.models.ViewType;
import taskie.models.TaskEndDateComparator;
import taskie.parser.CommandParser;

public class ViewCommand implements ICommand {
	private CommandType _commandType = CommandType.VIEW;
	private Logger _logger = Logger.getLogger(CommandParser.class.getName());
	// @author A0121555M
	private ViewType _viewType;
	private LocalDate _startDate;
	private LocalTime _startTime;
	private LocalDate _endDate;
	private LocalTime _endTime;
	private String _searchKeywords;

	public ViewCommand(ViewType viewType) {
		_viewType = viewType;
	}

	public ViewCommand(ViewType viewType, LocalDate startDate,
			LocalTime startTime, LocalDate endDate, LocalTime endTime,
			String searchKeywords) {
		super();
		_viewType = viewType;
		_startDate = startDate;
		_endDate = endDate;
		_startTime = startTime;
		_endTime = endTime;
		_searchKeywords = searchKeywords;
	}

	public ViewCommand(ViewType viewType, LocalDateTime startDateTime,
			LocalDateTime endDateTime, String searchKeywords) {
		super();
		_viewType = viewType;
		this.setStartDateTime(startDateTime);
		this.setEndDateTime(endDateTime);
		_searchKeywords = searchKeywords;
	}

	public LocalDateTime getStartDateTime() {
		try {
			return LocalDateTime.of(_startDate,
					(_startTime == null) ? LocalTime.MAX : _startTime);
		} catch (NullPointerException e) {
			return null;
		}
	}

	public void setStartDateTime(LocalDateTime startDateTime) {
		if (startDateTime == null) {
			this.setStartDate(null);
			this.setStartTime(null);
		} else {
			this.setStartDate(startDateTime.toLocalDate());
			this.setStartTime(startDateTime.toLocalTime());
		}
	}

	public LocalDateTime getEndDateTime() {
		try {
			return LocalDateTime.of(_endDate,
					(_endTime == null) ? LocalTime.MAX : _endTime);
		} catch (NullPointerException e) {
			return null;
		}
	}

	public void setEndDateTime(LocalDateTime endDateTime) {
		if (endDateTime == null) {
			this.setEndDate(null);
			this.setEndTime(null);
		} else {
			this.setEndDate(endDateTime.toLocalDate());
			this.setEndTime(endDateTime.toLocalTime());
		}
	}

	public LocalDate getStartDate() {
		return _startDate;
	}

	public void setStartDate(LocalDate startDate) {
		if (_endDate == null) {
			this._endDate = startDate;
		}

		if (_endDate.isBefore(startDate)) {
			this._startDate = this._endDate;
			this._endDate = startDate;
		} else {
			this._startDate = startDate;
		}
	}

	public LocalDate getEndDate() {
		return _endDate;
	}

	public void setEndDate(LocalDate endDate) {
		if (_startDate == null) {
			this._startDate = endDate;
		}

		if (_startDate.isAfter(endDate)) {
			this._endDate = this._startDate;
			this._startDate = endDate;
		} else {
			this._endDate = endDate;
		}
	}

	public LocalTime getStartTime() {
		return _startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this._startTime = startTime;
	}

	public LocalTime getEndTime() {
		return _endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this._endTime = endTime;
	}

	public String getSearchKeywords() {
		return _searchKeywords;
	}

	public void setSearchKeywords(String searchKeywords) {
		this._searchKeywords = searchKeywords.toLowerCase();
	}

	public CommandType getCommandType() {
		return _commandType;
	}

	// @author A0097582N
	@Override
	public void execute() {
		_logger.log(
				Level.INFO,
				"ViewType: "+this._viewType+"\nStartDate: " + this.getStartDate() + " StartTime: "
						+ this.getStartTime() + "\nEndDate: "
						+ this.getEndDate() + " EndTime: " + this.getEndTime()
						+ "\nSearch Keywords: " + this.getSearchKeywords());
		switch (_viewType) {
		case ALL:
			executeViewAll();
			break;
		case UPCOMING:
			executeViewUpcoming();
			break;
		case OVERDUE:
			executeViewOverdue();
			break;
		case COMPLETED:
			executeViewCompleted();
			break;
		case SEARCH:
			executeViewSearch();
			break;
		}
	}

	private void executeViewSearch() {
		ArrayList<Task> tasks = null;
		ArrayList<Task> tasksToDisplay = null;
		try {
			tasks = Taskie.Controller.getStorage().getTaskList();
		} catch (TaskRetrievalFailedException e) {
			Taskie.Controller.getUI().display(e.getMessage());
		}
		if (_startDate == null && _endDate == null && _searchKeywords != null) {
			tasksToDisplay = findSearchedTasks(tasks);

		} else if (_searchKeywords != null) {
			tasksToDisplay = findTasksByDate(tasks);
			tasksToDisplay = findSearchedTasks(tasksToDisplay);
		} else {
			tasksToDisplay = findTasksByDate(tasks);
		}
		tasksToDisplay.sort(new TaskEndDateComparator());
		Taskie.Controller.getUI().display(
				tasksToDisplay.toArray(new Task[tasksToDisplay.size()]));

	}

	private ArrayList<Task> findTasksByDate(ArrayList<Task> tasks) {
		if (_startDate == null) {
			return findTasksBeforeEndDate(tasks);
		} else if (_endDate == null) {
			return findTasksFromStartDate(tasks);
		}
		return findTasksBetweenDates(tasks);
	}

	private ArrayList<Task> findTasksBeforeEndDate(ArrayList<Task> tasks) {
		ArrayList<Task> tasksToDisplay = new ArrayList<Task>();
		LocalDateTime endDateTime = this.getEndDateTime();

		for (Task task : tasks) {
			if (task.isDone()) {
				break;
			}
			TaskType taskType = task.getTaskType();
			if (taskType == TaskType.FLOATING) {
				tasksToDisplay.add(task);
			} else if (taskType == TaskType.DEADLINE 
					&&task.getEndDateTime().isBefore(endDateTime)){
				tasksToDisplay.add(task);
			}else if(task.getStartDateTime().isBefore(endDateTime)){
				tasksToDisplay.add(task);
			}
		}
		return tasksToDisplay;
	}

	private ArrayList<Task> findTasksFromStartDate(ArrayList<Task> tasks) {
		ArrayList<Task> tasksToDisplay = new ArrayList<Task>();
		LocalDateTime startDateTime = this.getStartDateTime();

		for (Task task : tasks) {
			if (task.isDone()) {
				break; // if task is done, we do not need to add it
			}
			TaskType taskType = task.getTaskType();
			assert taskType!=null;
			if (taskType == TaskType.FLOATING) {
				tasksToDisplay.add(task);
			} else {
				if (task.getEndDateTime().isAfter(startDateTime)) {
					tasksToDisplay.add(task);
				}
			}
		}
		return tasksToDisplay;
	}

	private ArrayList<Task> findTasksBetweenDates(ArrayList<Task> tasks) {
		ArrayList<Task> tasksToDisplay = new ArrayList<Task>();
		LocalDateTime startDateTime = this.getStartDateTime();
		LocalDateTime endDateTime = this.getStartDateTime();

		for (Task task : tasks) {

			if (task.isDone()) {
				continue;
			} else {
				TaskType taskType = task.getTaskType();
				if (taskType == TaskType.FLOATING) {
					tasksToDisplay.add(task);

				} else if (taskType == TaskType.DEADLINE) {
					if (task.getEndDateTime().isAfter(startDateTime)
							&& task.getEndDateTime().isBefore(endDateTime)) {
						tasksToDisplay.add(task);
					}
				} else {
					assert taskType != null;
					if (task.getStartDateTime().isBefore(endDateTime)
							|| task.getEndDateTime().isAfter(startDateTime)) {
						tasksToDisplay.add(task);
					}
				}

			}
		}
		return tasksToDisplay;
	}

	private ArrayList<Task> findSearchedTasks(ArrayList<Task> tasks) {
		ArrayList<Task> searchedTasks = new ArrayList<Task>();
		for (Task task : tasks) {
			String taskTitle = task.getTitle();
			taskTitle = taskTitle.toLowerCase();
			if (taskTitle.contains(_searchKeywords.trim())) {
				searchedTasks.add(task);
			}
		}
		return searchedTasks;
	}

	private void executeViewOverdue() {
		ArrayList<Task> tasks = null;
		ArrayList<Task> tasksToDisplay = new ArrayList<Task>();
		LocalDateTime now = LocalDateTime.now();
		
		try {
			tasks = Taskie.Controller.getStorage().getTaskList();
		} catch (TaskRetrievalFailedException e) {
			Taskie.Controller.getUI().display(e.getMessage());
		}
		
		for (Task task:tasks){
			if(task.isDone()){
				break;
			}
			TaskType taskType=task.getTaskType();
			if(taskType==TaskType.FLOATING){
				tasksToDisplay.add(task);
			}else if(task.getEndDateTime().isBefore(now)){
				tasksToDisplay.add(task);
			}
		}
		tasksToDisplay.sort(new TaskEndDateComparator());
		Taskie.Controller.getUI().display(tasksToDisplay.toArray(new Task[tasksToDisplay.size()]));
			
	}
	


	private void executeViewCompleted() {
		ArrayList<Task> tasks = null;
		ArrayList<Task> tasksToDisplay = new ArrayList<Task>();
		
		try {
			tasks = Taskie.Controller.getStorage().getTaskList();
		} catch (TaskRetrievalFailedException e) {
			Taskie.Controller.getUI().display(e.getMessage());
		}
		for (Task task:tasks){
			if(task.isDone()){
				tasksToDisplay.add(task);

			}
		}
		tasksToDisplay.sort(new TaskEndDateComparator());
		Taskie.Controller.getUI().display(tasksToDisplay.toArray(new Task[tasksToDisplay.size()]));
			
	}


	private void executeViewUpcoming() {
		ArrayList<Task> tasks = null;
		ArrayList<Task> tasksToDisplay = new ArrayList<Task>();
		LocalDateTime now = LocalDateTime.now();
		
		try {
			tasks = Taskie.Controller.getStorage().getTaskList();
		} catch (TaskRetrievalFailedException e) {
			Taskie.Controller.getUI().display(e.getMessage());
		}
		
		for (Task task:tasks){
			if(task.isDone()){
				break;
			}
			TaskType taskType=task.getTaskType();
			System.out.println(taskType);
			if(taskType==TaskType.FLOATING){
				tasksToDisplay.add(task);
			}else if(taskType==TaskType.DEADLINE 
					&& task.getEndDateTime().isAfter(now)){
				tasksToDisplay.add(task);
			}else if(task.getStartDateTime().isAfter(now)){
				tasksToDisplay.add(task);
			}
		}
		tasksToDisplay.sort(new TaskEndDateComparator());
		Taskie.Controller.getUI().display(tasksToDisplay.toArray(new Task[tasksToDisplay.size()]));
	}

	private ArrayList<Task> executeViewAll() {
		ArrayList<Task> tasksToDisplay = new ArrayList<Task>();
		try {

			ArrayList<Task> tasks = Taskie.Controller.getStorage()
					.getTaskList();
			for (Task task : tasks) {
				if (!task.isDone()) {

					tasksToDisplay.add(task);
				}
			}
			tasksToDisplay.sort(new TaskEndDateComparator());
			Taskie.Controller.getUI().display(
					tasksToDisplay.toArray(new Task[tasksToDisplay.size()]));
			return tasksToDisplay;
		} catch (TaskRetrievalFailedException e) {
			Taskie.Controller.getUI().display(e.getMessage());
		}
		return tasksToDisplay;
	}

	// @author A0121555M
	public void undo() throws UndoNotSupportedException {
		throw new UndoNotSupportedException();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("CommandType:View");
		sb.append("ViewType:" + _viewType + ",");

		try {
			sb.append("StartDateTime:" + this.getStartDateTime() + ",");
		} catch (NullPointerException e) {
			sb.append("StartDateTime:null,");
		}
		try {
			sb.append("EndDateTime:" + this.getEndDateTime() + ",");
		} catch (NullPointerException e) {
			sb.append("EndDateTime,");
		}

		sb.append("SearchKeywords:"
				+ ((_searchKeywords == null) ? "" : _searchKeywords));

		return sb.toString();
	}
}
