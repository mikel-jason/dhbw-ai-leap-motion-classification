package de.dhbw.tinf18e.LeapMotionClassifier.leap.stateMachines;

import com.himanshuvirmani.StateMachine;
import com.himanshuvirmani.exceptions.TransitionCreationException;
import com.himanshuvirmani.exceptions.TransitionException;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class PalmXStateMachine implements IStateMachine {

    private static final Logger LOGGER = LogManager.getLogger(PalmXStateMachine.class);

    private StateMachine<State, Event> machine = new StateMachine(State.NEUTRAL);

    public enum State implements IStateMachineState {
        NEUTRAL, LEFT, LEFT_RIGHT, RIGHT, RIGHT_LEFT
    }

    public enum Event implements IStateMachineEvent {
        HOLD, LEFT, RIGHT
    }

    @Getter
    private int count = 0;

    {
        try {
            // left -> right
            machine.transition().from(State.NEUTRAL).to(State.LEFT).on(Event.LEFT).create();
            machine.transition().from(State.LEFT).to(State.LEFT_RIGHT).on(Event.RIGHT).setOnSuccessListener((from, to, on) -> count++).create();

            // right -> left
            machine.transition().from(State.NEUTRAL).to(State.RIGHT).on(Event.RIGHT).create();
            machine.transition().from(State.NEUTRAL).to(State.RIGHT_LEFT).on(Event.LEFT).setOnSuccessListener((from, to, on) -> count++).create();

            // to neutral
            machine.transition().from(State.LEFT).to(State.NEUTRAL).on(Event.HOLD).create();
            machine.transition().from(State.RIGHT).to(State.NEUTRAL).on(Event.HOLD).create();
            machine.transition().from(State.LEFT_RIGHT).to(State.NEUTRAL).on(Event.HOLD).create();
            machine.transition().from(State.RIGHT_LEFT).to(State.NEUTRAL).on(Event.HOLD).create();

        } catch (TransitionCreationException e) {
            LOGGER.error("Cannot initialize state machine transitions", e);
        }

        machine.setStateChangeListener((from, to, on) -> LOGGER.info("Changed state from " + from + " to " + to));
    }

    @Override
    public void fire(IStateMachineEvent event) throws TransitionException {
        machine.fire((Event) event);
    }

    @Override
    public IStateMachineState getCurrentState() {
        return machine.getCurrentState();
    }
}
