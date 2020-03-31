import java.util.concurrent.Semaphore;

/**
 * Class Monitor
 * To synchronize dining philosophers.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class Monitor {
    /*
     * ------------
     * Data members
     * ------------
     */

    private enum status {eating, hungry, thinking, talking}

    private status state[];
    private Object locks [];
    //private Semaphore self [];
    private int N;

    /**
     * Constructor
     */
    public Monitor(int piNumberOfPhilosophers) {
        // TODO: set appropriate number of chopsticks based on the # of philosophers

        state = new status[piNumberOfPhilosophers];
        for (int i = 0; i < piNumberOfPhilosophers; i++) {
            state[i] = status.thinking;
        }
        //self = new Semaphore[piNumberOfPhilosophers];
        N = piNumberOfPhilosophers;
        locks = new Object[piNumberOfPhilosophers];

        for (int i = 0; i < piNumberOfPhilosophers; i++){
            locks[i] = new Object();
        }

    }

    /*
     * -------------------------------
     * User-defined monitor procedures
     * -------------------------------
     */

    private synchronized void test(final int piTID) {

        synchronized (locks[piTID]) {

            int specialCaseNb = 0;

            if (piTID == 0) {
                specialCaseNb = N;
            } else {
                specialCaseNb = piTID;
            }

            System.out.println("\n****");
            System.out.println("pid:  " + piTID);
            System.out.println("left neighbor status: (state[(specialCaseNb - 1) % N], left neighbor pid: " + (specialCaseNb - 1) % N + ", result: " + (state[(specialCaseNb - 1) % N]));
            System.out.println("status that is checked: status.eating, result: " + status.eating);
            System.out.println("condition#1: (state[(specialCaseNb - 1) % N] != status.eating, result: " + (state[(specialCaseNb - 1) % N] != status.eating));
            System.out.println("-------");
            System.out.println("myself status: state[piTID], result: " + state[piTID]);
            System.out.println("status that is checked: status.hungry, result: " + status.hungry);
            System.out.println("condition#2: state[piTID] == status.hungry, result: " + (state[piTID] == status.hungry));
            System.out.println("-------");
            System.out.println("right neighbor status: state[(piTID + 1) % N], right neighbor pid: " + (piTID + 1) % N + ", result: " + state[(piTID + 1) % N]);
            System.out.println("status that is checked: status.eating, result: " + status.eating);
            System.out.println("condition#3: state[(piTID + 1) % N] != status.eating), result: " + (state[(piTID + 1) % N] != status.eating));
            System.out.println("****\n");

            if ((state[(specialCaseNb - 1) % N] != status.eating) && (state[piTID] == status.hungry) && (state[(piTID + 1) % N] != status.eating)) {

                System.out.println("Inside test method ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ Philosopher [" + piTID + "] is eating...");

                state[piTID] = status.eating;
                locks[piTID].notify();
//			try {
//				self[piTID].acquire();
//			}catch (InterruptedException ie){
//
//			}

            }
        }


    }

    /**
     * Grants request (returns) to eat when both chopsticks/forks are available.
     * Else forces the philosopher to wait()
     */
    public synchronized void pickUp(final int piTID) {
        //SSystem.out.println("piTID: "+piTID+"\n");

        synchronized (locks[piTID]) {

            state[piTID] = status.hungry;
            test(piTID);
            if (state[piTID] != status.eating) {
                try {
                    //self[piTID].wait();
                    locks[piTID].wait();
                } catch (InterruptedException ie) {

                    DiningPhilosophers.reportException(ie);

                }
            }
        }


    }

    /**
     * When a given philosopher's done eating, they put the chopstiks/forks down
     * and let others know they are available.
     */
    public synchronized void putDown(final int piTID) {



            state[piTID] = status.thinking;
            int specialCaseNb = 0;

            if (piTID == 0) {
                specialCaseNb = N;
            } else {
                specialCaseNb = piTID;
            }

        System.out.println("################ piTID: "+piTID+" Testing left neighbor");
            test((specialCaseNb - 1) % N);
        System.out.println("################ piTID: "+piTID+" Testing right neighbor");
            test((piTID + 1) % N);
    }

    /**
     * Only one philopher at a time is allowed to philosophy
     * (while she is not eating).
     */
    public synchronized void requestTalk() {
        // ...
    }

    /**
     * When one philosopher is done talking stuff, others
     * can feel free to start talking.
     */
    public synchronized void endTalk() {
        // ...
    }
}

// EOF
