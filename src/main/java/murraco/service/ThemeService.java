package murraco.service;

import murraco.model.Theme;
import murraco.repository.ThemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThemeService {

  @Autowired
  private ThemeRepository themeRepository;

  public Theme findByUniquename(String uniquename){
    return themeRepository.findByUniquename(uniquename);
  }

  public List<Theme> findByUsername(String username){
    return themeRepository.findByUsername(username);
  }

}
