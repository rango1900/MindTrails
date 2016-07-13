package edu.virginia.psyc.r34.persistence;

import edu.virginia.psyc.r34.domain.PiParticipant;
import org.mindtrails.domain.Participant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data provides basic Crud operations (find, save, delete ...)
 * on PiParticipant when it finds this interface.
 */
public interface PiParticipantRepository extends JpaRepository<PiParticipant, Long> {
    PiParticipant findByEmail(String email);

    @Query(" select p from PiParticipant as p" +
            " where lower(p.fullName) like '%' || lower(:search) || '%'" +
            " or lower(p.email) like '%' || lower(:search) || '%'" +
            " order by lower(p.fullName)")
    Page<Participant> search(@Param("search") String search, Pageable pageable);

}
