package murraco.repository;

import murraco.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailRepository extends JpaRepository<Email, Integer> {

    List<Email> findByUniquename(String uniquename);

}
