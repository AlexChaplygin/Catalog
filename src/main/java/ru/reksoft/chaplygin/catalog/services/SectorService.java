package ru.reksoft.chaplygin.catalog.services;

import ru.reksoft.chaplygin.catalog.dto.SectorDTO;
import ru.reksoft.chaplygin.catalog.entity.Sector;

import java.util.Set;

public interface SectorService {

    Set<SectorDTO> findAllSectors();

    Sector findById(Integer id);
}
