
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import cn.jxnu.model.User;
import cn.jxnu.service.IUserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class TestMyBatis {
    private static Logger logger = Logger.getLogger(TestMyBatis.class);

    @Resource
    private IUserService userService;


    @Test
    public void test() {
        User user = userService.getUserByID(5);
        logger.info("值："+user.getUserName());
        logger.info(JSON.toJSONString(user));
    }
    @Test
    public void testUpdate(){
        User user=new User();
        user.setAge(12);
        user.setPassword("123");
        user.setUserName("luyan");
        user.setId(1);
        int i=userService.updateUser(user);
        logger.info(i);
    }
    @Test
    public void testDelete(){
        int i=userService.deleteUser(5);
        logger.info(i);
    }
}
