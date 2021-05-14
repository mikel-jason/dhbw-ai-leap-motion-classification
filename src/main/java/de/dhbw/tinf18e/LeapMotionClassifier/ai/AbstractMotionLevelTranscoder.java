package de.dhbw.tinf18e.LeapMotionClassifier.ai;

import java.util.ArrayList;
import java.util.List;

/**
 * Base for transforming a frequency leven to input for the DST algorithm.
 *
 * One implementation per motion required as Spring @Service. These are pulled
 * by autowiring, so implementing and annotation is enough to add a new transcoder
 */
public abstract class AbstractMotionLevelTranscoder {

    /** FrequencyLevel to related Difficulty classes, formatted as bitmap for DST algorithm */
    public abstract List<Integer> getMeasureEntryCode(FrequencyLevel level);

    /** Motion handled by this transcoder to read corresponding levels */
    // impl to make adding new transcoders without other adjustments
    public abstract Motion getMotion();

    /** index in DST bitmap to Difficulty, corresponds to getDstBitmap(List) **/
    protected static Difficulty getDifficulty(int index) {
        switch (index) {
            case 0: return Difficulty.ClassA;
            case 1: return Difficulty.ClassB;
            case 2: return Difficulty.ClassC;
            case 3: return Difficulty.ClassD;
        }
        throw new RuntimeException("Unknown class index: " + index);
    }

    /**
     * Get priority of Difficulty, affecting chosen Difficulty on same DST estimation. In such a situation,
     * the higher priority is chosen
     * */
    protected static int getPriority(Difficulty diff) {
        switch (diff) {
            case ClassD: return 0;
            case ClassA: return 1;
            case ClassB: return 2;
            case ClassC: return 3;
        }
        throw new RuntimeException("Unknown priority of Difficulty: " + diff);
    }

    /** to DST bitmap. order important, corresponds to getDifficulty(int) */
    protected static List<Integer> getDstBitmap(List<Difficulty> difficulties) {
        List<Integer> flags = new ArrayList<>();
        flags.add(difficulties.contains(Difficulty.ClassA) ? 1 : 0);
        flags.add(difficulties.contains(Difficulty.ClassB) ? 1 : 0);
        flags.add(difficulties.contains(Difficulty.ClassC) ? 1 : 0);
        flags.add(difficulties.contains(Difficulty.ClassD) ? 1 : 0);
        return flags;
    }

    /** Certainty of the DST measure entry */
    public double getCertainty(FrequencyLevel level) {
        // set standard value, can be overridden
        // detecting a RANDOM level is penalized more towards ClassD
        // by returning a higher certainty
        if (FrequencyLevel.RANDOM.equals(level))
            return 0.9;

        return 0.6;
    }

}
