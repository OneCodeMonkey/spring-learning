package com.liuyang1.spring_learning.mybatis.mapper;

import com.liuyang1.spring_learning.mybatis.entity.User;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

/**
 * @author OneCodeMonkey
 */
public interface UserMapper {
    /**
     * selectUserById
     *
     * @param id
     * @return
     */
    User selectUserById(Integer id);
}
