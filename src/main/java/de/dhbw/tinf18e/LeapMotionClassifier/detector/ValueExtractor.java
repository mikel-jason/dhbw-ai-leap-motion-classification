package de.dhbw.tinf18e.LeapMotionClassifier.detector;

import de.dhbw.tinf18e.LeapMotionClassifier.leap.LeapRecord;

/**
 * Lambda to provide a method to select the correct value from a record
 */
@FunctionalInterface
public interface ValueExtractor {
    double getValue(LeapRecord record);
}
