package org.mindtrails.MockClasses;

import org.mindtrails.domain.Participant;
import org.mindtrails.persistence.ParticipantRepository;
import org.mindtrails.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.security.Principal;

/**
 * Largely a wrapper around the Participant Repository.  Allows us to
 * save, create, and find customized participant objects.
 */
@Service
public class TestParticipantService implements ParticipantService {

    @Autowired
    ParticipantRepository repository;


    @Override
    public Participant create() {
        Participant p = new Participant();
        p.setStudy(new TestStudy());
        return new Participant();
    }

    @Override
    public Participant get(Principal p) {
        return repository.findByEmail(p.getName());
    }

    @Override
    public Participant findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public Participant findOne(long id) {
        return repository.findOne(id);
    }

    @Override
    public Page findAll(PageRequest pageRequest) {
        return repository.findAll(pageRequest);
    }

    @Override
    public Page search(String search, PageRequest pageRequest) {
        return repository.search(search, pageRequest);
    }

    @Override
    public void saveNew(Participant p, HttpSession session) {
        save(p);
    }

    @Override
    public void save(Participant p) {
            repository.save(p);
    }

    @Override
    public void flush() {
        repository.flush();
    }

    @Override
    public long count() {
        return repository.count();
    }
}
