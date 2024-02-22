package biscof.clientcontactapi.repository;

import biscof.clientcontactapi.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {

    List<Email> findByClientId(Long clientId);

}
