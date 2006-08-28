package org.carlmanaster.allelogram.model;

public class Settings {

    public static String stripComments(String line) {
        int commentStart = line.indexOf('!');
        if (commentStart < 0)
            return line;
        return line.substring(0, commentStart).trim();
    }

}
