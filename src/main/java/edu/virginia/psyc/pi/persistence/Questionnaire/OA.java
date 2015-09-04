package edu.virginia.psyc.pi.persistence.Questionnaire;

import edu.virginia.psyc.pi.domain.Session;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 5/26/14
 * Time: 1:55 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="OA")
@Data
public class OA implements QuestionnaireData {

    public static int NO_ANSWER = 555;

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private ParticipantDAO participantDAO;
    private Date date;
    private String session;

    private int anxious_freq;
    private int anxious_sev;
    private int avoid;
    private int interfere;
    private int interfere_social;

    public OA(int anxious_freq, int anxious_sev, int avoid, int interfere, int interfere_social) {
        this.anxious_freq=anxious_freq;
        this.anxious_sev = anxious_sev;
        this.avoid = avoid;
        this.interfere = interfere;
        this.interfere_social = interfere_social;
    }

    public OA(){}

    public double score() {

        int sum = 0;
        double total = 0.0;

        if(anxious_freq != NO_ANSWER) { sum += anxious_freq; total++;}
        if(anxious_sev != NO_ANSWER) { sum += anxious_sev; total++;}
        if(avoid != NO_ANSWER) { sum += avoid; total++;}
        if(interfere != NO_ANSWER) { sum += interfere; total++;}
        if(interfere_social != NO_ANSWER) { sum += interfere_social; total++;}

        if (total == 0) return 0;
        return(sum / total);
    }


    public boolean atRisk(OA original) {
        return (score() / original.score()) > 1.3;
    }


}
