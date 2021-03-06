package org.sunbird.learner.actors.otp;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.sunbird.actor.core.BaseActor;
import org.sunbird.actor.router.ActorConfig;
import org.sunbird.datasecurity.impl.LogMaskServiceImpl;
import org.sunbird.keys.JsonKey;
import org.sunbird.learner.util.OTPUtil;
import org.sunbird.operations.ActorOperations;
import org.sunbird.request.Request;
import org.sunbird.request.RequestContext;
import org.sunbird.response.Response;

@ActorConfig(
  tasks = {},
  asyncTasks = {"sendOTP"},
  dispatcher = "notification-dispatcher"
)
public class SendOTPActor extends BaseActor {
  public static final String RESET_PASSWORD = "resetPassword";
  private LogMaskServiceImpl logMaskService = new LogMaskServiceImpl();

  @Override
  public void onReceive(Request request) throws Throwable {
    if (ActorOperations.SEND_OTP.getValue().equals(request.getOperation())) {
      sendOTP(request);
    } else {
      onReceiveUnsupportedOperation("SendOTPActor");
    }
  }

  private void sendOTP(Request request) {
    String type = (String) request.getRequest().get(JsonKey.TYPE);
    String key = (String) request.getRequest().get(JsonKey.KEY);
    String otp = (String) request.getRequest().get(JsonKey.OTP);
    String template = (String) request.getRequest().get(JsonKey.TEMPLATE_ID);
    if (JsonKey.EMAIL.equalsIgnoreCase(type)
        || JsonKey.PREV_USED_EMAIL.equalsIgnoreCase(type)
        || JsonKey.RECOVERY_EMAIL.equalsIgnoreCase(type)) {
      String userId = (String) request.get(JsonKey.USER_ID);
      logger.info(
          request.getRequestContext(),
          "SendOTPActor:sendOTP : Sending OTP via email for Key = "
              + logMaskService.maskEmail(key)
              + " or userId "
              + userId);
      sendOTPViaEmail(key, otp, userId, template, request.getRequestContext());
    } else if (JsonKey.PHONE.equalsIgnoreCase(type)
        || JsonKey.PREV_USED_PHONE.equalsIgnoreCase(type)
        || JsonKey.RECOVERY_PHONE.equalsIgnoreCase(type)) {
      logger.info(
          request.getRequestContext(),
          "SendOTPActor:sendOTP : Sending OTP via sms for Key = " + logMaskService.maskPhone(key));
      sendOTPViaSMS(key, otp, template, request.getRequestContext());
    } else {
      logger.info(request.getRequestContext(), "SendOTPActor:sendOTP : No Email/Phone provided.");
    }
    Response response = new Response();
    response.put(JsonKey.RESPONSE, JsonKey.SUCCESS);
    sender().tell(response, self());
  }

  private void sendOTPViaEmail(
      String key, String otp, String otpType, String template, RequestContext context) {
    Map<String, Object> emailTemplateMap = new HashMap<>();
    emailTemplateMap.put(JsonKey.EMAIL, key);
    emailTemplateMap.put(JsonKey.OTP, otp);
    emailTemplateMap.put(JsonKey.OTP_EXPIRATION_IN_MINUTES, OTPUtil.getOTPExpirationInMinutes());
    emailTemplateMap.put(JsonKey.TEMPLATE_ID, template);
    Request emailRequest = null;
    if (StringUtils.isBlank(otpType)) {
      emailRequest = OTPUtil.sendOTPViaEmail(emailTemplateMap, context);
    } else {
      emailRequest = OTPUtil.sendOTPViaEmail(emailTemplateMap, RESET_PASSWORD, context);
    }
    emailRequest.setRequestContext(context);
    logger.info(
        context,
        "SendOTPActor:sendOTP : Calling EmailServiceActor for Key = "
            + logMaskService.maskEmail(key));
    tellToAnother(emailRequest);
  }

  private void sendOTPViaSMS(String key, String otp, String template, RequestContext context) {
    Map<String, Object> otpMap = new HashMap<>();
    otpMap.put(JsonKey.PHONE, key);
    otpMap.put(JsonKey.OTP, otp);
    otpMap.put(JsonKey.TEMPLATE_ID, template);
    otpMap.put(JsonKey.OTP_EXPIRATION_IN_MINUTES, OTPUtil.getOTPExpirationInMinutes());
    logger.info(
        context,
        "SendOTPActor:sendOTPViaSMS : Calling OTPUtil.sendOTPViaSMS for Key = "
            + logMaskService.maskPhone(key));
    OTPUtil.sendOTPViaSMS(otpMap, context);
  }
}
