package gss.server.util;

public class StringUtils {
    
    public static String convertToDelimitedString(int[] arr) {
        return convertToDelimitedString(arr, ", ");
    }
    
    public static String convertToDelimitedString(int[] arr, String delimiter) {

        StringBuilder sb = new StringBuilder("["); // starting brace

        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1) {
                sb.append(delimiter);
            }
        }        
        sb.append("]");

        return sb.toString();

    }

}
