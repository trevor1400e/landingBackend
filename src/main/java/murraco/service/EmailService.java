package murraco.service;

import murraco.model.Email;
import murraco.model.Theme;
import murraco.repository.EmailRepository;
import murraco.repository.ThemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

  @Autowired
  private EmailRepository emailRepository;

  public List<Email> findByUniquename(String uniquename){
    return emailRepository.findByUniquename(uniquename);
  }

}
