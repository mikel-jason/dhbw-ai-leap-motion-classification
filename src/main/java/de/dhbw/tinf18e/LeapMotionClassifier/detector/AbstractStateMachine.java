package de.dhbw.tinf18e.LeapMotionClassifier.detector;

import de.dhbw.tinf18e.LeapMotionClassifier.ai.FrequencyLevel;
import de.dhbw.tinf18e.LeapMotionClassifier.leap.LeapFrame;
import lombok.Setter;

public abstract class AbstractStateMachine {

    enum State {
        NEUTRAL, A, A_B, B, B_A
    }

    // cannot bind with @Value b.c. class is abstract
    // cannot set in constructor b.c. it's overridden in impl classes
    @Setter
    int framesPerSecond;

    protected abstract void fire(EdgeDetector.Edge edge);

    public abstract FrequencyLevel next(LeapFrame frame);

    protected abstract void incrementCount();

    public abstract Integer getCount();

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
