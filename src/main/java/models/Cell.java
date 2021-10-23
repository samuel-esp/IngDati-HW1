package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

@JsonDeserialize
@JsonSerialize
@Getter @Setter
public class Cell {

    private String className;
    private String innerHTML;
    private String isHeader;
    private String type;
    @JsonProperty("Coordinates")
    private Coordinate Coordinates;
    @JsonProperty("Rows")
    private Row[] rows;
    private String cleanedText;

}
