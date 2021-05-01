package de.dhbw.tinf18e.LeapMotionClassifier.detector;

import com.himanshuvirmani.exceptions.TransitionCreationException;
import de.dhbw.tinf18e.LeapMotionClassifier.ai.Motion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Responsible for instantiating each state machine for analyzing the data.
 *
 * For an instantiation required:
 * <ul>
 *     <li>Values for the edge detector</li>
 *     <li>Values (thresholds) for the frequency level classification</li>
 *     <li>A value extractor lambda</li>
 *     <li>A state machine to define observed state(s)</li>
 * </ul>
 */
@Service
public class StateMachineFactory {

    // general
    @Value("${time.fps}")
    private int FRAMES_PER_SECOND;

    private static final Logger LOGGER = LogManager.getLogger(StateMachineFactory.class);

    // palm X
    @Value("${detectors.palmX.num_frames}")
    private int palmXNumFrames;

    @Value("${detectors.palmX.threshold}")
    private double palmXThreshold;

    @Value("${motion.counter-move-horizontal.low}")
    private double HORIZONTAL_MAX_LOW;

    @Value("${motion.counter-move-horizontal.medium}")
    private double HORIZONTAL_MAX_MEDIUM;

    // palm Y
    @Value("${detectors.palmY.num_frames}")
    private int palmYNumFrames;

    @Value("${detectors.palmY.threshold}")
    private double palmYThreshold;

    @Value("${motion.move-vertical.low}")
    private double VERTICAL_MAX_LOW;

    @Value("${motion.move-vertical.medium}")
    private double VERTICAL_MAX_MEDIUM;

    public StateMachine createForPalmX() {

        EdgeDetector detector = new EdgeDetector(palmXNumFrames, palmXThreshold, r -> r.getPalmPositionX());
        com.himanshuvirmani.StateMachine<AbstractStateMachine.State, EdgeDetector.Edge> machine = new com.himanshuvirmani.StateMachine(AbstractStateMachine.State.NEUTRAL);

        StateMachine stateMachine = new StateMachine(machine, detector, HORIZONTAL_MAX_LOW, HORIZONTAL_MAX_MEDIUM, Motion.HorizontalMovement);
        stateMachine.setFramesPerSecond(FRAMES_PER_SECOND);

        try {

            // UP -> DOWN
            machine.transition().from(AbstractStateMachine.State.NEUTRAL).to(AbstractStateMachine.State.A).on(EdgeDetector.Edge.UP).create();
            machine.transition().from(AbstractStateMachine.State.A).to(AbstractStateMachine.State.A_B).on(EdgeDetector.Edge.DOWN).setOnSuccessListener((from, to, on) -> stateMachine.incrementCount()).create();

            // DOWN -> UP
            machine.transition().from(AbstractStateMachine.State.NEUTRAL).to(AbstractStateMachine.State.B).on(EdgeDetector.Edge.DOWN).create();
            machine.transition().from(AbstractStateMachine.State.B).to(AbstractStateMachine.State.B_A).on(EdgeDetector.Edge.UP).setOnSuccessListener((from, to, on) -> stateMachine.incrementCount()            ).create();

            // back to NEUTRAL
            machine.transition().from(AbstractStateMachine.State.A).to(AbstractStateMachine.State.NEUTRAL).on(EdgeDetector.Edge.NEUTRAL).create();
            machine.transition().from(AbstractStateMachine.State.A_B).to(AbstractStateMachine.State.NEUTRAL).on(EdgeDetector.Edge.NEUTRAL).create();
            machine.transition().from(AbstractStateMachine.State.B).to(AbstractStateMachine.State.NEUTRAL).on(EdgeDetector.Edge.NEUTRAL).create();
            machine.transition().from(AbstractStateMachine.State.B_A).to(AbstractStateMachine.State.NEUTRAL).on(EdgeDetector.Edge.NEUTRAL).create();

        } catch (TransitionCreationException e) {
            LOGGER.error("Cannot create state machine for palm X / (counter) horizontal movement", e);
        }

        return stateMachine;
    }

    public StateMachine createForPalmY() {

        EdgeDetector detector = new EdgeDetector(palmYNumFrames, palmYThreshold, r -> r.getPalmPositionY());
        com.himanshuvirmani.StateMachine<AbstractStateMachine.State, EdgeDetector.Edge> machine = new com.himanshuvirmani.StateMachine(AbstractStateMachine.State.NEUTRAL);

        StateMachine stateMachine = new StateMachine(machine, detector, VERTICAL_MAX_LOW, VERTICAL_MAX_MEDIUM, Motion.VerticalCounterMovement);
        stateMachine.setFramesPerSecond(FRAMES_PER_SECOND);

        try {

            // in NEUTRAL
            machine.transition().from(AbstractStateMachine.State.NEUTRAL).to(AbstractStateMachine.State.A).on(EdgeDetector.Edge.UP).setOnSuccessListener((from, to, on) -> stateMachine.incrementCount()).create();
            machine.transition().from(AbstractStateMachine.State.NEUTRAL).to(AbstractStateMachine.State.B).on(EdgeDetector.Edge.DOWN).setOnSuccessListener((from, to, on) -> stateMachine.incrementCount()).create();

            // in A
            machine.transition().from(AbstractStateMachine.State.A).to(AbstractStateMachine.State.NEUTRAL).on(EdgeDetector.Edge.NEUTRAL).create();
            machine.transition().from(AbstractStateMachine.State.A).to(AbstractStateMachine.State.B).on(EdgeDetector.Edge.DOWN).setOnSuccessListener((from, to, on) -> stateMachine.incrementCount()).create();

            // in B
            machine.transition().from(AbstractStateMachine.State.B).to(AbstractStateMachine.State.NEUTRAL).on(EdgeDetector.Edge.NEUTRAL).create();
            machine.transition().from(AbstractStateMachine.State.B).to(AbstractStateMachine.State.A).on(EdgeDetector.Edge.UP).setOnSuccessListener((from, to, on) -> stateMachine.incrementCount()).create();

        } catch (TransitionCreationException e) {
            LOGGER.error("Cannot create state machine for palm Y / vertical movement", e);
        }

        return stateMachine;
    }
}
