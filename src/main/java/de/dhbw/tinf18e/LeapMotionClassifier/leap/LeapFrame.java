package de.dhbw.tinf18e.LeapMotionClassifier.leap;

import de.dhbw.tinf18e.LeapMotionClassifier.leap.detector.IDetector;
import de.dhbw.tinf18e.LeapMotionClassifier.leap.detector.PalmXDetector;
import de.dhbw.tinf18e.LeapMotionClassifier.leap.detector.PalmYDetector;
import de.dhbw.tinf18e.LeapMotionClassifier.leap.stateMachines.IStateMachineState;
import de.dhbw.tinf18e.LeapMotionClassifier.leap.stateMachines.PalmXStateMachine;
import de.dhbw.tinf18e.LeapMotionClassifier.leap.stateMachines.PalmYStateMachine;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class LeapFrame {

    public enum Motion {
        MoveVertical,
        MoveHorizontal
    }

    private final LeapRecord leapRecord;
    @Getter private IStateMachineState palmXState;
    @Getter private IStateMachineState palmYState;

    public void analyze(IDetector detector) {
        detector.next(leapRecord);
        if (detector.getClass() == PalmYDetector.class) {
            palmYState = detector.getState();
        } else if (detector.getClass() == PalmXDetector.class) {
            palmXState = detector.getState();
        } else {
            throw new RuntimeException("Unknown detector of class: " + detector.getClass().getCanonicalName());
        }
    }

    public Set<Motion> getMotions() {
        Set<Motion> set = new HashSet<>();

        if (getPalmYState() == PalmYStateMachine.State.UP_DOWN || getPalmYState() == PalmYStateMachine.State.DOWN_UP)
            set.add(Motion.MoveVertical);
        if (getPalmXState() == PalmXStateMachine.State.LEFT_RIGHT || getPalmXState() == PalmXStateMachine.State.RIGHT_LEFT)
            set.add(Motion.MoveHorizontal);

        return set;
    }

}
