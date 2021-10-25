import lucene.Converter;
import lucene.Indexer;
import lucene.SortMapByValueUtil;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import statistics.BarChart;
import statistics.Statistics;
import org.apache.lucene.index.IndexReader;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {

        System.out.println("TYPE 1 TO SEE SOME STATS ...\n");
        System.out.println("TYPE 2 TO START THE INDEXING PROCESS ...\n");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str = br.readLine();

        if(Integer.parseInt(str)==1){

            System.out.println("EXECUTING STATS WAIT ...\n");
            Statistics s  = new Statistics();

            System.out.println("TABLES NUMBER: " + s.getTablesNumber());
            System.out.println("COLUMNS PER TABLE AVERAGE: " + s.getColumnsPerTableAvg());
            System.out.println("COLUMNS DISTRIBUTION: \n" + s.getColumnsDistribution());
            System.out.println("ROWS DISTRIBUTION: \n" + s.getRowsDistribution());
            System.out.println("NULL VALUES PER TABLE AVERAGE: " + s.getNullValuePerTableAvg());

            BarChart columnDistributionChart = new BarChart("Columns Distribution",
                    "Columns Distribution", s.getColumnsDistribution(), "Columns");
            columnDistributionChart.pack( );
            columnDistributionChart.setVisible( true );

            BarChart rowsDistributionChart = new BarChart("Rows Distribution",
                    "Rows Distribution", s.getColumnsDistribution(), "Rows");
            rowsDistributionChart.pack( );
            rowsDistributionChart.setVisible( true );

        }
        else if(Integer.parseInt(str)==2){

            System.out.println("INDEXING ...\n");
            Indexer i = new Indexer();
            System.out.println("\nTASK COMPLETED");

        }else if(Integer.parseInt(str)==3){


            //That's what searching does. Just search using a TermQuery to get all the docIds for a given term:

            String s = "Rome Milan";
            Converter c = new Converter();
            List<String> tokenList = c.parseKeywords(new WhitespaceAnalyzer(), "Table", s);
            Path path = Paths.get("target/idx2");
            System.out.println(tokenList);
/*
            IndexReader reader = DirectoryReader.open(FSDirectory.open(path));
            IndexSearcher searcher = new IndexSearcher(reader);
            QueryParser parser = new QueryParser("Table", new EnglishAnalyzer());
            Query query = parser.parse("ciao");
            TopDocs hits = searcher.search(query, maxHits);
            for (int i = 0; i < hits.scoreDocs.length; i++) {
                ScoreDoc scoreDoc = hits.scoreDocs[i];
                Document doc = searcher.doc(scoreDoc.doc);


                int docid = scoreDoc.doc;

            }

            for(int i: l){

            }*/
            HashMap<String, List<Integer>> map = new HashMap<>();
            for(String token: tokenList) {
                IndexReader reader = DirectoryReader.open(FSDirectory.open(path));
                IndexSearcher searcher = new IndexSearcher(reader);
                System.out.println(token);
                TopDocs docs = searcher.search(new TermQuery(new Term("Table", token)), 20000000);
                System.out.println(docs.scoreDocs.length);
                List<Integer> docList = new ArrayList<>();
                for (ScoreDoc doc : docs.scoreDocs) {
                    //System.out.println("ciao");
                    //System.out.println(doc);
                    int docid = doc.doc;
                    docList.add(docid);
                    map.put(token, docList);
                    //System.out.println(docid);
                }
            }


            System.out.println("PREPARING MAP ...");
            HashMap<Integer, Integer> topKOverlapMergeList = new HashMap<>();
            for (Map.Entry<String, List<Integer>> entry : map.entrySet()) {
                List<Integer> docList = entry.getValue();
                for(int docItem: docList){

                    Integer temp = topKOverlapMergeList.get(docItem);
                    if(temp == null) {
                        temp = 1;
                        topKOverlapMergeList.put(docItem, temp);
                    }else{
                        temp = temp + 1;
                        topKOverlapMergeList.put(docItem, temp);
                    }

                }
            }


            SortMapByValueUtil sorter = new SortMapByValueUtil();
            Map<Integer, Integer> topKSorted;
            topKSorted = sorter.sortByValue(topKOverlapMergeList, false);

            //int K = 10;
            //int i = 0;
            for (Map.Entry<Integer, Integer> entry : topKSorted.entrySet()) {
                //if(i!=K) {
                    System.out.println("DOCUMENT NUMBER: " + entry.getKey().toString() + " REPETITIONS: " + entry.getValue().toString() +"\n");
                    //i = i + 1;
                //}/*else{
                    //break;
                //}
            }



        }
        else{
            System.out.println("YOUR INPUT IS NOT VALID...");
        }

    }
}
