package com.yeyanxiang.util.gitv;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by yezi on 15-12-16.
 */
public class GMTDateFormat extends SimpleDateFormat {


    /**
     * Constructs a new {@code SimpleDateFormat} for formatting and parsing
     * dates and times in the {@code SHORT} style for the user's default locale.
     * See "<a href="../util/Locale.html#default_locale">Be wary of the default locale</a>".
     */
    public GMTDateFormat() {
        super();
        init();
    }

    /**
     * Constructs a new {@code SimpleDateFormat} using the specified
     * non-localized pattern and the {@code DateFormatSymbols} and {@code
     * Calendar} for the user's default locale.
     * See "<a href="../util/Locale.html#default_locale">Be wary of the default locale</a>".
     *
     * @param pattern the pattern.
     * @throws NullPointerException     if the pattern is {@code null}.
     * @throws IllegalArgumentException if {@code pattern} is not considered to be usable by this
     *                                  formatter.
     */
    public GMTDateFormat(String pattern) {
        super(pattern);
        init();
    }

    /**
     * Constructs a new {@code SimpleDateFormat} using the specified
     * non-localized pattern and {@code DateFormatSymbols} and the {@code
     * Calendar} for the user's default locale.
     * See "<a href="../util/Locale.html#default_locale">Be wary of the default locale</a>".
     *
     * @param template the pattern.
     * @param value    the DateFormatSymbols.
     * @throws NullPointerException     if the pattern is {@code null}.
     * @throws IllegalArgumentException if the pattern is invalid.
     */
    public GMTDateFormat(String template, DateFormatSymbols value) {
        super(template, value);
        init();
    }

    /**
     * Constructs a new {@code SimpleDateFormat} using the specified
     * non-localized pattern and the {@code DateFormatSymbols} and {@code
     * Calendar} for the specified locale.
     *
     * @param template the pattern.
     * @param locale   the locale.
     * @throws NullPointerException     if the pattern is {@code null}.
     * @throws IllegalArgumentException if the pattern is invalid.
     */
    public GMTDateFormat(String template, Locale locale) {
        super(template, locale);
        init();
    }

    /**
     * Constructs a new {@code SimpleDateFormat} using the specified
     * non-localized pattern and the {@code DateFormatSymbols} and {@code
     * Calendar} for the user's default locale.
     * See "<a href="../util/Locale.html#default_locale">Be wary of the default locale</a>".
     *
     * @param pattern the pattern.
     * @param GMT     the GMT.
     * @throws NullPointerException     if the pattern is {@code null}.
     * @throws IllegalArgumentException if {@code pattern} is not considered to be usable by this
     *                                  formatter.
     */
    public GMTDateFormat(String pattern, String GMT) {
        super(pattern);
        init(GMT);
    }

    /**
     * Constructs a new {@code SimpleDateFormat} using the specified
     * non-localized pattern and {@code DateFormatSymbols} and the {@code
     * Calendar} for the user's default locale.
     * See "<a href="../util/Locale.html#default_locale">Be wary of the default locale</a>".
     *
     * @param template the pattern.
     * @param value    the DateFormatSymbols.
     * @param GMT      the GMT.
     * @throws NullPointerException     if the pattern is {@code null}.
     * @throws IllegalArgumentException if the pattern is invalid.
     */
    public GMTDateFormat(String template, DateFormatSymbols value, String GMT) {
        super(template, value);
        init(GMT);
    }

    /**
     * Constructs a new {@code SimpleDateFormat} using the specified
     * non-localized pattern and the {@code DateFormatSymbols} and {@code
     * Calendar} for the specified locale.
     *
     * @param template the pattern.
     * @param locale   the locale.
     * @param GMT      the GMT.
     * @throws NullPointerException     if the pattern is {@code null}.
     * @throws IllegalArgumentException if the pattern is invalid.
     */
    public GMTDateFormat(String template, Locale locale, String GMT) {
        super(template, locale);
        init(GMT);
    }

    private void init() {
        setTimeZone(TimeZone.getTimeZone("GMT+8"));
    }

    private void init(String GMT) {
        setTimeZone(TimeZone.getTimeZone(GMT));
    }

}
