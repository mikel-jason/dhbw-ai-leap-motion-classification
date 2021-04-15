package de.dhbw.tinf18e.LeapMotionClassifier.io;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import de.dhbw.tinf18e.LeapMotionClassifier.leap.LeapFrame;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class LeapFrameWriter {

    private class WrittenFrame {

        private String observations;

        WrittenFrame(LeapFrame frame) {
            observations = frame.getObservations().toString();
        }

    }

    public void write(List<LeapFrame> frames, Path path) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        BufferedWriter writer = Files.newBufferedWriter(path);
        StatefulBeanToCsv<WrittenFrame> beanToCsv = new StatefulBeanToCsvBuilder<WrittenFrame>(writer)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .build();
        beanToCsv.write(frames.stream().map(WrittenFrame::new));
        writer.close();
    }

}
