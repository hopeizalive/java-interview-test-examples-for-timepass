package com.example.interview.studycli.runall;

/**
 * Stable one-line summary for console and PASS/FAIL reports: walk to root cause, prefer message text.
 * Use this everywhere instead of duplicating {@code failureMessage} helpers per study module.
 */
public final class RunAllThrowableFormatter {

    private RunAllThrowableFormatter() {}

    /** Short line for stderr / summary tables (root cause, not full chain). */
    public static String rootCauseMessage(Throwable t) {
        Throwable c = t;
        while (c.getCause() != null && c.getCause() != c) {
            c = c.getCause();
        }
        String m = c.getMessage();
        if (m == null || m.isBlank()) {
            return c.getClass().getSimpleName();
        }
        return c.getClass().getSimpleName() + ": " + m;
    }
}
