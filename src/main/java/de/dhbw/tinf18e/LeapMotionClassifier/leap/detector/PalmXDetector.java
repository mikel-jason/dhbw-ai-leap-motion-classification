package de.dhbw.tinf18e.LeapMotionClassifier.leap.detector;

import com.himanshuvirmani.exceptions.TransitionException;
import de.dhbw.tinf18e.LeapMotionClassifier.leap.LeapRecord;
import de.dhbw.tinf18e.LeapMotionClassifier.leap.stateMachines.IStateMachineState;
import de.dhbw.tinf18e.LeapMotionClassifier.leap.stateMachines.PalmXStateMachine;
import de.dhbw.tinf18e.LeapMotionClassifier.leap.stateMachines.PalmXStateMachine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PalmXDetector implements IDetector {

    @Autowired
    private PalmXStateMachine stateMachine;

    @Value("detectors.palmX.num_frames")
    private int numFrames;

    @Value("detectors.palmX.threshold")
    private int threshold;

    private EdgeDetectorQueue edgeDetector = new EdgeDetectorQueue(numFrames, threshold, record -> record.getPalmPositionX());

    @Override
    public void next(LeapRecord record) {
        edgeDetector.next(record);
        try {
            if (edgeDetector.isThresholdReached()) {
                if (edgeDetector.isRising()) {
                    stateMachine.fire(PalmXStateMachine.Event.LEFT);
                } else {
                    stateMachine.fire(PalmXStateMachine.Event.RIGHT);
                }
            } else {
                stateMachine.fire(PalmXStateMachine.Event.HOLD);
            }
        } catch (TransitionException e) {
            if (!e.getMessage().startsWith("No transitions defined from Current State"))
                e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return stateMachine.getCount();
    }

    @Override
    public IStateMachineState getState() {
        return stateMachine.getCurrentState();
    }
}
