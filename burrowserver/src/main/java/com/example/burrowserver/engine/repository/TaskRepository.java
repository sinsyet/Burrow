package com.example.burrowserver.engine.repository;

import com.example.burrowserver.engine.repository.base.AbsTask;

import java.util.HashMap;

/**
 * @author ygx
 *
 * 基本任务仓库
 */

public class TaskRepository {

    private static HashMap<Integer,AbsTask> mBurrowTasks = new HashMap<>();
    static {
        mBurrowTasks.put(10,null);
    }
}
