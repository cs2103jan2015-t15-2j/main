//@author A0121555M
package taskie.parser;

import java.util.Date;
import java.util.List;

import com.joestelmach.natty.DateGroup;

public class NattyParser implements Parser {
	private com.joestelmach.natty.Parser _parser;

	public NattyParser() {
		_parser = new com.joestelmach.natty.Parser();
	}

	public void parse(String input) {
		List<DateGroup> groups = _parser.parse(input);
		for(DateGroup group : groups) {
			  List dates = group.getDates();
			  int line = group.getLine();
			  int column = group.getPosition();
			  String matchingValue = group.getText();
			  boolean isRecurreing = group.isRecurring();
			  Date recursUntil = group.getRecursUntil();
		}
	}
}
