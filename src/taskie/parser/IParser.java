package taskie.parser;

//parser is responsible for taking in input from user. 
public interface IParser {
	
	//parses input to string[]
	String[] getInput(String userInput);
}
