import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExcelWriter {

    private static String[] columns = {"id", "LNU", "LT", "NU", "PI", "AD", "OT", "NL", "DN_Before", "DN_After", "DK", "Kod_diya", "TYPE", "D_NAK", "N_NAK", "T_NAK", "від діяльності", "K_DREE"};
    //    private static String[] columns = {"Name", "Email", "Date Of Birth", "Salary"};

    // Initializing licenses data to insert into the excel file

    public static <T extends License> void write(List<T> licenses, String fileName) throws IOException, InvalidFormatException {
        // Create a Workbook
        Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

        // Create a Sheet
        Sheet sheet = workbook.createSheet("License");

        // Create a Font for styling header cells
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
//        headerFont.setColor(IndexedColors.RED.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Create a Row
        Row headerRow = sheet.createRow(0);

        // Create cells
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        // Create Other rows and cells with licenses data
        int rowNum = 1;
        for (License license : licenses) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(license.getId());
            row.createCell(1).setCellValue(license.getLnu());
            row.createCell(2).setCellValue(license.getLt());
            row.createCell(3).setCellValue(license.getNu());
            row.createCell(4).setCellValue(license.getPi());
            row.createCell(5).setCellValue(license.getAd());
            row.createCell(6).setCellValue(license.getOt());
            row.createCell(7).setCellValue(license.getNl());
            row.createCell(8).setCellValue(license.getDn().format(DateTimeFormatter.ofPattern("yyyyMMdd")));

            if (license instanceof LicenseFilter) {
                LocalDate dnCorrect = ((LicenseFilter) license).getDnCorrect();
                if (Objects.nonNull(dnCorrect)) {
                    row.createCell(9).setCellValue(dnCorrect.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                }
            }
            row.createCell(10).setCellValue(license.getDk());

            row.createCell(11).setCellValue(license.getKodDila());
            row.createCell(12).setCellValue(license.getType());
            row.createCell(13).setCellValue(license.getD_nak());
            row.createCell(14).setCellValue(license.getN_nak());
            row.createCell(15).setCellValue(license.getT_nak());
            row.createCell(16).setCellValue(license.getActivity());
            row.createCell(17).setCellValue(license.getK_dree());

        }

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream(fileName);
        workbook.write(fileOut);
        fileOut.close();

        // Closing the workbook
        workbook.close();
    }
}
