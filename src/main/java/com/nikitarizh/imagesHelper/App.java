package com.nikitarizh.imagesHelper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {

    public static Logger logger = LogManager.getRootLogger();

    private static final boolean prod = true;
    private static final boolean console = false;

    public static void main(String[] args) {
        if (prod) {
            if (console) {
                UI.start();
            }
            else {
                GUI.main(args);
            }
        }
    }
}
