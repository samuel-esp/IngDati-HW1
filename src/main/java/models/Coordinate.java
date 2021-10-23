package models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

@JsonDeserialize
@JsonSerialize
@Getter
@Setter
public class Coordinate {

    private int row;
    private int column;

}
