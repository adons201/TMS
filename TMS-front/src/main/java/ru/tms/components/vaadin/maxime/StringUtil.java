package ru.tms.components.vaadin.maxime;

/**
 * String utility class.
 */
public abstract class StringUtil {

    /**
     * Checks if the string is not blank.
     *
     * @param str
     *            the string to check.
     * @return {@code true} if the string is not blank.
     * @see #isBlank(String)
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * Checks if the string is blank
     *
     * @param string
     *            the string to check.
     *
     * @return {@code true} if the string is blank.
     * @see #isEmpty(String)
     */
    public static boolean isBlank(String string) {
        if (string == null || string.length() == 0) {
            return true;
        }
        int stringLength = string.length();
        for (int i = 0; i < stringLength; i++) {
            if ((Character.isWhitespace(string.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns a null-safe string.
     *
     * @param value
     *            the value.
     * @see #getNullSafeString(String,String)
     * @see #notEmpty(String)
     *
     * @return the value, or an empty string if value is {@code null}. This is same as <code>getNullSafeString(value, "")</code>
     */
    public static String getNullSafeString(String value) {
        return getNullSafeString(value,"");
    }

    /**
     * Returns a null-safe string.
     *
     * @param value
     *            the value.
     * @param defaultValue
     *            value to be returned if is null
     * @see #getNullSafeString(String)
     * @see #notEmpty(String,String)
     *
     * @return the value, or defaultValue string if value is {@code null}.
     */
    public static String getNullSafeString(String value, String defaultValue) {
        return value == null ? defaultValue : value;
    }

    /**
     * Returns a empty-safe string.
     *
     * @param value
     *            the value.
     *
     * @return true if value is null or contains just blanks.
     */
    public static boolean isEmpty(String value) {
        return value ==  null || value.trim().length() == 0;
    }

    /**
     * Returns a empty-safe string.
     *
     * @param value
     *            the value.
     * @param defaultValue
     *            value to be returned if is empty
     * @see #isEmpty(String)
     *
     * @return the value, or defaultValue string if value is {{@link #isEmpty(String)}}.
     */
    public static String notEmpty(String value, String defaultValue) {
        return isEmpty(value) ? defaultValue  : value;
    }

    /**
     * Returns a empty-safe string.
     *
     * @param value
     *            the value.
     * @see #isEmpty(String)
     *
     * @return the value, or an empty string if value is {{@link #isEmpty(String)}}.
     */
    public static String notEmpty(String value) {
        return notEmpty(value, "");
    }

    public static String toValid3ByteUTF8String(String s) {
        final int length = s.length();
        StringBuilder b = new StringBuilder(length);
        for (int offset = 0; offset < length; ) {
            final int codepoint = s.codePointAt(offset);

            // do something with the codepoint
            if (codepoint > "\uFFFF".codePointAt(0)) {
                b.append("\uFFFD");
            } else {
                if (Character.isValidCodePoint(codepoint)) {
                    b.appendCodePoint(codepoint);
                } else {
                    b.append("\uFFFD");
                }
            }
            offset += Character.charCount(codepoint);
        }
        return b.toString();
    }

}