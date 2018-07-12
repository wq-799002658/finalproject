import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Created by wq on 2018/7/12.
 */
public class TestLucenc {
    @Test
    public  void createIndex() throws IOException {
        //索引库位置
        Directory directory=FSDirectory.open(new File("E:/index"));
        Analyzer analyzer=new StandardAnalyzer(Version.LUCENE_44);
        //索引写入相关的配置 1.版本号 2.分词器
        IndexWriterConfig config=new IndexWriterConfig(Version.LUCENE_44,analyzer);
        IndexWriter indexWriter=new IndexWriter(directory,config);
        //创建文档模型
        for (int i = 0; i < 10; i++) {
             Document document=new Document();
             document.add(new StringField("id","1"+i, Field.Store.YES));
             document.add(new StringField("title","星期四", Field.Store.YES));
            document.add(new TextField("content","你好！今天天气很好！", Field.Store.YES));
            document.add(new StringField("author","王小Q", Field.Store.YES));
            indexWriter.addDocument(document);

        }
        indexWriter.commit();
        indexWriter.close();


    }
    @Test
    public void  searchIndex() throws IOException {
            Directory dir= FSDirectory.open(new File("E:/index"));
            IndexReader indexReader= DirectoryReader.open(dir);
        IndexSearcher indexSearcher=new IndexSearcher(indexReader);
        //查询条件
        Query query=new TermQuery(new Term("content","你"));
        TopDocs topDocs=indexSearcher.search(query,100);
        ScoreDoc[] scoreDocs=topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            float score=scoreDoc.score;
            int doc=scoreDoc.doc;
            Document document=indexSearcher.doc(doc);
            String title=document.get("title");
            String id=document.get("id");
            String content=document.get("content");
            String author=document.get("author");
            System.out.println("=================");
            System.out.println("文章得分---"+score);
            System.out.println("this is id:"+id);
            System.out.println("this is title:"+title);
            System.out.println("this is content:"+content);
            System.out.println("this is author:"+author);



        }
    }
    @Test
    public void indexDelete() throws IOException {
        Directory directory=FSDirectory.open(new File("E:/index"));
        IndexWriterConfig config=new IndexWriterConfig(Version.LUCENE_44,new StandardAnalyzer(Version.LUCENE_44));
        IndexWriter indexWriter=new IndexWriter(directory,config);
        indexWriter.deleteDocuments(new Term("content","天"));
        indexWriter.commit();
    }
    @Test
public  void indexUpdate() throws IOException {
        Directory directory=FSDirectory.open(new File("E:/index"));
        IndexWriterConfig config=new IndexWriterConfig(Version.LUCENE_44,new StandardAnalyzer(Version.LUCENE_44));
        IndexWriter indexWriter=new IndexWriter(directory,config);
        Document document = new Document();
        document.add(new StringField("id", "11", Field.Store.YES));
        document.add(new StringField("title", "背影", Field.Store.YES));
        document.add(new TextField("content", "你站在这里不要动，我去给你买两个橘子不不不不不不不不不不不不不不不不不不不不", Field.Store.YES));
        document.add(new StringField("author", "朱自清", Field.Store.YES));
        indexWriter.updateDocument(new Term("id", "11"), document);
        indexWriter.commit();

    }
}
