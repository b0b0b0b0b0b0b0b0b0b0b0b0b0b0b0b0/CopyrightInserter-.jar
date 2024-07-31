package org.b0b0b0;

import org.b0b0b0.processing.JarProcessor;

public class CopyrightInserter {

    public static void main(String[] args) {
        JarProcessor processor = new JarProcessor();
        processor.processJars();
    }
}
