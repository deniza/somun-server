package gss;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author deniz
 */
public class GssLogger {
    
    static Logger logger = LoggerFactory.getLogger(GssLogger.class);
    
    public static void info(String message) {
        logger.info(message);
    }

    public static void info(String message, Object... args) {        
        logger.info(String.format(message, args));
    }
    
    public static void error(String message) {
        logger.error(message);
    }

    public static void error(String message, Object... args) {        
        logger.error(String.format(message, args));
    }
        
}
