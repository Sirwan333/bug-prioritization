import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class getGoldStandard {
    public String[] getGS(String inputFile) throws IOException {

        FileInputStream fs = new FileInputStream(inputFile);
        XSSFWorkbook workbook = new XSSFWorkbook(fs);
        XSSFSheet sheet = workbook.getSheetAt(1);
        int bugSize = sheet.getLastRowNum();
        String[] gs = new String[bugSize];
        for (int i = 0; i < bugSize; i++){
            Row row = sheet.getRow(i+1);
            Cell cell = row.getCell(0);
            String bug;
            if (cell.toString().charAt(3) == '0'){
                bug = "BUG"+cell.toString().substring(4,cell.toString().length());
            } else {
                bug = "BUG"+cell.toString().substring(3,cell.toString().length());
            }

            gs[i] = bug;

        }
        workbook.close();
        fs.close();
        return gs;
    }
}
