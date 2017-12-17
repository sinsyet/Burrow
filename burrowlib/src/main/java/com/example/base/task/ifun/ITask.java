package com.example.base.task.ifun;

/**
 * @author YGX
 *
 * <p> 处理数据的接口 </p>
 * @param <T> 需要处理的数据类型
 *
 * @see com.example.base.task.abs.AbsUDPTask
 */
public interface ITask<T> {

    void handle(T t);
}