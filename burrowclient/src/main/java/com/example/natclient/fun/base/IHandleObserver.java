package com.example.natclient.fun.base;

/**
 * @author YGX
 *
 * selector处理观察者
 */

public interface IHandleObserver {

    /**
     * 心跳反馈的时候
     * @param tag 标签
     * @param host 主机名
     * @param port 端口号
     */
    void onPitpat(String tag, String host, int port);

    /**
     * 注册成功后
     * @param tag
     * @param host
     * @param port
     */
    void onRegistered(String tag, String host, int port);

}
