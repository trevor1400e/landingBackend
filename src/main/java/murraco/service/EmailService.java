package murraco.service;

import murraco.model.CollectedEmail;
import murraco.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    @Autowired
    private EmailRepository emailRepository;

    public List<CollectedEmail> findByUniquename(String uniquename) {
        return emailRepository.findByUniquename(uniquename);
    }

}
