package statistics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dto.WrapperDTO;
import lombok.Getter;
import lombok.Setter;
import models.Cell;
import models.MaxDimension;
import models.Row;
import models.Table;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

@JsonDeserialize
@JsonSerialize
@Getter @Setter
public class Statistics {

    private int tablesNumber;
    private int columnsPerTableAvg;
    private int rowsPerTableAvg;
    private int nullValuePerTableAvg;
    private HashMap<Integer, Integer> rowsDistribution;
    private HashMap<Integer, Integer> columnsDistribution;
    private HashMap<Integer, Integer> distinctValuesDistribution;
    private int distinctValues;

    public Statistics() throws IOException {
        WrapperDTO statsDTO = stats();
        this.tablesNumber = statsDTO.getTablesWrapper();
        this.columnsPerTableAvg = statsDTO.getColumnsWrapper() / statsDTO.getTablesWrapper();
        this.rowsPerTableAvg = statsDTO.getRowsWrapper() / statsDTO.getTablesWrapper();
        this.rowsDistribution = statsDTO.getRowsDistributionWrapper();
        this.columnsDistribution = statsDTO.getColumnsDistributionWrapper();
        this.nullValuePerTableAvg = statsDTO.getNullWrapper() / statsDTO.getTablesWrapper();
        this.distinctValues = statsDTO.getDistinctValuesWrapper();
        this.distinctValuesDistribution = statsDTO.getDistinctValuesDistributionMap();
    }

    private WrapperDTO stats(){
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory();

        var tablesWrapper = new Object(){ int tablesCount = 0; };
        var distinctValuesWrapper = new Object(){ int distinctValues = 0; };
        var nullValuesWrapper = new Object(){ int nullCount = 0; };
        var columnsWrapper = new Object(){ int columnsCount = 0; };
        var rowsWrapper = new Object(){ int rowsCount = 0; };
        var columnsDistributionWrapper = new Object(){ HashMap<Integer, Integer> columnsDistributionCountMap = new HashMap<>(); };
        var rowsDistributionWrapper = new Object(){ HashMap<Integer, Integer> rowsDistributionCountMap = new HashMap<>(); };
        var distinctValuesDistributionWrapper = new Object(){ HashMap<Integer, Integer> distinctValuesDistributionMap = new HashMap<>(); };


        try(BufferedReader br = new BufferedReader(new FileReader("tables.json"))) {
            Iterator<Table> value = mapper.readValues(jsonFactory.createParser(br), Table.class);
            value.forEachRemaining((u) -> {

               tablesWrapper.tablesCount++;
               columnsWrapper.columnsCount = columnsWrapper.columnsCount + u.getMaxDimensions().getColumn();
               rowsWrapper.rowsCount = rowsWrapper.rowsCount + u.getMaxDimensions().getRow();

                Integer temp = columnsDistributionWrapper.columnsDistributionCountMap.get(u.getMaxDimensions().getColumn());
                if(temp == null){
                    temp = 1;
                    columnsDistributionWrapper.columnsDistributionCountMap.put(u.getMaxDimensions().getColumn(), temp);
                }else {
                    temp = temp +1;
                    columnsDistributionWrapper.columnsDistributionCountMap.put(u.getMaxDimensions().getColumn(), temp);
                }

                Integer newTemp = rowsDistributionWrapper.rowsDistributionCountMap.get(u.getMaxDimensions().getRow());
                if(newTemp == null){
                    newTemp = 1;
                    rowsDistributionWrapper.rowsDistributionCountMap.put(u.getMaxDimensions().getRow(), newTemp);
                }else {
                    newTemp = newTemp + 1;
                    rowsDistributionWrapper.rowsDistributionCountMap.put(u.getMaxDimensions().getRow(), newTemp);
                }

               for(Cell c: u.getCells()){
                    if(c.getType().equals("EMPTY")){
                        nullValuesWrapper.nullCount = nullValuesWrapper.nullCount + 1;
                    }
               }

                List<HashMap<Integer, Set<String>>> columnMapList = new ArrayList<>();

                for(Cell c: u.getCells()){
                    if(c.getCoordinates().getRow() == 0){
                        int columnIndex = c.getCoordinates().getColumn();
                        HashMap<Integer, Set<String>> map = new HashMap<Integer, Set<String>>();
                        map.put(columnIndex, new HashSet<String>());
                        columnMapList.add(columnIndex, map);
                    }
                    else{
                        int columnIndex = c.getCoordinates().getColumn();
                        try { HashMap<Integer, Set<String>> map = columnMapList.get(columnIndex);
                            map.get(columnIndex).add(c.getCleanedText());
                        }
                        catch (IndexOutOfBoundsException a) {  }
                    }
                }

                for(HashMap<Integer, Set<String>> m: columnMapList){
                    for (Map.Entry<Integer, Set<String>> entry : m.entrySet()) {
                        Integer t = distinctValuesDistributionWrapper.distinctValuesDistributionMap.get(entry.getValue().size());
                        if(t == null){
                            t = 1;
                            distinctValuesDistributionWrapper.distinctValuesDistributionMap.put(entry.getValue().size(), t);
                        }else {
                            t = t +1;
                            distinctValuesDistributionWrapper.distinctValuesDistributionMap.put(entry.getValue().size(), t);
                        }
                        distinctValuesWrapper.distinctValues = distinctValuesWrapper.distinctValues + entry.getValue().size();
                    }
                }


            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        WrapperDTO statsDTO = new WrapperDTO();
        statsDTO.setTablesWrapper(tablesWrapper.tablesCount);
        statsDTO.setColumnsWrapper(columnsWrapper.columnsCount);
        statsDTO.setRowsWrapper(rowsWrapper.rowsCount);
        statsDTO.setRowsDistributionWrapper(rowsDistributionWrapper.rowsDistributionCountMap);
        statsDTO.setColumnsDistributionWrapper(columnsDistributionWrapper.columnsDistributionCountMap);
        statsDTO.setNullWrapper(nullValuesWrapper.nullCount);
        statsDTO.setDistinctValuesWrapper(distinctValuesWrapper.distinctValues);
        statsDTO.setDistinctValuesDistributionMap(distinctValuesDistributionWrapper.distinctValuesDistributionMap);

        return statsDTO;
    }


}


