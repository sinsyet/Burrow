package com.example.natclient.fun.base;

import com.example.natclient.bean.NatResponse;

/**
 *
 * @author YGX
 *
 * 请求观察者
 */

public interface IRequestObserver {

    void onResponse(NatResponse resp);
}
