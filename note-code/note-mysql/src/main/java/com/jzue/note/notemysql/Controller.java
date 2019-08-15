package com.jzue.note.notemysql;

import com.jzue.note.notemysql.service.LockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: junzexue
 * @Date: 2019/8/15 上午9:17
 * @Description:
 **/
@RequestMapping
@RestController
public class Controller {

    @Autowired
    private LockService lockService;

    @GetMapping("/optimisticLock")
    public String optimisticLock(){
        lockService.optimisticLockExample();
        return "optimisticLock demo---request success..........";
    }
    /**
     * cas 保持并发不出错
     **/
    @GetMapping("/optimisticLockCAS")
    public String optimisticLockCAS(){
        try {
            lockService.optimisticLockExampleCAS();
        }catch (Exception e){
            optimisticLockCAS();
        }
        return "optimisticLockCAS demo---request success..........";
    }

    @GetMapping("/pessimisticLock")
    public String pessimisticLock(){
        lockService.pessimisticLocExample();
        return "pessimisticLock demo---request success..........";
    }

    @GetMapping("/shareLock")
    public String shareLock(){
        lockService.shareLockExample();
        return "shareLock demo---request success..........";
    }
}
