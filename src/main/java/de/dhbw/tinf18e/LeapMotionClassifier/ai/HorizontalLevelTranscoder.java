package de.dhbw.tinf18e.LeapMotionClassifier.ai;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class HorizontalLevelTranscoder extends AbstractMotionLevelTranscoder {
    @Override
    public List<Integer> get(FrequencyLevel level) {
        switch (level) {
            case HIGH:
                return transformToFlags(Arrays.asList(Difficulty.ClassA, Difficulty.ClassB));
            case MEDIUM:
                return transformToFlags(Arrays.asList(Difficulty.ClassB, Difficulty.ClassC));
            case LOW:
                return transformToFlags(Arrays.asList(Difficulty.ClassC));
        }
        return null;
    }

    @Override
    public Motion getMotion() {
        return Motion.HorizontalCounterMovement;
    }
}
