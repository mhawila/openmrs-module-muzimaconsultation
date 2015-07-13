<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>
<openmrs:htmlInclude file="/moduleResources/muzimaconsultation/styles/custom/custom.css"/>
<openmrs:htmlInclude file="/moduleResources/muzimaconsultation/styles/bootstrap/css/bootstrap.css"/>

<openmrs:htmlInclude file="/moduleResources/muzimaconsultation/js/jquery/jquery.js" />

<openmrs:htmlInclude file="/moduleResources/muzimaconsultation/js/angular/angular.js"/>
<openmrs:htmlInclude file="/moduleResources/muzimaconsultation/js/angular/angular-resource.js"/>

<openmrs:htmlInclude file="/moduleResources/muzimaconsultation/js/ui-bootstrap/ui-bootstrap-custom-tpls-0.4.0.js"/>

<h3><spring:message code="muzimaconsultation.manage.sms"/></h3>

<a href="sendsms.form">Send SMS</a>
<%@ include file="/WEB-INF/template/footer.jsp" %>

