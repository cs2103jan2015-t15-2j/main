package taskie.database;

import taskie.models.Task;

public class Storage implements IStorage {
	
	public Storage(){
		
	}
	
	@Override
	public Boolean setStorageLocation(String fileDir) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Task[] retrieveTaskList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Task[] storeTaskList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean addFloatingTask(Task taskToAdd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean addDeadlineTask(Task taskToAdd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean addTimedTask(Task taskToAdd) {
		// TODO Auto-generated method stub
		return null;
	}

}
