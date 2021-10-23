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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@JsonDeserialize
@JsonSerialize
@Getter @Setter
public class Statistics {

    private int tablesNumber;
    private int columnsPerTableAvg;
    private int nullValuePerTableAvg;
    private HashMap<Integer, Integer> rowsDistribution;
    private HashMap<Integer, Integer> columnsDistribution;
    private int distinctValuesDistribution;

    public Statistics() throws IOException {
        WrapperDTO statsDTO = stats();
        this.tablesNumber = statsDTO.getTablesWrapper();
        this.columnsPerTableAvg = statsDTO.getColumnsWrapper() / statsDTO.getTablesWrapper();
        this.rowsDistribution = statsDTO.getRowsDistributionWrapper();
        this.columnsDistribution = statsDTO.getColumnsDistributionWrapper();
        this.nullValuePerTableAvg = statsDTO.getNullWrapper() / statsDTO.getTablesWrapper();
    }

    private WrapperDTO stats(){
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory();

        var tablesWrapper = new Object(){ int tablesCount = 0; };
        var nullValuesWrapper = new Object(){ int nullCount = 0; };
        var columnsWrapper = new Object(){ int columnsCount = 0; };
        var rowsWrapper = new Object(){ int rowsCount = 0; };
        var columnsDistributionWrapper = new Object(){ HashMap<Integer, Integer> columnsDistributionCountMap = new HashMap<>(); };
        var rowsDistributionWrapper = new Object(){ HashMap<Integer, Integer> rowsDistributionCountMap = new HashMap<>(); };


        try(BufferedReader br = new BufferedReader(new FileReader("tables.json"))) {
            Iterator<Table> value = mapper.readValues(jsonFactory.createParser(br), Table.class);
            value.forEachRemaining((u) -> {

               tablesWrapper.tablesCount++;
               columnsWrapper.columnsCount = columnsWrapper.columnsCount + u.getMaxDimensions().getColumn();
               rowsWrapper.rowsCount = rowsWrapper.rowsCount + u.getMaxDimensions().getRow();

               /*
                List<Integer> temp = columnsDistributionWrapper.columnsDistributionCountMap.get(u.getMaxDimensions().getColumn());
                if(temp == null){
                    temp = new ArrayList<>();
                    temp.add(tablesWrapper.tablesCount);
                    columnsDistributionWrapper.columnsDistributionCountMap.put(u.getMaxDimensions().getColumn(), temp);
                }else {
                    temp.add(tablesWrapper.tablesCount);
                }*/

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

        return statsDTO;
    }


}


