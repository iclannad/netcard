package com.blink.blinkp2p.Controller.Activity.login;

import java.util.Comparator;

/**
 * Created by Administrator on 2017/4/24.
 */
public class SortComparator<T> implements Comparator<T> {

    @Override
    public int compare(T lhs, T rhs) {
        // TODO Auto-generated method stub
        LoginBeanGson a=(LoginBeanGson) lhs;
        LoginBeanGson b=(LoginBeanGson) rhs;
        if(a.getTime()-b.getTime()>=0)
            return -1;
        return 1;
    }

}