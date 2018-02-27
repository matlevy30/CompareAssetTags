import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class ReadingNlyte extends Reading {
	private XSSFSheet reader;

	ReadingNlyte() throws IOException {
		String fileName = "CompareAssetTags/src/Nlyte_Assets.xlsx";
		XSSFWorkbook myWorkBook = new XSSFWorkbook(new FileInputStream(fileName));
		reader = myWorkBook.getSheetAt(0);
		myWorkBook.close();
		lines = new ArrayList<>();
		// Header Fields
		header = new String[12];
	}

	public void readLines() throws IOException {
		// Get iterator to all the rows in current sheet
		Iterator<Row> rowIterator = reader.iterator();
		// Blank Lines
		rowIterator.next();
		rowIterator.next();
		// Header
		createHeader(rowIterator.next());
		while (rowIterator.hasNext()) {
			// Grabbing each row
			Row row = rowIterator.next();
			// For each row, iterate through each columns
			Iterator<Cell> cellIterator = row.cellIterator();
			String[] line = cellIterator(cellIterator);
			if (filterLocation(line) && filterType(line)) {
				if (blankTag(line) || blankSerial(line)) {
					lines.add(new NlyteSheet(line));
					
				}
			}
		}
	}
	
	// Filtering Blank and N/A Serial Number
	private boolean blankSerial(String[] line) {
		return !line[3].equals("") && !line[3].equals("N/A");
	}

	// Filtering Blank and N/A Asset Tag
	private boolean blankTag(String[] line) {
		String tag = line[4].toUpperCase();
		return !tag.equals("") && !tag.equals("N/A") && !tag.contains("CHILD");
	}

	// Filtering Type
	private boolean filterType(String[] line) {
		return line[10].equals("Server") || line[10].equals("Cabinet") || line[10].equals("Chassis")
				|| line[10].equals("Peripheral") || line[10].equals("KVMSwitch") || line[10].equals("Network")
				|| line[10].equals("Powerstrip");
	}

	// Filtering Locations
	protected boolean filterLocation(String[] line) {
		// OCC Locations Rooms
		if (line[7].contains("Highlands Ranch")) {
			if (line[7].contains("Data Hall") || line[7].contains("Floor") || line[7].contains("PKI Cage") || line[7].contains("SDC-2-AAA-75 Storage")) {
				return true;
			}
		}
		// OCE Locations Rooms
		else if (line[7].contains("Ashburn")) {
			if (line[7].contains("POD ") || line[7].contains("Back Office") || line[7].contains("Admin and Security\\Floor")) {
				return true;
			}
		} else if (line[7].contains("Singapore")) {
			if (line[7].contains("Floor 6") && !(line[4].equals("H1YZZ52")))
			return true;
		}
		return false;
	}

	// Getting the header of the file
	private void createHeader(Row row) {
		// For each row, iterate through each columns
		Iterator<Cell> cellIterator = row.cellIterator();
		header = cellIterator(cellIterator);
	}

	private String[] cellIterator(Iterator<Cell> cellIterator) {
		String[] values = new String[12];
		int i = 0;
		while (cellIterator.hasNext()) {
			// Read each column cell
			Cell cell = cellIterator.next();

			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC:
				values[i] = Double.toString(cell.getNumericCellValue());
				break;
			case Cell.CELL_TYPE_STRING:
				values[i] = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_BLANK:
				values[i] = " ";
				break;
			default:
				values[i] = " ";
			}
			++i;
		}
		return values;
	}

}
