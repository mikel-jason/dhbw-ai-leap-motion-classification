package de.dhbw.tinf18e.LeapMotionClassifier.detector;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EdgeDetectorFactory {

    @Value("${detectors.palmX.num_frames}")
    private int palmXNumFrames;

    @Value("${detectors.palmX.threshold}")
    private double palmXThreshold;

    @Value("${detectors.palmY.num_frames}")
    private int palmYNumFrames;

    @Value("${detectors.palmY.threshold}")
    private double palmYThreshold;

    public EdgeDetector createForPalmX() {
        return new EdgeDetector(palmXNumFrames, palmXThreshold, r -> r.getPalmPositionX());
    }

    public EdgeDetector createForPalmY() {
        return new EdgeDetector(palmYNumFrames, palmYThreshold, r -> r.getPalmPositionY());
    }
}
