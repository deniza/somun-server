package gss.server.util;

import java.util.Date;

public class Time {
    
    public static long now() {
        return new Date().getTime();
    }

}
