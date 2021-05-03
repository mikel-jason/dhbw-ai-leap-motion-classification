package de.dhbw.tinf18e.LeapMotionClassifier.ai;

import de.dhbw.tinf18e.LeapMotionClassifier.ai.dempster.DempsterHandler;
import de.dhbw.tinf18e.LeapMotionClassifier.ai.dempster.Measure;
import de.dhbw.tinf18e.LeapMotionClassifier.leap.LeapFrame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DifficultyClassifier {

    private static Logger LOGGER = LogManager.getLogger(DifficultyClassifier.class);

    @Autowired
    List<AbstractMotionLevelTranscoder> transcoders;

    public Difficulty classify(LeapFrame frame) {
        DempsterHandler handler = new DempsterHandler(Difficulty.values().length);

        for (AbstractMotionLevelTranscoder transcoder : transcoders) {
            Measure measure = handler.addMeasure();
            Motion motion = transcoder.getMotion();
            FrequencyLevel level = frame.getFrequencyLevel(motion);
            measure.addEntry(transcoder.get(level), transcoder.getCertainty(level));
        }

        handler.accumulateAllMeasures();
        Measure accumulatedMeasure = handler.getFirstMeasure();

        double currentPlausibility = 0.0;
        Difficulty difficulty = Difficulty.ClassD;

        for (int i = 0; i < 4; i++) {
            double testPlausibility = accumulatedMeasure.calculatePlausability(i);

            if (testPlausibility > currentPlausibility) {
                currentPlausibility = testPlausibility;
                difficulty = AbstractMotionLevelTranscoder.getDifficulty(i);
            }
        }

        return difficulty;
    }
}
