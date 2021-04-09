import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class getBugSize {
    public int getSize(String inputFile) throws IOException {
        FileInputStream fs = new FileInputStream(inputFile);
        XSSFWorkbook workbook = new XSSFWorkbook(fs);
        XSSFSheet sheet = workbook.getSheetAt(0);
        int bugSize = sheet.getLastRowNum();
        return bugSize;
    }
}
