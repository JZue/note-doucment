package com.jzue.msauth.dao;

import com.jzue.msauth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.Repository;

import javax.jws.soap.SOAPBinding;
import java.util.List;

/**
 * @Author: junzexue
 * @Date: 2019/4/8 上午10:28
 * @Description:
 **/
public interface UserDao extends JpaRepository<User,String>, JpaSpecificationExecutor<User> {

    List<User> findByUNameAndPWord(String uname, String pword);
}
