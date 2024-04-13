package com.doll.dto;

import com.doll.entity.Component;
import lombok.Data;

@Data
public class ComponentDto extends Component {
    private  String categoryName;
    private  Integer copies;
}
