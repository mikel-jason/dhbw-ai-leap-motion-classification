package de.dhbw.tinf18e.LeapMotionClassifier.leap;

import de.dhbw.tinf18e.LeapMotionClassifier.leap.detector.IDetector;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class LeapFrame {

    private static final Logger LOGGER = LogManager.getLogger(LeapFrame.class);

    private final LeapRecord leapRecord;
    @Getter private final int frameNumber;

    @Getter
    private Set<Observation> observations = new HashSet<Observation>();

    public void analyze(IDetector detector) {
        detector.next(leapRecord);
        if (detector.getObservation() != null) {
            observations.add(detector.getObservation());
        }
    }

}
