package org.vaelow233.vconomy.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListUtil {
    public static <T> List<T> of(T... items) {
        return new ArrayList<>(Arrays.asList(items));
    }
}
