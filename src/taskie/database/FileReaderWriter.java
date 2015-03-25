//@author A0121555M
package taskie.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileReaderWriter {
	private static final String DEFAULT_ENCODING = "UTF-8";

	private Path _file;
	private OutputStreamWriter _writer;
	private BufferedReader _reader;

	public FileReaderWriter(Path file) throws IOException {
		this._file = file;
	}

	private void initialize() throws IOException {
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
}
