package de.dhbw.tinf18e.LeapMotionClassifier.leap;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeapProcessor {

    public void start(List<LeapRecord> data) {
        List<LeapRecord> validData = data.stream().filter(record -> record.getValid() == 1).collect(Collectors.toList());
    }

}
