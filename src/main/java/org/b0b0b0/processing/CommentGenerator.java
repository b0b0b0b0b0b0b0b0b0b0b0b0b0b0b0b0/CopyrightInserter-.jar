package org.b0b0b0.processing;
import java.util.concurrent.ThreadLocalRandom;
public class CommentGenerator {
    private static final String[] COLORS = new String[]{
            "31m", "32m", "33m", "34m", "35m", "36m", "37m", "90m"
    };
    public static String generateComment() {
        String color = COLORS[ThreadLocalRandom.current().nextInt(COLORS.length)];
        return "\u001B[" + color + ConfigLoader.getDetailedComment() + "\u001B[0m";
    }
}
