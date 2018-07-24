package ru.reksoft.chaplygin.catalog.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.reksoft.chaplygin.catalog.entity.Sector;

import java.util.List;

public interface SectorRepository extends CrudRepository<Sector, Integer> {

    /**
     * Get all sectors with empty parent.
     *
     * @return list of sectors
     */
    List<Sector> findAllByParentIsNull();
}
