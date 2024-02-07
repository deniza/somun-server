package gss.server.util;

import java.util.ArrayList;

public class ArrayHelper {

    public static int[] toIntArray(ArrayList<Integer> input) {

        int output[] = new int[input.size()];
        int index = 0;
        for (Integer i : input) {
            output[index++] = i;
        }

        return output;

    }

}
