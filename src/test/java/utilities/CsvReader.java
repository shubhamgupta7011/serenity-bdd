package utilities;

import au.com.bytecode.opencsv.CSVReader;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.typesafe.config.Config;

import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Map;

public class CsvReader {
    private static Config conf = ConfigLoader.load();
    private static String testDataFilePath = conf.getString("testDataFilePath");

    private static List<Map<?, ?>> listOfCsvData;

    public static String[][] readCsvFile(String csvFileName) {
        String file = testDataFilePath + csvFileName;
        int row = 0, column = 0, index = 0;
        String[][] record = new String[0][];
        try {
            CSVReader csvReader = new CSVReader(new FileReader(file));
            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                for (int i = 0; i < nextRecord.length; i++) {
                    column = Math.max(nextRecord.length, column);
                }
                row++;
            }
            csvReader = new CSVReader(new FileReader(file));
            record = new String[row][column];
            while ((nextRecord = csvReader.readNext()) != null) {
                System.arraycopy(nextRecord, 0, record[index], 0, nextRecord.length);
                index++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return record;
    }

    public static List<Map<?, ?>> readCsv(String fileName) {
        try {
            File file = new File(System.getProperty("user.dir") + "/src/test/resources/testData/" + fileName + ".csv");
            CsvSchema bootstrap = CsvSchema.emptySchema().withHeader();
            CsvMapper csvMapper = new CsvMapper();
            MappingIterator<Map<?, ?>> mappingIterator = csvMapper.readerWithSchemaFor(Map.class).with(bootstrap).readValues(file);
            listOfCsvData = mappingIterator.readAll();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return listOfCsvData;
    }

}
