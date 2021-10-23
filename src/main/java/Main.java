import statistics.BarChart;
import statistics.Statistics;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        System.out.println("EXECUTING ...");
        Statistics s  = new Statistics();

        System.out.println("TABLES NUMBER: " + s.getTablesNumber());
        System.out.println("COLUMNS PER TABLE AVERAGE: " + s.getColumnsPerTableAvg());
        System.out.println("COLUMNS DISTRIBUTION: \n" + s.getColumnsDistribution());
        System.out.println("ROWS DISTRIBUTION: \n" + s.getRowsDistribution());
        System.out.println("NULL VALUES PER TABLE AVERAGE: " + s.getNullValuePerTableAvg());

        BarChart columnDistributionChart = new BarChart("Columns Distribution",
                "Columns Distribution", s.getColumnsDistribution(), "Columns");
        columnDistributionChart.pack( );
        //RefineryUtilities.centerFrameOnScreen( chart );
        columnDistributionChart.setVisible( true );

        BarChart rowsDistributionChart = new BarChart("Rows Distribution",
                "Rows Distribution", s.getColumnsDistribution(), "Rows");
        rowsDistributionChart.pack( );
        //RefineryUtilities.centerFrameOnScreen( chart );
        rowsDistributionChart.setVisible( true );


    }
}
