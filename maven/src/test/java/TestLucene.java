import cn.jxnu.model.Book;
import cn.jxnu.model.User;
import cn.jxnu.utils.LuceneIndex;
import cn.jxnu.utils.LuceneIndex_book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class TestLucene {
    @Test
    public void testAddLucene() throws Exception{
        User user = new User();
        user.setId(1);
        user.setUserName("luyan");
        user.setPassword("luayn");
        LuceneIndex luceneIndex=new LuceneIndex();
        luceneIndex.addIndex(user);
    }
    @Test
    public void delete() throws  Exception{
    LuceneIndex luceneIndex=new LuceneIndex();
    luceneIndex.deleteIndex("1");
    }
    @Test
    public void testAddLuceneBook() throws  Exception{
        Book book=new Book();
        book.setId(1);
        book.setBookName("无声告白");
        book.setIsbn("765723523");
        book.setAuthor("tom");
        book.setSummary("分公司" +
                "部分归属风格风格和山东分公司发法国红酒上个寒假是否说鬼话浮生恢复时间规划规划规划");
        LuceneIndex_book luceneIndex_book=new LuceneIndex_book();
        luceneIndex_book.addIndex(book);
    }
    @Test
    public void testQuery() throws Exception{
        LuceneIndex_book luceneIndex_book=new LuceneIndex_book();
       List<Book> bookList= luceneIndex_book.searchBlog("无声告");
       if(bookList.size()<=0){
           System.out.println("empty");
       }else{
           for (Book b:bookList){
               System.out.println(b.getBookName());
               System.out.println(b.getAuthor());
               System.out.println(b.getSummary());
           }
       }
    }
}