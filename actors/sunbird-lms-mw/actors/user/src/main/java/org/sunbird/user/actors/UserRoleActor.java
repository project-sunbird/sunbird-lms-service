package org.sunbird.user.actors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.sunbird.actor.router.ActorConfig;
import org.sunbird.keys.JsonKey;
import org.sunbird.learner.actors.role.service.RoleService;
import org.sunbird.learner.util.DataCacheHandler;
import org.sunbird.learner.util.Util;
import org.sunbird.operations.ActorOperations;
import org.sunbird.request.Request;
import org.sunbird.request.RequestContext;
import org.sunbird.response.Response;
import org.sunbird.telemetry.dto.TelemetryEnvKey;
import org.sunbird.user.service.UserRoleService;
import org.sunbird.user.service.impl.UserRoleServiceImpl;

@ActorConfig(
  tasks = {"getRoles", "assignRoles", "assignRolesV2"},
  asyncTasks = {},
  dispatcher = "most-used-two-dispatcher"
)
public class UserRoleActor extends UserBaseActor {

  @Override
  public void onReceive(Request request) throws Throwable {
    Util.initializeContext(request, TelemetryEnvKey.USER);
    String operation = request.getOperation();

    switch (operation) {
      case "getRoles":
        getRoles();
        break;

      case "assignRoles":
        assignRoles(request);
        break;

      case "assignRolesV2":
        assignRoles(request);
        break;

      default:
        onReceiveUnsupportedOperation("UserRoleActor");
    }
  }

  private void getRoles() {
    Response response = DataCacheHandler.getRoleResponse();
    if (response == null) {
      response = RoleService.getUserRoles();
      DataCacheHandler.setRoleResponse(response);
    }
    sender().tell(response, self());
  }

  @SuppressWarnings("unchecked")
  private void assignRoles(Request actorMessage) {
    UserRoleService userRoleService = UserRoleServiceImpl.getInstance();
    List<Map<String, Object>> userRolesList = new ArrayList<>();

    Map<String, Object> requestMap = actorMessage.getRequest();
    requestMap.put(JsonKey.REQUESTED_BY, actorMessage.getContext().get(JsonKey.USER_ID));

    if (actorMessage.getOperation().equals(ActorOperations.ASSIGN_ROLES.getValue())) {
      requestMap.put(JsonKey.ROLE_OPERATION, "assignRole");
      List<String> roles = (List<String>) requestMap.get(JsonKey.ROLES);
      RoleService.validateRoles(roles);
      userRolesList = userRoleService.updateUserRole(requestMap, actorMessage.getRequestContext());
    } else {
      List<Map<String, Object>> roleList =
          (List<Map<String, Object>>) requestMap.get(JsonKey.ROLES);
      RoleService.validateRolesV2(roleList);
      userRolesList =
          userRoleService.updateUserRoleV2(requestMap, actorMessage.getRequestContext());
    }
    Response response = new Response();
    response.put(JsonKey.RESPONSE, JsonKey.SUCCESS);

    sender().tell(response, self());
    syncUserRoles(
        JsonKey.USER,
        (String) requestMap.get(JsonKey.USER_ID),
        userRolesList,
        actorMessage.getRequestContext());
    generateTelemetryEvent(
        requestMap,
        (String) requestMap.get(JsonKey.USER_ID),
        "userLevel",
        actorMessage.getContext());
  }

  private void syncUserRoles(
      String type, String userId, List<Map<String, Object>> userRolesList, RequestContext context) {
    Request request = new Request();
    request.setRequestContext(context);
    request.setOperation(ActorOperations.UPDATE_USER_ROLES_ES.getValue());
    request.getRequest().put(JsonKey.TYPE, type);
    request.getRequest().put(JsonKey.USER_ID, userId);
    request.getRequest().put(JsonKey.ROLES, userRolesList);
    logger.info(context, "UserRoleActor:syncUserRoles: Syncing to ES");
    try {
      tellToAnother(request);
    } catch (Exception ex) {
      logger.error(
          context,
          "UserRoleActor:syncUserRoles: Exception occurred with error message = " + ex.getMessage(),
          ex);
    }
  }
}
