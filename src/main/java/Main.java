import dto.IndexLoaderDTO;
import global.GlobalVariables;
import lucene.IndexLoaderUtil;
import lucene.Indexer;
import lucene.MergeListAlgorithm;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.*;
import statistics.BarChart;
import statistics.Statistics;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {


    public static void main(String[] args) throws IOException {

        System.out.println("LOADING THIS TASK CAN TAKE SOME MINUTES...\n");
        IndexLoaderDTO indexLoaderDTO = null;
        System.out.println("TYPE 0 TO EXIT...\n");
        System.out.println("TYPE 1 TO SEE SOME STATS ...\n");
        System.out.println("TYPE 2 TO START THE INDEXING PROCESS ...\n");
        System.out.println("TYPE 3 TO EXECUTE MERGE LIST - TOP K OVERLAP ...\n");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str = br.readLine();
        while (Integer.parseInt(str) != 0) {
            if (Integer.parseInt(str) == 1) {

                System.out.println("EXECUTING STATS WAIT ...\n");
                Statistics s = new Statistics();

                System.out.println("TABLES NUMBER: " + s.getTablesNumber());
                System.out.println("COLUMNS PER TABLE AVERAGE: " + s.getColumnsPerTableAvg());
                System.out.println("ROWS PER TABLE AVERAGE: " + s.getRowsPerTableAvg());
                System.out.println("COLUMNS DISTRIBUTION: \n" + s.getColumnsDistribution());
                System.out.println("ROWS DISTRIBUTION: \n" + s.getRowsDistribution());
                System.out.println("NULL VALUES PER TABLE AVERAGE: " + s.getNullValuePerTableAvg());
                System.out.println("DISTINCT VALUES PER COLUMN: " + s.getDistinctValues());

                BarChart columnDistributionChart = new BarChart("Columns Distribution",
                        "Columns Distribution", s.getColumnsDistribution(), "Columns");
                columnDistributionChart.pack();
                columnDistributionChart.setVisible(true);

                BarChart rowsDistributionChart = new BarChart("Rows Distribution",
                        "Rows Distribution", s.getRowsDistribution(), "Rows");
                rowsDistributionChart.pack();
                rowsDistributionChart.setVisible(true);

                BarChart distinctValuesDistributionChart = new BarChart("Distinct Value Distribution Per Column",
                        "Distinct Value Distribution Per Column", s.getDistinctValuesDistribution(), "Columns");
                distinctValuesDistributionChart.pack();
                distinctValuesDistributionChart.setVisible(true);

                System.out.println("\nTASK COMPLETED...\n");
                System.out.println("TYPE 0 TO EXIT...\n");
                System.out.println("TYPE 1 TO SEE SOME STATS ...\n");
                System.out.println("TYPE 2 TO START THE INDEXING PROCESS ...\n");
                System.out.println("TYPE 3 TO EXECUTE MERGE LIST - TOP K OVERLAP ...\n");

            } else if (Integer.parseInt(str) == 2) {

                System.out.println("INDEXING ...\n");
                Indexer i = new Indexer();
                System.out.println("OPTIMIZING THE INDEX THIS CAN TAKE A COUPLE OF MINUTES");
                indexLoaderDTO = new IndexLoaderUtil().IndexerLoaderUtil();
                System.out.println("\n TASK COMPLETED...\n");
                System.out.println("TYPE 0 TO EXIT...\n");
                System.out.println("TYPE 1 TO SEE SOME STATS ...\n");
                System.out.println("TYPE 2 TO START THE INDEXING PROCESS ...\n");
                System.out.println("TYPE 3 TO EXECUTE MERGE LIST - TOP K OVERLAP ...\n");


            } else if (Integer.parseInt(str) == 3) {

                System.out.println("TYPE AN INPUT STRING ... \n");
                String input = br.readLine();
                System.out.println("TYPE A K ... \n");
                String K = br.readLine();

                MergeListAlgorithm m = new MergeListAlgorithm(indexLoaderDTO.getSearcher(), indexLoaderDTO.getPath());
                m.runPrint(input, Integer.parseInt(K));

                System.out.println("\nTASK COMPLETED...\n");
                System.out.println("TYPE 0 TO EXIT...\n");
                System.out.println("TYPE 1 TO SEE SOME STATS ...\n");
                System.out.println("TYPE 2 TO START THE INDEXING PROCESS ...\n");
                System.out.println("TYPE 3 TO EXECUTE MERGE LIST - TOP K OVERLAP ...\n");

            } else {
                System.out.println("YOUR INPUT IS NOT VALID ...");
            }

            str = br.readLine();

        }
    }
}
