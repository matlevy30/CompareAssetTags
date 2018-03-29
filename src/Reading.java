import java.io.IOException;
import java.util.ArrayList;

public abstract class Reading {

	ArrayList<Sheet> lines;
	String[] header;


	ArrayList<Sheet> getList() {
		return lines;
	}
	
	public abstract void readLines() throws IOException;

	protected boolean filterLocation(String[] line) {
		throw new UnsupportedOperationException("Can't do this with UAPM sheet");
	}


	//Return Header of File
	String[] getHeader() {
		return header;
	}
	
}
