package de.dhbw.tinf18e.LeapMotionClassifier.ai;

import de.dhbw.tinf18e.LeapMotionClassifier.leap.LeapFrame;
import de.dhbw.tinf18e.LeapMotionClassifier.leap.detector.IDetector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
@Service
public class FrequencyAnalyzer {

    @Value("${time.fps}")
    int FRAMES_PER_SECOND;

    public FrequencyLevel analyze(IDetector detector, LeapFrame frame) {
        frame.analyze(detector);
        double frequency = (double) detector.getCount() * 60.0 * (double) FRAMES_PER_SECOND / (double) frame.getFrameNumber();
        if (frequency <= 1) {
            return FrequencyLevel.LOW;
        } else if(frequency <= 2) {
            return FrequencyLevel.MEDIUM;
        } else {
            return FrequencyLevel.HIGH;
        }
    }
}
