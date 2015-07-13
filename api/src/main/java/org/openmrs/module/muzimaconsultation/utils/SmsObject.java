package org.openmrs.module.muzimaconsultation.utils;

import java.util.ArrayList;
import java.util.List;

public class SmsObject {
    private String message;
    private List<String> phoneNumbers;

    public SmsObject() {
    }

    public SmsObject(String message) {
        this.message = message;
    }

    public SmsObject(String message, List<String> phoneNumbers) {
        this.message = message;
        this.phoneNumbers = phoneNumbers;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public void addPhoneNumber(String phoneNumber) {
        if(phoneNumber != null) {
            if(phoneNumbers==null){
                phoneNumbers = new ArrayList<String>();
                phoneNumbers.add(phoneNumber);
            }
        }
    }
}
