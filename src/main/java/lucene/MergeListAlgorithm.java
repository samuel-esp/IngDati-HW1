package lucene;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MergeListAlgorithm {

    public MergeListAlgorithm(){


    }


    public Map<Integer, Integer> run(String inputString, int K) throws IOException {

        //Converto la string in token
        Converter c = new Converter();
        List<String> tokenList = c.parseKeywords(new WhitespaceAnalyzer(), "Table", inputString);
        Path path = Paths.get("target/idx");
        System.out.println(tokenList);

        //Per ogni token genero una mappa Token -> Lista<Documenti>
        HashMap<String, List<Integer>> map = new HashMap<>();
        for (String token : tokenList) {

            System.out.println("EXECUTING " + token );
            IndexReader reader = DirectoryReader.open(FSDirectory.open(path));
            IndexSearcher searcher = new IndexSearcher(reader);
            TopDocs docs = searcher.search(new TermQuery(new Term("Table", token)), 20000000);
            System.out.println("NUMBER OF DOCUMENTS FOUND FOR " + token + " " + docs.scoreDocs.length);
            List<Integer> docList = new ArrayList<>();
            for (ScoreDoc doc : docs.scoreDocs) {
                int docid = doc.doc;
                docList.add(docid);
                map.put(token, docList);
            }
        }


        //Creo e popolo un'altra mappa per salvarmi Documento -> Numero Ripetizioni
        HashMap<Integer, Integer> topKOverlapMergeList = new HashMap<>();
        for (
                Map.Entry<String, List<Integer>> entry : map.entrySet()) {
            List<Integer> docList = entry.getValue();
            for (int docItem : docList) {

                Integer temp = topKOverlapMergeList.get(docItem);
                if (temp == null) {
                    temp = 1;
                    topKOverlapMergeList.put(docItem, temp);
                } else {
                    temp = temp + 1;
                    topKOverlapMergeList.put(docItem, temp);
                }

            }
        }

        //Ordino la mappa sui valori (i valori corrispondono al numero delle ripetizioni)
        SortMapByValueUtil sorter = new SortMapByValueUtil();
        Map<Integer, Integer> topKSorted;
        topKSorted = sorter.sortByValue(topKOverlapMergeList, false);


        return topKSorted;

    }

    public void runPrint(String inputString, int K) throws IOException{

        Map<Integer, Integer> topKSorted = this.run(inputString, K);

        int i = 0;
        for (Map.Entry<Integer, Integer> entry : topKSorted.entrySet()) {
            if(i!=K) {
                System.out.println("DOCUMENT NUMBER: " + entry.getKey().toString() + " REPETITIONS: " + entry.getValue().toString() + "\n");
                i = i + 1;
            }else{
                break;
            }
        }


    }


}
