package de.dhbw.tinf18e.LeapMotionClassifier.detector;

import de.dhbw.tinf18e.LeapMotionClassifier.leap.LeapRecord;

/**
 * Detector for time-series data working with a queue
 */
public class EdgeDetector {

    private EdgeDetectorQueue edgeDetectorQueue;

    public enum Edge {
        UP, DOWN, NEUTRAL
    }

    /**
     * @param numFrames Number of frames so be considered
     * @param threshold Diff (max - min) to be reached to be recognized as significant
     * @param extractor Lambda to retrieve the relevant value from a record
     */
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
