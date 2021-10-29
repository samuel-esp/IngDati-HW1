package lucene;

import dto.IndexLoaderDTO;
import global.GlobalVariables;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class IndexLoaderUtil {

    public IndexLoaderDTO IndexerLoaderUtil() throws IOException {

        Path path = Paths.get(new GlobalVariables().getPath());
        IndexReader reader = DirectoryReader.open(FSDirectory.open(path));
        IndexSearcher searcher = new IndexSearcher(reader);
        searcher.setSimilarity(new ClassicSimilarity());
        searcher.search(new TermQuery(new Term("Table", "test")), 20000000);

        IndexLoaderDTO indexLoaderDTO = new IndexLoaderDTO(path, searcher);

        return indexLoaderDTO;

    }

}
