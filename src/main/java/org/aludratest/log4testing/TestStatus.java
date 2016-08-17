package org.aludratest.log4testing;

/**
 * Enumeration for the possible states of a {@link TestLogElement}.
 * 
 * @author Marcel Malitz
 * @author Volker Bergmann
 */
public enum TestStatus {

    /** Status of a test that experienced a functional fault. */
    FAILED(true),

    /** Status of a test that was cancelled because a system was not accessible. */
    FAILEDACCESS(true),

    /** Status of a test that was cancelled because the system did not respond in time. */
    FAILEDPERFORMANCE(true),

    /** Status of a test that failed because of faults in the test automation. */
    FAILEDAUTOMATION(true),

    /** Status of a test that failed due to a reason of uncertain type. */
    INCONCLUSIVE(true),

    /** Status of a test that is currently running. */
    RUNNING(false),

    /** Status of a test that has not been started yet. */
    PENDING(false),

    /** Status of a test that has been executed, but possible failures not included in the failure count. */
    IGNORED(false),

    /** Status of a test that has executed correctly. */
    PASSED(false);

    private boolean failure;

    TestStatus(boolean failure) {
        this.failure = failure;
    }

    /** Tells if the status signals a test that finished without success.
     *  @return true if the status indicates a kind of test failure, otherwise true. */
    public boolean isFailure() {
        return failure;
    }

}