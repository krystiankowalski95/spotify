package pl.lodz.p.it.zzpj.spotify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TempUtils {
    public static List<Integer> getRandomFromRangeUnreapeated(int max, int count){
        Integer[] arr = new Integer[max];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i;
        }
        Collections.shuffle(Arrays.asList(arr));
        return new ArrayList<>(Arrays.asList(arr).subList(0, count));
    }
}
