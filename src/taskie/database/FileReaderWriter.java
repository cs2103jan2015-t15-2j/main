//@author A0121555M
package taskie.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;

import taskie.exceptions.FileExistsException;

public class FileReaderWriter {
	private static final String DEFAULT_ENCODING = "UTF-8";

	private Logger _logger;
	private Path _file;
	private OutputStreamWriter _writer;
	private BufferedReader _reader;

	public FileReaderWriter() {
		_logger = Logger.getLogger(FileReaderWriter.class.getName());
	}
	
	public FileReaderWriter(Path file) throws IOException {
		this();
		_logger.log(Level.INFO, "Opening File: " + file.toString());
		_file = file;
		if (!Files.exists(_file)) {
			initialize();
		}
	}

	private void initialize() throws IOException {
		Files.createFile(_file);
	}

	public void write(String str) throws IOException {
		OutputStream out = Files.newOutputStream(_file, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		_writer = new OutputStreamWriter(out, DEFAULT_ENCODING);
		_writer.write(str);
		_writer.flush();
	}

	public String read() throws IOException {
		InputStream in = Files.newInputStream(_file);
		BufferedReader _reader = new BufferedReader(new InputStreamReader(in, DEFAULT_ENCODING));
		String text = "";
		String line;

		while ((line = _reader.readLine()) != null) {
			text += line;
		}

		return text;
	}

	public void close() throws IOException {
		if (_writer != null) {
			_writer.close();
		}

		if (_reader != null) {
			_reader.close();
		}
	}
	
	public void moveFile(Path oldPath, Path newPath, boolean overwrite) throws FileExistsException, IOException {
		if ( Files.exists(newPath) && !overwrite ) {
			throw new FileExistsException(newPath.toString());
		}
			
		Files.move(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING);
	}

	public void deleteFile(Path path) {
		File fileTemp = new File(path.toString());
		if (fileTemp.exists()) {
			fileTemp.delete();
		}
	}
}
