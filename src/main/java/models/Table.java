package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

@JsonDeserialize
@JsonSerialize
@Getter @Setter
public class Table{

    @JsonIgnore
    private String _id;
    private String className;
    private String id;
    private String beginIndex;
    private String endIndex;
    private Cell[] cells;
    private String referenceContext;
    private String type;
    private String classe;
    private MaxDimension maxDimensions;
    @JsonIgnore
    private HeadersCleaned headersCleaned;
    private String keyColumn;


}

