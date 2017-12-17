package com.example.burrowlib.fun.base;


import com.example.burrowlib.bean.NatResponse;

/**
 *
 * @author YGX
 *
 * 请求观察者
 */

public interface IRequestObserver {

    void onResponse(NatResponse resp);
}
