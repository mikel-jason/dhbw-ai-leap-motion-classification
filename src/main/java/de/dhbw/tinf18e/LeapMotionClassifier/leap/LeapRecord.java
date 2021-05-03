package de.dhbw.tinf18e.LeapMotionClassifier.leap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.Setter;

/**
 * Entity representing a LEAP data record as given
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LeapRecord {
    private double palmPositionX;
    private double palmPositionY;
    private double palmPositionZ;
    private short valid;
    private double thumb;
    private double spread;
}
