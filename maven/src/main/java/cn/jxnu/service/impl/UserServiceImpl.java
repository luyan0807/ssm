package cn.jxnu.service.impl;

import cn.jxnu.service.IUserService;
import org.springframework.stereotype.Service;
import cn.jxnu.dao.UserDao;
import cn.jxnu.model.User;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author luyan
 * @version 1.0
 * @date 2018-1-10
 */
@Service("userService")
public class UserServiceImpl implements IUserService{
    @Resource
    private UserDao userdao;

    @Override
    public User getUserByID(int id) {
        return this.userdao.selectByPrimaryKey(id);
    }
    @Override
    public User checkUser(String username,String password){
        return this.userdao.checkUser(username,password);
    }
    @Override
    public int insertUser(User user){
        return this.userdao.insert(user);
    }
    @Override
    public List<User> getAllUser(){return  this.userdao.getAllUser();}
    @Override
    public int updateUser(User user){return this.userdao.updateUser(user);}
    @Override
    public int deleteUser(Integer id){return  this.userdao.deleteByPrimaryKey(id);}
}
