import au.com.bytecode.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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
			if (exceptionTagsOCE(line) && exceptionTagsOCC(line)) {
				lines.add(new UAPMSheet(line));
			}
			line = reader.readNext();
		}

	}

	private boolean exceptionTagsOCE(String[] line) {
		String[] tags = { "5000006041", "1000124937", "1000124938", "1000094431" };
		for (String tag : tags) {
			if (tag.equals(line[1])) {
				return false;
			}
		}
		return true;
	}

	private boolean exceptionTagsOCC(String[] line) {
		String[] tags = {"1000120832", "1000120833", "1000081933", "1000065358"};
		for (String tag : tags) {
			if (tag.equals(line[1])) {
				return false;
			}
		}
		return true;
	}

}
