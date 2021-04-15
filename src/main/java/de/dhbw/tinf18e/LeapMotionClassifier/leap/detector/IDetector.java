package de.dhbw.tinf18e.LeapMotionClassifier.leap.detector;

import de.dhbw.tinf18e.LeapMotionClassifier.leap.LeapRecord;
import de.dhbw.tinf18e.LeapMotionClassifier.leap.Observation;

public interface IDetector {
    void next(LeapRecord record);

    int getCount();

    StateMachine.State getState();

    Observation getObservation();
}
