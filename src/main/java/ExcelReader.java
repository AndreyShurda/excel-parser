import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class ExcelReader {

    public static void main(String[] args) throws IOException, InvalidFormatException, InvocationTargetException, IllegalAccessException {
        long before = System.currentTimeMillis();
//        List<License> licensesOld = parseLicense("LicenseRegistry20191011-2.xlsx");
        String fileName = "LicenseRegistry20191111.xlsx";
        List<License> licensesOld = getOldLicenses(fileName, 1);
        List<License> licensesNew = parseNewLicense(fileName, 0);
        List<License> licensesAMC = parseAMCLicense("AMC-2.xlsx");

        List<License> filteredLicensesNew = licensesNew.stream()
                .filter(license -> !license.getNl().isEmpty())
                .collect(Collectors.toList());

        filteredLicensesNew.addAll(licensesAMC);

//        List<License> licensesNew = parseNewLicense("SubjectStockPaperTrader.xlsx");
//        List<License> licensesNew = parseNewLicense("LicenseRegistry20191106.xlsx");
//        List<License> licensesNew = parseAMCLicense("AMC_LICENSE.xlsx");

        Map<String, List<License>> map = licensesOld.stream()
                .filter(license -> license.getNl().length() > 1)
                .collect(Collectors.groupingBy(license -> license.getNl().substring(0, 2)));

//        Set<String> collect = groupByFirstTwoLicenseSymbols(licensesNew);

        List<LicenseFilter> duplicators = deleteDuplicators(licensesOld, filteredLicensesNew);


        ExcelWriter.write(duplicators, "duplicates.xlsx");
        ExcelWriter.write(licensesOld, "withoutDuplicators.xlsx");

//        List<String> collect = licensesOld.stream()
//                .map(license -> license.toStringWithSeparator(";"))
//                .collect(Collectors.toList());
//
//        collect.add(0,"LNU;LT;NU;PI;AD;OT;NL;DN;DK;Kod_diya;TYPE;D_NAK;N_NAK;T_NAK;від діяльності;K_DREE");
//
//        CsvParser.writeTofile(collect, "resultWithoutDuplicators.csv");

        System.out.println("finish: " + (System.currentTimeMillis() - before) + " ms");
    }

    private static Set<String> groupByFirstTwoLicenseSymbols(List<License> licensesNew) {
        return licensesNew.stream()
                .filter(license -> license.getNl().length() > 1)
                .map(license -> license.getNl().substring(0, 2))
                .collect(Collectors.toSet());
    }

    private static List<LicenseFilter> deleteDuplicators(List<License> licensesOld, List<License> licensesNew) throws IllegalAccessException, InvocationTargetException {
        List<LicenseFilter> duplicators = new ArrayList<>();

        for (License license : licensesNew) {
            Iterator<License> iterator = licensesOld.iterator();
            while (iterator.hasNext()) {
                License licenseOld = iterator.next();
                if (license.equals(licenseOld)) {
                    LicenseFilter dest = new LicenseFilter();
                    BeanUtils.copyProperties(dest, licenseOld);
                    duplicators.add(dest);
                    iterator.remove();
                }
            }
        }

        return duplicators;
    }

    private static List<License> parseAMCLicense(String fileName) throws IOException, InvalidFormatException {
        DataFormatter dataFormatter = new DataFormatter();

        Sheet sheet = getSheet(fileName, 0);

        List<License> licenses = new ArrayList<>();
        int i = 1;
        for (Row row : sheet) {
            if (i > 1) {
                License license = new License();
                String lt = dataFormatter.formatCellValue(row.getCell(1)).trim();
                String nl = dataFormatter.formatCellValue(row.getCell(2)).trim();
                if (StringUtils.isBlank(lt) || StringUtils.isBlank(nl)) {
                    continue;
                }
                license.setLt(lt);
                license.setNl(nl);
                licenses.add(license);
            }
            i++;
        }

        return licenses;
    }

    private static Sheet getSheet(String fileName, int sheetNumber) throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(new File(fileName));
        Sheet sheet = workbook.getSheetAt(sheetNumber);
        workbook.close();
        return sheet;
    }

    private static List<License> parseNewLicense(String fileName, int sheetNumber) throws IOException, InvalidFormatException {
        Sheet sheet = getSheet(fileName, sheetNumber);

        // Create a DataFormatter to format and get each cell's value as String
        DataFormatter dataFormatter = new DataFormatter();

        List<License> licenses = new ArrayList<>();
        int i = 1;
        for (Row row : sheet) {
            if (i > 1) {
                String lt = dataFormatter.formatCellValue(row.getCell(2)).trim();
                License licenseB = createLicense(dataFormatter, row, lt,25, 26);
                License licenseD = createLicense(dataFormatter, row, lt,42, 43);
                License licenseA = createLicense(dataFormatter, row, lt,60, 61);
                License licenseU = createLicense(dataFormatter, row, lt,77, 78);
                licenses.add(licenseB);
                licenses.add(licenseD);
                licenses.add(licenseA);
                licenses.add(licenseU);
            }
            i++;
        }

        return licenses;
    }

    private static License createLicense(DataFormatter dataFormatter, Row row, String lt, int numberColumn, int serialColumn) {
        License license = new License();
//        String lt = dataFormatter.formatCellValue(row.getCell(ltColumn)).trim();
        license.setLt(lt);
        String licenseName = getLicenseNumber(row, numberColumn, serialColumn);
//                if (lt.equals("") || lt.equals("0") || licenseName.isEmpty()) {
//                    return null;
//                }

        license.setNl(licenseName);
        return license;
    }

    private static String getLicenseNumber(Row row, int numberColumn, int serialColumn) {
        DataFormatter dataFormatter = new DataFormatter();
        String number = dataFormatter.formatCellValue(row.getCell(numberColumn));
        String serial = dataFormatter.formatCellValue(row.getCell(serialColumn));
        String licenseName = serial + number;
        return licenseName.trim();
    }

    private static List<License> parseLicense(String fileName) throws IllegalAccessException, InvocationTargetException, IOException, InvalidFormatException {
        List<License> licensesAll = getOldLicenses(fileName, 0);
        List<License> licenses = licensesAll.stream()
                .filter(license -> license.getNl().contains("БН"))
                .collect(Collectors.toList());


        Map<String, List<License>> listMap = licenses.stream()
                .collect(Collectors.groupingBy(License::getLt));
//                .collect(Collectors.groupingBy(License::getLt, Collectors.toCollection(TreeSet::new)));


        Map<String, TreeSet<License>> stringTreeSetMap = licenses.stream()
                .collect(Collectors.groupingBy(License::getNl, Collectors.toCollection(TreeSet::new)));


        Map<String, TreeSet<License>> treeSetMap = new HashMap<>();

        List<LicenseFilter> filterList = new ArrayList<>();
        for (Map.Entry<String, TreeSet<License>> stringTreeSetEntry : stringTreeSetMap.entrySet()) {
            TreeSet<License> value = stringTreeSetEntry.getValue();
            if (value.size() > 1) {
                Optional<License> first = value.stream().findFirst();
                treeSetMap.put(stringTreeSetEntry.getKey(), value);
                for (License license : value) {
                    LocalDate dn = first.get().getDn();
                    if (!dn.equals(license.getDn())) {
                        LicenseFilter dest = new LicenseFilter();
                        BeanUtils.copyProperties(dest, license);
                        System.out.println("Incorrect row" + license);
                        dest.setDnCorrect(dn);
                        filterList.add(dest);
                    }
                }
            }
        }


//        createCsv(treeSetMap);

        List<License> licenseArrayList = new ArrayList<>();
        treeSetMap.forEach((key, value) -> value.stream()
                .forEach(licenseArrayList::add));


        ExcelWriter.write(filterList, "poi-license-file.xlsx");

        return licenses;
    }

    private static List<License> getOldLicenses(String fileName, int sheetNumber) throws IOException, InvalidFormatException {
        Sheet sheet = getSheet(fileName, sheetNumber);
        DataFormatter dataFormatter = new DataFormatter();
        List<License> licenses = new ArrayList<>();

        int i = 1;
        for (Row row : sheet) {
            if (i > 1) {
                License license = new License();
                license.setId(i);
                license.setLnu(dataFormatter.formatCellValue(row.getCell(0)));
                String lt = dataFormatter.formatCellValue(row.getCell(1)).trim();
                license.setLt(lt);
                license.setNu(dataFormatter.formatCellValue(row.getCell(2)));
                license.setPi(dataFormatter.formatCellValue(row.getCell(3)));
                license.setAd(dataFormatter.formatCellValue(row.getCell(4)));
                license.setOt(dataFormatter.formatCellValue(row.getCell(5)));
                String nl = dataFormatter.formatCellValue(row.getCell(6)).trim();
//                if (nl.equalsIgnoreCase("АА240865")) {
//                    System.out.println();
//                }
//                if (nl.contains("БН")) {
//                    continue;
//                }
                license.setNl(nl);

                LocalDate localDate;
                try {
                    localDate = LocalDate.parse(dataFormatter.formatCellValue(row.getCell(7)).trim(), DateTimeFormatter.ofPattern("yyyyMMdd"));
                } catch (DateTimeParseException e) {
                    System.out.println(i + " : license number: " + license.getNl() + " : " + e.getMessage());
//                    license.setDn(localDate);
                    continue;
                }
                license.setDn(localDate);
                license.setDk(dataFormatter.formatCellValue(row.getCell(8)));
                license.setKodDila(dataFormatter.formatCellValue(row.getCell(9)));
                license.setType(dataFormatter.formatCellValue(row.getCell(10)));
                license.setD_nak(dataFormatter.formatCellValue(row.getCell(11)));
                license.setN_nak(dataFormatter.formatCellValue(row.getCell(12)));
                license.setT_nak(dataFormatter.formatCellValue(row.getCell(13)));
                license.setActivity(dataFormatter.formatCellValue(row.getCell(14)));
                license.setK_dree(dataFormatter.formatCellValue(row.getCell(15)));
                licenses.add(license);
            }
            i++;
        }
        return licenses;
    }

    @FunctionalInterface
    interface Worker {
        List<License> work(Sheet sheet);
    }

    private static void createCsv(Map<String, TreeSet<License>> treeSetMap) throws IOException {
        List<String> strings = new ArrayList<>();
        strings.add("LNU;LT;NU;PI;AD;OT;NL;DN;DK;Kod_diya;TYPE;D_NAK;N_NAK;T_NAK;від діяльності;K_DREE");
        treeSetMap.forEach((key, value) -> value.stream()
                .map(license -> license.toStringWithSeparator(";").trim())
                .forEach(strings::add));


        CsvParser.writeTofile(strings, "resultFile.csv");
    }
}
