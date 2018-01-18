package cn.jxnu.dao;
import cn.jxnu.model.User;

import java.util.List;

/**
 * @author luyan
 * @version 1.0
 */
public interface UserDao {
    /**
     * delete user by PrimaryKey
     * @param id
     * @return int
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * insert User
     * @param record
     * @return int
     */
    int insert(User record);

    /**
     * insert User by Selective
     * @param record
     * @return int
     */
    int insertSelective(User record);

    /**
     * select User by PrimaryKey
     * @param id
     * @return User
     */
    User selectByPrimaryKey(Integer id);

    /**
     * update User by PrimaryKeySelective
     * @param record
     * @return int
     */
    int updateByPrimaryKeySelective(User record);

    /**
     * update User by PrimaryKey
     * @param record
     * @return int
     */
    int updateByPrimaryKey(User record);

    /**
     * check User by username and password
     * @param username
     * @param password
     * @return User
     */
    User checkUser(String username,String password);

    List<User> getAllUser();

    int updateUser(User user);
}
