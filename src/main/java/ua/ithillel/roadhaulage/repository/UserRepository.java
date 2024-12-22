package ua.ithillel.roadhaulage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ua.ithillel.roadhaulage.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email) throws UsernameNotFoundException;;

}
