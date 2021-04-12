package de.dhbw.tinf18e.LeapMotionClassifier.leap.detector;

import de.dhbw.tinf18e.LeapMotionClassifier.leap.LeapRecord;

@FunctionalInterface
public interface ValueExtractor {
    double getValue(LeapRecord record);
}
