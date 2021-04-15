package de.dhbw.tinf18e.LeapMotionClassifier.leap.detector;

import de.dhbw.tinf18e.LeapMotionClassifier.leap.Observation;
import org.springframework.stereotype.Service;

@Service
public class EdgeDetectorFactory {

    public EdgeDetector create(int numFrames, double threshold, ValueExtractor extractor, Observation observation) {
        EdgeDetector edgeDetector = new EdgeDetector(numFrames, threshold, extractor, observation);

        return edgeDetector;
    }
}
