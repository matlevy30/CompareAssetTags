import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import au.com.bytecode.opencsv.CSVReader;

public class ReadingUAPM extends Reading {

	private CSVReader reader;

	public ReadingUAPM() throws FileNotFoundException {

		String fileName = "src/UAPM.csv";
		reader = new CSVReader(new FileReader(fileName));
		lines = new ArrayList<>();
	}

	public void readLines() throws IOException {

		// Header
		header = reader.readNext();
		// First Line
		String[] line = reader.readNext();

		while (line != null) {
			// Filtering Locations
			if (filterStatus(line) && filterLocation(line))
				lines.add(new UAPMSheet(line));
			line = reader.readNext();
		}

	}

	protected boolean filterLocation(String[] line) {
		// OCC Locations
		if (line[6].contains("Highlands Ranch")) {
			if (line[4].equals("Data Center") || line[4].contains("Data Hall") || line[4].contains("Telco")
					|| line[4].equals("Demarc") || line[4].equals("Hallway") || line[4].equals("Loading Dock")
					|| line[4].contains("Storage Room") || line[4].contains("Tape") || line[4].contains("VOCC")) {
				return true;
			}
		}
		// OCE Locations
		if (line[6].contains("Ashburn")) {
			if (line[4].contains("Pod") && !(line[5].equals("Tape Library"))) {
				return true;
			}
		}
		return false;
	}

	private boolean filterStatus(String[] line) {
		if (line[3].equals("In Use")) {
			return true;
		}
		return false;
	}

}
