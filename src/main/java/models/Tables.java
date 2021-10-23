package models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonSerialize
@JsonDeserialize
public class Tables {

    private Table[] tables;

}
