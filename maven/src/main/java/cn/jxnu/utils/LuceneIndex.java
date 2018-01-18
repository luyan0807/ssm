package cn.jxnu.utils;

import cn.jxnu.model.User;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.StringReader;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class LuceneIndex {
    private Directory dir=null;


    /**
     * 获取IndexWriter实例
     * @return
     * @throws Exception
     */
    private IndexWriter getWriter()throws Exception{
        /**
         * 生成的索引我放在了C盘，可以根据自己的需要放在具体位置
         */
        dir= FSDirectory.open(Paths.get("D://lucene"));
        SmartChineseAnalyzer analyzer=new SmartChineseAnalyzer();
        IndexWriterConfig iwc=new IndexWriterConfig(analyzer);
        IndexWriter writer=new IndexWriter(dir, iwc);
        return writer;
    }
    /**
     * 添加博客索引
     * @param user
     */
    public void addIndex(User user)throws Exception{
        IndexWriter writer=getWriter();
        Document doc=new Document();
        doc.add(new StringField("id",String.valueOf(user.getId()), Field.Store.YES));
        /**
         * yes是会将数据存进索引，如果查询结果中需要将记录显示出来就要存进去，如果查询结果
         * 只是显示标题之类的就可以不用存，而且内容过长不建议存进去
         * 使用TextField类是可以用于查询的。
         */
        doc.add(new TextField("username", user.getUserName(), Field.Store.YES));
        doc.add(new TextField("password",user.getPassword(), Field.Store.YES));
        writer.addDocument(doc);
        writer.close();
    }
    /**
     * 更新博客索引
     * @param user
     * @throws Exception
     */
    public void updateIndex(User user)throws Exception{
        IndexWriter writer=getWriter();
        Document doc=new Document();
        doc.add(new StringField("id",String.valueOf(user.getId()), Field.Store.YES));
        doc.add(new TextField("username", user.getUserName(), Field.Store.YES));
        doc.add(new TextField("password",user.getPassword(), Field.Store.YES));
        writer.updateDocument(new Term("id", String.valueOf(user.getId())), doc);
        writer.close();
    }
    /**
     * 删除指定博客的索引
     * @param userId
     * @throws Exception
     */
    public void deleteIndex(String userId)throws Exception{
        IndexWriter writer=getWriter();
        writer.deleteDocuments(new Term("id", userId));
        writer.forceMergeDeletes(); // 强制删除
        writer.commit();
        writer.close();
    }
    /**
     * 查询用户
     * @param q 查询关键字
     * @return
     * @throws Exception
     */
    public List<User> searchBlog(String q)throws Exception{
        /**
         * 注意的是查询索引的位置得是存放索引的位置，不然会找不到。
         */
        dir= FSDirectory.open(Paths.get("D://lucene"));
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher is=new IndexSearcher(reader);
        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
        SmartChineseAnalyzer analyzer=new SmartChineseAnalyzer();
        /**
         * username和description就是我们需要进行查找的两个字段
         * 同时在存放索引的时候要使用TextField类进行存放。
         */
        QueryParser parser=new QueryParser("bookName",analyzer);
        Query query=parser.parse(q);
        QueryParser parser2=new QueryParser("",analyzer);
        Query query2=parser2.parse(q);
        booleanQuery.add(query, BooleanClause.Occur.SHOULD);
        booleanQuery.add(query2, BooleanClause.Occur.SHOULD);
        TopDocs hits=is.search(booleanQuery.build(), 100);
        QueryScorer scorer=new QueryScorer(query);
        Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
        /**
         * 这里可以根据自己的需要来自定义查找关键字高亮时的样式。
         */
        SimpleHTMLFormatter simpleHTMLFormatter=new SimpleHTMLFormatter("<b><font color='red'>","</font></b>");
        Highlighter highlighter=new Highlighter(simpleHTMLFormatter, scorer);
        highlighter.setTextFragmenter(fragmenter);
        List<User> userList=new LinkedList<User>();
        for(ScoreDoc scoreDoc:hits.scoreDocs){
            Document doc=is.doc(scoreDoc.doc);
            User user=new User();
            if("".equals(doc.get("id")) || doc.get("id")==null){
             user.setId(null);
            }
            user.setId(Integer.parseInt(doc.get(("id"))));
            user.setPassword(doc.get(("password")));
            String username=doc.get("username");
            String password=doc.get("password");
            if(username!=null){
                TokenStream tokenStream = analyzer.tokenStream("username", new StringReader(username));
                String husername=highlighter.getBestFragment(tokenStream, username);
                if(StringUtil.isEmpty(husername)){
                    user.setUserName(username);
                }else{
                    user.setUserName(husername);
                }
            }
            if(password!=null){
                TokenStream tokenStream = analyzer.tokenStream("password", new StringReader(password));
                String hContent=highlighter.getBestFragment(tokenStream, password);
                if(StringUtil.isEmpty(hContent)){
                    if(password.length()<=200){
                        user.setPassword(password);
                    }else{
                        user.setPassword(password.substring(0, 200));
                    }
                }else{
                    user.setPassword(hContent);
                }
            }
            userList.add(user);
        }
        return userList;
    }



}
