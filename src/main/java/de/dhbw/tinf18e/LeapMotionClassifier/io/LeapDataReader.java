package de.dhbw.tinf18e.LeapMotionClassifier.io;

import com.opencsv.bean.CsvToBeanBuilder;
import de.dhbw.tinf18e.LeapMotionClassifier.entities.LeapRecord;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class LeapDataReader {

    public List<LeapRecord> readLeapData(Path path) throws IOException {

        Reader reader = Files.newBufferedReader(path);
        List<LeapRecord> list = new CsvToBeanBuilder<LeapRecord>(reader)
                .withType(LeapRecord.class)
                .build()
                .parse();

        reader.close();
        return list;
    }
}
