package de.dhbw.tinf18e.LeapMotionClassifier.ai;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMotionLevelTranscoder {
    public abstract List<Integer> get(FrequencyLevel level);
    public abstract Motion getMotion();

    // set standard value, can be overridden
    public double getCertainty(FrequencyLevel level) {
        return 0.6;
    }

    protected List<Integer> transformToFlags(List<Difficulty> difficulties) {
        List<Integer> flags = new ArrayList<>();
        flags.add(difficulties.contains(Difficulty.ClassA) ? 1 : 0);
        flags.add(difficulties.contains(Difficulty.ClassB) ? 1 : 0);
        flags.add(difficulties.contains(Difficulty.ClassC) ? 1 : 0);
        flags.add(difficulties.contains(Difficulty.ClassD) ? 1 : 0);
        return flags;
    }

    protected static Difficulty getDifficulty(int index) {
        switch (index) {
            case 0: return Difficulty.ClassA;
            case 1: return Difficulty.ClassB;
            case 2: return Difficulty.ClassC;
            case 3: return Difficulty.ClassD;
        }
        throw new RuntimeException("Unknown class index: " + index);
    }
    protected static int getPriority(Difficulty diff) {
        // ordered: on same criteria values, class with higher priority is selected
        switch (diff) {
            case ClassD: return 0;
            case ClassA: return 1;
            case ClassB: return 2;
            case ClassC: return 3;
        }
        throw new RuntimeException("Unknown priority of Difficulty: " + diff);
    }

}
