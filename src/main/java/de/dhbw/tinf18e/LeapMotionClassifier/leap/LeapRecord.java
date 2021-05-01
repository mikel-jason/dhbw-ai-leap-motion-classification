package de.dhbw.tinf18e.LeapMotionClassifier.leap;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import lombok.Setter;

@Data
public class LeapRecord {

    @CsvBindByName(column = "Palm_Position_X")
    private double palmPositionX;

    @CsvBindByName(column = "Palm_Position_Y")
    private double palmPositionY;

    @CsvBindByName(column = "Palm_Position_Z")
    private double palmPositionZ;

    @CsvBindByName(column = "Valid")
    private short valid;

    @CsvBindByName(column = "Thumb")
    private double thumb;

    @CsvBindByName(column = "Spread")
    private double spread;
}
