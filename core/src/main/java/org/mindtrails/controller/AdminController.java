package org.mindtrails.controller;

import org.mindtrails.domain.Participant;
import org.mindtrails.domain.forms.ParticipantListForm;
import org.mindtrails.domain.tango.Account;
import org.mindtrails.domain.tango.Order;
import org.mindtrails.domain.tango.Reward;
import org.mindtrails.service.EmailService;
import org.mindtrails.service.ExportService;
import org.mindtrails.service.ParticipantService;
import org.mindtrails.service.TangoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/26/14
 * Time: 10:04 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger LOG = LoggerFactory.getLogger(AdminController.class);

    private static final int PER_PAGE=20; // Number of users to display per page.

    @Autowired
    private EmailService emailService;

    @Autowired
    private TangoService tangoService;

    @Autowired
    private ExportService exportService;

    @Value("${export.disableDownloads}")
    private String downloadsDisabled;

    @Autowired
    private ParticipantService participantService;


    @RequestMapping(method = RequestMethod.GET)
    public String listParticipants(ModelMap model,Principal principal,
                            final @RequestParam(value = "search", required = false, defaultValue = "") String search,
                            final @RequestParam(value = "page", required = false, defaultValue = "0") String pageParam) {

        Participant p = participantService.get(principal);

        ParticipantListForm form;
        Page<Participant> daoList;
        PageRequest pageRequest;

        int page = Integer.parseInt(pageParam);

        pageRequest = new PageRequest(page, PER_PAGE);

        if(search.isEmpty()) {
            daoList = participantService.findAll(pageRequest);
        } else {
            daoList = participantService.search(search, pageRequest);
        }

        form = new ParticipantListForm();
        for(Participant participant : daoList) {
            form.add(participant.getStudy().getCurrentSession().getName());
        }

        model.addAttribute("visiting", true);
        model.addAttribute("participants", daoList);
        model.addAttribute("search", search);
        model.addAttribute("paging", daoList);
        return "admin/participants";
    }

    @RequestMapping(value="/listEmails", method=RequestMethod.GET)
    public String listEmails(ModelMap model, Principal principal) {
        Participant p = participantService.get(principal);
        model.addAttribute("visiting", true);
        model.addAttribute("participant", p);
        model.addAttribute("emails", emailService.emailTypes());
        return "admin/listEmails";
    }

    @RequestMapping(value="/sendEmail/{type}")
    public String sendEmail(ModelMap model, Principal principal,
                            @PathVariable("type") String type) throws Exception {
        Participant p = participantService.findByEmail(principal.getName());

        if(type.equals(EmailService.TYPE.GIFTCARD.toString())) {
            Reward reward = tangoService.createGiftCard(p, "test", 1);  // This would actually award a gift card, if you need to do some testing.
            this.emailService.sendGiftCard(p, reward, 100);
        } else if(type.equals(EmailService.TYPE.ALERT_ADMIN)) {
            this.emailService.sendAtRiskAlertToAdmin(p, "Test Message about a dropping score.");
        } else {
            this.emailService.sendEmail(p, type);
        }
        return "redirect:/admin/listEmails";
    }

    @RequestMapping(value="/listSessions", method=RequestMethod.GET)
    public String listSessions(ModelMap model, Principal principal) {
        Participant p = participantService.get(principal);
        model.addAttribute("participant", p);
        model.addAttribute("sessions", p.getStudy().getSessions());
        model.addAttribute("visiting", true);
        return "admin/listSessions";
    }

    // Trying to write a methods to get Tango Account information. By Diheng
    @RequestMapping(value="/tango",method = RequestMethod.GET)
    public String tangoInfo(ModelMap model, Principal principal){
        Account a = tangoService.getAccountInfo();
        model.addAttribute("tango",a);
        model.addAttribute("loggedIn", true);
        return "admin/tango";
    }

    @RequestMapping(value="/participant/giftCard")
    public String giftCard(ModelMap model, Principal principal) throws Exception {
        Participant p = participantService.findByEmail(principal.getName());
        Reward r = tangoService.createGiftCard(p, "AdminAwarded",100);
        this.emailService.sendGiftCard(p, r, 100);
        model.addAttribute("participant", p);
        model.addAttribute("visiting", true);
        return "/admin/participant_form";
    }

    @RequestMapping(value="/rewardInfo/{orderId}", method = RequestMethod.GET)
    public String showRewardInfo(ModelMap model, Principal principal, @PathVariable ("orderId") String orderId) {
        Order order = tangoService.getOrderInfo(orderId);
        model.addAttribute("order",order);
        model.addAttribute("visiting", true);
        return "admin/rewardInfo";
    }

}
