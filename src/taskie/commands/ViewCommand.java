package taskie.commands;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

import taskie.Taskie;
import taskie.exceptions.TaskRetrievalFailedException;
import taskie.exceptions.UndoNotSupportedException;
import taskie.models.CommandType;
import taskie.models.Task;
import taskie.models.TaskType;
import taskie.models.ViewType;
import taskie.models.TaskEndDateComparator;

public class ViewCommand implements ICommand {
	private CommandType _commandType = CommandType.VIEW;

	//@author A0121555M
	private ViewType _viewType;
	private LocalDate _startDate;
	private LocalTime _startTime;
	private LocalDate _endDate;
	private LocalTime _endTime;
	private String _searchKeywords;

	public ViewCommand(ViewType viewType) {
		_viewType = viewType;
	}

	public ViewCommand(ViewType viewType, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, String searchKeywords) {
		super();
		_viewType = viewType;
		_startDate = startDate;
		_endDate = endDate;
		_startTime = startTime;
		_endTime = endTime;
		_searchKeywords = searchKeywords;
	}

	public ViewCommand(ViewType viewType, LocalDateTime startDateTime, LocalDateTime endDateTime, String searchKeywords) {
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
		if ( startDateTime == null ) {
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
		if ( endDateTime == null ) {
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

	//@author A0097582N
	@Override
	public void execute() {
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
		try {
			HashMap<TaskType, ArrayList<Task>> taskLists = Taskie.Controller.getStorage().retrieveTaskMap();
			ArrayList<Task> tasks = new ArrayList<Task>();
			if (taskLists.get(TaskType.DEADLINE) != null) {
				tasks.addAll(taskLists.get(TaskType.DEADLINE));
			}
			if (taskLists.get(TaskType.TIMED) != null) {
				tasks.addAll(taskLists.get(TaskType.TIMED));
			}
			if (taskLists.get(TaskType.FLOATING) != null) {
				tasks.addAll(taskLists.get(TaskType.FLOATING));
			}
			Taskie.Controller.getUI().display(findSearchedTasks(tasks));
		} catch (TaskRetrievalFailedException e) {
			Taskie.Controller.getUI().display(e.getMessage());
		}
	}

	private Task[] findSearchedTasks(ArrayList<Task> tasks) {
		ArrayList<Task> searchedTasks = new ArrayList<Task>();
		for (Task task : tasks) {
			String taskTitle=task.getTitle();
			taskTitle=taskTitle.toLowerCase();
			if (taskTitle.contains(_searchKeywords.trim())) {
				searchedTasks.add(task);
			}
		}
		return searchedTasks.toArray(new Task[searchedTasks.size()]);
	}

	private void executeViewCompleted() {
		try {
			HashMap<TaskType, ArrayList<Task>> taskLists = Taskie.Controller.getStorage().retrieveTaskMap();
			ArrayList<Task> tasks = new ArrayList<Task>();
			if (taskLists.get(TaskType.DEADLINE) != null) {
				tasks.addAll(taskLists.get(TaskType.DEADLINE));
			}
			if (taskLists.get(TaskType.TIMED) != null) {
				tasks.addAll(taskLists.get(TaskType.TIMED));
			}
			if (taskLists.get(TaskType.FLOATING) != null) {
				tasks.addAll(taskLists.get(TaskType.FLOATING));
			}
			Taskie.Controller.getUI().display(findCompletedTasks(tasks));
		} catch (TaskRetrievalFailedException e) {
			Taskie.Controller.getUI().display(e.getMessage());
		}
	}

	private Task[] findCompletedTasks(ArrayList<Task> tasks) {
		ArrayList<Task> completedTasks = new ArrayList<Task>();
		for (Task task : tasks) {
			if (task.isDone()) {
				completedTasks.add(task);
			}
		}
		return completedTasks.toArray(new Task[completedTasks.size()]);
	}

	private void executeViewOverdue() {
		try {
			HashMap<TaskType, ArrayList<Task>> taskLists = Taskie.Controller.getStorage().retrieveTaskMap();
			ArrayList<Task> tasksWithDate = new ArrayList<Task>();
			tasksWithDate.addAll(taskLists.get(TaskType.DEADLINE));
			tasksWithDate.addAll(taskLists.get(TaskType.TIMED));
			Taskie.Controller.getUI().display(findOverDueTasksAndSort(tasksWithDate));
		} catch (TaskRetrievalFailedException e) {
			Taskie.Controller.getUI().display(e.getMessage());
		}
	}

	private Task[] findOverDueTasksAndSort(ArrayList<Task> tasksWithDate) {
		ArrayList<Task> unmarkedAndOverdueTask = new ArrayList<Task>();
		for (Task task : tasksWithDate) {
			if (!task.isDone()
					&& task.getEndDateTime().isBefore(LocalDateTime.now())) {
				unmarkedAndOverdueTask.add(task);
			}
		}
		unmarkedAndOverdueTask.sort(new taskie.models.TaskEndDateComparator());
		return unmarkedAndOverdueTask.toArray(new Task[unmarkedAndOverdueTask.size()]);
	}

	private void executeViewUpcoming() {
		try {
			HashMap<TaskType, ArrayList<Task>> taskLists = Taskie.Controller.getStorage().retrieveTaskMap();
			ArrayList<Task> tasksWithDate = new ArrayList<Task>();
			if (taskLists.get(TaskType.DEADLINE) != null) {
				tasksWithDate.addAll(taskLists.get(TaskType.DEADLINE));
			}
			if (taskLists.get(TaskType.TIMED) != null) {
				tasksWithDate.addAll(taskLists.get(TaskType.TIMED));
			}
			tasksWithDate = findTasksAfterTodayAndSort(tasksWithDate);
			ArrayList<Task> tasksWithoutDate = taskLists.get(TaskType.FLOATING);
			if (tasksWithoutDate != null) {
				tasksWithoutDate = findUndoneFloatingTask(tasksWithoutDate);
				tasksWithDate.addAll(tasksWithoutDate);
			}
			Taskie.Controller.getUI().display(tasksWithDate.toArray(new Task[tasksWithDate.size()]));
		} catch (TaskRetrievalFailedException e) {
			Taskie.Controller.getUI().display(e.getMessage());
		}
	}

	private ArrayList<Task> findUndoneFloatingTask(
			ArrayList<Task> tasksWithoutDate) {
		ArrayList<Task> unmarkedTasks = new ArrayList<Task>();
		for (Task task : tasksWithoutDate) {
			if (!task.isDone()) {
				unmarkedTasks.add(task);
			}
		}
		return unmarkedTasks;
	}

	private ArrayList<Task> findTasksAfterTodayAndSort(
			ArrayList<Task> tasksWithDate) {
		ArrayList<Task> tasksAfterToday = new ArrayList<Task>();
		for (Task task : tasksWithDate) {
			LocalDateTime now = LocalDateTime.now();
			if (task.getEndDateTime().isAfter(now)
					&& !task.isDone())
				tasksAfterToday.add(task);
		}
		tasksAfterToday.sort(new TaskEndDateComparator());
		return tasksAfterToday;
	}

	private ArrayList<Task> executeViewAll() {
		ArrayList<Task> tasksToDisplay = new ArrayList<Task>();
		try {
			ArrayList<Task> tasks=Taskie.Controller.getStorage().getTaskList();
			for(Task task :tasks){
				if(!task.getTaskStatus()){
					tasksToDisplay.add(task);
				}
			}
			tasksToDisplay.sort(new TaskEndDateComparator());
			Taskie.Controller.getUI().display(tasksToDisplay.toArray(new Task[tasksToDisplay.size()]));
			return tasksToDisplay;
		} catch (TaskRetrievalFailedException e) {
			Taskie.Controller.getUI().display(e.getMessage());
		}
		return tasksToDisplay;
	}

	
	//@author A0121555M
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

		sb.append("SearchKeywords:" + ((_searchKeywords == null) ? "" : _searchKeywords));
		
		return sb.toString();
	}
}
