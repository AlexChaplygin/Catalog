package ru.reksoft.chaplygin.catalog.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.reksoft.chaplygin.catalog.entity.Sector;

import java.util.Set;

@Getter
@Setter
@Builder
public class SectorDTO {

    private Integer id;
    private Sector parent;
    private Set<SectorDTO> items;
    private String title;
    private Boolean isSelected;
}
