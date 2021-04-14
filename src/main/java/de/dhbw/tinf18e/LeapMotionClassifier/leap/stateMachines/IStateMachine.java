package de.dhbw.tinf18e.LeapMotionClassifier.leap.stateMachines;

import com.himanshuvirmani.exceptions.TransitionException;

public interface IStateMachine {

    void fire(IStateMachineEvent event) throws TransitionException;

    IStateMachineState getCurrentState();

    int getCount();

}
