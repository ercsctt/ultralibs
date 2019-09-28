package uk.co.ericscott.ultralibs.time;

import com.google.common.base.Preconditions;
import org.apache.commons.lang.time.FastDateFormat;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicBoolean;

public class DateTimeFormats {

    // The format used to show one decimal without a trailing zero.
    public static final ThreadLocal<DecimalFormat> REMAINING_SECONDS = ThreadLocal.withInitial(() -> {
        return new DecimalFormat("0.#");
    });

    public static final ThreadLocal<DecimalFormat> REMAINING_SECONDS_TRAILING = ThreadLocal.withInitial(() -> {
        return new DecimalFormat("0.0");
    });

}
