package org.sunbird.learner.actors.tenantpreference;

import static akka.testkit.JavaTestKit.duration;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;
import java.util.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.sunbird.actor.router.RequestRouter;
import org.sunbird.actor.service.BaseMWService;
import org.sunbird.actorutil.InterServiceCommunicationFactory;
import org.sunbird.cassandraimpl.CassandraOperationImpl;
import org.sunbird.common.ElasticSearchHelper;
import org.sunbird.common.exception.ProjectCommonException;
import org.sunbird.common.models.response.Response;
import org.sunbird.common.models.util.ActorOperations;
import org.sunbird.common.models.util.JsonKey;
import org.sunbird.common.models.util.datasecurity.DecryptionService;
import org.sunbird.common.request.Request;
import org.sunbird.common.responsecode.ResponseCode;
import org.sunbird.helper.ServiceFactory;
import org.sunbird.learner.util.DataCacheHandler;
import org.sunbird.learner.util.Util;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
  ServiceFactory.class,
  BaseMWService.class,
  RequestRouter.class,
  InterServiceCommunicationFactory.class,
  ElasticSearchHelper.class,
  Util.class,
  DecryptionService.class,
  DataCacheHandler.class,
})
@PowerMockIgnore({"javax.management.*"})
public class TenantPreferenceManagementActorTest {

  private ActorSystem system = ActorSystem.create("system");
  private static final Props props = Props.create(TenantPreferenceManagementActor.class);
  private static CassandraOperationImpl cassandraOperation;
  private static final String orgId = "hhjcjr79fw4p89";
  private static final String USER_ID = "vcurc633r8911";

  @BeforeClass
  public static void beforeClass() {

    PowerMockito.mockStatic(ServiceFactory.class);
    cassandraOperation = mock(CassandraOperationImpl.class);
  }

  @Before
  public void beforeEachTest() {

    PowerMockito.mockStatic(ServiceFactory.class);
    when(ServiceFactory.getInstance()).thenReturn(cassandraOperation);
    when(cassandraOperation.insertRecord(
            Mockito.anyString(), Mockito.anyString(), Mockito.anyMap(), Mockito.any()))
        .thenReturn(createCassandraInsertSuccessResponse());
    cassandraOperation.deleteRecord(Mockito.anyString(), Mockito.anyString(), Mockito.anyMap());
    try {
      when(cassandraOperation.updateRecord(
              Mockito.anyString(),
              Mockito.anyString(),
              Mockito.anyMap(),
              Mockito.anyMap(),
              Mockito.any()))
          .thenReturn(createCassandraInsertSuccessResponse());
    } catch (Exception e) {
    }
  }

  private Response createCassandraInsertSuccessResponse() {
    Response response = new Response();
    response.put(JsonKey.RESPONSE, JsonKey.SUCCESS);
    return response;
  }

  @Test
  public void testCreateSuccessWithTenantPreferenceAlreadyExists() {

    TestKit probe = new TestKit(system);
    ActorRef subject = system.actorOf(props);
    Request actorMessage = new Request();
    Map<String, Object> map = new HashMap<>();
    map.put(JsonKey.KEY, "anyKey");
    map.put(JsonKey.DATA, new HashMap<>());
    map.put(JsonKey.ORG_ID, orgId);
    actorMessage.setRequest(map);
    actorMessage.setOperation(ActorOperations.CREATE_TENANT_PREFERENCE.getValue());
    when(cassandraOperation.getRecordsByProperties(
            Mockito.anyString(), Mockito.anyString(), Mockito.anyMap(), Mockito.any()))
        .thenReturn(cassandraGetRecordByProperty());
    subject.tell(actorMessage, probe.getRef());
    ProjectCommonException exception =
        probe.expectMsgClass(duration("10 second"), ProjectCommonException.class);
    Assert.assertTrue(null != exception);
  }

  @Test
  public void testCreateSuccessWithTenantPreferenceDoesNotExists() {

    TestKit probe = new TestKit(system);
    ActorRef subject = system.actorOf(props);
    Request actorMessage = new Request();
    Map<String, Object> map = new HashMap<>();
    map.put(JsonKey.KEY, "teacher_declaration");
    map.put(JsonKey.ORG_ID, orgId);
    actorMessage.setRequest(map);
    actorMessage.setOperation(ActorOperations.CREATE_TENANT_PREFERENCE.getValue());
    when(cassandraOperation.getRecordsByProperties(
            Mockito.anyString(), Mockito.anyString(), Mockito.anyMap(), Mockito.any()))
        .thenReturn(cassandraGetRecordByPropertiesEmptyResponse());
    subject.tell(actorMessage, probe.getRef());
    Response res = probe.expectMsgClass(duration("10 second"), Response.class);
    Assert.assertTrue(null != res.get(JsonKey.RESPONSE));
  }

  @Test
  public void testUpdateTenantPreferenceSuccess() {
    TestKit probe = new TestKit(system);
    ActorRef subject = system.actorOf(props);
    Request actorMessage = new Request();
    Map<String, Object> map = new HashMap<>();
    map.put(JsonKey.KEY, "anyKey");
    map.put(JsonKey.DATA, new HashMap<>());
    map.put(JsonKey.ORG_ID, orgId);
    actorMessage.setRequest(map);
    actorMessage.setOperation(ActorOperations.UPDATE_TENANT_PREFERENCE.getValue());
    when(cassandraOperation.getRecordsByProperties(
            Mockito.anyString(), Mockito.anyString(), Mockito.anyMap(), Mockito.any()))
        .thenReturn(cassandraGetRecordByProperty());
    subject.tell(actorMessage, probe.getRef());
    Response res = probe.expectMsgClass(duration("10 second"), Response.class);
    Assert.assertTrue(null != res.get(JsonKey.RESPONSE));
  }

  @Test
  public void testUpdateTenantPreferenceWithInvalidKey() {
    TestKit probe = new TestKit(system);
    ActorRef subject = system.actorOf(props);
    Request actorMessage = new Request();
    Map<String, Object> map = new HashMap<>();
    map.put(JsonKey.KEY, "teacher_declaration");
    map.put(JsonKey.DATA, new HashMap<>());
    map.put(JsonKey.ORG_ID, orgId);
    actorMessage.setRequest(map);
    actorMessage.setOperation(ActorOperations.UPDATE_TENANT_PREFERENCE.getValue());
    when(cassandraOperation.getRecordsByProperties(
            Mockito.anyString(), Mockito.anyString(), Mockito.anyMap(), Mockito.any()))
        .thenReturn(cassandraGetRecordByPropertiesEmptyResponse());
    subject.tell(actorMessage, probe.getRef());
    ProjectCommonException exception =
        probe.expectMsgClass(duration("10 second"), ProjectCommonException.class);
    Assert.assertTrue(null != exception);
    Assert.assertEquals(ResponseCode.preferenceNotFound.getErrorCode(), exception.getCode());
  }

  @Test
  public void testUpdateTenantPreferenceFailureWithInvalidOrgId() {
    TestKit probe = new TestKit(system);
    ActorRef subject = system.actorOf(props);
    Request actorMessage = new Request();
    Map<String, Object> map = new HashMap<>();
    map.put(JsonKey.KEY, "teacher_declaration");
    map.put(JsonKey.DATA, new HashMap<>());
    map.put(JsonKey.ORG_ID, "");
    actorMessage.setRequest(map);
    actorMessage.setOperation(ActorOperations.UPDATE_TENANT_PREFERENCE.getValue());
    when(cassandraOperation.getRecordsByProperties(
            Mockito.anyString(), Mockito.anyString(), Mockito.anyMap(), Mockito.any()))
        .thenReturn(cassandraGetRecordByPropertiesEmptyResponse());
    subject.tell(actorMessage, probe.getRef());
    ProjectCommonException exception =
        probe.expectMsgClass(duration("10 second"), ProjectCommonException.class);
    Assert.assertTrue(null != exception);
    Assert.assertEquals(ResponseCode.preferenceNotFound.getErrorCode(), exception.getCode());
  }

  @Test
  public void testGetTenantPreferenceSuccess() {
    TestKit probe = new TestKit(system);
    ActorRef subject = system.actorOf(props);
    Request actorMessage = new Request();
    Map<String, Object> map = new HashMap<>();
    map.put(JsonKey.KEY, "anyKey");
    map.put(JsonKey.ORG_ID, orgId);
    actorMessage.setRequest(map);
    actorMessage.setOperation(ActorOperations.GET_TENANT_PREFERENCE.getValue());
    when(cassandraOperation.getRecordsByProperties(
            Mockito.anyString(), Mockito.anyString(), Mockito.anyMap(), Mockito.any()))
        .thenReturn(cassandraGetRecordByProperty());
    subject.tell(actorMessage, probe.getRef());
    Response res = probe.expectMsgClass(duration("10 second"), Response.class);
    Assert.assertTrue(null != res);
  }

  @Test
  public void testDeleteTenantPreferenceSuccess() {
    TestKit probe = new TestKit(system);
    ActorRef subject = system.actorOf(props);
    Request actorMessage = new Request();
    Map<String, Object> map = new HashMap<>();
    map.put(JsonKey.KEY, "anyKey");
    map.put(JsonKey.ORG_ID, orgId);
    actorMessage.setRequest(map);
    actorMessage.setOperation(ActorOperations.DELETE_TENANT_PREFERENCE.getValue());
    when(cassandraOperation.getRecordsByProperties(
            Mockito.anyString(), Mockito.anyString(), Mockito.anyMap()))
        .thenReturn(cassandraGetRecordByProperty());
    subject.tell(actorMessage, probe.getRef());
    Response res = probe.expectMsgClass(duration("10 second"), Response.class);
    Assert.assertTrue(null != res);
  }

  @Test
  public void testDeleteTenantPreferenceFailure() {
    TestKit probe = new TestKit(system);
    ActorRef subject = system.actorOf(props);
    Request actorMessage = new Request();
    Map<String, Object> map = new HashMap<>();
    map.put(JsonKey.KEY, "anyKey");
    map.put(JsonKey.ORG_ID, orgId);
    actorMessage.setRequest(map);
    actorMessage.setOperation(ActorOperations.DELETE_TENANT_PREFERENCE.getValue());
    when(cassandraOperation.getRecordsByProperties(
            Mockito.anyString(), Mockito.anyString(), Mockito.anyMap()))
        .thenReturn(cassandraGetRecordByPropertiesEmptyResponse());
    subject.tell(actorMessage, probe.getRef());
    ProjectCommonException exception =
        probe.expectMsgClass(duration("10 second"), ProjectCommonException.class);
    Assert.assertTrue(null != exception);
    Assert.assertEquals(ResponseCode.preferenceNotFound.getErrorCode(), exception.getCode());
  }

  @Test
  public void testGetTenantPreferenceSuccessWithKeysDiff() {
    TestKit probe = new TestKit(system);
    ActorRef subject = system.actorOf(props);
    Request actorMessage = new Request();
    Map<String, Object> map = new HashMap<>();
    map.put(JsonKey.KEY, "teacher");
    map.put(JsonKey.ORG_ID, orgId);
    actorMessage.setRequest(map);
    actorMessage.setOperation(ActorOperations.GET_TENANT_PREFERENCE.getValue());
    when(cassandraOperation.getRecordsByProperties(
            Mockito.anyString(), Mockito.anyString(), Mockito.anyMap(), Mockito.any()))
        .thenReturn(cassandraGetRecordByPropertiesEmptyResponse());
    subject.tell(actorMessage, probe.getRef());
    ProjectCommonException exception =
        probe.expectMsgClass(duration("10 second"), ProjectCommonException.class);
    Assert.assertTrue(null != exception);
    Assert.assertEquals(ResponseCode.preferenceNotFound.getErrorCode(), exception.getCode());
  }

  @Test
  public void testWithInvalidOperationType() {

    TestKit probe = new TestKit(system);
    ActorRef subject = system.actorOf(props);
    Request actorMessage = new Request();

    Map<String, Object> map = new HashMap<>();
    map.put(JsonKey.TERM_AND_CONDITION_STATUS, "ACCEPTED");

    actorMessage.getRequest().put(JsonKey.TENANT_PREFERENCE, map);
    actorMessage.getRequest().put(JsonKey.ROOT_ORG_ID, orgId);
    actorMessage.getRequest().put(JsonKey.REQUESTED_BY, USER_ID);
    actorMessage.setOperation("InvalidOperation");

    subject.tell(actorMessage, probe.getRef());
    ProjectCommonException exc =
        probe.expectMsgClass(duration("10 second"), ProjectCommonException.class);
    Assert.assertTrue(null != exc);
  }

  private static Response cassandraGetRecordByProperty() {
    Response response = new Response();
    List<Map<String, Object>> list = new ArrayList();
    Map<String, Object> map = new HashMap<>();
    map.put(JsonKey.KEY, "anyKey");
    map.put(JsonKey.ORG_ID, orgId);
    map.put(
        JsonKey.DATA,
        "{\"default\":{\"action\":\"volunteer\",\"templateName\":\"volunteer\",\"fields\":[[{\"title\":\"Please confirm that ALL the following items are verified (by ticking the check-boxes) before you can publish:\",\"contents\":[{\"name\":\"Appropriateness\",\"checkList\":[\"No Hate speech, Abuse, Violence, Profanity\",\"No Discrimination or Defamation\",\"Is suitable for children\"]}]}]]}}");
    list.add(map);
    response.put(JsonKey.RESPONSE, list);
    return response;
  }

  private static Response cassandraGetRecordByPropertiesEmptyResponse() {
    Response response = new Response();
    List<Map<String, Object>> list = new ArrayList();
    response.put(JsonKey.RESPONSE, list);
    return response;
  }
}
