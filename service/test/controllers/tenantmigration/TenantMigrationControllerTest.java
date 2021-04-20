package controllers.tenantmigration;

import static org.junit.Assert.assertEquals;

import controllers.BaseApplicationTest;
import controllers.DummyActor;
import controllers.TestUtil;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modules.OnRequestHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.sunbird.common.models.util.JsonKey;
import org.sunbird.common.request.HeaderParam;
import org.sunbird.common.responsecode.ResponseCode;
import play.mvc.Result;

@PrepareForTest(OnRequestHandler.class)
@PowerMockIgnore({"javax.management.*", "jdk.internal.reflect.*"})
public class TenantMigrationControllerTest extends BaseApplicationTest {

  public static Map<String, List<String>> headerMap;

  @Before
  public void before() {
    setup(DummyActor.class);
    headerMap = new HashMap<>();
    headerMap.put(HeaderParam.X_Consumer_ID.getName(), Arrays.asList("Some consumer ID"));
    headerMap.put(HeaderParam.X_Device_ID.getName(), Arrays.asList("Some device ID"));
    headerMap.put(
        HeaderParam.X_Authenticated_Userid.getName(), Arrays.asList("Some authenticated user ID"));
    headerMap.put(JsonKey.MESSAGE_ID, Arrays.asList("Some message ID"));
    headerMap.put(HeaderParam.X_APP_ID.getName(), Arrays.asList("Some app Id"));
  }

  @After
  public void tearDown() throws Exception {
    headerMap.clear();
  }

  @Test
  public void testMigrationUserSuccess() {
    Result result =
        TestUtil.performTest("/v1/user/migrate", "POST", getSuccessMigrationReq(), application);
    assertEquals(
        ResponseCode.success.getErrorCode().toLowerCase(), TestUtil.getResponseCode(result));
  }

  @Test
  public void testMigrationUserFailure() {
    Result result =
        TestUtil.performTest(
            "/v1/user/migrate", "POST", getFailureMigrationReq(JsonKey.CHANNEL), application);
    assertEquals(
        ResponseCode.mandatoryParamsMissing.getErrorCode(), TestUtil.getResponseCode(result));
  }

  @Test
  public void tesPrivatetMigrationUserSuccess() {
    Result result =
        TestUtil.performTest(
            "/private/user/v1/migrate", "PATCH", getSuccessMigrationReq(), application);
    assertEquals(
        ResponseCode.success.getErrorCode().toLowerCase(), TestUtil.getResponseCode(result));
  }

  private Map<String, Object> getSuccessMigrationReq() {
    Map<String, Object> request = new HashMap<>();
    Map<String, String> reqMap = new HashMap<>();
    reqMap.put(JsonKey.ACTION, "accept");
    reqMap.put(JsonKey.USER_ID, "userId");
    reqMap.put(JsonKey.USER_EXT_ID, "abc_ext_id");
    reqMap.put(JsonKey.CHANNEL, "TN");
    request.put(JsonKey.REQUEST, reqMap);
    return request;
  }

  private Map<String, Object> getFailureMigrationReq(String param) {
    Map<String, Object> request = new HashMap<>();
    Map<String, String> reqMap = new HashMap<>();
    reqMap.put(JsonKey.ACTION, "accept");
    reqMap.put(JsonKey.USER_ID, "userId");
    reqMap.put(JsonKey.USER_EXT_ID, "abc_ext_id");
    reqMap.put(JsonKey.CHANNEL, "TN");
    reqMap.remove(param);
    request.put(JsonKey.REQUEST, reqMap);
    return request;
  }
}
