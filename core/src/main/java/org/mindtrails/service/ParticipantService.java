package org.mindtrails.service;

import org.mindtrails.domain.Participant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.servlet.http.HttpSession;
import java.security.Principal;

/**
 * Provides a means to create and save participants in custom ways.
 */
public interface ParticipantService {

    /**
     * Creates a new object that is an instance of, or extension of
     * Participant.  Be certain it set's the participants Study!
     */
    Participant create();

    /** Returns a participant associated with given spring security
     * model.
     */
    Participant get(Principal p);

    Participant findByEmail(String email);

    Participant findOne(long id);

    Page findAll(PageRequest pageRequest);

    Page search(String search, PageRequest pageRequest);

    /**
     * When saving an object for the first time, there may
     * be data lingering in the session that needs to be
     * collected and associated with the Participant.  This
     * allows that data to be captured and associated.
     * @param p
     * @param session The current HTTP Session.
     */
    void saveNew(Participant p, HttpSession session);

    void save(Participant p);

    void flush();

    /** Returns the total number of participants **/
    long count();

}
