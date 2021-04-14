package de.dhbw.tinf18e.LeapMotionClassifier.leap;

import de.dhbw.tinf18e.LeapMotionClassifier.leap.detector.IDetector;
import de.dhbw.tinf18e.LeapMotionClassifier.leap.detector.PalmYDetector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeapProcessor {

    private static final Logger LOGGER = LogManager.getLogger(LeapProcessor.class);

    @Autowired
    private PalmYDetector palmYDetector;

    public void start(List<LeapRecord> data) {

        List<LeapFrame> frames = data.stream()
                .filter(record -> record.getValid() == 1)
                .map(record -> {
                    LeapFrame frame = new LeapFrame(record);
                    frame.analyze(palmYDetector);
                    return frame;
                })
                .collect(Collectors.toList());

        LOGGER.info("Palm Y detected changes -> " + palmYDetector.getCount());
    }
}
