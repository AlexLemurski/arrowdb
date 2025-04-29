package com.example.arrowdb.auxiliary;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;

import static com.example.arrowdb.auxiliary.FilePathResource.PERSPECTIVE_DIRECTORY;

@Component
public class ExcelReader {

    public final String getValueOfEquipments(String filePath, int sheetIndex, int rowIndex, int columnIndex){
        String value = null;
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(fis)) {
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            DataFormatter formatter = new DataFormatter();
            Sheet sheet = workbook.getSheetAt(sheetIndex); // Читаем указанный лист
            Row row = sheet.getRow(rowIndex); // Читаем указанную строку
            if (row != null) {
                Cell cell = row.getCell(columnIndex); //Читаем указанную колонку
                if (cell.getCellType() == CellType.FORMULA) {
                    value = formatter.formatCellValue(cell, evaluator);
                } else {
                    value = formatter.formatCellValue(cell);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return value;
    }
}