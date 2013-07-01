<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>
<openmrs:htmlInclude file="/moduleResources/muzimaconsultation/styles/flatui/bootstrap/css/bootstrap.css"/>
<openmrs:htmlInclude file="/moduleResources/muzimaconsultation/styles/flatui/css/flat-ui.css"/>

<openmrs:htmlInclude file="/moduleResources/muzimaconsultation/js/jquery/jquery.js" />

<openmrs:htmlInclude file="/moduleResources/muzimaconsultation/js/angular/angular.js"/>
<openmrs:htmlInclude file="/moduleResources/muzimaconsultation/js/angular/angular-resource.js"/>

<openmrs:htmlInclude file="/moduleResources/muzimaconsultation/styles/custom/custom.css"/>
<openmrs:htmlInclude file="/moduleResources/muzimaconsultation/js/custom/app.js"/>
<openmrs:htmlInclude file="/moduleResources/muzimaconsultation/js/custom/controller.js"/>

<openmrs:htmlInclude file="/moduleResources/muzimaconsultation/js/ui-bootstrap/ui-bootstrap-custom-0.4.0.js"/>
<openmrs:htmlInclude file="/moduleResources/muzimaconsultation/js/ui-bootstrap/ui-bootstrap-custom-tpls-0.4.0.js"/>

<h3><spring:message code="muzimaconsultation.consultations.list"/></h3>
<div class="bootstrap-scope" ng-app="muzimaconsultation">
    <div ng-view ></div>
</div>

<%@ include file="/WEB-INF/template/footer.jsp" %>
