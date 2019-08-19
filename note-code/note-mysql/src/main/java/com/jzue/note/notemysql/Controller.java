package com.jzue.note.notemysql;

import com.jzue.note.notemysql.entity.BigData;
import com.jzue.note.notemysql.service.BigDataService;
import com.jzue.note.notemysql.service.LockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.concurrent.ThreadPoolExecutor;

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

    @Autowired
    private BigDataService bigDataService;

    @Autowired
    private ThreadPoolExecutor executor;

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


    @GetMapping("generateData")
    public String generateData(){
        Long startTimestamp=System.currentTimeMillis();
        for(int i=0;i<40;i++){
            executor.execute(()->{
                bigDataService.produceData();
            });
        }
//        System.out.println(" All spend time............"+(System.currentTimeMillis()-startTimestamp));
//        return "generateData................success--耗时："+(System.currentTimeMillis()-startTimestamp);
        return "任务已提交";
    }
}
