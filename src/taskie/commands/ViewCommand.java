package taskie.commands;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

import taskie.Taskie;
import taskie.models.CommandType;
import taskie.models.Task;
import taskie.models.ViewType;
import taskie.models.TaskEndDateComparator;

public class ViewCommand implements ICommand {

	private static final String DEADLINED_TASKNAME = "deadlined";
	private static final String TIMED_TASKNAME = "timed";
	private static final String FLOATING_TASKNAME = "floating";

	private CommandType _commandType = CommandType.VIEW;

	// @author A0121555M
	private ViewType _viewType;
	private LocalDate _startDate;
	private LocalDate _endDate;
	private LocalTime _startTime;
	private LocalTime _endTime;
	private String _searchKeywords;

	public ViewCommand(ViewType viewType) {
		_viewType = viewType;
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

	// @author A0097582N
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
		HashMap<String, ArrayList<Task>> taskLists = Taskie.Controller
				.getStorage().retrieveTaskMap();

		ArrayList<Task> tasks = new ArrayList<Task>();
		if (taskLists.get(DEADLINED_TASKNAME) != null) {
			tasks.addAll(taskLists.get(DEADLINED_TASKNAME));
		}
		if (taskLists.get(TIMED_TASKNAME) != null) {
			tasks.addAll(taskLists.get(TIMED_TASKNAME));
		}
		if (taskLists.get(FLOATING_TASKNAME) != null) {
			tasks.addAll(taskLists.get(FLOATING_TASKNAME));
		}
		Taskie.Controller.getUI().display(findSearchedTasks(tasks));

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
		return toArray(searchedTasks);
	}

	private void executeViewCompleted() {
		HashMap<String, ArrayList<Task>> taskLists = Taskie.Controller
				.getStorage().retrieveTaskMap();
		ArrayList<Task> tasks = new ArrayList<Task>();
		if (taskLists.get(DEADLINED_TASKNAME) != null) {
			tasks.addAll(taskLists.get(DEADLINED_TASKNAME));
		}
		if (taskLists.get(TIMED_TASKNAME) != null) {
			tasks.addAll(taskLists.get(TIMED_TASKNAME));
		}
		if (taskLists.get(FLOATING_TASKNAME) != null) {
			tasks.addAll(taskLists.get(FLOATING_TASKNAME));
		}
		Taskie.Controller.getUI().display(findCompletedTasks(tasks));

	}

	private Task[] findCompletedTasks(ArrayList<Task> tasks) {
		ArrayList<Task> completedTasks = new ArrayList<Task>();
		for (Task task : tasks) {
			if (task.getTaskStatus()) {
				completedTasks.add(task);
			}
		}
		return toArray(completedTasks);
	}

	private void executeViewOverdue() {
		HashMap<String, ArrayList<Task>> taskLists = Taskie.Controller
				.getStorage().retrieveTaskMap();
		ArrayList<Task> tasksWithDate = new ArrayList<Task>();
		tasksWithDate.addAll(taskLists.get(DEADLINED_TASKNAME));
		tasksWithDate.addAll(taskLists.get(TIMED_TASKNAME));
		Taskie.Controller.getUI().display(
				findOverDueTasksAndSort(tasksWithDate));

	}

	private Task[] findOverDueTasksAndSort(ArrayList<Task> tasksWithDate) {
		ArrayList<Task> unmarkedAndOverdueTask = new ArrayList<Task>();
		for (Task task : tasksWithDate) {
			if (!task.getTaskStatus()
					&& task.getEndDateTime().isBefore(LocalDateTime.now())) {
				unmarkedAndOverdueTask.add(task);
			}
		}
		unmarkedAndOverdueTask.sort(new taskie.models.TaskEndDateComparator());
		return toArray(unmarkedAndOverdueTask);
	}

	private void executeViewUpcoming() {
		HashMap<String, ArrayList<Task>> taskLists = Taskie.Controller
				.getStorage().retrieveTaskMap();
		ArrayList<Task> tasksWithDate = new ArrayList<Task>();
		if (taskLists.get(DEADLINED_TASKNAME) != null) {
			tasksWithDate.addAll(taskLists.get(DEADLINED_TASKNAME));
		}
		if (taskLists.get(TIMED_TASKNAME) != null) {
			tasksWithDate.addAll(taskLists.get(TIMED_TASKNAME));
		}
		tasksWithDate = findTasksAfterTodayAndSort(tasksWithDate);
		ArrayList<Task> tasksWithoutDate = taskLists.get(FLOATING_TASKNAME);
		if (tasksWithoutDate != null) {
			tasksWithoutDate = findUndoneFloatingTask(tasksWithoutDate);
			tasksWithDate.addAll(tasksWithoutDate);
		}
		Taskie.Controller.getUI().display(toArray(tasksWithDate));

	}

	private ArrayList<Task> findUndoneFloatingTask(
			ArrayList<Task> tasksWithoutDate) {
		ArrayList<Task> unmarkedTasks = new ArrayList<Task>();
		for (Task task : tasksWithoutDate) {
			if (!task.getTaskStatus()) {
				unmarkedTasks.add(task);
			}
		}
		return unmarkedTasks;
	}

	private ArrayList<Task> findTasksAfterTodayAndSort(
			ArrayList<Task> tasksWithDate) {
		ArrayList<Task> tasksAfterToday = new ArrayList<Task>();
		for (Task task : tasksWithDate) {
			if (task.getEndDateTime().isAfter(LocalDateTime.now())
					&& !task.getTaskStatus())
				tasksAfterToday.add(task);
		}
		tasksAfterToday.sort(new TaskEndDateComparator());
		return tasksAfterToday;
	}

	private void executeViewAll() {
		HashMap<String, ArrayList<Task>> taskLists = Taskie.Controller
				.getStorage().retrieveTaskMap();
		ArrayList<Task> tasksWithDate = new ArrayList<Task>();
		if (taskLists.get(DEADLINED_TASKNAME) != null) {
			tasksWithDate.addAll(taskLists.get(DEADLINED_TASKNAME));
		}
		if (taskLists.get(TIMED_TASKNAME) != null) {
			tasksWithDate.addAll(taskLists.get(TIMED_TASKNAME));
		}
		ArrayList<Task> tasksWithoutDate = taskLists.get(FLOATING_TASKNAME);
		tasksWithDate.sort(new TaskEndDateComparator());
		if (tasksWithoutDate != null) {
			tasksWithDate.addAll(tasksWithoutDate);
		}
		Taskie.Controller.getUI().display(toArray(tasksWithDate));
	}

	private Task[] toArray(ArrayList<Task> taskList) {
		int size = taskList.size();
		Task[] taskListArr = new Task[size];
		for (int i = 0; i < size; i++) {
			taskListArr[i] = taskList.get(i);
		}
		return taskListArr;
	}
	
	//@author A0121555M
	@Override	
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

		sb.append("SearchKeywords:" + _searchKeywords);
		
		return sb.toString();
	}
}
