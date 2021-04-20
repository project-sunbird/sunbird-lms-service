package org.sunbird.learner.actors.bulkupload;

import static akka.testkit.JavaTestKit.duration;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.sunbird.cassandraimpl.CassandraOperationImpl;
import org.sunbird.common.exception.ProjectCommonException;
import org.sunbird.common.models.response.Response;
import org.sunbird.common.models.util.BulkUploadActorOperation;
import org.sunbird.common.models.util.GeoLocationJsonKey;
import org.sunbird.common.models.util.JsonKey;
import org.sunbird.common.request.Request;
import org.sunbird.helper.ServiceFactory;
import org.sunbird.learner.actors.bulkupload.dao.impl.BulkUploadProcessDaoImpl;
import org.sunbird.learner.util.Util;

/**
 * Test case for Location Bulk upload.
 *
 * @author arvind on 30/4/18.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@PrepareForTest({
  ServiceFactory.class,
  Util.class,
  LocationBulkUploadActor.class,
  BulkUploadProcessDaoImpl.class,
  LocationBulkUploadBackGroundJobActor.class
})
@PowerMockIgnore({
  "javax.management.*",
  "javax.net.ssl.*",
  "javax.security.*",
  "jdk.internal.reflect.*"
})
@RunWith(PowerMockRunner.class)
public class LocationBulkUploadActorTest {

  private static ActorSystem system;
  private static final Props props = Props.create(LocationBulkUploadActor.class);
  private static final String USER_ID = "user123";
  private static final String LOCATION_TYPE = "State";
  private static CassandraOperationImpl cassandraOperation;

  @BeforeClass
  public static void setUp() {
    system = ActorSystem.create("system");
    PowerMockito.mockStatic(ServiceFactory.class);
    cassandraOperation = mock(CassandraOperationImpl.class);
    when(ServiceFactory.getInstance()).thenReturn(cassandraOperation);
  }

  @Test
  @Ignore
  public void testLocationBulkUploadWithProperData() throws Exception {
    when(cassandraOperation.getRecordById(
            Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
        .thenReturn(getCassandraRecordByIdForUserResponse());
    when(cassandraOperation.insertRecord(
            Mockito.anyString(), Mockito.anyString(), Mockito.anyMap()))
        .thenReturn(createCassandraInsertSuccessResponse());
    TestKit probe = new TestKit(system);
    ActorRef subject = system.actorOf(props);
    List<String> headerLine =
        Arrays.asList(
            GeoLocationJsonKey.PROPERTY_NAME,
            GeoLocationJsonKey.CODE,
            GeoLocationJsonKey.PARENT_CODE,
            GeoLocationJsonKey.PARENT_ID);
    List<String> firstDataLine = Arrays.asList("location_name", "location-code", null, null);
    String jsonString = createLines(headerLine, firstDataLine);
    Request reqObj = getRequestObjectForLocationBulkUpload(LOCATION_TYPE, jsonString.getBytes());
    subject.tell(reqObj, probe.getRef());
    Response res = probe.expectMsgClass(duration("100 second"), Response.class);
    String processId = (String) res.get(JsonKey.PROCESS_ID);
    Assert.assertTrue(null != processId);
  }

  private Response createCassandraInsertSuccessResponse() {
    Response response = new Response();
    response.put(JsonKey.RESPONSE, JsonKey.SUCCESS);
    return response;
  }

  private Response getCassandraRecordByIdForUserResponse() {
    Response response = new Response();
    List<Map<String, Object>> userList = new ArrayList<>();
    Map<String, Object> map = new HashMap<>();
    map.put(JsonKey.USER_ID, "VALID-USER-ID");
    map.put(JsonKey.CHANNEL, "anyChannel");
    userList.add(map);
    response.put(JsonKey.RESPONSE, userList);
    return response;
  }

  @Test
  public void testLocationBulkUploadWithInvalidAttributeNames() throws Exception {
    TestKit probe = new TestKit(system);
    ActorRef subject = system.actorOf(props);
    List<String> headerLine =
        Arrays.asList(
            GeoLocationJsonKey.PROPERTY_NAME + "invalid",
            GeoLocationJsonKey.CODE,
            GeoLocationJsonKey.PARENT_CODE,
            GeoLocationJsonKey.PARENT_ID);
    List<String> firstDataLine = Arrays.asList("location_name", "location-code", null, null);
    String jsonString = createLines(headerLine, firstDataLine);
    Request reqObj = getRequestObjectForLocationBulkUpload(LOCATION_TYPE, jsonString.getBytes());
    subject.tell(reqObj, probe.getRef());
    ProjectCommonException res =
        probe.expectMsgClass(duration("10 second"), ProjectCommonException.class);
    Assert.assertTrue(null != res);
  }

  @Test
  public void testLocationBulkUploadWithoutMandatoryFieldCode() throws Exception {
    TestKit probe = new TestKit(system);
    ActorRef subject = system.actorOf(props);
    List<String> headerLine =
        Arrays.asList(
            GeoLocationJsonKey.PROPERTY_NAME,
            GeoLocationJsonKey.PARENT_CODE,
            GeoLocationJsonKey.PARENT_ID);
    List<String> firstDataLine = Arrays.asList("location_name", null, null);
    String jsonString = createLines(headerLine, firstDataLine);
    Request reqObj = getRequestObjectForLocationBulkUpload(LOCATION_TYPE, jsonString.getBytes());
    subject.tell(reqObj, probe.getRef());
    ProjectCommonException res =
        probe.expectMsgClass(duration("10 second"), ProjectCommonException.class);
    Assert.assertTrue(null != res);
  }

  @Test
  public void testLocationBulkUploadWithoutAnyDataRecord() throws Exception {
    TestKit probe = new TestKit(system);
    ActorRef subject = system.actorOf(props);
    List<String> headerLine =
        Arrays.asList(
            GeoLocationJsonKey.PROPERTY_NAME,
            GeoLocationJsonKey.PARENT_CODE,
            GeoLocationJsonKey.PARENT_ID);
    String jsonString = createLines(headerLine);
    Request reqObj = getRequestObjectForLocationBulkUpload(LOCATION_TYPE, jsonString.getBytes());
    subject.tell(reqObj, probe.getRef());
    ProjectCommonException res =
        probe.expectMsgClass(duration("10 second"), ProjectCommonException.class);
    Assert.assertTrue(null != res);
  }

  @Test
  public void testLocationBulkUploadWithExtraAttributeNameValue() throws Exception {
    TestKit probe = new TestKit(system);
    ActorRef subject = system.actorOf(props);
    List<String> headerLine =
        Arrays.asList(
            GeoLocationJsonKey.PROPERTY_NAME,
            GeoLocationJsonKey.CODE,
            GeoLocationJsonKey.PARENT_CODE,
            GeoLocationJsonKey.PARENT_ID,
            GeoLocationJsonKey.PROPERTY_VALUE);
    List<String> firstDataLine =
        Arrays.asList("location_name", "location-code", null, null, "value");
    String jsonString = createLines(headerLine, firstDataLine);
    Request reqObj = getRequestObjectForLocationBulkUpload(LOCATION_TYPE, jsonString.getBytes());
    subject.tell(reqObj, probe.getRef());
    ProjectCommonException res =
        probe.expectMsgClass(duration("10 second"), ProjectCommonException.class);
    Assert.assertTrue(null != res);
  }

  private Request getRequestObjectForLocationBulkUpload(String locationType, byte[] file) {
    Request reqObj = new Request();
    reqObj.setOperation(BulkUploadActorOperation.LOCATION_BULK_UPLOAD.getValue());
    HashMap<String, Object> innerMap = new HashMap<>();
    innerMap.put(JsonKey.CREATED_BY, USER_ID);
    innerMap.put(JsonKey.OBJECT_TYPE, JsonKey.LOCATION);
    innerMap.put(JsonKey.FILE, file);
    innerMap.put(GeoLocationJsonKey.LOCATION_TYPE, locationType);
    reqObj.getRequest().put(JsonKey.DATA, innerMap);
    return reqObj;
  }

  private String createLines(List<String>... list) throws JsonProcessingException {

    StringBuilder builder = new StringBuilder();
    for (List<String> l : list) {
      String.join(",", l);
      builder.append(String.join(",", l));
      builder.append(System.lineSeparator());
    }
    return builder.toString();
  }
}
