package de.dhbw.tinf18e.LeapMotionClassifier.leap;

import de.dhbw.tinf18e.LeapMotionClassifier.leap.detector.IDetector;
import de.dhbw.tinf18e.LeapMotionClassifier.leap.detector.PalmYDetector;
import de.dhbw.tinf18e.LeapMotionClassifier.leap.stateMachines.IStateMachineState;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LeapFrame {

    private final LeapRecord leapRecord;
    @Getter private IStateMachineState palmYState;

    public void analyze(IDetector detector) {
        detector.next(leapRecord);
        if (detector.getClass() == PalmYDetector.class) {
            palmYState = detector.getState();
        } else {
            throw new RuntimeException("Unknown detector of class: " + detector.getClass().getCanonicalName());
        }
    }

}
