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
		return LocalDateTime.of(_startDate,
				(_startTime == null) ? LocalTime.MAX : _startTime);
	}

	public void setStartDateTime(LocalDateTime startDateTime) {
		this.setStartDate(startDateTime.toLocalDate());
		this.setStartTime(startDateTime.toLocalTime());
	}

	public LocalDateTime getEndDateTime() {
		return LocalDateTime.of(_endDate, (_endTime == null) ? LocalTime.MAX
				: _endTime);
	}

	public void setEndDateTime(LocalDateTime endDateTime) {
		this.setEndDate(endDateTime.toLocalDate());
		this.setEndTime(endDateTime.toLocalTime());
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
		this._searchKeywords = searchKeywords;
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
		// TODO Auto-generated method stub

	}

	private void executeViewCompleted() {
		HashMap<String, ArrayList<Task>> taskLists = Taskie.Storage
				.retrieveTaskMap();
		ArrayList<Task> tasks = taskLists.get(DEADLINED_TASKNAME);
		tasks.addAll(taskLists.get(TIMED_TASKNAME));
		tasks.addAll(taskLists.get(FLOATING_TASKNAME));
		Taskie.UI.display(findCompletedTasks(tasks));

	}

	private Task[] findCompletedTasks(ArrayList<Task> tasks) {
		ArrayList<Task> completedTasks = new ArrayList<Task>();
		for(Task task : tasks){
			if(task.getTaskStatus()){
				completedTasks.add(task);
			}
		}
		return (Task[]) completedTasks.toArray();
	}

	private void executeViewOverdue() {
		HashMap<String, ArrayList<Task>> taskLists = Taskie.Storage
				.retrieveTaskMap();
		ArrayList<Task> tasksWithDate = taskLists.get(DEADLINED_TASKNAME);
		tasksWithDate.addAll(taskLists.get(TIMED_TASKNAME));
		Taskie.UI.display(findOverDueTasksAndSort(tasksWithDate));

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
		return (Task[]) unmarkedAndOverdueTask.toArray();
	}

	private void executeViewUpcoming() {
		HashMap<String, ArrayList<Task>> taskLists = Taskie.Storage
				.retrieveTaskMap();
		ArrayList<Task> tasksWithDate = taskLists.get(DEADLINED_TASKNAME);
		tasksWithDate.addAll(taskLists.get(TIMED_TASKNAME));
		ArrayList<Task> tasksWithoutDate = taskLists.get(FLOATING_TASKNAME);
		Task[] tasksWithDateArr = findTasksAfterTodayAndSort(tasksWithDate);
		Task[] tasksWithoutDateArr = findUndoneFloatingTask(tasksWithoutDate);
		Taskie.UI.display(tasksWithDateArr);
		Taskie.UI.display(tasksWithoutDateArr);

	}

	private Task[] findUndoneFloatingTask(ArrayList<Task> tasksWithoutDate) {
		ArrayList<Task> unmarkedTasks = new ArrayList<Task>();
		for (Task task : tasksWithoutDate) {
			if (!task.getTaskStatus()) {
				unmarkedTasks.add(task);
			}
		}
		return (Task[]) unmarkedTasks.toArray();
	}

	private Task[] findTasksAfterTodayAndSort(ArrayList<Task> tasksWithDate) {
		ArrayList<Task> tasksAfterToday = new ArrayList<Task>();
		for (Task task : tasksWithDate) {
			if (task.getEndDateTime().isBefore(LocalDateTime.now())
					&& !task.getTaskStatus())
				tasksAfterToday.add(task);
		}
		tasksAfterToday.sort(new TaskEndDateComparator());
		return (Task[]) tasksAfterToday.toArray();
	}

	private void executeViewAll() {
		HashMap<String, ArrayList<Task>> taskLists = Taskie.Storage
				.retrieveTaskMap();
		ArrayList<Task> tasksWithDate = taskLists.get(DEADLINED_TASKNAME);
		tasksWithDate.addAll(taskLists.get(TIMED_TASKNAME));
		ArrayList<Task> tasksWithoutDate = taskLists.get(FLOATING_TASKNAME);
		tasksWithDate.sort(new TaskEndDateComparator());
		Taskie.UI.display((Task[]) tasksWithoutDate.toArray());
		Taskie.UI.display((Task[]) tasksWithoutDate.toArray());
	}
}
