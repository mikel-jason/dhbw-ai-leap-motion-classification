package de.dhbw.tinf18e.LeapMotionClassifier.leap.detector;

import com.himanshuvirmani.exceptions.TransitionException;
import de.dhbw.tinf18e.LeapMotionClassifier.leap.LeapRecord;
import de.dhbw.tinf18e.LeapMotionClassifier.leap.stateMachines.IStateMachineState;
import de.dhbw.tinf18e.LeapMotionClassifier.leap.stateMachines.PalmYStateMachine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PalmYDetector implements IDetector {

    @Autowired
    private PalmYStateMachine stateMachine;

    @Value("detectors.palmY.num_frames")
    private int numFrames;

    @Value("detectors.palmY.threshold")
    private int threshold;

    private EdgeDetectorQueue edgeDetector = new EdgeDetectorQueue(numFrames, threshold, record -> record.getPalmPositionY());

    @Override
    public void next(LeapRecord record) {
        edgeDetector.next(record);
        try {
            if (edgeDetector.isThresholdReached()) {
                if (edgeDetector.isRising()) {
                    stateMachine.fire(PalmYStateMachine.Event.UP);
                } else {
                    stateMachine.fire(PalmYStateMachine.Event.DOWN);
                }
            } else {
                stateMachine.fire(PalmYStateMachine.Event.HOLD);
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
