package com.yeyanxiang.util.gitv;

import java.util.List;

/**
 * Deal with the List class
 * Author: fengwx
 * Date: 13-9-23
 */
public class ListUtils {

    public static boolean equalsNull(List<?> list) {
        if (list != null && list.size() > 0) {
            return false;
        }
        return true;
    }
}
