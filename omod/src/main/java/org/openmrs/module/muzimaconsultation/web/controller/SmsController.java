package org.openmrs.module.muzimaconsultation.web.controller;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.muzimaconsultation.utils.SmsObject;
import org.openmrs.module.muzimaconsultation.utils.SmsUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@RequestMapping(value = "module/muzimaconsultation/")
public class SmsController {
    private Log log = LogFactory.getLog(SmsController.class);

    @RequestMapping(value = "sendsms.form", method = RequestMethod.GET)
    public void sendSms(){}

    @RequestMapping(value="sendsms.form", method = RequestMethod.POST)
    @ResponseBody
    public boolean sendSms(@RequestParam String message, @RequestParam("phone_numbers") String phoneNumbers)
            throws IOException {
        if(StringUtils.isBlank(message)) {
            log.debug("Can not send an empty message");
            return false;
        }

        if(StringUtils.isBlank(phoneNumbers)) {
            log.debug("No phone number specified");
            return false;
        }

        //Construct Sms object
        SmsObject smsObject = null;
        //Check if it is multiple numbers
        if(StringUtils.contains(phoneNumbers,",")) {
            //Split the numbers
            smsObject = new SmsObject(message);

            for(String phone: StringUtils.split(phoneNumbers, ",")) {
                smsObject.addPhoneNumber(phone.trim());
            }
        } else {
            smsObject = new SmsObject(message);
            smsObject.addPhoneNumber(phoneNumbers);
        }

        if(smsObject != null) {
            log.info("Sending sms " + message + " to " + phoneNumbers);
            User authenticatedUser = Context.getAuthenticatedUser();
            String username = authenticatedUser.getUsername();
            if(username == null) {
                username = authenticatedUser.getSystemId();
            }
            return SmsUtil.sendSms(smsObject, username, authenticatedUser.getSystemId());
        }

        log.debug("Sms " + message + " not sent");
        return  false;
    }
}
