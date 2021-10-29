package dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import models.Table;

import java.util.HashMap;
import java.util.List;

@Getter @Setter
public class WrapperDTO {

    private int tablesWrapper;
    private int columnsWrapper;
    private int rowsWrapper;
    private int nullWrapper;
    private int distinctValuesWrapper;
    private HashMap<Integer, Integer> columnsDistributionWrapper;
    private HashMap<Integer, Integer> rowsDistributionWrapper;
    private HashMap<Integer, Integer> distinctValuesDistributionMap;

}
