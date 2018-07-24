package ru.reksoft.chaplygin.catalog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.reksoft.chaplygin.catalog.dto.SectorDTO;
import ru.reksoft.chaplygin.catalog.dto.TokenObjectDTO;
import ru.reksoft.chaplygin.catalog.dto.UserDTO;
import ru.reksoft.chaplygin.catalog.dto.UserForSaveDTO;
import ru.reksoft.chaplygin.catalog.entity.User;
import ru.reksoft.chaplygin.catalog.security.TokenService;
import ru.reksoft.chaplygin.catalog.services.SectorService;
import ru.reksoft.chaplygin.catalog.services.UserService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/catalog")
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private SectorService sectorService;

    @Autowired
    private TokenService tokenService;

    /**
     * Get all users from DB
     *
     * @return List<User>
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<User> getAllUsers() {

        return userService.findAllUsers();
    }

    /**
     * Get all sectors from DB
     *
     * @return Set<SectorDTO>
     */
    @RequestMapping(value = "/sectors", method = RequestMethod.GET)
    public Set<SectorDTO> getAllSectors() {

        return sectorService.findAllSectors();
    }

    /**
     * Save new user with sectors.
     * Return registration token
     *
     * @param user
     * @return TokenObjectDTO
     */
    @PutMapping("/save")
    public TokenObjectDTO saveNewUser(@RequestBody UserForSaveDTO user) {
        userService.saveUser(user);

        return tokenService.createToken(user.getUser().getName());
    }

    /**
     * Update user information with sectors.
     * Return registration token
     *
     * @param user
     * @return TokenObjectDTO
     */
    @PutMapping("/update")
    public void updateUser(@RequestBody UserForSaveDTO user) {
        userService.saveUser(user);
    }

    /**
     * Get user by id
     *
     * @param id
     * @return UserDTO
     */
    @GetMapping("/get/{id}")
    public UserDTO getUser(@PathVariable("id") Integer id) {

        return userService.findById(id);
    }

}
