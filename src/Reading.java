import java.io.IOException;
import java.util.ArrayList;

public abstract class Reading {
	
	protected ArrayList<Sheet> lines;
	protected String[] header;
	
	
	public ArrayList<Sheet> getList() {
		return lines;
	}
	
	public abstract void readLines() throws IOException;
	
	protected abstract boolean filterLocation(String[] line);
	
	
	//Return Header of File 
	public String[] getHeader() {
		return header;
	}
	
}
