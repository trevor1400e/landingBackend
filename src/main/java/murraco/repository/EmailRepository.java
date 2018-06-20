package murraco.repository;

import murraco.model.CollectedEmail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailRepository extends JpaRepository<CollectedEmail, Integer> {

    List<CollectedEmail> findByUniquename(String uniquename);

}
