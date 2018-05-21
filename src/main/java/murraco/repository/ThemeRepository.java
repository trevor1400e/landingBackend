package murraco.repository;

import murraco.model.Theme;
import murraco.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface ThemeRepository extends JpaRepository<Theme, Integer> {

  List<Theme> findByUsername(String username);

  Theme findByUniquename(String uniquename);

}
