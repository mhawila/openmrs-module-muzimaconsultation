package org.openmrs.module.muzimaconsultation.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.Role;
import org.openmrs.api.context.Context;
import org.openmrs.web.WebConstants;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Date;
import java.util.UUID;

public class SmsUtil {
    public static String GP_APP_NAME = "muzimaconsultation.smsApp";
    public static String GP_APP_PASSWORD = "muzimaconsultation.smsAppPassword";
    public static String GP_GATEWAY_URL = "muzimaconsultation.smsGatewayUrl";
    public static int PERSON_PHONE_ATTR_TYPE_ID = 10;
    public static String smsGatewayUrl;
    public static String sendUrl;

    private static Log log = LogFactory.getLog(SmsUtil.class);

    static {
        String url = Context.getAdministrationService().getGlobalProperty(GP_GATEWAY_URL);
        if (StringUtils.hasLength(url)) {
            if (!url.endsWith("/")) url += "/";
            smsGatewayUrl = url;
            sendUrl = smsGatewayUrl + "message";
        } else {
            log.error("Global property " + GP_GATEWAY_URL + " for sms feature to work");
        }
    }

    public static boolean sendSms(final SmsObject smsObject, final String sender, final String senderIdentifier)
            throws IOException{
        if(smsObject==null) {
            log.error("Sms object cannot be null");
            return false;
        }

        String jsonToSend = createJsonToSend(smsObject,sender,senderIdentifier);
        if(!StringUtils.hasLength(jsonToSend) || sendUrl==null) {
            log.debug("Blank message cannot be sent");
            return false;
        }

        CloseableHttpClient client = HttpClients.createDefault();
        UsernamePasswordCredentials gateWayCredentials = getCredentials();

        CredentialsProvider credProvider = new BasicCredentialsProvider();

        String host;
        if(smsGatewayUrl.startsWith("http")){
            int start = smsGatewayUrl.indexOf("//") + 2;
            int end = smsGatewayUrl.indexOf(":", start) ;
            host = smsGatewayUrl.substring(start, end);
        } else {
            host = smsGatewayUrl;
        }

        credProvider.setCredentials(new AuthScope(host,AuthScope.ANY_PORT,AuthScope.ANY_REALM,"basic"),
                gateWayCredentials);

        HttpPost postRequest = new HttpPost(sendUrl);
        postRequest.addHeader("Content-Type", "application/json");
        postRequest.setEntity(new StringEntity(jsonToSend));       //Add the json body.

        HttpClientContext context = HttpClientContext.create();
        context.setCredentialsProvider(credProvider);

        CloseableHttpResponse response = client.execute(postRequest, context);

        if(response.getStatusLine().getStatusCode()!= HttpURLConnection.HTTP_OK) {
            int errorCode = response.getStatusLine().getStatusCode();
            String phrase = response.getStatusLine().getReasonPhrase();
            throw new RuntimeException("Failed : HTTP Error Code " + errorCode + " message: " + phrase);
        }
        log.debug("Message sent with response " + response);
        return true;
    }

    public static SmsObject createSmsObject(final String sourceUuid, final Encounter encounter, final Person recipient, final Role role) {
        Person sender = encounter.getProvider();

        Patient patient = encounter.getPatient();
        String patientName = patient.getPersonName().getFullName();
        String senderName = sender.getPersonName().getFullName();
        String recipientName = "User";
        String phone = null;
        if (recipient != null) {
            recipientName = recipient.getPersonName().getFullName();
            phone = recipient.getAttribute(PERSON_PHONE_ATTR_TYPE_ID).getValue();
        } else if (role != null) {
            recipientName = role.getRole();
        }

        StringBuilder sb = new StringBuilder("Dear ").
                append(recipientName).
                append(", ").
                append("Please review the newly created consultation request by ").
                append(senderName).
                append(" for the following patient: ").
                append(patientName).
                append(" For more information visit ").
                append(WebConstants.WEBAPP_NAME).
                append("/admin/encounters/encounter.form?encounterId=").
                append(encounter.getEncounterId());

        SmsObject smsObject = new SmsObject(sb.toString());
        if(phone != null) {
            smsObject.addPhoneNumber(phone);
            return smsObject;
        } else {
            log.error("Recipient " + recipientName + " does not have phone number associated with their accounts");
        }
        return null;
    }

    private static String createOneTimeToken() {
        //Create something based on timestamp
        String token = new Date().toString();
        token = token.replace(" ", "");

        token = UUID.randomUUID() + token;
        return token;
    }

    private static String createJsonToSend(SmsObject smsObject, String sender, String senderId) {
        if (smsObject == null) return null;

        StringBuilder sb = new StringBuilder();
        sb.append("{")
                .append("\"token\" : \"").append(createOneTimeToken()).append("\",")
                .append("\"sender\" : { ")
                .append("\"name\" : \"").append(sender).append("\",")
                .append("\"id\" : \"").append(senderId).append("\"")
                .append("},")
                .append("\"message\" : \"").append(smsObject.getMessage()).append("\",");

        //Add recipients
        sb.append("\"recipients\" : [ ");
        int i;
        for (i = 0; i < smsObject.getPhoneNumbers().size() - 1; i++) {
            sb.append("\"").append(smsObject.getPhoneNumbers().get(i)).append("\",");
        }

        //Add the last phone number
        sb.append("\"").append(smsObject.getPhoneNumbers().get(i)).append("\"]")
                .append("}");
        return sb.toString();
    }

    private static UsernamePasswordCredentials getCredentials() {
        String appName = Context.getAdministrationService().getGlobalProperty(GP_APP_NAME);
        String appPassword = Context.getAdministrationService().getGlobalProperty(GP_APP_PASSWORD);

        if(!StringUtils.hasLength(appName) || !StringUtils.hasLength(appPassword)){
            throw new SmsAPIException("Both global properties "+GP_APP_NAME+" and "+GP_APP_PASSWORD+
            " must be specified for sms feature to work");
        }

        return new UsernamePasswordCredentials(appName,appPassword);
    }
}
