package de.dhbw.tinf18e.LeapMotionClassifier.io;

import de.dhbw.tinf18e.LeapMotionClassifier.leap.LeapRecord;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class LeapDataReader {

    public List<LeapRecord> readLeapData(Path path) throws Exception {
        try {
            CsvMapper mapper = new CsvMapper();
            mapper.disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            CsvSchema schema = mapper.schemaFor(LeapRecord.class).withHeader().withColumnSeparator(',');

            ObjectReader reader = mapper.readerFor(LeapRecord.class).with(schema);

            return reader.<LeapRecord>readValues(Files.newBufferedReader(path)).readAll();
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("Exception in CSV Parser!");
        }
    }
}
