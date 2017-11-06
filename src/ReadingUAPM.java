import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import au.com.bytecode.opencsv.CSVReader;

public class ReadingUAPM extends Reading {

	private CSVReader reader;

	ReadingUAPM() throws FileNotFoundException {

		String fileName = "CompareAssetTags/src/UAPM.csv";
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
			if (filterStatus(line) && filterLocation(line)) {
					lines.add(new UAPMSheet(line));
			}
			line = reader.readNext();
		}

	}

	protected boolean filterLocation(String[] line) {
		// OCC Locations
		if (line[9].contains("Highlands Ranch")) {
			if (line[4].equals("Data Center") || line[4].contains("Data Hall") || line[4].contains("Telco")
					|| line[4].equals("Demarc") || line[4].equals("Hallway") || line[4].equals("Loading Dock")
					|| line[4].contains("Storage Room") || line[4].contains("Tape") || line[4].contains("VOCC")) {
				return exceptionTagsOCC(line);
			}
		}
		// OCE Locations
		if (line[6].contains("Ashburn")) {
			if (line[4].contains("Pod") && !(line[5].equals("Tape Library"))) {
				return exceptionTags(line);
			}
		}
		return false;
	}

	private boolean filterStatus(String[] line) {
		return line[3].equals("In Use");
	}

	private boolean exceptionTags(String[] line) {
		String[] tags = { "5000006041", "1000124937", "1000124938", "1000094431" };
		for (String tag : tags) {
			if (tag.equals(line[1])) {
				return false;
			}
		}
		return true;
	}
	
	private boolean exceptionTagsOCC(String[] line) {
		String[] tags = { "1000120832", "1000120833" };
		for (String tag : tags) {
			if (tag.equals(line[1])) {
				return false;
			}
		}
		return true;
	}

}
