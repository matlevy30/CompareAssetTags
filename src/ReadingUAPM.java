import com.opencsv.CSVReader;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.prefs.CsvPreference;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class ReadingUAPM extends Reading {

	private CSVReader reader;

	ReadingUAPM() throws IOException {

		String fileName = "CompareAssetTags/src/UAPM.csv";
		Reader file = Files.newBufferedReader(Paths.get(fileName));

		reader = new CSVReader(file);
		lines = new ArrayList<>();
	}

	/**
	 * Sets up the processors used for the examples. There are 10 CSV columns, so 10 processors are defined. Empty
	 * columns are read as null (hence the NotNull() for mandatory columns).
	 */
	private static CellProcessor[] getProcessors() {
		final CellProcessor[] processors = new CellProcessor[]{
				new NotNull(), // Asset Name
				new NotNull(), // Asset Tag
				new NotNull(), // Serial Number
				new NotNull(), // Status
				new NotNull(), // Location Room
				new NotNull(), // Grid
				new NotNull(), // Floor
				new NotNull(), // Model Name
				new NotNull(), // Model Number
				new NotNull(), // Location
				new NotNull() // Class
		};

		return processors;
	}

	@Override
	protected boolean filterLocation(String[] line) {
		// OCC Locations
		if (line[9].contains("Highlands Ranch OCC")) {
			return true;
		}
		// OCE Locations
		else if (line[9].contains("Ashburn")) {
			return true;
		}
		//OCS Locations
		else if (line[9].contains("Singapore KC2")) {
			return true;
		}
		//Retire Location
		else if (line[9].equals("Disposal Facility")) {
			return true;
		}
		//Empty Retire Location
		else if (line[9].equals("")) {
			return true;
		}
		return false;
	}

	public void readLines() throws IOException {

		ICsvListReader listReader = null;
		try {
			listReader = new CsvListReader(new FileReader("CompareAssetTags/src/UAPM.csv"), CsvPreference.EXCEL_PREFERENCE);

			header = listReader.getCSVHeader(true);
			final CellProcessor[] processors = getProcessors();

			List<String> customerList;
			while ((customerList = listReader.read(processors)) != null) {
				String[] array = customerList.stream().toArray(String[]::new);
				// Filtering Locations
				if (filterLocation(array)) {
					if (exceptionTagsOCC(array)) {
						lines.add(new UAPMSheet(array));
					}
				}
			}
		} finally {
			if (listReader != null) {
				listReader.close();
			}
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