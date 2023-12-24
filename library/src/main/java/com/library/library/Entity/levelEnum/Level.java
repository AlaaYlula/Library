package com.library.library.Entity.levelEnum;

public enum Level {
    ERROR,
    WARN,
    INFO;

    public static Level fromString(String levelValue) {
        return Level.valueOf(levelValue.toUpperCase());
    }
}
