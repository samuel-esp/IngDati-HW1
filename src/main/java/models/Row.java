package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

@JsonDeserialize
@JsonSerialize
@Getter @Setter
public class Row {

    @JsonProperty("LinkBlue")
    private String LinkBlue;
    @JsonProperty("Title")
    private String title;
    @JsonProperty("Href")
    private String Href;


}
