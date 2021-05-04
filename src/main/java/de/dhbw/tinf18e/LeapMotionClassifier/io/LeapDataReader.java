package de.dhbw.tinf18e.LeapMotionClassifier.io;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import de.dhbw.tinf18e.LeapMotionClassifier.leap.LeapRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class LeapDataReader {

    private static final Logger LOGGER = LogManager.getLogger(LeapDataReader.class);

    public List<LeapRecord> readLeapData(Path path) {
        try {
            CsvMapper mapper = new CsvMapper();
            mapper.enable(CsvParser.Feature.IGNORE_TRAILING_UNMAPPABLE);
            mapper.disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            CsvSchema schema = mapper.schemaFor(LeapRecord.class).withHeader().withColumnSeparator(',');

            ObjectReader reader = mapper.readerFor(LeapRecord.class).with(schema);

            return reader.<LeapRecord>readValues(Files.newBufferedReader(path)).readAll();
        } catch (IOException e) {
            LOGGER.error("Exception in CSV Parser!", e);
            return new ArrayList<>();
        }
    }
}
