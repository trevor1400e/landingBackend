package murraco.repository;

import murraco.model.Theme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ThemeRepository extends JpaRepository<Theme, Integer> {

    List<Theme> findByUsername(String username);

    Theme findByUniquename(String uniquename);

}
