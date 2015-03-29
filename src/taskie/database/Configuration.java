//@author A0135137L
package taskie.database;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import taskie.exceptions.ConfigurationFailedException;

public class Configuration {
	private static final String DEFAULT_STORAGE_DIRECTORY = System.getProperty("user.home");
	private static final String DATABASE_FILENAME = "taskie.txt";
	private static final String CONFIG_FILENAME = "config.txt";
	
	private Path _configPath;
	private Path _databasePath;
	private FileReaderWriter _frw;
	private Logger _logger;
	
	public Configuration() throws ConfigurationFailedException {
		String configPath = DEFAULT_STORAGE_DIRECTORY + "/" + CONFIG_FILENAME;
		_configPath = FileSystems.getDefault().getPath(configPath);
		_logger = Logger.getLogger(Configuration.class.getName());
		try {
			_frw = new FileReaderWriter(_configPath);
			String databasePath = _frw.read();
			
			if (databasePath.equals("")) {
				String filePath = DEFAULT_STORAGE_DIRECTORY + "/" + DATABASE_FILENAME;
				setDatabasePath(filePath);
			} else {
				_databasePath = FileSystems.getDefault().getPath(databasePath);
			}

			_logger.log(Level.INFO, "Configuration initialized.");
		} catch (IOException ex) {
			throw new ConfigurationFailedException();
		}
	}
	
	public Path getDatabasePath() {
		return _databasePath;
	}
	
	public void setDatabasePath (Path newDatabasePath) throws ConfigurationFailedException {
		try {
			_databasePath = newDatabasePath;
			_frw.write(_databasePath.toString());
		} catch (IOException ex) {
			throw new ConfigurationFailedException();
		}
	}
	
	public void setDatabasePath(String newDatabasePath) throws ConfigurationFailedException {
		Path databasePath = FileSystems.getDefault().getPath(newDatabasePath);
		setDatabasePath (databasePath);	
	}
	
}
