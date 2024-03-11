package com.example.dy.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkFormatter {

    private static final String URL_PATTERN = "(https?://\\S+)";
    private static final Pattern pattern = Pattern.compile(URL_PATTERN);

    public static String formatLinks(String content) {
        Matcher matcher = pattern.matcher(content);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            matcher.appendReplacement(sb, "<a href=\"$1\">$1</a>");
        }
        matcher.appendTail(sb);

        return sb.toString();
    }
}
