package de.dhbw.tinf18e.LeapMotionClassifier.leap.detector;

import com.himanshuvirmani.exceptions.TransitionException;
import de.dhbw.tinf18e.LeapMotionClassifier.leap.LeapRecord;
import de.dhbw.tinf18e.LeapMotionClassifier.leap.Observation;

public class EdgeDetector implements IDetector {

    private StateMachine stateMachine;

    private EdgeDetectorQueue edgeDetector;

    private Observation observation;

    public EdgeDetector(int numFrames, double threshold, ValueExtractor extractor, Observation observation) {
        edgeDetector = new EdgeDetectorQueue(numFrames, threshold, extractor);
        stateMachine = new StateMachine();
        this.observation = observation;
    }

    @Override
    public void next(LeapRecord record) {
        edgeDetector.next(record);
        try {
            if (edgeDetector.isThresholdReached()) {
                if (edgeDetector.isRising()) {
                    stateMachine.fire(StateMachine.Event.A);
                } else {
                    stateMachine.fire(StateMachine.Event.B);
                }
            } else {
                stateMachine.fire(StateMachine.Event.HOLD);
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
    public StateMachine.State getState() {
        return stateMachine.getCurrentState();
    }

    @Override
    public Observation getObservation() {
        if (getState() == StateMachine.State.A_B || getState() == StateMachine.State.B_A)
            return observation;
        return null;
    }
}
