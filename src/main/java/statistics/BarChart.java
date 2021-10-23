package statistics;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class BarChart extends ApplicationFrame {

    public BarChart(String applicationTitle , String chartTitle, HashMap<Integer, Integer> map, String type) {
        super( applicationTitle );
        JFreeChart barChart = ChartFactory.createBarChart(
                chartTitle,
                "Category",
                "Score",
                createDataset(map, type),
                PlotOrientation.HORIZONTAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel( barChart );
        chartPanel.setPreferredSize(new java.awt.Dimension( 780 , 360 ) );
        setContentPane( chartPanel );
    }

    private CategoryDataset createDataset(HashMap<Integer, Integer> map, String type) {
        final DefaultCategoryDataset dataset =
                new DefaultCategoryDataset( );

        Map<Integer, Integer> treeMap = new TreeMap<Integer, Integer>(map);
        for (Map.Entry<Integer, Integer> entry : treeMap.entrySet()) {
            if(entry.getKey()<40) {
                dataset.addValue(entry.getValue(), type, Integer.toString(entry.getKey()));
            }
        }


        return dataset;
    }

}
