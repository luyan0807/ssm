package cn.jxnu.utils;

import cn.jxnu.model.Book;
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

public class LuceneIndex_book {
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
        dir= FSDirectory.open(Paths.get("D://lucene//book"));
        SmartChineseAnalyzer analyzer=new SmartChineseAnalyzer();
        IndexWriterConfig iwc=new IndexWriterConfig(analyzer);
        IndexWriter writer=new IndexWriter(dir, iwc);
        return writer;
    }
    /**
     * 添加博客索引
     * @param book
     */
    public void addIndex(Book book)throws Exception{

         /**
         * yes是会将数据存进索引，如果查询结果中需要将记录显示出来就要存进去，如果查询结果
         * 只是显示标题之类的就可以不用存，而且内容过长不建议存进去
         * 使用TextField类是可以用于查询的。
         */

        IndexWriter writer=getWriter();
        Document doc = new Document();

        doc.add(new StringField("id",String.valueOf(book.getId()), Field.Store.YES));
        doc.add(new TextField("bookName",String.valueOf(book.getBookName()), Field.Store.YES));
        doc.add(new TextField("isbn",String.valueOf(book.getIsbn()), Field.Store.YES));
        doc.add(new TextField("author",String.valueOf(book.getAuthor()), Field.Store.YES));
        doc.add(new TextField("summary",String.valueOf(book.getSummary()), Field.Store.YES));
        writer.addDocument(doc);
        writer.close();


    }
    /**
     * 更新博客索引
     * @param book
     * @throws Exception
     */
    public void updateIndex(Book book)throws Exception{
        IndexWriter writer=getWriter();
        Document doc=new Document();

        doc.add(new StringField("id",String.valueOf(book.getId()), Field.Store.YES));
        doc.add(new TextField("bookName",String.valueOf(book.getBookName()), Field.Store.YES));
        doc.add(new TextField("isbn",String.valueOf(book.getIsbn()), Field.Store.YES));
        doc.add(new TextField("author",String.valueOf(book.getAuthor()), Field.Store.YES));
        doc.add(new TextField("summary",String.valueOf(book.getSummary()), Field.Store.YES));

        writer.updateDocument(new Term("id", String.valueOf(book.getId())), doc);
        writer.close();
    }
    /**
     * 删除指定博客的索引
     * @param bookId
     * @throws Exception
     */
    public void deleteIndex(String bookId)throws Exception{
        IndexWriter writer=getWriter();
        writer.deleteDocuments(new Term("id",bookId));
        writer.forceMergeDeletes();
        writer.commit();
        writer.close();
    }
    /**
     * 查询用户
     * @param q 查询关键字
     * @return
     * @throws Exception
     */
    public List<Book> searchBlog(String q)throws Exception{
        /**
         * 注意的是查询索引的位置得是存放索引的位置，不然会找不到。
         */
        dir= FSDirectory.open(Paths.get("D://lucene//book"));
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher is=new IndexSearcher(reader);
        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
        SmartChineseAnalyzer analyzer=new SmartChineseAnalyzer();
        /**
         * username和description就是我们需要进行查找的两个字段
         * 同时在存放索引的时候要使用TextField类进行存放。
         */
        QueryParser parser=new QueryParser("bookName",analyzer);
        Query query=parser.parse(q+"~");
        QueryParser parser2=new QueryParser("author",analyzer);
        Query query2=parser2.parse(q+"~");
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
        List<Book> bookList=new LinkedList<Book>();
        for(ScoreDoc scoreDoc:hits.scoreDocs){
            Document doc=is.doc(scoreDoc.doc);
            Book book=new Book();
            if("".equals(doc.get("id")) || doc.get("id")==null){
             book.setId(null);
            }
            book.setId(Integer.parseInt(doc.get(("id"))));
            book.setAuthor(doc.get("author"));
            book.setIsbn(doc.get("isbn"));
            String bookName=doc.get("bookName");
            String summary=doc.get("summary");
            if(bookName!=null){
                TokenStream tokenStream = analyzer.tokenStream("bookName", new StringReader(bookName));
                String husername=highlighter.getBestFragment(tokenStream, bookName);
                if(StringUtil.isEmpty(husername)){
                    book.setBookName(bookName);
                }else{
                    book.setBookName(husername);
                }
            }
            if(summary!=null){
                TokenStream tokenStream = analyzer.tokenStream("summary", new StringReader(summary));
                String hContent=highlighter.getBestFragment(tokenStream, summary);
                if(StringUtil.isEmpty(hContent)){
                    if(summary.length()<=200){
                        book.setSummary(summary);
                    }else{
                        book.setSummary(summary.substring(0, 200));
                    }
                }else{
                    book.setSummary(hContent);
                }
            }
            bookList.add(book);
        }
        return bookList;
    }



}
