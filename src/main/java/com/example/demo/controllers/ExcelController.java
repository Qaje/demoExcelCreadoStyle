package com.example.demo.controllers;
import com.example.demo.services.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/excel")
public class ExcelController {

    @Autowired
    private ExcelService excelService;

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadExcel() {
        try {
            List<String[]> data = Arrays.asList(
                    new String[]{"Header1", "Header2", "Header3"},
                    new String[]{"Row1-Col1", "Row1-Col2", "Row1-Col3"},
                    new String[]{"Row2-Col1", "Row2-Col2", "Row2-Col3"},
                    new String[]{"Row3-Col1", "Row3-Col2", "Row3-Col3"}
            );

            byte[] excelData = excelService.generateExcelWithInterleavedRows(data);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=generated_report.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

