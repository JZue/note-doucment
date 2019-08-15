package com.jzue.note.notemysql.service;

import com.jzue.note.notemysql.entity.OptimisticLock;
import com.jzue.note.notemysql.entity.PessimisticLock;
import com.jzue.note.notemysql.repository.OptimisticLockRepository;
import com.jzue.note.notemysql.repository.PessimisticLockRepository;
import org.hibernate.StaleObjectStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallback;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: junzexue
 * @Date: 2019/8/15 上午9:06
 * @Description:
 **/
@Service
public class LockService {

    @Autowired
    private OptimisticLockRepository optimisticLockRepository;

    @Autowired
    private PessimisticLockRepository pessimisticLockRepository;


    @Autowired
    private ThreadPoolExecutor executor;

    /**
     * 乐观锁实现，@Version，并发的时候，会抛出异常,
     **/
    @Transactional
    public void optimisticLockExample(){
        // 快照读
        OptimisticLock one = optimisticLockRepository.getOne(1L);
        one.setCount(one.getCount() + 1);
        optimisticLockRepository.save(one);
    }

    /**
     * 乐观锁实现，cas,这个具体逻辑具体设计，此处逻辑较少~我直
     **/
    @Transactional
    public void optimisticLockExampleCAS() {
        OptimisticLock one = optimisticLockRepository.getOne(1L);
        one.setCount(one.getCount() + 1);
        optimisticLockRepository.save(one);
    }

    /**
     *悲观锁 也叫独占锁（x）锁
     **/
    @Transactional
    public void pessimisticLocExample(){
        PessimisticLock one=pessimisticLockRepository.getOneForUpdate(1L);
        one.setCount(one.getCount() + 1);
        pessimisticLockRepository.save(one);
    }

    /**
     * 共享锁--典型的死锁写法，多个线程同事获取了共享锁，然后到时save操作被阻塞，会抛异常--Deadlock found when trying to get lock; try restarting transaction
     **/
    @Transactional
    public void shareLockExample(){
        PessimisticLock one=pessimisticLockRepository.getOneForShare(1L);
        one.setCount(one.getCount() + 1);
        pessimisticLockRepository.save(one);
    }

}
