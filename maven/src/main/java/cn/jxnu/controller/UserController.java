package cn.jxnu.controller;

import cn.jxnu.model.Book;
import cn.jxnu.model.User;
import cn.jxnu.service.IUserService;
import cn.jxnu.utils.LuceneIndex;
import cn.jxnu.utils.LuceneIndex_book;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author luyan
 * @version 1.0
 * @date 2018-1-10
 */

@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    @RequestMapping("/showUser")
    public @ResponseBody Map<String,Object> showUser() {
        Map<String,Object> map=new HashMap<String, Object>(16);
        List<User> userList=userService.getAllUser();
        if(userList.size()<=0){
            map.put("code", "1");
            map.put("message","没有数据");
            map.put("userList",null);
        }else{
            map.put("userList",userList);
            map.put("code","0");
            map.put("message","查询成功");
        }
        return map;
    }
    @RequestMapping("/login")
    public @ResponseBody Map<String,String> login(HttpServletRequest request){
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        Map<String,String> map=new HashMap<String, String>(16);
        if((!"".equals(username)) || (!"".equals(password))){
            User u=userService.checkUser(username,password);
            if (u != null) {
               map.put("code","0");
               map.put("message","登陆成功");
            } else {
                map.put("code","1");
                map.put("message","登陆失败,用户名或密码错误");
            }
        }else{
            map.put("code","1");
            map.put("message","登陆失败,用户名或密码为空");
        }
        return map;
    }
    @RequestMapping("/register")
    public @ResponseBody Map<String,String> register(User user){
        Map<String,String> map=new HashMap<String, String>(16);
        int i=userService.insertUser(user);
        if(i==0){
            map.put("code","0");
            map.put("message","添加用户失败");
        }else {
            map.put("code", "1");
            map.put("message", "添加用户成功");
        }
        return map;
    }
    @RequestMapping("/updateUser")
    public  @ResponseBody Map<String,String> updateUser(User user){
        Map<String,String> map=new HashMap<String, String>(16);
        int i=userService.updateUser(user);
        if(i==1){
            map.put("code","0");
            map.put("message","修改成功");
        }else {
            map.put("code", "1");
            map.put("message", "修改失败");
        }
        return map;
    }
    @RequestMapping("/deleteUser")
    public @ResponseBody Map<String,String> deleteUser(String id){
        Map<String,String> map=new HashMap<String, String>(16);
        if("".equals(id)){
            map.put("code","1");
            map.put("message","id 为空 删除失败");
        }
        int i=userService.deleteUser(Integer.parseInt(id));
        if(i==1){
            map.put("code","0");
            map.put("message","删除成功");
        }
        return map;
    }
    @RequestMapping("/q")
    public @ResponseBody Map<String,Object> search(@RequestParam(value = "q", required = false,defaultValue = "") String q,
                         @RequestParam(value = "page", required = false, defaultValue = "1") String page,
                         Model model,
                         HttpServletRequest request) throws Exception {
        Map<String,Object> map=new HashMap<String, Object>(16);
        LuceneIndex luceneIndex = new LuceneIndex() ;
        List<User> userList = luceneIndex.searchBlog(q);
        /**
         * 关于查询之后的分页我采用的是每次分页发起的请求都是将所有的数据查询出来，
         * 具体是第几页再截取对应页数的数据，典型的拿空间换时间的做法，如果各位有什么
         * 高招欢迎受教。
         */
        Integer toIndex = userList.size() >= Integer.parseInt(page) * 5 ? Integer.parseInt(page) * 5 : userList.size();
        List<User> newList = userList.subList((Integer.parseInt(page) - 1) * 5, toIndex);
        map.put("userList",newList);
        map.put("resultTotal",userList.size());
        map.put("q",q);
        return map;
    }
    /**
     * 查询之后的分页
     * @param page
     * @param totalNum
     * @param q
     * @param pageSize
     * @param projectContext
     * @return
     */
    private String genUpAndDownPageCode(int page,Integer totalNum,String q,Integer pageSize,String projectContext){
        long totalPage=totalNum%pageSize==0?totalNum/pageSize:totalNum/pageSize+1;
        StringBuffer pageCode=new StringBuffer();
        if(totalPage==0){
            return "";
        }else{
            pageCode.append("<nav>");
            pageCode.append("<ul class='pager' >");
            if(page>1){
                pageCode.append("<li><a href='"+projectContext+"/q?page="+(page-1)+"&q="+q+"'>上一页</a></li>");
            }else{
                pageCode.append("<li class='disabled'><a href='#'>上一页</a></li>");
            }
            if(page<totalPage){
                pageCode.append("<li><a href='"+projectContext+"/q?page="+(page+1)+"&q="+q+"'>下一页</a></li>");
            }else{
                pageCode.append("<li class='disabled'><a href='#'>下一页</a></li>");
            }
            pageCode.append("</ul>");
            pageCode.append("</nav>");
        }
        return pageCode.toString();
    }
    @RequestMapping("/qBook")
    public @ResponseBody Map<String,Object> searchBook(@RequestParam(value = "q", required = false,defaultValue = "") String q,
                                                   @RequestParam(value = "page", required = false, defaultValue = "1") String page,
                                                   HttpServletRequest request) throws Exception {
        Map<String,Object> map=new HashMap<String, Object>(16);
        LuceneIndex_book luceneIndex_book=new LuceneIndex_book();
        List<Book> bookList = luceneIndex_book.searchBlog(q);
        /**
         * 关于查询之后的分页我采用的是每次分页发起的请求都是将所有的数据查询出来，
         * 具体是第几页再截取对应页数的数据，典型的拿空间换时间的做法，如果各位有什么
         * 高招欢迎受教。
         */
        map.put("userList",bookList);
        map.put("resultTotal",bookList.size());
        map.put("q",q);
        return map;
    }
}