import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

class WriteExcel {

    private static final String fileName = "CompareAssetTags/src/Missing.xlsx";
    private XSSFWorkbook workbook;

    WriteExcel() {
        workbook = new XSSFWorkbook();
    }

    void writtingSheet(String[] header, ArrayList<Sheet> list, String sheetName) throws IOException {
        XSSFSheet sheet = workbook.createSheet(sheetName);

        writtingHeader(header, sheet);
        writtingSheet(list, sheet);

        try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
            workbook.write(outputStream);
        }
    }

    private void writtingSheet(ArrayList<Sheet> list, XSSFSheet sheet) {
        int rowCount = 1;
        for (Sheet line:list) {
            String[] val = line.values;
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            for (String field:val) {
                Cell cell = row.createCell(columnCount++);
                cell.setCellValue(field);
            }
        }
    }

    private void writtingHeader(String[] header, XSSFSheet sheet) {
            Row row = sheet.createRow(0);
            int columnCount = 0;
            for (String field:header) {
                Cell cell = row.createCell(columnCount++);
                cell.setCellValue(field);
            }
    }

}
