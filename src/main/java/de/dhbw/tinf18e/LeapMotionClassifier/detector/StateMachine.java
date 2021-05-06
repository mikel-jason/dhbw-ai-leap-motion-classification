package de.dhbw.tinf18e.LeapMotionClassifier.detector;

import com.himanshuvirmani.exceptions.TransitionException;
import de.dhbw.tinf18e.LeapMotionClassifier.ai.FrequencyLevel;
import de.dhbw.tinf18e.LeapMotionClassifier.ai.Motion;
import de.dhbw.tinf18e.LeapMotionClassifier.leap.LeapFrame;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Machine to analyze a time-series value based on an EdgeDetector
 */
@RequiredArgsConstructor
public class StateMachine {

    enum State {
        NEUTRAL, A, A_B, B, B_A
    }

    private static final Logger LOGGER = LogManager.getLogger(StateMachine.class);

    /** Finite state machine */
    private final com.himanshuvirmani.StateMachine<State, EdgeDetector.Edge> machine;

    @Setter
    int framesPerSecond;

    private final EdgeDetector detector;

    /** Max value to be classified as LOW frequency */
    private final double MAX_LOW;

    /** Max value to be classified as MEDIUM frequency */
    private final double MAX_MEDIUM;

    /** Motion to be analyzed with this machine */
    private final Motion motion;

    /** Count of relevant state(s) */
    @Getter
    private Integer count = 0;

    /**
     * Load next frame to machine to be analyzed
     * @param frame
     * @return
     */
    public FrequencyLevel next(LeapFrame frame) {
        EdgeDetector.Edge edge = detector.next(frame.getLeapRecord());
        frame.setEdge(motion, edge);
        fire(edge);
        return getFrequencyLevel(getCount(), frame.getFrameNumber(), MAX_LOW, MAX_MEDIUM);
    }

    /**
     * Apply edge on finite state machine
     * @param edge
     */
    protected void fire(EdgeDetector.Edge edge) {
        try {
            machine.fire(edge);
        } catch (TransitionException e) {
            // 3rd party lib throws an error when there's no matching transition
            // hide error to not have to define every transition possible
            if (!e.getMessage().startsWith("No transitions defined from "))
                LOGGER.error("Cannot perform, transition", e);
        }
    }

    protected void incrementCount() {
        // required as extra method to be available in factory for state machine transition success handlers
        count++;
    }

    protected FrequencyLevel getFrequencyLevel(double count, double frameNumber, double max_low, double max_mid) {
        double frequency = count * 60.0 * (double) framesPerSecond / frameNumber;
        if (frequency <= max_low) {
            return FrequencyLevel.LOW;
        } else if (frequency <= max_mid) {
            return FrequencyLevel.MEDIUM;
        } else {
            return FrequencyLevel.HIGH;
        }
    }
}
