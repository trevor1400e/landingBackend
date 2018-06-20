package murraco.service;

import murraco.model.Page;
import murraco.repository.ThemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThemeService {

    @Autowired
    private ThemeRepository themeRepository;

    public Page findByUniquename(String uniquename) {
        return themeRepository.findByUniquename(uniquename);
    }

    public List<Page> findByUsername(String username) {
        return themeRepository.findByUsername(username);
    }

}
