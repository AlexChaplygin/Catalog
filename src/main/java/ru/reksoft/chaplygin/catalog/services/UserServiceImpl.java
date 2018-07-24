package ru.reksoft.chaplygin.catalog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.reksoft.chaplygin.catalog.dto.SectorDTO;
import ru.reksoft.chaplygin.catalog.dto.TokenObjectDTO;
import ru.reksoft.chaplygin.catalog.dto.UserDTO;
import ru.reksoft.chaplygin.catalog.dto.UserForSaveDTO;
import ru.reksoft.chaplygin.catalog.entity.Sector;
import ru.reksoft.chaplygin.catalog.entity.User;
import ru.reksoft.chaplygin.catalog.repositories.UsersRepository;
import ru.reksoft.chaplygin.catalog.security.TokenService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    private UsersRepository usersRepository;
    private SectorService sectorService;
    private TokenService tokenService;

    @Autowired
    UserServiceImpl(UsersRepository usersRepository, SectorService sectorService, TokenService tokenService) {
        this.usersRepository = usersRepository;
        this.sectorService = sectorService;
        this.tokenService = tokenService;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<User> findAllUsers() {
        List<User> list = new ArrayList<>();
        usersRepository.findAll().forEach(list::add);

        return list;
    }

    @Override
    public UserDTO findById(Integer id) {

        User user = usersRepository.findById(id);
        Set<SectorDTO> allSectors = sectorService.findAllSectors();

        // find selectable sectors
        checkSelectableFields(allSectors, new ArrayList<>(user.getSectors()));

        // create security token from user name
        TokenObjectDTO tokenObject = tokenService.createToken(user.getName());

        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .tokenObject(tokenObject)
                .sectors(allSectors)
                .agree(user.getAgree())
                .build();
    }

    @Override
    public User saveUser(UserForSaveDTO user) {
        User userForSave = user.getUser();
        Set<Sector> list = new HashSet<>();

        for (String s : user.getSectorIds().split(",")) {
            list.add(sectorService.findById(Integer.valueOf(s)));
        }

        userForSave.setSectors(list);
        return usersRepository.save(userForSave);
    }

    /**
     * Compare all sectors and selectable sectors by user
     *
     * @param allSectors
     * @param userSectors
     */
    private void checkSelectableFields(Set<SectorDTO> allSectors, List<Sector> userSectors) {

        for (SectorDTO sector : allSectors) {
            if (checkSectorsHaveValue(userSectors, sector.getId())) {
                sector.setIsSelected(true);
            }

            if (sector.getItems() != null) {
                checkSelectableFields(sector.getItems(), userSectors);
            }
        }
    }

    /**
     * Check is current sector selected
     *
     * @param userSectors
     * @param id
     * @return Boolean
     */
    private Boolean checkSectorsHaveValue(List<Sector> userSectors, Integer id) {
        for (Sector sector : userSectors) {
            if (sector.getId() == id) {
                return true;
            }
        }

        return false;
    }
}
