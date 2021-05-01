package de.dhbw.tinf18e.LeapMotionClassifier.io;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import de.dhbw.tinf18e.LeapMotionClassifier.ai.FrequencyLevel;
import de.dhbw.tinf18e.LeapMotionClassifier.ai.Motion;
import de.dhbw.tinf18e.LeapMotionClassifier.detector.EdgeDetector;
import de.dhbw.tinf18e.LeapMotionClassifier.leap.LeapFrame;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class LeapFrameWriter {

    /**
     * Inner class for selecting data to be written to csv
     *
     * Note: Columns will be ordered alphabetically, not corresponding to their order in here
     */
    private class WrittenFrame {

        private int frameNum;
        private double palmPositionX;
        private double palmPositionY;
        private EdgeDetector.Edge palmXEdge;
        private EdgeDetector.Edge palmYEdge;
        private FrequencyLevel horizontalMovement;
        private FrequencyLevel verticalCounterMovement;

        WrittenFrame(LeapFrame frame) {
            frameNum = frame.getFrameNumber();
            palmPositionX = frame.getLeapRecord().getPalmPositionX();
            palmPositionY = frame.getLeapRecord().getPalmPositionY();
            palmXEdge = frame.getEdge(Motion.HorizontalMovement);
            palmYEdge = frame.getEdge(Motion.VerticalCounterMovement);
            horizontalMovement = frame.getFrequencyLevel(Motion.HorizontalMovement);
            verticalCounterMovement = frame.getFrequencyLevel(Motion.VerticalCounterMovement);
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
