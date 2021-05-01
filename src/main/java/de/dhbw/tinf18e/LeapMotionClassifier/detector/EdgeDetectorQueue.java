package de.dhbw.tinf18e.LeapMotionClassifier.detector;

import de.dhbw.tinf18e.LeapMotionClassifier.leap.LeapRecord;
import lombok.RequiredArgsConstructor;

import java.util.Deque;
import java.util.LinkedList;

@RequiredArgsConstructor
public class EdgeDetectorQueue {

    private final int NUM_FRAMES;
    private final double THRESHOLD;
    private final ValueExtractor extractor;

    private Deque<LeapRecord> queue = new LinkedList();

    public void next(LeapRecord record) {
        queue.add(record);
        while (queue.size() > NUM_FRAMES) {
            queue.remove();
        }
    }

    public double getMax() {
        return queue.stream().map(record -> extractor.getValue(record)).max(Double::compare).get();
    }

    public double getMin() {
        return queue.stream().map(record -> extractor.getValue(record)).min(Double::compare).get();
    }

    public double getOldest() {
        return extractor.getValue(queue.getFirst());
    }

    public double getNewest() {
        return extractor.getValue(queue.getLast());
    }

    public boolean isThresholdReached() {
        return (getMax() - getMin()) >= THRESHOLD;
    }

    public boolean isRising() {
        return getOldest() < getNewest();
    }

    public boolean isDropping() {
        return getOldest() > getNewest();
    }


}
