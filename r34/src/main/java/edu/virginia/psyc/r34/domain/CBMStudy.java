package edu.virginia.psyc.r34.domain;

import org.mindtrails.domain.BaseStudy;
import org.mindtrails.domain.Session;
import org.mindtrails.domain.Study;
import org.mindtrails.domain.Task;
import org.mindtrails.domain.tracking.TaskLog;
import lombok.Data;
import org.slf4j.LoggerFactory;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 6/12/14
 * Time: 8:21 AM
 * The Participants progress through a series of sessions, made up of individual tasks, described here in code.
 */
@Entity
@Table(name = "study")
@Data
@DiscriminatorValue("CBM")
public class CBMStudy extends BaseStudy implements Study {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(CBMStudy.class);

    public enum NAME {
        ELIGIBLE, PRE, SESSION1, SESSION2, SESSION3, SESSION4, SESSION5, SESSION6, SESSION7, SESSION8, POST
    }

    public CBMStudy() {}

    public CBMStudy(String currentSession, int taskIndex, Date lastSessionDate, List<TaskLog> taskLogs, boolean receiveGiftCards) {
        super(currentSession, taskIndex, lastSessionDate, taskLogs, receiveGiftCards);
    }

    @Override
    public List<Session> getStatelessSessions() {
        List<Session> sessions = new ArrayList<Session>();

        sessions.add(new Session(NAME.ELIGIBLE.toString(), "Eligible", 0, 0, getTasks(NAME.ELIGIBLE)));
        sessions.add(new Session(NAME.PRE.toString(), "Initial Assessment", 500, 0, getTasks(NAME.PRE)));
        sessions.add(new Session(NAME.SESSION1.toString(), "Day 1 Training", 0, 0, getTasks(NAME.SESSION1)));
        sessions.add(new Session(NAME.SESSION2.toString(), "Day 2 Training", 0, 2, getTasks(NAME.SESSION2)));
        sessions.add(new Session(NAME.SESSION3.toString(), "Day 3 Training", 500, 2, getTasks(NAME.SESSION3)));
        sessions.add(new Session(NAME.SESSION4.toString(), "Day 4 Training", 0, 2, getTasks(NAME.SESSION4)));
        sessions.add(new Session(NAME.SESSION5.toString(), "Day 5 Training", 0, 2, getTasks(NAME.SESSION5)));
        sessions.add(new Session(NAME.SESSION6.toString(), "Day 6 Training", 500, 2, getTasks(NAME.SESSION6)));
        sessions.add(new Session(NAME.SESSION7.toString(), "Day 7 Training", 0, 2, getTasks(NAME.SESSION7)));
        sessions.add(new Session(NAME.SESSION8.toString(), "Day 8 Training", 500, 2, getTasks(NAME.SESSION8)));
        sessions.add(new Session(NAME.POST.toString(), "2 Months Post Training", 1000, 60, getTasks(NAME.POST)));

        // Little messyness here, add an index value to each session as this is used to set the image to
        // display in some of the templates for the r34 study.
        int i = -1;
        for(Session s: sessions) { s.setIndex(i); i++; }

        return sessions;
    }

    public String getDisplayName(String name) {
        for(Session s : getStatelessSessions()) {
            if(s.getName().equals(name)) return s.getDisplayName();
        }
        return "";
    }

    /**
     * In the future we should refactor this, so that we load the sessions from a
     * configuration file, or from the database.  But neither of these directions
     * seems clean to me, as they will naturally depend on certain code and paths
     * existing.  So here is how sessions and tasks are defined, until requirements,
     * or common sense dictate a more complex solution.
     * <p>
     * Note:  Arguments to Task are:  unique name, display name, type, and
     * approximate time it takes to complete in minutes.  If this value is 0, it
     * will not be displayed in the overview page.
     *
     * @param name The Name of a given session.
     * @return
     */
    protected List<Task> getTasks(NAME name) {

        List<Task> tasks = new ArrayList<Task>();
        switch (name) {
            case PRE:
                tasks.add(new Task("Credibility", "Consent to participate", Task.TYPE.questions, 2));
                tasks.add(new Task("Demographic", "Demographics", Task.TYPE.questions, 2));
                tasks.add(new Task("MentalHealthHxTx", "Mental health history", Task.TYPE.questions, 2));
                tasks.add(new Task("QOL", "Satisfaction", Task.TYPE.questions, 0));
                tasks.add(new Task("RecognitionRatings", "Completing short stories", Task.TYPE.playerScript, 0));
                tasks.add(new Task("RR", "Completing short stories - Continued", Task.TYPE.questions, 0));
                tasks.add(new Task("BBSIQ", "Why things happen", Task.TYPE.questions, 0));
                tasks.add(new Task("DASS21_DS", "Mood assessment", Task.TYPE.questions, 0));
                tasks.add(new Task("DD", "Assessment", Task.TYPE.questions, 15));
                tasks.add(new Task("OA", "Anxiety review", Task.TYPE.questions, 1));
                tasks.add(new Task("AnxietyTriggers", "Personal anxiety triggers", Task.TYPE.questions, 0));
                break;
            case SESSION1:
                tasks.add(new Task("SUDS", "How anxious you feel", Task.TYPE.questions, 0));
                tasks.add(new Task("ImageryPrime", "Use your imagination", Task.TYPE.questions, 0));
                tasks.add(new Task("ImpactAnxiousImagery", "Impact questions", Task.TYPE.questions, 0));
                tasks.add(new Task("FirstSessionSentences", "Training stories", Task.TYPE.playerScript, 20));
                tasks.add(new Task("SUDS", "How anxious you feel", Task.TYPE.questions, 0));
                tasks.add(new Task("CC", "Follow up", Task.TYPE.questions, 0));
                tasks.add(new Task("OA", "Anxiety review", Task.TYPE.questions, 1));
                break;
            case SESSION2:
                tasks.add(new Task("ImageryPrime", "Use your imagination", Task.TYPE.questions, 0));
                tasks.add(new Task("ImpactAnxiousImagery", "Impact questions", Task.TYPE.questions, 0));
                tasks.add(new Task("SecondSessionSentences", "Training stories", Task.TYPE.playerScript, 20));
                tasks.add(new Task("OA", "Anxiety review", Task.TYPE.questions, 1));

                break;
            case SESSION3:
                tasks.add(new Task("SUDS", "How anxious you feel", Task.TYPE.questions, 0));
                tasks.add(new Task("ImageryPrime", "Use your imagination", Task.TYPE.questions, 0));
                tasks.add(new Task("ImpactAnxiousImagery", "Impact questions", Task.TYPE.questions, 0));
                tasks.add(new Task("ThirdSessionSentences", "Training stories", Task.TYPE.playerScript, 20));
                tasks.add(new Task("SUDS", "How anxious you feel", Task.TYPE.questions, 0));
                tasks.add(new Task("CC", "Follow up", Task.TYPE.questions, 0));
                tasks.add(new Task("RecognitionRatings", "Completing short stories", Task.TYPE.playerScript, 0));
                tasks.add(new Task("RR", "Completing short stories - Continued", Task.TYPE.questions, 0));
                tasks.add(new Task("BBSIQ", "Why things happen", Task.TYPE.questions, 0));
                tasks.add(new Task("QOL", "How satisfied you feel", Task.TYPE.questions, 0));
                tasks.add(new Task("DASS21_DS", "Mood assessment", Task.TYPE.questions, 0));
                tasks.add(new Task("DD_FU", "Assessment", Task.TYPE.questions, 15));
                tasks.add(new Task("OA", "Anxiety review", Task.TYPE.questions, 1));
//                tasks.add(new Task("SAPo", "State Anxiety", Task.TYPE.questions, 3));
                break;
            case SESSION4:
                tasks.add(new Task("ImageryPrime", "Use your imagination", Task.TYPE.questions, 0));
                tasks.add(new Task("ImpactAnxiousImagery", "Impact questions", Task.TYPE.questions, 0));
                tasks.add(new Task("FourthSessionSentences", "Training stories", Task.TYPE.playerScript, 20));
                tasks.add(new Task("OA", "Anxiety review", Task.TYPE.questions, 1));
                break;
            case SESSION5:
                tasks.add(new Task("ImageryPrime", "Use your imagination", Task.TYPE.questions, 0));
                tasks.add(new Task("ImpactAnxiousImagery", "Impact questions", Task.TYPE.questions, 0));
                tasks.add(new Task("FifthSessionSentences", "Training stories", Task.TYPE.playerScript, 20));
                tasks.add(new Task("OA", "Anxiety review", Task.TYPE.questions, 1));

                break;
            case SESSION6:
                tasks.add(new Task("SUDS", "How anxious you feel", Task.TYPE.questions, 0));
                tasks.add(new Task("ImageryPrime", "Use your imagination", Task.TYPE.questions, 0));
                tasks.add(new Task("ImpactAnxiousImagery", "Impact questions", Task.TYPE.questions, 0));
                tasks.add(new Task("SixthSessionSentences", "Training stories", Task.TYPE.playerScript, 20));
                tasks.add(new Task("SUDS", "How anxious you feel", Task.TYPE.questions, 0));
                tasks.add(new Task("CC", "Follow up", Task.TYPE.questions, 0));
                tasks.add(new Task("RecognitionRatings", "Completing short stories", Task.TYPE.playerScript, 0));
                tasks.add(new Task("RR", "Completing short stories - Continued", Task.TYPE.questions, 0));
                tasks.add(new Task("BBSIQ", "Why things happen", Task.TYPE.questions, 0));
                tasks.add(new Task("QOL", "How satisfied you feel", Task.TYPE.questions, 0));
                tasks.add(new Task("DASS21_DS", "Mood assessment", Task.TYPE.questions, 0));
                tasks.add(new Task("DD_FU", "Assessment", Task.TYPE.questions, 15));
                tasks.add(new Task("OA", "Anxiety review", Task.TYPE.questions, 1));
//                tasks.add(new Task("SAPo", "State Anxiety", Task.TYPE.questions, 3));
                break;
            case SESSION7:
                tasks.add(new Task("ImageryPrime", "Use your imagination", Task.TYPE.questions, 0));
                tasks.add(new Task("ImpactAnxiousImagery", "Impact questions", Task.TYPE.questions, 0));
                tasks.add(new Task("SeventhSessionSentences", "Training stories", Task.TYPE.playerScript, 20));
                tasks.add(new Task("OA", "Anxiety review", Task.TYPE.questions, 1));

                break;
            case SESSION8:
                tasks.add(new Task("SUDS", "How anxious you feel", Task.TYPE.questions, 0));
                tasks.add(new Task("ImageryPrime", "Use your imagination", Task.TYPE.questions, 0));
                tasks.add(new Task("ImpactAnxiousImagery", "Impact questions", Task.TYPE.questions, 0));
                tasks.add(new Task("EighthSessionSentences", "Training stories", Task.TYPE.playerScript, 20));
                tasks.add(new Task("SUDS", "How anxious you feel", Task.TYPE.questions, 0));
                tasks.add(new Task("CC", "Follow up", Task.TYPE.questions, 0));
                tasks.add(new Task("RecognitionRatings", "Completing short stories", Task.TYPE.playerScript, 0));
                tasks.add(new Task("RR", "Completing short stories - Continued", Task.TYPE.questions, 0));
                tasks.add(new Task("BBSIQ", "Why things happen", Task.TYPE.questions, 0));
//                tasks.add(new Task("SAPo", "State Anxiety", Task.TYPE.questions, 3));
                tasks.add(new Task("QOL", "How satisfied you feel", Task.TYPE.questions, 0));
                tasks.add(new Task("DASS21_DS", "Mood assessment", Task.TYPE.questions, 0));
                tasks.add(new Task("DD_FU", "Assessment", Task.TYPE.questions, 15));
                tasks.add(new Task("OA", "Anxiety review", Task.TYPE.questions, 1));
                tasks.add(new Task("DASS21_AS", "Recent anxiety symptoms", Task.TYPE.questions, 0));
                tasks.add(new Task("CIHS", "Change in help seeking", Task.TYPE.questions, 1));

                break;
            case POST:
                tasks.add(new Task("RecognitionRatings", "Completing short stories", Task.TYPE.playerScript, 0));
                tasks.add(new Task("RR", "Completing short stories - Continued", Task.TYPE.questions, 0));
                tasks.add(new Task("BBSIQ", "Why things happen", Task.TYPE.questions, 0));

//                tasks.add(new Task("SAPo", "State Anxiety", Task.TYPE.questions, 3));
                tasks.add(new Task("QOL", "How satisfied you feel", Task.TYPE.questions, 0));
                tasks.add(new Task("DASS21_DS", "Mood assessment", Task.TYPE.questions, 0));
                tasks.add(new Task("DD_FU", "Assessment", Task.TYPE.questions, 15));
                tasks.add(new Task("OA", "Anxiety review", Task.TYPE.questions, 1));
                tasks.add(new Task("DASS21_AS", "Recent anxiety symptoms", Task.TYPE.questions, 0));
                tasks.add(new Task("CIHS", "Change in help seeking", Task.TYPE.questions, 1));
                tasks.add(new Task("MultiUserExperience", "Evaluating the program", Task.TYPE.questions, 2));

        }
        return tasks;
    }


}