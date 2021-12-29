package com.jzue.note.notemysql.repository;

import com.jzue.note.notemysql.entity.PessimisticLock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @Author: junzexue
 * @Date: 2019/8/15 下午5:18
 * @Description:
 **/
public interface PessimisticLockRepository extends JpaRepository<PessimisticLock,Long>, JpaSpecificationExecutor<PessimisticLock> {

    @Query(value = "select * from pessimistic_lock p where p.id=?1 for update",nativeQuery =true )
    PessimisticLock getOneForUpdate(Long id);

    @Query(value = "select * from pessimistic_lock p where p.id=?1 Lock in share mode",nativeQuery =true )
    PessimisticLock getOneForShare(Long id);
}
