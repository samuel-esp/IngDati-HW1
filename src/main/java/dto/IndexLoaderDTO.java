package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.lucene.search.IndexSearcher;

import java.nio.file.Path;

@Getter @Setter
@AllArgsConstructor
public class IndexLoaderDTO {

    private Path path;
    private IndexSearcher searcher;

}
