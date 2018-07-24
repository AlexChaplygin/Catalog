package ru.reksoft.chaplygin.catalog.services;

import ru.reksoft.chaplygin.catalog.dto.UserDTO;
import ru.reksoft.chaplygin.catalog.dto.UserForSaveDTO;
import ru.reksoft.chaplygin.catalog.entity.User;

import java.util.List;

public interface UserService {

    List<User> findAllUsers();

    UserDTO findById(Integer id);

    User saveUser(UserForSaveDTO user);

}
