package de.dhbw.tinf18e.LeapMotionClassifier.leap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Entity representing a LEAP data record as given
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LeapRecord {

    @JsonProperty("Palm_Position_X")
    private double palmPositionX;

    @JsonProperty("Palm_Position_Y")
    private double palmPositionY;

    @JsonProperty("Palm_Position_Z")
    private double palmPositionZ;

    @JsonProperty("Valid")
    private short valid;

    @JsonProperty("Thumb")
    private double thumb;

    @JsonProperty("Spread")
    private double spread;
}
