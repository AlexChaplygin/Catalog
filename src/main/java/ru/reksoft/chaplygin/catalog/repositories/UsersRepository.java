package ru.reksoft.chaplygin.catalog.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.reksoft.chaplygin.catalog.entity.User;

public interface UsersRepository extends CrudRepository<User, Integer> {

    User findById(Integer id);

}
