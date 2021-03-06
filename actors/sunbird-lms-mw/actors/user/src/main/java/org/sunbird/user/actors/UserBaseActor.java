package org.sunbird.user.actors;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.sunbird.actor.core.BaseActor;
import org.sunbird.actorutil.location.LocationClient;
import org.sunbird.actorutil.location.impl.LocationClientImpl;
import org.sunbird.exception.ProjectCommonException;
import org.sunbird.exception.ResponseCode;
import org.sunbird.kafka.KafkaClient;
import org.sunbird.keys.JsonKey;
import org.sunbird.learner.util.DataCacheHandler;
import org.sunbird.learner.util.FormApiUtil;
import org.sunbird.learner.util.Util;
import org.sunbird.location.service.LocationService;
import org.sunbird.location.service.LocationServiceImpl;
import org.sunbird.models.location.Location;
import org.sunbird.operations.LocationActorOperation;
import org.sunbird.request.Request;
import org.sunbird.request.RequestContext;
import org.sunbird.response.Response;
import org.sunbird.telemetry.util.TelemetryUtil;
import org.sunbird.user.service.UserLookupService;
import org.sunbird.user.service.impl.UserLookUpServiceImpl;
import org.sunbird.util.ProjectUtil;

public abstract class UserBaseActor extends BaseActor {

  protected UserLookupService userLookupService = UserLookUpServiceImpl.getInstance();
  protected LocationClient locationClient = LocationClientImpl.getInstance();

  protected void generateUserTelemetry(
      Map<String, Object> userMap, Request request, String userId, String operationType) {
    Request telemetryReq = new Request();
    telemetryReq.getRequest().put("userMap", userMap);
    telemetryReq.getRequest().put("userId", userId);
    telemetryReq.getRequest().put(JsonKey.OPERATION_TYPE, operationType);
    telemetryReq.setContext(request.getContext());
    telemetryReq.setOperation("generateUserTelemetry");
    tellToAnother(telemetryReq);
  }

  protected void generateTelemetryEvent(
      Map<String, Object> requestMap,
      String userId,
      String objectType,
      Map<String, Object> context) {
    List<Map<String, Object>> correlatedObject = new ArrayList<>();
    Map<String, Object> targetObject =
        TelemetryUtil.generateTargetObject(userId, JsonKey.USER, JsonKey.UPDATE, null);
    Map<String, Object> telemetryAction = new HashMap<>();

    switch (objectType) {
      case "userLevel":
        telemetryAction.put("AssignRole", "role assigned at user level");
        TelemetryUtil.telemetryProcessingCall(
            telemetryAction, targetObject, correlatedObject, context);
        break;
      case "blockUser":
        telemetryAction.put(JsonKey.BLOCK_USER, "user blocked");
        TelemetryUtil.telemetryProcessingCall(
            JsonKey.BLOCK_USER, telemetryAction, targetObject, correlatedObject, context);
        break;
      case "unblockUser":
        telemetryAction.put(JsonKey.UNBLOCK_USER, "user unblocked");
        TelemetryUtil.telemetryProcessingCall(
            JsonKey.UNBLOCK_USER, telemetryAction, targetObject, correlatedObject, context);
        break;
      default:
        // Do Nothing
    }
  }

  protected Response insertIntoUserLookUp(Map<String, Object> userMap, RequestContext context) {
    List<Map<String, Object>> list = new ArrayList<>();
    Map<String, Object> lookUp = new HashMap<>();
    if (userMap.get(JsonKey.PHONE) != null) {
      lookUp.put(JsonKey.TYPE, JsonKey.PHONE);
      lookUp.put(JsonKey.USER_ID, userMap.get(JsonKey.ID));
      lookUp.put(JsonKey.VALUE, userMap.get(JsonKey.PHONE));
      list.add(lookUp);
    }
    if (userMap.get(JsonKey.EMAIL) != null) {
      lookUp = new HashMap<>();
      lookUp.put(JsonKey.TYPE, JsonKey.EMAIL);
      lookUp.put(JsonKey.USER_ID, userMap.get(JsonKey.ID));
      lookUp.put(JsonKey.VALUE, userMap.get(JsonKey.EMAIL));
      list.add(lookUp);
    }
    if (CollectionUtils.isNotEmpty((List) userMap.get(JsonKey.EXTERNAL_IDS))) {
      Map<String, Object> externalId =
          ((List<Map<String, Object>>) userMap.get(JsonKey.EXTERNAL_IDS))
              .stream()
              .filter(x -> x.get(JsonKey.ID_TYPE).equals(x.get(JsonKey.PROVIDER)))
              .findFirst()
              .orElse(null);
      if (MapUtils.isNotEmpty(externalId)) {
        lookUp = new HashMap<>();
        lookUp.put(JsonKey.TYPE, JsonKey.USER_LOOKUP_FILED_EXTERNAL_ID);
        lookUp.put(JsonKey.USER_ID, userMap.get(JsonKey.ID));
        // provider is the orgId, not the channel
        lookUp.put(
            JsonKey.VALUE, externalId.get(JsonKey.ID) + "@" + externalId.get(JsonKey.PROVIDER));
        list.add(lookUp);
      }
    }
    if (userMap.get(JsonKey.USERNAME) != null) {
      lookUp = new HashMap<>();
      lookUp.put(JsonKey.TYPE, JsonKey.USER_LOOKUP_FILED_USER_NAME);
      lookUp.put(JsonKey.USER_ID, userMap.get(JsonKey.ID));
      lookUp.put(JsonKey.VALUE, userMap.get(JsonKey.USERNAME));
      list.add(lookUp);
    }
    Response response = null;
    if (CollectionUtils.isNotEmpty(list)) {
      response = userLookupService.insertRecords(list, context);
    }
    return response;
  }

  protected void removeUnwanted(Map<String, Object> reqMap) {
    reqMap.remove(JsonKey.ADDRESS);
    reqMap.remove(JsonKey.EDUCATION);
    reqMap.remove(JsonKey.JOB_PROFILE);
    reqMap.remove(JsonKey.ORGANISATION);
    reqMap.remove(JsonKey.REGISTERED_ORG);
    reqMap.remove(JsonKey.ROOT_ORG);
    reqMap.remove(JsonKey.IDENTIFIER);
    reqMap.remove(JsonKey.ORGANISATIONS);
    reqMap.remove(JsonKey.IS_DELETED);
    reqMap.remove(JsonKey.EXTERNAL_ID);
    reqMap.remove(JsonKey.ID_TYPE);
    reqMap.remove(JsonKey.EXTERNAL_ID_TYPE);
    reqMap.remove(JsonKey.PROVIDER);
    reqMap.remove(JsonKey.EXTERNAL_ID_PROVIDER);
    reqMap.remove(JsonKey.EXTERNAL_IDS);
    reqMap.remove(JsonKey.ORGANISATION_ID);
    reqMap.remove(JsonKey.ROLES);
    Util.getUserDefaultValue()
        .keySet()
        .stream()
        .forEach(
            key -> {
              if (!JsonKey.PASSWORD.equalsIgnoreCase(key)) {
                reqMap.remove(key);
              }
            });
  }

  protected void validateAndGetLocationCodes(Request userRequest) {
    Object locationCodes = userRequest.getRequest().get(JsonKey.LOCATION_CODES);
    validateLocationCodesDataType(locationCodes);
    if (CollectionUtils.isNotEmpty((List) locationCodes)) {
      List<Location> locationList = getLocationList(locationCodes, userRequest.getRequestContext());
      String stateCode = validateAndGetStateLocationCode(locationList);
      List<String> allowedLocationTypeList =
          getStateLocationTypeConfig(stateCode, userRequest.getRequestContext());
      String stateId = null;
      List<String> set = new ArrayList<>();
      for (Location location : locationList) {
        isValidLocationType(location.getType(), allowedLocationTypeList);
        if (location.getType().equalsIgnoreCase(JsonKey.STATE)) {
          stateId = location.getId();
        }
        if (location.getType().equals(JsonKey.LOCATION_TYPE_SCHOOL)) {
          userRequest.getRequest().put(JsonKey.ORG_EXTERNAL_ID, location.getCode());
        } else {
          set.add(location.getCode());
        }
      }
      if (StringUtils.isNotBlank((String) userRequest.getRequest().get(JsonKey.ORG_EXTERNAL_ID))) {
        userRequest.getRequest().put(JsonKey.STATE_ID, stateId);
      }
      userRequest.getRequest().put(JsonKey.LOCATION_CODES, set);
    }
  }

  protected List<String> getStateLocationTypeConfig(String stateCode, RequestContext context) {
    Map<String, List<String>> locationTypeConfigMap = DataCacheHandler.getLocationTypeConfig();
    if (MapUtils.isEmpty(locationTypeConfigMap)
        || CollectionUtils.isEmpty(locationTypeConfigMap.get(stateCode))) {
      Map<String, Object> userProfileConfigMap = FormApiUtil.getProfileConfig(stateCode, context);
      // If config is not available check the default profile config
      if (MapUtils.isEmpty(userProfileConfigMap) && !JsonKey.DEFAULT_PERSONA.equals(stateCode)) {
        stateCode = JsonKey.DEFAULT_PERSONA;
        if (CollectionUtils.isEmpty(locationTypeConfigMap.get(stateCode))) {
          userProfileConfigMap = FormApiUtil.getProfileConfig(stateCode, context);
          if (MapUtils.isNotEmpty(userProfileConfigMap)) {
            List<String> locationTypeList =
                FormApiUtil.getLocationTypeConfigMap(userProfileConfigMap);
            if (CollectionUtils.isNotEmpty(locationTypeList)) {
              locationTypeConfigMap.put(stateCode, locationTypeList);
            }
          }
        }
      } else {
        List<String> locationTypeList = FormApiUtil.getLocationTypeConfigMap(userProfileConfigMap);
        if (CollectionUtils.isNotEmpty(locationTypeList)) {
          locationTypeConfigMap.put(stateCode, locationTypeList);
        }
      }
    }
    return locationTypeConfigMap.get(stateCode);
  }

  protected String validateAndGetStateLocationCode(List<Location> locationList) {
    String stateCode = "";
    for (Location location : locationList) {
      if (JsonKey.STATE.equals(location.getType())) {
        stateCode = location.getCode();
      }
    }
    // Throw an exception if location codes update does not contains state code
    if (StringUtils.isBlank(stateCode)) {
      throw new ProjectCommonException(
          ResponseCode.mandatoryParamsMissing.getErrorCode(),
          ProjectUtil.formatMessage(
              ResponseCode.mandatoryParamsMissing.getErrorMessage(),
              JsonKey.LOCATION_CODES + " of type State"),
          ResponseCode.CLIENT_ERROR.getResponseCode());
    }
    return stateCode;
  }

  protected List<Location> getLocationList(Object locationCodes, RequestContext context) {
    // As of now locationCode can take array of only location codes and map of location Codes which
    // include type and code of the location
    List<Location> locationList = new ArrayList<>();
    if (((List) locationCodes).get(0) instanceof String) {
      List<String> locations = (List<String>) locationCodes;
      locationList =
          locationClient.getLocationsByCodes(
              getActorRef(LocationActorOperation.SEARCH_LOCATION.getValue()), locations, context);
    }

    if (((List) locationCodes).get(0) instanceof Map) {
      locationList = createLocationLists((List<Map<String, String>>) locationCodes);
    }
    return locationList;
  }

  protected void validateLocationCodesDataType(Object locationCodes) {
    if ((locationCodes != null) && !(locationCodes instanceof List)) {
      throw new ProjectCommonException(
          ResponseCode.dataTypeError.getErrorCode(),
          ProjectUtil.formatMessage(
              ResponseCode.dataTypeError.getErrorMessage(), JsonKey.LOCATION_CODES, JsonKey.LIST),
          ResponseCode.CLIENT_ERROR.getResponseCode());
    }
  }

  protected List<Location> createLocationLists(List<Map<String, String>> locationCodes) {
    List<Location> locations = new ArrayList<>();
    for (Map<String, String> locationMap : locationCodes) {
      Location location = new Location();
      location.setCode(locationMap.get(JsonKey.CODE));
      location.setType(locationMap.get(JsonKey.TYPE));
      locations.add(location);
    }
    return locations;
  }

  protected boolean isValidLocationType(String type, List<String> typeList) {
    if (null != type
        && CollectionUtils.isNotEmpty(typeList)
        && !typeList.contains(type.toLowerCase())) {
      throw new ProjectCommonException(
          ResponseCode.invalidValue.getErrorCode(),
          ProjectUtil.formatMessage(
              ResponseCode.invalidValue.getErrorMessage(), JsonKey.LOCATION_TYPE, type, typeList),
          ResponseCode.CLIENT_ERROR.getResponseCode());
    }
    return true;
  }

  protected void populateLocationCodesFromProfileLocation(Map<String, Object> userMap) {
    if (userMap.containsKey(JsonKey.PROFILE_LOCATION)) {
      userMap.remove(JsonKey.LOCATION_CODES);
      List<Map<String, String>> profLocList =
          (List<Map<String, String>>) userMap.get(JsonKey.PROFILE_LOCATION);
      List<String> locationCodes = null;
      if (CollectionUtils.isNotEmpty(profLocList)) {
        locationCodes =
            profLocList.stream().map(m -> m.get(JsonKey.CODE)).collect(Collectors.toList());
        userMap.put(JsonKey.LOCATION_CODES, locationCodes);
      }
      userMap.remove(JsonKey.PROFILE_LOCATION);
    }
  }

  protected void populateUserTypeAndSubType(Map<String, Object> userMap) {
    userMap.remove(JsonKey.USER_TYPE);
    userMap.remove(JsonKey.USER_SUB_TYPE);
    if (userMap.containsKey(JsonKey.PROFILE_USERTYPE)) {
      Map<String, Object> userTypeAndSubType =
          (Map<String, Object>) userMap.get(JsonKey.PROFILE_USERTYPE);
      userMap.put(JsonKey.USER_TYPE, userTypeAndSubType.get(JsonKey.TYPE));
      userMap.put(JsonKey.USER_SUB_TYPE, userTypeAndSubType.get(JsonKey.SUB_TYPE));
    }
  }

  protected void populateProfileUserType(
      Map<String, Object> userMap, RequestContext requestContext) {
    Map<String, String> userTypeAndSubType = new HashMap<>();
    userMap.remove(JsonKey.PROFILE_USERTYPE);
    if (userMap.containsKey(JsonKey.USER_TYPE)) {
      userTypeAndSubType.put(JsonKey.TYPE, (String) userMap.get(JsonKey.USER_TYPE));
      if (userMap.containsKey(JsonKey.USER_SUB_TYPE)) {
        userTypeAndSubType.put(JsonKey.SUB_TYPE, (String) userMap.get(JsonKey.USER_SUB_TYPE));
      } else {
        userTypeAndSubType.put(JsonKey.SUB_TYPE, null);
      }
      try {
        ObjectMapper mapper = new ObjectMapper();
        userMap.put(JsonKey.PROFILE_USERTYPE, mapper.writeValueAsString(userTypeAndSubType));
      } catch (Exception ex) {
        logger.error(requestContext, "Exception occurred while mapping", ex);
        ProjectCommonException.throwServerErrorException(ResponseCode.SERVER_ERROR);
      }
      userMap.remove(JsonKey.USER_TYPE);
      userMap.remove(JsonKey.USER_SUB_TYPE);
    }
  }

  protected void writeDataToKafka(Map<String, Object> data) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      String event = mapper.writeValueAsString(data);
      // user_events
      KafkaClient.send(event, ProjectUtil.getConfigValue("sunbird_user_create_sync_topic"));
    } catch (Exception ex) {
      logger.error("Exception occurred while writing event to kafka", ex);
    }
  }

  protected void convertValidatedLocationCodesToIDs(
      Map<String, Object> userMap, RequestContext context) {
    if (userMap.containsKey(JsonKey.LOCATION_IDS)
        && CollectionUtils.isEmpty((List<String>) userMap.get(JsonKey.LOCATION_IDS))) {
      userMap.remove(JsonKey.LOCATION_IDS);
    }
    if (!userMap.containsKey(JsonKey.LOCATION_IDS)
        && userMap.containsKey(JsonKey.LOCATION_CODES)
        && !CollectionUtils.isEmpty((List<String>) userMap.get(JsonKey.LOCATION_CODES))) {
      LocationService locationService = LocationServiceImpl.getInstance();
      ObjectMapper mapper = new ObjectMapper();
      List<Map<String, String>> locationIdTypeList =
          locationService.getValidatedRelatedLocationIdAndType(
              (List<String>) userMap.get(JsonKey.LOCATION_CODES), context);
      if (locationIdTypeList != null && !locationIdTypeList.isEmpty()) {
        try {
          userMap.put(JsonKey.PROFILE_LOCATION, mapper.writeValueAsString(locationIdTypeList));
        } catch (Exception ex) {
          logger.error(context, "Exception occurred while mapping", ex);
          ProjectCommonException.throwServerErrorException(ResponseCode.SERVER_ERROR);
        }

        userMap.remove(JsonKey.LOCATION_CODES);
      } else {
        ProjectCommonException.throwClientErrorException(
            ResponseCode.invalidParameterValue,
            MessageFormat.format(
                ResponseCode.invalidParameterValue.getErrorMessage(),
                JsonKey.LOCATION_CODES,
                userMap.get(JsonKey.LOCATION_CODES)));
      }
    }
  }
}
