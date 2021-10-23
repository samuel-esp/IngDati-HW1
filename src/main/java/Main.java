import lucene.Indexer;
import statistics.BarChart;
import statistics.Statistics;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;

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

        }
        else{
            System.out.println("YOUR INPUT IS NOT VALID...");
        }
        
    }
}
