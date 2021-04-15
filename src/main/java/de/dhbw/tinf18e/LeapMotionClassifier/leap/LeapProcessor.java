package de.dhbw.tinf18e.LeapMotionClassifier.leap;

import de.dhbw.tinf18e.LeapMotionClassifier.io.LeapFrameWriter;
import de.dhbw.tinf18e.LeapMotionClassifier.leap.detector.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeapProcessor {

    private static final Logger LOGGER = LogManager.getLogger(LeapProcessor.class);

    @Autowired
    private EdgeDetectorFactory edgeDetectorFactory;

    @Value("${detectors.palmX.num_frames}")
    private int palmXNumFrames;

    @Value("${detectors.palmX.threshold}")
    private double palmXThreshold;

    @Value("${detectors.palmY.num_frames}")
    private int palmYNumFrames;

    @Value("${detectors.palmY.threshold}")
    private double palmYThreshold;

//    @Autowired
//    private PalmYDetector palmYDetector;
//
//    @Autowired
//    private PalmXDetector palmXDetector;

    @Autowired
    private LeapFrameWriter leapFrameWriter;

    @Autowired
    ApplicationArguments args;

    public void start(List<LeapRecord> data) {

        EdgeDetector palmXDetector = edgeDetectorFactory.create(palmXNumFrames, palmXThreshold, record -> record.getPalmPositionX(), Observation.MoveHorizontal);
        EdgeDetector palmYDetector = edgeDetectorFactory.create(palmYNumFrames, palmYThreshold, record -> record.getPalmPositionY(), Observation.MoveVertical);

        List<LeapFrame> frames = data.stream()
                .filter(record -> record.getValid() == 1)
                .map(record -> {
                    LeapFrame frame = new LeapFrame(record);
                    frame.analyze(palmYDetector);
                    frame.analyze(palmXDetector);
                    if (frame.getObservations().size() > 0)
                        LOGGER.info(frame.getObservations());
                    return frame;
                })
                .collect(Collectors.toList());

        try {
            String fileArg;
            try {
                fileArg = args.getOptionValues("out").get(0);
            } catch (Exception e) {
                LOGGER.info("Argument out not set, skip writing to CSV");
                return;
            }
            leapFrameWriter.write(frames, Path.of(fileArg));
        } catch (Exception e) {
            LOGGER.error("Cannot write frames to CSV", e);
        }
    }
}
