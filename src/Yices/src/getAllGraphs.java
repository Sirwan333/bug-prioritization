import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class getAllGraphs {


    int[][] bugMatrix;

    public getAllGraphs() throws IOException {

    }

    public int[][] getBugGraph(String inputFile, int bugSize, int cellPosition) throws IOException {
        bugMatrix = new int[bugSize][bugSize];
        FileInputStream fs = new FileInputStream(inputFile);
        XSSFWorkbook workbook = new XSSFWorkbook(fs);
        XSSFSheet sheet = workbook.getSheetAt(0);
        for (int i = 1; i < bugSize + 1; i++) {
            for (int j = i + 1; j < bugSize + 1; j++) {
                Row row = sheet.getRow(i);
                Row nextRow = sheet.getRow(j);

                Cell cell = row.getCell(0);
                Cell cell2 = nextRow.getCell(0);
                int size = cell.toString().length();

                int srcNode = Integer.parseInt(cell.toString().substring(3, 5));
                int desNode = Integer.parseInt(cell2.toString().substring(3, 5));
                Cell relatedBugsCell = row.getCell(cellPosition);
                int node1 = (int) relatedBugsCell.getNumericCellValue();
                Cell nextRelatedBugsCell = nextRow.getCell(cellPosition);
                int node2 = (int) nextRelatedBugsCell.getNumericCellValue();
                if (node1 > node2) {
                    bugMatrix[srcNode][desNode] = 1;
                    bugMatrix[desNode][srcNode] = -1;
                } else if (node2 > node1) {
                    bugMatrix[srcNode][desNode] = -1;
                    bugMatrix[desNode][srcNode] = 1;
                } else {
                    bugMatrix[srcNode][desNode] = 0;
                    bugMatrix[desNode][srcNode] = 0;
                }
            }
        }
        workbook.close();
        fs.close();
        return bugMatrix;
    }

    public void printRelatedBugsGraph(int[][] relatedBugsGraph, int bugSize) {
        System.out.println("--------------Related Bugs Graph----------------");
        for (int i = 0; i < bugSize; i++) {
            for (int j = 0; j < bugSize; j++) {
                System.out.printf("%8d", relatedBugsGraph[i][j]);
            }
            System.out.println();
        }
        System.out.println();
        for (int i = 0; i < bugSize; i++) {
            System.out.print("Bug " + i + " has more related bugs than Bug: ");
            for (int j = 0; j < bugSize; j++) {
                if (relatedBugsGraph[i][j] == 1) {
                    System.out.print(j + " ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public void printImportanceGraph(int[][] importanceMatrix, int bugSize) {
        System.out.println("--------------Importance Graph----------------");
        for (int i = 0; i < bugSize; i++) {
            for (int j = 0; j < bugSize; j++) {
                System.out.printf("%8d", importanceMatrix[i][j]);
            }
            System.out.println();
        }
        System.out.println();
        for (int i = 0; i < bugSize; i++) {
            System.out.print("Bug " + i + " is more important than Bug: ");
            for (int j = 0; j < bugSize; j++) {
                if (importanceMatrix[i][j] == 1) {
                    System.out.print(j + " ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public void printComponentGraph(int[][] componentImpactGraph, int bugSize) {
        System.out.println("--------------Component Graph----------------");
        for (int i = 0; i < bugSize; i++) {
            for (int j = 0; j < bugSize; j++) {
                System.out.printf("%8d", componentImpactGraph[i][j]);
            }
            System.out.println();
        }
        System.out.println();
        for (int i = 0; i < bugSize; i++) {
            System.out.print("Bug " + i + " has more impact than Bug: ");
            for (int j = 0; j < bugSize; j++) {
                if (componentImpactGraph[i][j] == 1) {
                    System.out.print(j + " ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public void printAgeGraph(int[][] ageMatrix, int bugSize) {
        System.out.println("--------------Age Graph----------------");
        for (int i = 0; i < bugSize; i++) {
            for (int j = 0; j < bugSize; j++) {
                System.out.printf("%8d", ageMatrix[i][j]);
            }
            System.out.println();
        }
        System.out.println();
        for (int i = 0; i < bugSize; i++) {
            System.out.print("Bug " + i + " is more important than Bug: ");
            for (int j = 0; j < bugSize; j++) {
                if (ageMatrix[i][j] == 1) {
                    System.out.print(j + " ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

}
