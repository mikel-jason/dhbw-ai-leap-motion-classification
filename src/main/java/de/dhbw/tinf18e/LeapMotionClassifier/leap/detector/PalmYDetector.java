package de.dhbw.tinf18e.LeapMotionClassifier.leap.detector;

import de.dhbw.tinf18e.LeapMotionClassifier.leap.LeapRecord;
import org.springframework.stereotype.Service;

@Service
public class PalmYDetector implements IDetector {

    private EdgeDetectorQueue edgeDetector = new EdgeDetectorQueue(25, 35.0, record -> record.getPalmPositionY());

    @Override
    public void next(LeapRecord record) {
        edgeDetector.next(record);
        if (edgeDetector.isThresholdReached()) {
            if (edgeDetector.isRising()) {
                System.out.println("Moving up");
            } else {
                System.out.println("Moving down");
            }
        } else {
            System.out.println("Not moving");
        }
    }
}
