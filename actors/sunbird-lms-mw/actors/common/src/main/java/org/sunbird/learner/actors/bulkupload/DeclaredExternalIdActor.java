package org.sunbird.learner.actors.bulkupload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.sunbird.actor.core.BaseActor;
import org.sunbird.actor.router.ActorConfig;
import org.sunbird.bean.SelfDeclaredUser;
import org.sunbird.cassandra.CassandraOperation;
import org.sunbird.common.models.response.Response;
import org.sunbird.common.models.util.BulkUploadActorOperation;
import org.sunbird.common.models.util.JsonKey;
import org.sunbird.common.models.util.ProjectLogger;
import org.sunbird.common.models.util.ProjectUtil;
import org.sunbird.common.request.Request;
import org.sunbird.common.responsecode.ResponseCode;
import org.sunbird.helper.ServiceFactory;
import org.sunbird.learner.actors.bulkupload.model.BulkMigrationUser;
import org.sunbird.learner.actors.bulkupload.util.UserUploadUtil;
import org.sunbird.learner.util.Util;
import org.sunbird.models.user.UserDeclareEntity;

@ActorConfig(
  tasks = {},
  asyncTasks = {"processExternalId"}
)
public class DeclaredExternalIdActor extends BaseActor {

  private Util.DbInfo usrExtIdDbInfo = Util.dbInfoMap.get(JsonKey.USR_EXT_ID_DB);
  private CassandraOperation cassandraOperation = ServiceFactory.getInstance();

  @Override
  public void onReceive(Request request) throws Throwable {
    String operation = request.getOperation();
    if (operation.equalsIgnoreCase(BulkUploadActorOperation.USER_BULK_MIGRATION.getValue())) {
      processDeclaredExternalId(request);
    } else {
      onReceiveUnsupportedOperation("userBulkMigration");
    }
  }

  private void processDeclaredExternalId(Request request) {
    Map requestMap = request.getRequest();
    String processId = (String) requestMap.get(JsonKey.PROCESS_ID);
    Map<String, Object> row = UserUploadUtil.getFullRecordFromProcessId(processId);
    BulkMigrationUser bulkMigrationUser = UserUploadUtil.convertRowToObject(row);
    UserUploadUtil.updateStatusInUserBulkTable(
        bulkMigrationUser.getId(), ProjectUtil.BulkProcessStatus.IN_PROGRESS.getValue());
    List<SelfDeclaredUser> userList = UserUploadUtil.getMigrationUserAsList(bulkMigrationUser);
    userList
        .parallelStream()
        .forEach(
            migrateUser -> {
              // add entry in usr_external_id
              // modify status to validated to user_declarations
              // call to migrate api
              switch (migrateUser.getInputStatus()) {
                case JsonKey.VALIDATED:
                  migrateDeclaredUser(request, migrateUser);
                  break;
                case JsonKey.REJECTED:
                  rejectDeclaredDetail(request, migrateUser);
                  break;
                case JsonKey.ERROR:
                  updateErrorDetail(request, migrateUser);
                  break;
                default:
              }
            });
  }

  private void updateErrorDetail(Request request, SelfDeclaredUser declaredUser) {
    Response response = new Response();
    response.setResponseCode(ResponseCode.OK);
    request.setOperation("updateUserSelfDeclarationsErrorType");
    Map<String, Object> requestMap = new HashMap();
    UserDeclareEntity userDeclareEntity = new UserDeclareEntity();
    userDeclareEntity.setOrgId(declaredUser.getOrgId());
    userDeclareEntity.setPersona(declaredUser.getPersona());
    userDeclareEntity.setUserId(declaredUser.getUserId());
    userDeclareEntity.setErrorType(declaredUser.getErrorType());
    userDeclareEntity.setStatus(declaredUser.getInputStatus());
    requestMap.put(JsonKey.DECLARATIONS, userDeclareEntity);
    tellToAnother(request);
  }

  private void rejectDeclaredDetail(Request request, SelfDeclaredUser declaredUser) {
    Response response = new Response();
    response.setResponseCode(ResponseCode.OK);
    request.setOperation("upsertUserSelfDeclarations");
    Map<String, Object> requestMap = new HashMap();
    UserDeclareEntity userDeclareEntity = new UserDeclareEntity();
    userDeclareEntity.setOrgId(declaredUser.getOrgId());
    userDeclareEntity.setPersona(declaredUser.getPersona());
    userDeclareEntity.setUserId(declaredUser.getUserId());
    userDeclareEntity.setOperation(JsonKey.REMOVE);
    requestMap.put(JsonKey.DECLARATIONS, userDeclareEntity);
    tellToAnother(request);
  }

  private void migrateDeclaredUser(Request request, SelfDeclaredUser declaredUser) {
    Response response = new Response();
    response.setResponseCode(ResponseCode.REDIRECTION_REQUIRED);
    request.setOperation(BulkUploadActorOperation.USER_BULK_MIGRATION.getValue());
    ProjectLogger.log("DeclaredExternalIdActor:migrateDeclaredUser ");
    try {
      Map<String, Object> requestMap = new HashMap();
      Map<String, String> externalIdMap = new HashMap();
      List<Map<String, String>> externalIdLst = new ArrayList();
      requestMap.put(JsonKey.USER_ID, declaredUser.getUserId());
      requestMap.put(JsonKey.CHANNEL, declaredUser.getChannel());
      externalIdMap.put(JsonKey.ID, declaredUser.getUserExternalId());
      externalIdMap.put(JsonKey.ID_TYPE, declaredUser.getChannel());
      externalIdMap.put(JsonKey.PROVIDER, declaredUser.getChannel());
      externalIdLst.add(externalIdMap);
      requestMap.put(JsonKey.EXTERNAL_IDS, externalIdLst);
      tellToAnother(request);
    } catch (Exception e) {
      ProjectLogger.log(e.getMessage(), e);
    }
    sender().tell(response, self());
  }
}