package de.dhbw.tinf18e.LeapMotionClassifier.ai;

import de.dhbw.tinf18e.LeapMotionClassifier.ai.dempster.DempsterHandler;
import de.dhbw.tinf18e.LeapMotionClassifier.ai.dempster.Measure;
import de.dhbw.tinf18e.LeapMotionClassifier.leap.LeapFrame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service managing the classification of frames to Difficulty
 */
@Service
public class DifficultyClassifier {

    private static Logger LOGGER = LogManager.getLogger(DifficultyClassifier.class);

    @Autowired
    List<AbstractMotionLevelTranscoder> transcoders;

    public Difficulty classify(LeapFrame frame) {
        /*
        Create new DST
        For transcoders (holding the motions, too)
            Add as a measure
            Add one entry holding all Difficulty classes which are possible with the given motion and level
        Summarize measures
        For Difficulty classes
            choose if either
            - higher plausibility
            - same plausibility, but higher priority
         */
        DempsterHandler handler = new DempsterHandler(Difficulty.values().length);

        for (AbstractMotionLevelTranscoder transcoder : transcoders) {
            Measure measure = handler.addMeasure();
            Motion motion = transcoder.getMotion();
            FrequencyLevel level = frame.getFrequencyLevel(motion);
            measure.addEntry(transcoder.getMeasureEntryCode(level), transcoder.getCertainty(level));
        }

        handler.accumulateAllMeasures();
        Measure accumulatedMeasure = handler.getFirstMeasure();

        // init values with class D (unknown)
        double currentPlausibility = 0.0;
        Difficulty currentDifficulty = Difficulty.ClassD;

        for (int i = 0; i < 4; i++) {
            double testPlausibility = accumulatedMeasure.calculatePlausability(i);
            Difficulty testDifficulty = AbstractMotionLevelTranscoder.getDifficulty(i);

            if (testPlausibility > currentPlausibility ||
                    (testPlausibility == currentPlausibility &&
                            AbstractMotionLevelTranscoder.getPriority(testDifficulty) > AbstractMotionLevelTranscoder.getPriority(currentDifficulty))
            ) {
                currentPlausibility = testPlausibility;
                currentDifficulty = testDifficulty;
            }
        }
        return currentDifficulty;
    }
}
