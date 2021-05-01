package de.dhbw.tinf18e.LeapMotionClassifier;

import de.dhbw.tinf18e.LeapMotionClassifier.io.LeapDataReader;
import de.dhbw.tinf18e.LeapMotionClassifier.leap.LeapProcessor;
import de.dhbw.tinf18e.LeapMotionClassifier.leap.LeapRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

@SpringBootApplication
public class LeapMotionClassifierApplication implements ApplicationRunner {

    private static final Logger LOGGER = LogManager.getLogger(LeapMotionClassifierApplication.class);

    @Autowired
    private LeapDataReader reader;

    @Autowired
    private LeapProcessor leapProcessor;

    public static void main(String[] args) {
        SpringApplication.run(LeapMotionClassifierApplication.class, args);
    }


    /**
     * Handles parsing input file and starts processpr
     * @see LeapProcessor#start(List) 
     * @param args
     */
    @Override
    public void run(ApplicationArguments args) {

        String fileArg;
        try {
            fileArg = args.getOptionValues("file").get(0);
            if (fileArg == null || fileArg.length() == 3) {
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            LOGGER.error("Missing argument: file");
            return;
        }

        try {
            Path path = (new File(fileArg)).toPath();
            List<LeapRecord> leapRecords = reader.readLeapData(path);
            leapProcessor.start(leapRecords);
        } catch (Exception e) {
            LOGGER.error(e);
        }

    }

}
