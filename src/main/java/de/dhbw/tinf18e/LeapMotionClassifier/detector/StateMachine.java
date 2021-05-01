package de.dhbw.tinf18e.LeapMotionClassifier.detector;

import com.himanshuvirmani.exceptions.TransitionException;
import de.dhbw.tinf18e.LeapMotionClassifier.ai.FrequencyLevel;
import de.dhbw.tinf18e.LeapMotionClassifier.ai.Motion;
import de.dhbw.tinf18e.LeapMotionClassifier.leap.LeapFrame;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RequiredArgsConstructor
public class StateMachine extends AbstractStateMachine {

    private static final Logger LOGGER = LogManager.getLogger(StateMachine.class);

    private final com.himanshuvirmani.StateMachine<State, EdgeDetector.Edge> machine;

    private final EdgeDetector detector;

    private final double MAX_LOW;

    private final double MAX_MEDIUM;

    private final Motion motion;

    @Getter
    private Integer count = 0;

    @Override
    public void fire(EdgeDetector.Edge edge) {
        try {
            machine.fire(edge);
        } catch (TransitionException e) {
            if (!e.getMessage().startsWith("No transitions defined from "))
                LOGGER.error("Cannot perform, transition", e);
        }
    }

    @Override
    public FrequencyLevel next(LeapFrame frame) {
        EdgeDetector.Edge edge = detector.next(frame.getLeapRecord());
        frame.setEdge(motion, edge);
        fire(edge);
        LOGGER.info(getCount());
        return getFrequencyLevel(getCount(), frame.getFrameNumber(), MAX_LOW, MAX_MEDIUM);
    }

    @Override
    public void incrementCount() {
        count++;
        LOGGER.info("New count general:" + count);
    }
}
