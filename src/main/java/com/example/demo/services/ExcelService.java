package com.example.demo.services;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelService {

    // Ruta donde se guardará el archivo Excel

    private static final String FILE_PATH = "C:\\test\\generated_report.xlsx";

    public byte[] generateExcelWithInterleavedRows(List<String[]> data) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");

        // Estilos para las filas
        CellStyle style1 = workbook.createCellStyle();
        style1.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style1.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        int rowIndex = 0;
        for (int i = 0; i < data.size(); i++) {
            Row row = sheet.createRow(rowIndex++);
            String[] rowData = data.get(i);

            CellStyle currentStyle = (i % 2 == 0) ? style1 : style2;

            for (int j = 0; j < rowData.length; j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue(rowData[j]);
                cell.setCellStyle(currentStyle);
            }
        }

        // Guardar el archivo en la unidad C
        try (FileOutputStream fileOut = new FileOutputStream(FILE_PATH)) {
            workbook.write(fileOut);
        }

        // Generar el archivo en formato byte array para enviar en la respuesta
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();  // Cierra el workbook después de escribir en el ByteArrayOutputStream

        return outputStream.toByteArray();
    }
}
