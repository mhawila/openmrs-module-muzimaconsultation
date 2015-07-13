<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>
<openmrs:htmlInclude file="/moduleResources/muzimaconsultation/styles/custom/custom.css"/>
<openmrs:htmlInclude file="/moduleResources/muzimaconsultation/styles/bootstrap/css/bootstrap.css"/>

<openmrs:htmlInclude file="/moduleResources/muzimaconsultation/js/jquery/jquery.js" />

<openmrs:htmlInclude file="/moduleResources/muzimaconsultation/js/angular/angular.js"/>
<openmrs:htmlInclude file="/moduleResources/muzimaconsultation/js/angular/angular-resource.js"/>

<openmrs:htmlInclude file="/moduleResources/muzimaconsultation/js/ui-bootstrap/ui-bootstrap-custom-tpls-0.4.0.js"/>

<h3><spring:message code="muzimaconsultation.send.sms"/></h3>

<div id="form"/>
<form method="POST" id="sms-form">
    message <textarea name="message" size="140" id="message"></textarea><br/>
    Contacts  <textarea name="phone_numbers" id="phone_numbers"></textarea> (comma separated list of phone numbers)
    <br/>          <!--A comma separated list of phone numbers-->
    <input type="button" value="Send" onclick="processSubmission()"/>
</form>
</div>
<div id="result"></div>
<a href="managesms.form">&lt;-- Go back</a>
<script>
    function processSubmission() {
        var msg = $("#message").val();
        var ph = $("#phone_numbers").val();
        if(msg.length==0 || ph.length==0) {
            alert("Error: Both message & phone numbers should be provided");
            return;
        }
        var data = {
            message : msg,
            phone_numbers : ph
        }
        $.post(
            "sendsms.form",
            data,
            function (success) {
                if(success==="true") {
                    $("#result").html("Message Sent");
                    $("#form").hide();
                }
            }
        );
    }
</script>
<%@ include file="/WEB-INF/template/footer.jsp" %>

