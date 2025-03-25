package edu.ntnu.fullstack.amazoom.service

import edu.ntnu.fullstack.amazoom.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService : IUserService {

    @Autowired
    private lateinit var userService: UserRepository

    @Transactional
    override fun getAll(): List<User> = userService.findAll();

    @Transactional
    override fun getById(id: Long): User = userService.findOneById(id);

}

interface IUserService {
    fun getAll(): List<User>
    fun getById(id: Long): User
}

// TODO: Move this out of the service package
@Repository
interface UserRepository : CrudRepository<User, Long> {
    override fun findAll(): List<User>;
    fun findOneById(id: Long): User;
}
