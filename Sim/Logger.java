package Sim;

import java.io.FileWriter;
import java.io.IOException;

public class Logger {

    public final static int INFO = 0;
    public final static int  DEBUG = 1;
    public final static int WARNING = 2;
    public final static int ERROR = 3;


    private static String debugPrefix = "[DEBUG]";
    private static String infoPrefix = "[INFO]";
    private static String warningPrefix = "[!!WARNING!!]";
    private static String errorPrefix = "[### ERROR! ###]";

    private FileWriter f;
    private static Logger singleton = null;
    private boolean log_to_file = false;

    private int _loggingLevel = INFO;

    public void startLogging(int loggingLevel) {
        log_to_file = false;
        _loggingLevel = loggingLevel;
    }


    public void startLogging(String filename, int loggingLevel) {
        _loggingLevel = loggingLevel;
        log_to_file = true;
        try {
            f = new FileWriter(filename+".txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void lineBreak(int logLevel) {
        if (_loggingLevel >= logLevel) {
            if(log_to_file) {
                try {
                    f.append("\n");
                }
                catch (Exception e) {
                    System.out.println("Error logging to file");
                }

            }
            else {
                System.out.println();
            }
        }
    }

    public void log(int logLevel, String message) {

        String m = "(Time:" + (double)Math.round(SimEngine.getTime()*100)/100 + "): " + message;
        switch (logLevel) {
            case DEBUG:
                m = debugPrefix + m;
                break;
            case INFO:
                m = infoPrefix + m;
                break;
            case WARNING:
                m = warningPrefix + m;
                break;
            case ERROR:
                m = errorPrefix + m;
                break;
        }
        if(_loggingLevel >= logLevel) {
            if (log_to_file) {
                try {
                    f.append(m);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                System.out.println(m);
            }

        }

    }

    public void close() {
        try {
            f.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Logger get() {
        if (singleton == null) {
            singleton = new Logger();
        }
        return singleton;
    }
 }