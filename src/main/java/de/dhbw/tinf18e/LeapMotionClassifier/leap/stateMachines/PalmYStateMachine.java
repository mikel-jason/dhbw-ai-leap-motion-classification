package de.dhbw.tinf18e.LeapMotionClassifier.leap.stateMachines;

import com.himanshuvirmani.StateMachine;
import com.himanshuvirmani.exceptions.TransitionCreationException;
import com.himanshuvirmani.exceptions.TransitionException;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class PalmYStateMachine implements IStateMachine {

    private static final Logger LOGGER = LogManager.getLogger(PalmYStateMachine.class);

    private StateMachine<State, Event> machine = new StateMachine(State.NEUTRAL);

    public enum State implements IStateMachineState {
        NEUTRAL, UP, UP_DOWN, DOWN, DOWN_UP
    }

    public enum Event implements IStateMachineEvent {
        HOLD, UP, DOWN
    }

    @Getter
    private int count = 0;

    {
        try {
            // up -> down
            machine.transition().from(State.NEUTRAL).to(State.UP).on(Event.UP).create();
            machine.transition().from(State.UP).to(State.UP_DOWN).on(Event.DOWN).setOnSuccessListener((from, to, on) -> count++).create();

            // down -> up
            machine.transition().from(State.NEUTRAL).to(State.DOWN).on(Event.DOWN).create();
            machine.transition().from(State.NEUTRAL).to(State.DOWN_UP).on(Event.UP).setOnSuccessListener((from, to, on) -> count++).create();

            // to neutral
            machine.transition().from(State.UP).to(State.NEUTRAL).on(Event.HOLD).create();
            machine.transition().from(State.DOWN).to(State.NEUTRAL).on(Event.HOLD).create();
            machine.transition().from(State.UP_DOWN).to(State.NEUTRAL).on(Event.HOLD).create();
            machine.transition().from(State.DOWN_UP).to(State.NEUTRAL).on(Event.HOLD).create();

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
