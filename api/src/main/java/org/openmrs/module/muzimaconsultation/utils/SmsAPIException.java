package org.openmrs.module.muzimaconsultation.utils;

import org.openmrs.api.APIException;

public class SmsAPIException extends APIException{
    public SmsAPIException(){
        super("Could not send SMS");
    }
    public SmsAPIException(String message) {
        super(message);
    }
}
