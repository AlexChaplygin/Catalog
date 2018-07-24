package ru.reksoft.chaplygin.catalog.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Setter
@Getter
@Builder
public class UserDTO {

    private Integer id;
    private String name;
    private TokenObjectDTO tokenObject;
    private Set<SectorDTO> sectors;
    private Boolean agree;
}
