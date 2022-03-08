package timing;
/* timing.Timer.java implements a simple stopwatch/timer type class based
 * on Java wall-clock time.
 *
 * RUNNING() == true  <==>  start() was called with no corresponding
 *                          call to stop()
 *
 * All times are given in units of msec.
 */

public class Timer {

    /* Initializes timing.Timer to 0 msec */
    public Timer() {
        reset();
    }

    /* Starts the timer. Accumulates time across multiple calls to start. */
    public void start() {
        running = true;
        tStart = System.nanoTime();
        tFinish = tStart;
    }

    /* Stops the timer.
     * Returns the time elapsed since the last matching call to start(), or
     * 0 if no such matching call was made. */
    public double stop() {
        tFinish = System.nanoTime();
        if (running) {
            running = false;
            double diff = tFinish - tStart;
            tAccum += diff;
            return diff/1000; // Return in microseconds!!
        }
        return 0;
    }

    /*
     * If RUNNING()     ==>     Returns the time since last call to start()
     * If !RUNNING()    ==>     Returns total elapsed time
     */
    public double elapsed() {
        if( running )
            return System.nanoTime() - tStart;

        return tAccum;
    }

    /* Stops timing if currently RUNNING() and resets the accumulated
     * time to 0. */
    public void reset() {
        running = false;
        tStart = tFinish = 0;
        tAccum = 0;
    }

    private boolean running;
    private double tStart;
    private double tFinish;
    private double tAccum;    // total time

}
