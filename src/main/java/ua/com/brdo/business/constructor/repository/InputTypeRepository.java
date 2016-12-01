package ua.com.brdo.business.constructor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import ua.com.brdo.business.constructor.model.InputType;

@Repository
public interface InputTypeRepository extends JpaRepository<InputType, Long> {
    Optional<InputType> findByTitle(String title);
}