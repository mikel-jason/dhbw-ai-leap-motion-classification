package de.dhbw.tinf18e.LeapMotionClassifier.detector;

import de.dhbw.tinf18e.LeapMotionClassifier.leap.LeapRecord;

public class EdgeDetector {

    private EdgeDetectorQueue edgeDetectorQueue;

    public enum Edge {
        UP, DOWN, NEUTRAL
    }

    public EdgeDetector(int numFrames, double threshold, ValueExtractor extractor) {
        edgeDetectorQueue = new EdgeDetectorQueue(numFrames, threshold, extractor);
    }

    public Edge next(LeapRecord record) {
        edgeDetectorQueue.next(record);
        if (edgeDetectorQueue.isThresholdReached()) {
            if (edgeDetectorQueue.isRising()) {
                return Edge.UP;
            } else {
                return Edge.DOWN;
            }
        }
        return Edge.NEUTRAL;
    }
}
