package lucene;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Cell;
import models.Table;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.codecs.Codec;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.codecs.simpletext.SimpleTextCodec;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Indexer {

    public Indexer() throws IOException {

        Path path = Paths.get("target/idx");

        try (Directory directory = FSDirectory.open(path)) {
            indexDocs(directory, new SimpleTextCodec());
            directory.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void indexDocs(Directory directory, Codec codec) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory();

        var tablesWrapper = new Object(){ int tablesCount = 0; };

        try(BufferedReader br = new BufferedReader(new FileReader("Table.json"))) {
            Iterator<Table> value = mapper.readValues(jsonFactory.createParser(br), Table.class);
            Analyzer defaultAnalyzer = new StandardAnalyzer();
            Map<String, Analyzer> perFieldAnalyzers = new HashMap<>();
            perFieldAnalyzers.put("Table", new WhitespaceAnalyzer());

            //inizializzo analyzer, indexwriter
            Analyzer analyzer = new PerFieldAnalyzerWrapper(defaultAnalyzer, perFieldAnalyzers);
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            if (codec != null) {
                config.setCodec(codec);
            }
            IndexWriter writer = null;
            try {
                writer = new IndexWriter(directory, config);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                writer.deleteAll();
            } catch (IOException e) {
                e.printStackTrace();
            }

            IndexWriter finalWriter = writer;

            value.forEachRemaining((u) -> {

                //Per ogni tabella creo una stringa contenente tutti i termini testuali al suo interno
                StringBuilder stringBuilder = new StringBuilder();

                for (Cell c: u.getCells()) {
                    stringBuilder.append(c.getCleanedText());
                    stringBuilder.append(" ");
                }

                if(tablesWrapper.tablesCount%100==0) {
                    System.out.println(tablesWrapper.tablesCount + "\n");
                }
                Document doc = new Document();
                doc.add(new TextField("Table", stringBuilder.toString(), Field.Store.YES));

                try {
                    finalWriter.addDocument(doc);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                tablesWrapper.tablesCount = tablesWrapper.tablesCount + 1;

            });

            try {
                finalWriter.commit();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                finalWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
