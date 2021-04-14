package de.dhbw.tinf18e.LeapMotionClassifier.leap;

import de.dhbw.tinf18e.LeapMotionClassifier.LeapMotionClassifierApplication;
import de.dhbw.tinf18e.LeapMotionClassifier.leap.detector.IDetector;
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
    private List<? extends IDetector> detectors;

    public void start(List<LeapRecord> data) {
        List<LeapRecord> validData = data.stream().filter(record -> record.getValid() == 1).collect(Collectors.toList());

        for (LeapRecord leapRecord : validData) {
            for (IDetector detector : detectors) {
                detector.next(leapRecord);
            }
        }
        for (IDetector detector : detectors) {
            LOGGER.info(detector.getClass().getName() + " -> " + detector.getCount());
        }
    }
}
