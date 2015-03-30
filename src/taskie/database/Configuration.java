//@author A0135137L
package taskie.database;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import taskie.Controller;
import taskie.exceptions.ConfigurationFailedException;

public class Configuration {
	private static final String DEFAULT_STORAGE_DIRECTORY = System.getProperty("user.home");
	private static final String CONFIG_FILENAME = "config.txt";
	
	private Controller _controller;
	private static Configuration _config;
	private Path _configPath;
	private Path _databasePath;
	private FileReaderWriter _frw;
	private Logger _logger;
	
	private Configuration(Controller controller) throws ConfigurationFailedException {
		String configPath = DEFAULT_STORAGE_DIRECTORY + "/" + CONFIG_FILENAME;
		_controller = controller;
		_configPath = FileSystems.getDefault().getPath(configPath);
		_logger = Logger.getLogger(Configuration.class.getName());
		try {
			_frw = new FileReaderWriter(_configPath);
			String databasePath = _frw.read();
			
			if (databasePath.equals("")) {
				setDatabasePath(DEFAULT_STORAGE_DIRECTORY);
			} else {
				_databasePath = FileSystems.getDefault().getPath(databasePath);
			}

			_logger.log(Level.INFO, "Configuration initialized.");
		} catch (IOException ex) {
			throw new ConfigurationFailedException();
		}
	}
	
	public static Configuration getInstance(Controller controller) {
		try {
			if (_config == null) {
				_config = new Configuration(controller);
			}
			return _config;
		} catch (ConfigurationFailedException ex) {
			ex.getMessage();
			return null;
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
