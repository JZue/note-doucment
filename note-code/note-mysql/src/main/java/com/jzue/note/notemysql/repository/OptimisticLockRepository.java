package com.jzue.note.notemysql.repository;

import com.jzue.note.notemysql.entity.OptimisticLock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Author: junzexue
 * @Date: 2019/8/15 上午9:04
 * @Description:
 **/
public interface OptimisticLockRepository extends JpaRepository<OptimisticLock,Long> , JpaSpecificationExecutor<OptimisticLock> {
}
