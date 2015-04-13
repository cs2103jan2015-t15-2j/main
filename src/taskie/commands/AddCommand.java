package taskie.commands;

/**
 * class representing an add command.
 * Still under development
 * Bugs: none known
 *
 */
//@author A0097582N
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import taskie.exceptions.TaskModificationFailedException;
import taskie.exceptions.TaskRetrievalFailedException;
import taskie.exceptions.TaskTypeNotSupportedException;
import taskie.models.CommandType;
import taskie.models.DisplayType;
import taskie.models.Messages;
import taskie.models.Task;
import taskie.models.TaskType;

public class AddCommand extends AbstractCommand {
	private String _taskName;
	private LocalDate _startDate;
	private LocalTime _startTime;
	private LocalDate _endDate;
	private LocalTime _endTime;
	private CommandType _commandType = CommandType.ADD;
	private Task _task;
	private ArrayList<Task> _conflictedTask;

	public AddCommand() {
		_taskName = null;
		_startDate = null;
		_startTime = null;
		_endDate = null;
		_endTime = null;
	}

	public AddCommand(Task task) {
		_taskName = task.getTitle();
		_startDate = task.getStartDate();
		_startTime = task.getStartTime();
		_endDate = task.getEndDate();
		_endTime = task.getEndTime();
	}

	public AddCommand(String taskName, LocalDate startDate,
			LocalTime startTime, LocalDate endDate, LocalTime endTime) {
		super();
		_taskName = taskName;
		_startDate = startDate;
		_startTime = startTime;
		_endDate = endDate;
		_endTime = endTime;
	}

	public AddCommand(String taskName, LocalDateTime startDateTime,
			LocalDateTime endDateTime) {
		super();
		_taskName = taskName;
		this.setStartDateTime(startDateTime);
		this.setEndDateTime(endDateTime);
	}

	public String getTaskName() {
		return _taskName;
	}

	public void setTaskName(String _taskName) {
		this._taskName = _taskName;
	}

	public LocalDate getStartDate() {
		return _startDate;
	}

	public void setStartDate(LocalDate _startDate) {
		this._startDate = _startDate;
	}

	public LocalTime getStartTime() {
		return _startTime;
	}

	public void setStartTime(LocalTime _startTime) {
		this._startTime = _startTime;
	}

	public LocalDate getEndDate() {
		return _endDate;
	}

	public void setEndDate(LocalDate _endDate) {
		this._endDate = _endDate;
	}

	public LocalTime getEndTime() {
		return _endTime;
	}

	public void setEndTime(LocalTime _endTime) {
		this._endTime = _endTime;
	}

	//@author A0121555M
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

	public CommandType getCommandType() {
		return _commandType;
	}

	public void execute() {
		assert _taskName != null;
		try {
			_task = new Task(_taskName, _startDate, _startTime, _endDate,
					_endTime);
			if (hasNoConflict()) {
				_controller.getStorage().addTask(_task);
				_controller.setLastTask(_task);
				_controller.getUI().display(DisplayType.SUCCESS,
						formatAddMsg(_task));
			} else {
				_controller.getStorage().addTask(_task);
				_controller.setLastTask(_task);
				_controller.getUI().display(DisplayType.ERROR,formatAddMsgWithWarning(_task));
			}
		} catch (TaskRetrievalFailedException e) {
			try {
				_controller.getStorage().addTask(_task); 	// this branch occurs
				_controller.setLastTask(_task);				// when task
															// retrieval fails
															// (for sanity check
															// purposes)
															// even if task
															// retrieval fails,
															// we ought to
															// try to add task.
			} catch (TaskTypeNotSupportedException
					| TaskModificationFailedException e1) {
				_controller.getUI().display(DisplayType.ERROR, e.getMessage());
			}
		} catch (TaskTypeNotSupportedException e) {
			_controller.getUI().display(DisplayType.ERROR, e.getMessage());
		} catch (TaskModificationFailedException e) {
			_controller.getUI().display(DisplayType.ERROR, e.getMessage());
		}
	}


	//@author A0097582N
	private boolean hasNoConflict() throws TaskRetrievalFailedException {
		Boolean returnVal=true;
		_conflictedTask = new ArrayList<Task>();
		ArrayList<Task> list = _controller.getStorage().getTaskList();
		for (int i=0;i<list.size();i++) {
			Task task = list.get(i);
			if (hasConflict(task, this._task)) {
				_conflictedTask.add(task);
				returnVal=false;
			}
		}
		return returnVal;
	}

	private boolean hasConflict(Task task1, Task task2) {
		if (task1.getTaskType() == TaskType.TIMED
				&& task2.getTaskType() == TaskType.TIMED) {
			if(hasTimeOverlap(task1, task2)){
				return true;
			}
		}
		return false;
	}

	private boolean hasTimeOverlap(Task task1, Task task2) {
		if(task1.getStartDateTime().isBefore(task2.getEndDateTime())
				&&task2.getStartDateTime().isBefore(task1.getEndDateTime())){
		return true;
		}
		return false;
	}

	private String formatAddMsg(Task task) {
		TaskType type = task.getTaskType();
		if (type == TaskType.FLOATING) {
			return String.format(taskie.models.Messages.ADD_FLOATING,
					task.getTitle());
		} else if (type == TaskType.DEADLINE) {
			return String.format(taskie.models.Messages.ADD_DEADLINED,
					task.getTitle(),
					_controller.getUI().formatDateTime(task.getEndDateTime()));
		} else {
			return String
					.format(taskie.models.Messages.ADD_TIMED,
							task.getTitle(),
							_controller.getUI().formatDateTime(
									task.getStartDateTime()),
							_controller.getUI().formatDateTime(
									task.getEndDateTime()));
		}
	}
	
	private String formatAddMsgWithWarning(Task newTask) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format(Messages.ADD_TIMED, newTask.getTitle()
				, _controller.getUI().formatDateTime(newTask.getStartDateTime())
				, _controller.getUI().formatDateTime(newTask.getEndDateTime())));
			sb.append(Messages.ADD_CONFLICT);
		for(int i=0;i<_conflictedTask.size();i++){
			Task task = _conflictedTask.get(i);
			if(i==_conflictedTask.size()-1 && i !=0){
				sb.append("and ");
				sb.append(task.getTitle());
				sb.append(".");
			}else if(i==_conflictedTask.size()-1 && i==0){
				sb.append(task.getTitle()+".");
			}else{
				sb.append(task.getTitle());
				sb.append(", ");
			}
		}
		sb.append(Messages.NEWLINE);
		return sb.toString();
	}

	//@author A0121555M
	@Override
	public void undo() {
		new DeleteCommand(_task).execute();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("CommandType:" + _commandType + ",");
		sb.append("TaskName:" + _taskName + ",");

		try {
			sb.append("StartDateTime:" + this.getStartDateTime() + ",");
		} catch (NullPointerException e) {
			sb.append("StartDateTime:null,");
		}
		try {
			sb.append("EndDateTime:" + this.getEndDateTime());
		} catch (NullPointerException e) {
			sb.append("EndDateTime");
		}

		return sb.toString();
	}
}
