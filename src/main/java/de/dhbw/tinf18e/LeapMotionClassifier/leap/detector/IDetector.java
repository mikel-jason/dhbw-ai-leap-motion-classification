package de.dhbw.tinf18e.LeapMotionClassifier.leap.detector;

import de.dhbw.tinf18e.LeapMotionClassifier.leap.LeapRecord;
import de.dhbw.tinf18e.LeapMotionClassifier.leap.stateMachines.IStateMachineState;

public interface IDetector {
    void next(LeapRecord record);

    int getCount();

    IStateMachineState getState();
}
