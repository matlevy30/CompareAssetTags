import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

class WriteExcel {

    private String fileName;
    private XSSFWorkbook workbook;

    WriteExcel(String location) {
        workbook = new XSSFWorkbook();
        fileName = "CompareAssetTags/src/" + location + "Missing.xlsx";
    }

    void writtingSheet(String[] header, ArrayList<Sheet> list, String sheetName, String location) throws IOException {
        XSSFSheet sheet = workbook.createSheet(sheetName);

        writtingHeader(header, sheet);
        writtingSheet(list, sheet, location);

        try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
            workbook.write(outputStream);
        }
    }

    private void writtingSheet(ArrayList<Sheet> list, XSSFSheet sheet, String location) {
        int rowCount = 1;
        for (Sheet line:list) {
            if (line.location().equals(location)) {
                String[] val = line.values;
                Row row = sheet.createRow(rowCount++);
                int columnCount = 0;
                for (String field : val) {
                    Cell cell = row.createCell(columnCount++);
                    cell.setCellValue(field);
                }
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
