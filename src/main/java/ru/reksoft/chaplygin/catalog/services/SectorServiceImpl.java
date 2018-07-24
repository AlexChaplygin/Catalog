package ru.reksoft.chaplygin.catalog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.reksoft.chaplygin.catalog.dto.SectorDTO;
import ru.reksoft.chaplygin.catalog.entity.Sector;
import ru.reksoft.chaplygin.catalog.repositories.SectorRepository;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class SectorServiceImpl implements SectorService {

    private SectorRepository sectorRepository;

    @Autowired
    SectorServiceImpl(SectorRepository sectorRepository) {
        this.sectorRepository = sectorRepository;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Set<SectorDTO> findAllSectors() {
        Set<Sector> list = new HashSet<>();

        sectorRepository.findAllByParentIsNull().forEach(list::add);

        // transform entities to DTOs
        return transformSectorToSectorDTOList(list);
    }

    @Override
    public Sector findById(Integer id) {
        return sectorRepository.findOne(id);
    }

    /**
     * Transform set of entities to set of DTOs
     *
     * @param list
     * @return Set<SectorDTO>
     */
    private Set<SectorDTO> transformSectorToSectorDTOList(Set<Sector> list) {

        Set<SectorDTO> sectorDTOS = new HashSet<>();

        // transform entity to DTOs
        for (Sector sector : list) {
            sectorDTOS.add(SectorDTO.builder()
                    .id(sector.getId())
                    .isSelected(false)
                    .items(transformSectorToSectorDTOList(sector.getItems()))
                    .parent(sector.getParent()).title(sector.getTitle()).build());
        }

        return sectorDTOS;
    }
}
