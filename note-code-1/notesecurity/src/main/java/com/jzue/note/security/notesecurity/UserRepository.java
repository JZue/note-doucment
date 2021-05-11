package com.jzue.note.security.notesecurity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author jzue
 * @date 2019/9/27 下午8:20
 **/
public interface UserRepository extends JpaRepository<User,Long> , JpaSpecificationExecutor<User> {
}
