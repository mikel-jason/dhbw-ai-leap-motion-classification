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

    @Value("${detectors.palmX.buffer-seconds:0.0}")
    private double palmXTBufferSeconds;

    @Value("${motion.counter-move-horizontal.low}")
    private double HORIZONTAL_MAX_LOW;

    @Value("${motion.counter-move-horizontal.medium}")
    private double HORIZONTAL_MAX_MEDIUM;

    // palm Y
    @Value("${detectors.palmY.num_frames}")
    private int palmYNumFrames;

    @Value("${detectors.palmY.threshold}")
    private double palmYThreshold;

    @Value("${detectors.palmY.buffer-seconds:0.0}")
    private double palmYTBufferSeconds;

    @Value("${motion.move-vertical.low}")
    private double VERTICAL_MAX_LOW;

    @Value("${motion.move-vertical.medium}")
    private double VERTICAL_MAX_MEDIUM;

    public StateMachine createForPalmX() {

        EdgeDetector detector = new EdgeDetector(palmXNumFrames, palmXThreshold, r -> r.getPalmPositionX());
        com.himanshuvirmani.StateMachine<StateMachine.State, EdgeDetector.Edge> machine = new com.himanshuvirmani.StateMachine(StateMachine.State.NEUTRAL);

        StateMachine stateMachine = getStateMachine(detector, machine, HORIZONTAL_MAX_LOW, HORIZONTAL_MAX_MEDIUM, Motion.HorizontalCounterMovement, (int) (palmXTBufferSeconds * FRAMES_PER_SECOND));
        stateMachine.setFramesPerSecond(FRAMES_PER_SECOND);

        try {

            // UP -> DOWN
            machine.transition().from(StateMachine.State.NEUTRAL).to(StateMachine.State.A).on(EdgeDetector.Edge.UP).create();
            machine.transition().from(StateMachine.State.A).to(StateMachine.State.A_B).on(EdgeDetector.Edge.DOWN).setOnSuccessListener((from, to, on) -> stateMachine.incrementCount()).create();

            // DOWN -> UP
            machine.transition().from(StateMachine.State.NEUTRAL).to(StateMachine.State.B).on(EdgeDetector.Edge.DOWN).create();
            machine.transition().from(StateMachine.State.B).to(StateMachine.State.B_A).on(EdgeDetector.Edge.UP).setOnSuccessListener((from, to, on) -> stateMachine.incrementCount()).create();

            // back to NEUTRAL
            machine.transition().from(StateMachine.State.A).to(StateMachine.State.NEUTRAL).on(EdgeDetector.Edge.NEUTRAL).create();
            machine.transition().from(StateMachine.State.A_B).to(StateMachine.State.NEUTRAL).on(EdgeDetector.Edge.NEUTRAL).create();
            machine.transition().from(StateMachine.State.B).to(StateMachine.State.NEUTRAL).on(EdgeDetector.Edge.NEUTRAL).create();
            machine.transition().from(StateMachine.State.B_A).to(StateMachine.State.NEUTRAL).on(EdgeDetector.Edge.NEUTRAL).create();

        } catch (TransitionCreationException e) {
            LOGGER.error("Cannot create state machine for palm X / (counter) horizontal movement", e);
        }

        return stateMachine;
    }

    public StateMachine createForPalmY() {

        EdgeDetector detector = new EdgeDetector(palmYNumFrames, palmYThreshold, r -> r.getPalmPositionY());
        com.himanshuvirmani.StateMachine<StateMachine.State, EdgeDetector.Edge> machine = new com.himanshuvirmani.StateMachine(StateMachine.State.NEUTRAL);

        StateMachine stateMachine = getStateMachine(detector, machine, VERTICAL_MAX_LOW, VERTICAL_MAX_MEDIUM, Motion.VerticalMovement, (int) (palmYTBufferSeconds * FRAMES_PER_SECOND));
        stateMachine.setFramesPerSecond(FRAMES_PER_SECOND);

        try {

            // in NEUTRAL
            machine.transition().from(StateMachine.State.NEUTRAL).to(StateMachine.State.A).on(EdgeDetector.Edge.UP).setOnSuccessListener((from, to, on) -> stateMachine.incrementCount()).create();
            machine.transition().from(StateMachine.State.NEUTRAL).to(StateMachine.State.B).on(EdgeDetector.Edge.DOWN).setOnSuccessListener((from, to, on) -> stateMachine.incrementCount()).create();

            // in A
            machine.transition().from(StateMachine.State.A).to(StateMachine.State.NEUTRAL).on(EdgeDetector.Edge.NEUTRAL).create();
            machine.transition().from(StateMachine.State.A).to(StateMachine.State.B).on(EdgeDetector.Edge.DOWN).setOnSuccessListener((from, to, on) -> stateMachine.incrementCount()).create();

            // in B
            machine.transition().from(StateMachine.State.B).to(StateMachine.State.NEUTRAL).on(EdgeDetector.Edge.NEUTRAL).create();
            machine.transition().from(StateMachine.State.B).to(StateMachine.State.A).on(EdgeDetector.Edge.UP).setOnSuccessListener((from, to, on) -> stateMachine.incrementCount()).create();

        } catch (TransitionCreationException e) {
            LOGGER.error("Cannot create state machine for palm Y / vertical movement", e);
        }

        return stateMachine;
    }

    private StateMachine getStateMachine(EdgeDetector detector, com.himanshuvirmani.StateMachine<StateMachine.State, EdgeDetector.Edge> machine, double max_low, double max_mid, Motion motion, int neutralBuffer) {
        if (neutralBuffer != 0)
            return new BufferedStateMachine(machine, detector, max_low, max_mid, motion, neutralBuffer);

        return new StateMachine(machine, detector, max_low, max_mid, motion);
    }
}
