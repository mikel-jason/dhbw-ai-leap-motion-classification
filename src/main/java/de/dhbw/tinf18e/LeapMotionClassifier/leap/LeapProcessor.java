package de.dhbw.tinf18e.LeapMotionClassifier.leap;

import de.dhbw.tinf18e.LeapMotionClassifier.leap.detector.PalmXDetector;
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

    @Autowired
    private PalmXDetector palmXDetector;

    public void start(List<LeapRecord> data) {

        List<LeapFrame> frames = data.stream()
                .filter(record -> record.getValid() == 1)
                .map(record -> {
                    LeapFrame frame = new LeapFrame(record);
                    frame.analyze(palmYDetector);
                    frame.analyze(palmXDetector);
                    if (frame.getMotions().size() > 0)
                        LOGGER.info(frame.getMotions());
                    return frame;
                })
                .collect(Collectors.toList());
    }
}
