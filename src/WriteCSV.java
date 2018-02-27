import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

class WriteCSV {

	// Delimiter used in CSV file
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";

	private String[] header;
	private ArrayList<Sheet> list;
	private String fileName;

	WriteCSV(String[] header, ArrayList<Sheet> list, String fileName) {
		this.header = header;
		this.list = list;
		this.fileName = fileName;
		
	}

	void wirte() {
		File f;
		FileWriter file = null;
		try {
			f = new File("CompareAssetTags\\src", fileName);
			file = new FileWriter(f);
			//Header
			int z = 0;
			while (z != header.length - 1) {
				file.append(header[z++]);
				file.append(COMMA_DELIMITER);
			}
			file.append(header[z]);
			file.append(NEW_LINE_SEPARATOR);
			
			
			//Rest of Content
			for (int i = 0; i != list.size(); ++i) {
				int j = 0;
				String[] l = list.get(i).getLine();
				while (j != l.length - 1) {
					file.append(l[j++]);
					file.append(COMMA_DELIMITER);
				}
				file.append(l[j]);
				file.append(NEW_LINE_SEPARATOR);
			}

		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} finally {
			try {
				file.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
				e.printStackTrace();
			}
		}

	}
}
