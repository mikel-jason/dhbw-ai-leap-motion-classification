package de.dhbw.tinf18e.LeapMotionClassifier.io;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import de.dhbw.tinf18e.LeapMotionClassifier.ai.Difficulty;
import de.dhbw.tinf18e.LeapMotionClassifier.ai.FrequencyLevel;
import de.dhbw.tinf18e.LeapMotionClassifier.ai.Motion;
import de.dhbw.tinf18e.LeapMotionClassifier.detector.EdgeDetector;
import de.dhbw.tinf18e.LeapMotionClassifier.leap.LeapFrame;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeapFrameWriter {

    /**
     * Inner class for selecting data to be written to csv
     * <p>
     * Note: Columns will be ordered alphabetically, not corresponding to their order in here
     */
    @Getter
    @Setter
    private class WrittenFrame {

        private int frameNum;
        private double palmPositionX;
        private double palmPositionY;
        private EdgeDetector.Edge palmXEdge;
        private EdgeDetector.Edge palmYEdge;
        private FrequencyLevel horizontalMovement;
        private FrequencyLevel verticalCounterMovement;
        private Difficulty difficulty;

        WrittenFrame(LeapFrame frame) {
            frameNum = frame.getFrameNumber();
            palmPositionX = frame.getLeapRecord().getPalmPositionX();
            palmPositionY = frame.getLeapRecord().getPalmPositionY();
            palmXEdge = frame.getEdge(Motion.HorizontalCounterMovement);
            palmYEdge = frame.getEdge(Motion.VerticalMovement);
            horizontalMovement = frame.getFrequencyLevel(Motion.HorizontalCounterMovement);
            verticalCounterMovement = frame.getFrequencyLevel(Motion.VerticalMovement);
            difficulty = frame.getDifficulty();
        }

    }

    public void write(List<LeapFrame> frames, Path path) throws IOException {

        CsvMapper mapper = new CsvMapper();
        mapper.disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);

        CsvSchema schema = mapper.schemaFor(WrittenFrame.class).withHeader().withColumnSeparator(',');
        ObjectWriter writer = mapper.writerFor(WrittenFrame.class).with(schema);

        writer.writeValues(path.toFile()).writeAll(frames.stream().map(WrittenFrame::new).collect(Collectors.toList()));
    }
}
