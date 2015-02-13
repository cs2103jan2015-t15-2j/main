package taskie;

public class Taskie {
	
	iTask[] _taskList=null;
	iStorage _storage=null;
	iGoogleSync _googleSync=null;
	iParser _parser= null;
	iUI _ui=null;
	
	public static void main(String[] args) {
		try {
			new Taskie(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Taskie(String[] args) {
		initialise(args);
		printWelcomeMsg();
		executeCommandUntilExit();
		prepareForExit();
		
	}

	private void initialise(String[] args) {
		//initialise global variables
		
		initialiseGlobalObjects(args);
		
		_storage.setStorageLocation(args[0]);
		
		syncWithGoogle();
		
		
	}

	private void syncWithGoogle() {
		iTask[] taskListFromGoogle=_googleSync.syncFrom();
		iTask[] taskListFromStorage=_storage.retrieveTaskList();
		_taskList=syncTask(taskListFromGoogle,taskListFromStorage);
	}

	private iTask[] syncTask(iTask[] taskListFromGoogle,
			iTask[] taskListFromStorage) {
		return null;
	}

	
	private void initialiseGlobalObjects(String[] args) {
		_storage=new Storage();
		_googleSync = new googleSync();
		_parser = new parser();
		_ui=new ui();
	}
}
