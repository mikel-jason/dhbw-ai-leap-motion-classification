package de.dhbw.tinf18e.LeapMotionClassifier.leap.detector;

import de.dhbw.tinf18e.LeapMotionClassifier.leap.LeapRecord;

public interface IDetector {
    void next(LeapRecord record);

    int getCount();
}
