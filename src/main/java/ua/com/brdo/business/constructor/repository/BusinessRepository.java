package ua.com.brdo.business.constructor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import ua.com.brdo.business.constructor.model.Business;
import ua.com.brdo.business.constructor.model.User;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {

    Optional<Business> findByTitle(final String title);

    List<Business> findByUser(final User user);
}
