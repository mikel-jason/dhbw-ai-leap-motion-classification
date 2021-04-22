package de.dhbw.tinf18e.LeapMotionClassifier.leap.detector;

import com.himanshuvirmani.exceptions.TransitionCreationException;
import com.himanshuvirmani.exceptions.TransitionException;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class StateMachine  {

    private static final Logger LOGGER = LogManager.getLogger(StateMachine.class);

    private com.himanshuvirmani.StateMachine<State, Event> machine = new com.himanshuvirmani.StateMachine(State.NEUTRAL);

    public enum State {
        NEUTRAL, A, A_B, B, B_A
    }

    public enum Event {
        HOLD, A, B
    }

    @Getter
    private int count = 0;

    {
        try {
            // A -> B
            machine.transition().from(State.NEUTRAL).to(State.A).on(Event.A).create();
            machine.transition().from(State.A).to(State.A_B).on(Event.B).setOnSuccessListener((from, to, on) -> count++).create();

            // B -> A
            machine.transition().from(State.NEUTRAL).to(State.B).on(Event.B).create();
            machine.transition().from(State.B).to(State.B_A).on(Event.A).setOnSuccessListener((from, to, on) -> count++).create();

            // to neutral
            machine.transition().from(State.A).to(State.NEUTRAL).on(Event.HOLD).create();
            machine.transition().from(State.B).to(State.NEUTRAL).on(Event.HOLD).create();
            machine.transition().from(State.A_B).to(State.NEUTRAL).on(Event.HOLD).create();
            machine.transition().from(State.B_A).to(State.NEUTRAL).on(Event.HOLD).create();

        } catch (TransitionCreationException e) {
            LOGGER.error("Cannot initialize state machine transitions", e);
        }
    }

    public void fire(Event event) throws TransitionException {
        machine.fire(event);
    }

    public State getCurrentState() {
        return machine.getCurrentState();
    }
}
