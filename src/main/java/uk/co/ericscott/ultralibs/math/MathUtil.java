package uk.co.ericscott.ultralibs.math;

import java.text.NumberFormat;
import java.util.Locale;

public class MathUtil
{
    /**
     * Rounds doubles in order for optimisation purposes for storage and
     * also comes in use at random times to why not?
     *
     * @param value  - Value to round.
     * @param places - decimal places
     * @return Nicely rounded double value.
     */
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException(); //Don't be the idiot that sets this off please.

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static String commaFormat(int value) {
        return NumberFormat.getNumberInstance(Locale.US).format(value);
    }
}
