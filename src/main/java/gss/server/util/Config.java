package gss.server.util;

import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 *
 * @author deniz
 */
public class Config {

    private final static String configFile = "server.conf";
    private static Properties props;
    
    static {

        props = new Properties();
        
        try {
            
            props.load(new BufferedReader(new InputStreamReader(new FileInputStream(configFile),"UTF-8")));
            
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    public static String get(String key) {
        return getString(key);
    }
    
    public static String getString(String key) {
        return props.getProperty(key);                
    }
    
    public static int getInt(String key) {
        return Integer.valueOf(getString(key));
    }
    
    public static boolean getBoolean(String key) {
        return getInt(key) == 1;
    }
    
    public static float getFloat(String key) {
        return Float.valueOf(getString(key));
    }
    
}
