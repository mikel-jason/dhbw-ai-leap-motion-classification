package de.dhbw.tinf18e.LeapMotionClassifier.leap;

import de.dhbw.tinf18e.LeapMotionClassifier.ai.FrequencyLevel;
import de.dhbw.tinf18e.LeapMotionClassifier.ai.Motion;
import de.dhbw.tinf18e.LeapMotionClassifier.detector.EdgeDetector;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Frame object storing all info of a certain frame of the measurement.
 */
@RequiredArgsConstructor
public class LeapFrame {

    private static final Logger LOGGER = LogManager.getLogger(LeapFrame.class);

    @Value("${time.fps}")
    int FRAMES_PER_SECOND;

    @Getter private final LeapRecord leapRecord;
    @Getter private final int frameNumber;

    private Map<Motion, EdgeDetector.Edge> motionEdges = new HashMap<>();
    private Map<Motion, FrequencyLevel> motionFrequencyLevels = new HashMap<>();

    public void setFrequencyLevel(Motion motion, FrequencyLevel frequencyLevel) {
        motionFrequencyLevels.put(motion, frequencyLevel);
    }

    public FrequencyLevel getFrequencyLevel(Motion motion) {
        return motionFrequencyLevels.get(motion);
    }

    public void setEdge(Motion motion, EdgeDetector.Edge edge) {
        motionEdges.put(motion, edge);
    }

    public EdgeDetector.Edge getEdge(Motion motion) {
        return motionEdges.get(motion);
    }

}
