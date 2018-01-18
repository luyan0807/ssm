package cn.jxnu.service;
import cn.jxnu.model.User;

import java.util.List;

/**
 * @author luyan
 * @version 1.0
 * @date 2018-1-10
 */
public interface IUserService {
   /**
    * get User by ID
    * @param id
    * @return User
    */
   public User getUserByID(int id);

   /**
    * check user by username and password
    * @param username
    * @param password
    * @return User
    */
   public User checkUser(String username,String password);

    /**
     * add User
     * @param user
     * @return int
     */
   public int insertUser(User user);

   /**
    * get all user
    * @return List<User>
    */
   public List<User> getAllUser();

   /**
    * update user by id
    * @param user
    * @return int
    */
   public int updateUser(User user);

   public int deleteUser(Integer id);
}
