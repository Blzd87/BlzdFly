package com.blzd.fly;

public class TimeUtil {

    public static long parseTime(String input) {

        input = input.toLowerCase();

        try {

            if (input.endsWith("s")) {
                return Long.parseLong(input.substring(0, input.length() - 1));
            }

            if (input.endsWith("m")) {
                return Long.parseLong(input.substring(0, input.length() - 1)) * 60;
            }

            if (input.endsWith("h")) {
                return Long.parseLong(input.substring(0, input.length() - 1)) * 3600;
            }

            if (input.endsWith("d")) {
                return Long.parseLong(input.substring(0, input.length() - 1)) * 86400;
            }

        } catch (NumberFormatException ignored) {
        }

        return -1;
    }

    public static String formatTime(long seconds) {

        long days = seconds / 86400;
        seconds %= 86400;

        long hours = seconds / 3600;
        seconds %= 3600;

        long minutes = seconds / 60;
        seconds %= 60;

        StringBuilder builder = new StringBuilder();

        if (days > 0) {
            builder.append(days).append("d ");
        }

        if (hours > 0) {
            builder.append(hours).append("h ");
        }

        if (minutes > 0) {
            builder.append(minutes).append("m ");
        }

        if (seconds > 0 || builder.isEmpty()) {
            builder.append(seconds).append("s");
        }

        return builder.toString().trim();
    }
}
