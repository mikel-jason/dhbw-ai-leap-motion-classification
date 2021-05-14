package de.dhbw.tinf18e.LeapMotionClassifier.detector;

import de.dhbw.tinf18e.LeapMotionClassifier.ai.Motion;

public class BufferedStateMachine extends StateMachine {

    /** Frames buffered to see if neutral is steady */
    private int frameBuffer;
    /** Number of observed neutral edges in a row */
    private int currentFrameBuffer = 0;

    public BufferedStateMachine(com.himanshuvirmani.StateMachine<State, EdgeDetector.Edge> machine, EdgeDetector detector, double MAX_LOW, double MAX_MEDIUM, double MAX_HIGH, Motion motion, int frameBuffer) {
        super(machine, detector, MAX_LOW, MAX_MEDIUM, MAX_HIGH, motion);
        this.frameBuffer = frameBuffer;
    }

    @Override
    protected void fire(EdgeDetector.Edge edge) {
        if (EdgeDetector.Edge.NEUTRAL.equals(edge) ) {
            if (currentFrameBuffer < frameBuffer) {
                currentFrameBuffer++;
                return;
            }
            /*
                Do nothing special when buffer is reached and neutrals are fired.
                currentFrameBuffer then does not matter anymore, but must not be
                reset until another edge is fired.
             */
        } else {
            // reset for all other edges
            currentFrameBuffer = 0;
        }
        super.fire(edge);
    }
}
