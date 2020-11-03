package org.sunbird.user.util;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import akka.dispatch.Futures;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.sunbird.cassandraimpl.CassandraOperationImpl;
import org.sunbird.common.ElasticSearchRestHighImpl;
import org.sunbird.common.exception.ProjectCommonException;
import org.sunbird.common.factory.EsClientFactory;
import org.sunbird.common.inf.ElasticSearchService;
import org.sunbird.common.models.response.Response;
import org.sunbird.common.models.util.JsonKey;
import org.sunbird.common.models.util.ProjectUtil;
import org.sunbird.common.models.util.datasecurity.EncryptionService;
import org.sunbird.common.models.util.datasecurity.impl.DefaultEncryptionServivceImpl;
import org.sunbird.common.request.RequestContext;
import org.sunbird.common.responsecode.ResponseCode;
import org.sunbird.dto.SearchDTO;
import org.sunbird.helper.ServiceFactory;
import org.sunbird.learner.util.DataCacheHandler;
import org.sunbird.learner.util.Util;
import org.sunbird.models.user.User;
import org.sunbird.models.user.UserDeclareEntity;
import scala.concurrent.Future;
import scala.concurrent.Promise;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
  ServiceFactory.class,
  CassandraOperationImpl.class,
  DataCacheHandler.class,
  EsClientFactory.class,
  ElasticSearchRestHighImpl.class,
  DefaultEncryptionServivceImpl.class,
  Util.class,
  EncryptionService.class,
  org.sunbird.common.models.util.datasecurity.impl.ServiceFactory.class,
  UserLookUp.class
})
@PowerMockIgnore({"javax.management.*"})
public class UserUtilTest {
  private static Response response;
  public static CassandraOperationImpl cassandraOperationImpl;
  private static ElasticSearchService esService;

  public void beforeEachTest() {
    PowerMockito.mockStatic(DataCacheHandler.class);
    response = new Response();
    List<Map<String, Object>> userMapList = new ArrayList<Map<String, Object>>();
    Map<String, Object> userMap = new HashMap<String, Object>();
    userMapList.add(userMap);
    Response existResponse = new Response();
    existResponse.put(JsonKey.RESPONSE, userMapList);
    PowerMockito.mockStatic(ServiceFactory.class);
    cassandraOperationImpl = mock(CassandraOperationImpl.class);
    when(ServiceFactory.getInstance()).thenReturn(cassandraOperationImpl);
    Map<String, String> settingMap = new HashMap<String, String>();
    settingMap.put(JsonKey.PHONE_UNIQUE, "True");
    when(ServiceFactory.getInstance()).thenReturn(cassandraOperationImpl);
    Map<String, Object> reqMap = new HashMap<>();
    reqMap.put(JsonKey.TYPE, JsonKey.EMAIL);
    reqMap.put(JsonKey.VALUE, "test@test.com");
    when(cassandraOperationImpl.getRecordsByCompositeKey(
            JsonKey.SUNBIRD, JsonKey.USER_LOOKUP, reqMap, null))
        .thenReturn(response);
    Map<String, Object> reqMapPhone = new HashMap<>();
    reqMap.put(JsonKey.TYPE, JsonKey.PHONE);
    reqMap.put(JsonKey.VALUE, "9663890400");
    when(cassandraOperationImpl.getRecordsByCompositeKey(
            JsonKey.SUNBIRD, JsonKey.USER_LOOKUP, reqMapPhone, null))
        .thenReturn(existResponse);
    when(DataCacheHandler.getConfigSettings()).thenReturn(settingMap);

    PowerMockito.mockStatic(EsClientFactory.class);
    esService = mock(ElasticSearchRestHighImpl.class);
    when(EsClientFactory.getInstance(Mockito.anyString())).thenReturn(esService);
    PowerMockito.when(
            cassandraOperationImpl.deleteRecord(
                Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.any()))
        .thenReturn(response);
    PowerMockito.mockStatic(Util.class);
  }

  @Test
  public void copyAndConvertExternalIdsToLower() {
    beforeEachTest();
    List<Map<String, String>> externalIds = new ArrayList<Map<String, String>>();
    Map<String, String> userExternalIdMap = new HashMap<String, String>();
    userExternalIdMap.put(JsonKey.ID, "test123");
    userExternalIdMap.put(JsonKey.PROVIDER, "State");
    userExternalIdMap.put(JsonKey.ID_TYPE, "UserExtId");
    externalIds.add(userExternalIdMap);
    externalIds = UserUtil.copyAndConvertExternalIdsToLower(externalIds);
    userExternalIdMap = externalIds.get(0);
    assertNotNull(userExternalIdMap.get(JsonKey.ORIGINAL_EXTERNAL_ID));
    assertEquals(userExternalIdMap.get(JsonKey.PROVIDER), "state");
  }

  @Test
  public void setUserDefaultValueForV3() {
    beforeEachTest();
    Map<String, Object> userMap = new HashMap<String, Object>();
    userMap.put(JsonKey.FIRST_NAME, "Test User");
    UserUtil.setUserDefaultValueForV3(userMap, null);
    assertNotNull(userMap.get(JsonKey.USERNAME));
    assertNotNull(userMap.get(JsonKey.STATUS));
    assertNotNull(userMap.get(JsonKey.ROLES));
  }

  @Test
  public void testValidateManagedUserLimit() {
    beforeEachTest();
    Map<String, Object> req = new HashMap<>();
    req.put(JsonKey.MANAGED_BY, "ManagedBy");
    List managedUserList = new ArrayList<User>();
    while (managedUserList.size() <= 31) {
      managedUserList.add(new User());
    }
    when(Util.searchUser(req, null)).thenReturn(managedUserList);
    try {
      UserUtil.validateManagedUserLimit("ManagedBy", null);
    } catch (ProjectCommonException e) {
      assertEquals(e.getResponseCode(), 400);
      assertEquals(e.getMessage(), ResponseCode.managedUserLimitExceeded.getErrorMessage());
    }
  }

  @Test
  public void testTransformExternalIdsToSelfDeclaredRequest() {
    beforeEachTest();
    List<Map<String, String>> externalIds = getExternalIds();
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put(JsonKey.USER_ID, "user1");
    requestMap.put(JsonKey.CREATED_BY, "user1");
    List<UserDeclareEntity> userDeclareEntityList =
        UserUtil.transformExternalIdsToSelfDeclaredRequest(externalIds, requestMap);
    Assert.assertEquals("add", userDeclareEntityList.get(0).getOperation());
  }

  @Test
  public void testfetchOrgIdByProvider() {
    beforeEachTest();
    List<String> providers = new ArrayList<>();
    providers.add("channel004");

    Map<String, Object> orgMap = new HashMap<>();
    List<Map<String, Object>> orgList = new ArrayList<>();

    orgMap.put("id", "1234");
    orgMap.put("channel", "channel004");
    orgList.add(orgMap);
    Map<String, Object> contentMap = new HashMap<>();
    contentMap.put(JsonKey.CONTENT, orgList);

    Promise<Map<String, Object>> promise = Futures.promise();
    promise.success(contentMap);
    Future<Map<String, Object>> test = promise.future();
    SearchDTO searchDTO = new SearchDTO();
    when(Util.createSearchDto(Mockito.anyMap())).thenReturn(searchDTO);
    when(esService.search(searchDTO, ProjectUtil.EsType.organisation.getTypeName(), null))
        .thenReturn(promise.future());
    Map<String, String> providerMap = UserUtil.fetchOrgIdByProvider(providers, null);
    Assert.assertTrue(true);
  }

  @Test
  public void testEncryptDeclareFields() throws Exception {
    beforeEachTest();
    List<Map<String, Object>> declarations = new ArrayList<>();
    Map<String, Object> declareFieldMap = new HashMap<>();
    Map<String, Object> userInfo = new HashMap<>();
    userInfo.put(JsonKey.DECLARED_EMAIL, "a**.com");
    userInfo.put(JsonKey.DECLARED_PHONE, "9****90");
    userInfo.put(JsonKey.DECLARED_DISTRICT, "Karnataka");
    declareFieldMap.put(JsonKey.INFO, userInfo);
    declarations.add(declareFieldMap);
    Map<String, Object> dbRecords = new HashMap<>();
    RequestContext context = new RequestContext();
    try {
      UserUtil.encryptDeclarationFields(declarations, dbRecords, context);
    } catch (Exception ex) {

    }
    Assert.assertTrue(true);
  }

  @Test
  public void testCreateSelfDeclaredObject() {
    Map<String, Object> declareFieldMap = new HashMap<>();
    declareFieldMap.put(JsonKey.USER_ID, "1234");
    declareFieldMap.put(JsonKey.ORG_ID, "012345678");
    declareFieldMap.put(JsonKey.PERSONA, "teacher");
    declareFieldMap.put(JsonKey.OPERATION, "add");
    Map<String, Object> userInfo = new HashMap<>();
    userInfo.put(JsonKey.DECLARED_EMAIL, "a**.com");
    userInfo.put(JsonKey.DECLARED_PHONE, "9****90");
    userInfo.put(JsonKey.DECLARED_DISTRICT, "Karnataka");
    declareFieldMap.put(JsonKey.INFO, userInfo);

    UserDeclareEntity userDeclareEntity =
        UserUtil.createUserDeclaredObject(declareFieldMap, "01245444444");
    Assert.assertEquals("SUBMITTED", userDeclareEntity.getStatus());
  }

  private List<Map<String, String>> getExternalIds() {
    beforeEachTest();
    List<Map<String, String>> externalIds = new ArrayList<>();
    Map<String, String> extId1 = new HashMap<>();
    extId1.put(JsonKey.ORIGINAL_ID_TYPE, JsonKey.DECLARED_EMAIL);
    extId1.put(JsonKey.ORIGINAL_PROVIDER, "0123");
    extId1.put(JsonKey.ORIGINAL_EXTERNAL_ID, "abc@diksha.com");
    extId1.put(JsonKey.OPERATION, "add");
    Map<String, String> extId2 = new HashMap<>();
    extId2.put(JsonKey.ORIGINAL_ID_TYPE, JsonKey.DECLARED_EMAIL);
    extId2.put(JsonKey.ORIGINAL_PROVIDER, "123");
    extId2.put(JsonKey.ORIGINAL_EXTERNAL_ID, "abc@diksha.com");
    extId2.put(JsonKey.OPERATION, "remove");

    externalIds.add(extId1);
    externalIds.add(extId2);

    return externalIds;
  }

  @Test
  public void testgetUserOrgDetailsDeActive() {
    beforeEachTest();
    Response response1 = new Response();
    List<Map<String, Object>> responseList = new ArrayList<>();
    Map<String, Object> result = new HashMap<>();
    result.put(JsonKey.IS_DELETED, true);
    result.put(JsonKey.USER_ID, "123-456-789");
    responseList.add(result);
    response1.getResult().put(JsonKey.RESPONSE, responseList);
    List<String> ids = new ArrayList<>();
    ids.add("123-456-789");
    when(ServiceFactory.getInstance()).thenReturn(cassandraOperationImpl);
    when(cassandraOperationImpl.getRecordsByPrimaryKeys(
            JsonKey.SUNBIRD, "user_organisation", ids, JsonKey.USER_ID, null))
        .thenReturn(response1);
    List<Map<String, Object>> res = UserUtil.getActiveUserOrgDetails("123-456-789", null);
    Assert.assertNotNull(res);
  }

  @Test
  public void testupdateExternalIdsWithProvider() {
    beforeEachTest();
    List<String> providers = new ArrayList<>();
    providers.add("channel004");

    Map<String, Object> orgMap = new HashMap<>();
    List<Map<String, Object>> orgList = new ArrayList<>();

    orgMap.put("id", "1234");
    orgMap.put("channel", "channel004");
    orgList.add(orgMap);
    Map<String, Object> contentMap = new HashMap<>();
    contentMap.put(JsonKey.CONTENT, orgList);

    Promise<Map<String, Object>> promise = Futures.promise();
    promise.success(contentMap);
    Future<Map<String, Object>> test = promise.future();
    SearchDTO searchDTO = new SearchDTO();
    when(Util.createSearchDto(Mockito.anyMap())).thenReturn(searchDTO);
    when(esService.search(searchDTO, ProjectUtil.EsType.organisation.getTypeName(), null))
        .thenReturn(promise.future());
    Map<String, String> externalIds = new HashMap<>();
    externalIds.put(JsonKey.PROVIDER, "1234");
    externalIds.put(JsonKey.USER_ID, "w131-2323-323-232-3232");
    List<Map<String, String>> externalIdList = new ArrayList<>();
    externalIdList.add(externalIds);
    UserUtil.updateExternalIdsWithProvider(externalIdList, null);
    Assert.assertTrue(true);
  }

  @Test
  public void testupdateExternalIdsProviderWithOrgId() {
    beforeEachTest();
    List<String> providers = new ArrayList<>();
    providers.add("channel004");

    Map<String, Object> orgMap = new HashMap<>();
    List<Map<String, Object>> orgList = new ArrayList<>();

    orgMap.put("id", "1234");
    orgMap.put("channel", "channel004");
    orgList.add(orgMap);
    Map<String, Object> contentMap = new HashMap<>();
    contentMap.put(JsonKey.CONTENT, orgList);

    Promise<Map<String, Object>> promise = Futures.promise();
    promise.success(contentMap);
    Future<Map<String, Object>> test = promise.future();
    SearchDTO searchDTO = new SearchDTO();
    when(Util.createSearchDto(Mockito.anyMap())).thenReturn(searchDTO);
    when(esService.search(searchDTO, ProjectUtil.EsType.organisation.getTypeName(), null))
        .thenReturn(promise.future());
    Map<String, String> externalIds = new HashMap<>();
    externalIds.put(JsonKey.PROVIDER, "channel1004");
    externalIds.put(JsonKey.USER_ID, "w131-2323-323-232-3232");
    List<Map<String, String>> externalIdList = new ArrayList<>();
    externalIdList.add(externalIds);
    Map<String, Object> userMap = new HashMap<>();
    userMap.put(JsonKey.EXTERNAL_IDS, externalIdList);
    try {
      UserUtil.updateExternalIdsProviderWithOrgId(userMap, null);
    } catch (Exception ex) {
      Assert.assertTrue(true);
      Assert.assertEquals(
          "Invalid value provider for parameter channel1004. Please provide a valid value.",
          ex.getMessage());
    }
  }

  @Test
  public void testUpdateExternalIdsProviderWithOrgId() {
    beforeEachTest();
    List<Map<String, String>> externalIds = new ArrayList<>();
    Map<String, String> extId1 = new HashMap<>();
    extId1.put(JsonKey.ORIGINAL_ID_TYPE, JsonKey.DECLARED_EMAIL);
    extId1.put(JsonKey.ORIGINAL_PROVIDER, "0123");
    extId1.put(JsonKey.ORIGINAL_EXTERNAL_ID, "abc@diksha.com");
    extId1.put(JsonKey.ID_TYPE, JsonKey.DECLARED_EMAIL);
    extId1.put(JsonKey.PROVIDER, "0123");
    extId1.put(JsonKey.EXTERNAL_ID, "abc@diksha.com");
    extId1.put(JsonKey.OPERATION, "add");
    externalIds.add(extId1);

    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put(JsonKey.USER_ID, "user1");
    requestMap.put(JsonKey.CHANNEL, "0123");
    requestMap.put(JsonKey.ROOT_ORG_ID, "012345678921");
    requestMap.put(JsonKey.EXTERNAL_IDS, externalIds);
    UserUtil.updateExternalIdsProviderWithOrgId(requestMap, null);
    Assert.assertTrue(true);
  }

  @Test
  public void testUpdateExternalIds2ProviderWithOrgId() {
    beforeEachTest();
    List<Map<String, String>> externalIds = new ArrayList<>();
    Map<String, String> extId1 = new HashMap<>();
    extId1.put(JsonKey.ORIGINAL_ID_TYPE, JsonKey.DECLARED_EMAIL);
    extId1.put(JsonKey.ORIGINAL_PROVIDER, "0123");
    extId1.put(JsonKey.ORIGINAL_EXTERNAL_ID, "abc@diksha.com");
    extId1.put(JsonKey.ID_TYPE, JsonKey.DECLARED_EMAIL);
    extId1.put(JsonKey.PROVIDER, "0123");
    extId1.put(JsonKey.EXTERNAL_ID, "abc@diksha.com");
    extId1.put(JsonKey.OPERATION, "add");
    Map<String, String> extId2 = new HashMap<>();
    extId2.put(JsonKey.ORIGINAL_ID_TYPE, JsonKey.DECLARED_EMAIL);
    extId2.put(JsonKey.ORIGINAL_PROVIDER, "01234");
    extId2.put(JsonKey.ORIGINAL_EXTERNAL_ID, "abc@diksha.com");
    extId2.put(JsonKey.ID_TYPE, JsonKey.DECLARED_EMAIL);
    extId2.put(JsonKey.PROVIDER, "01234");
    extId2.put(JsonKey.EXTERNAL_ID, "abc@diksha.com");
    extId2.put(JsonKey.OPERATION, "add");
    externalIds.add(extId1);
    externalIds.add(extId2);

    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put(JsonKey.USER_ID, "user1");
    requestMap.put(JsonKey.CHANNEL, "0123");
    requestMap.put(JsonKey.ROOT_ORG_ID, "012345678921");
    requestMap.put(JsonKey.EXTERNAL_IDS, externalIds);
    try {
      UserUtil.updateExternalIdsProviderWithOrgId(requestMap, null);
    } catch (Exception ex) {
      Assert.assertTrue(true);
      Assert.assertNotNull(ex);
    }
  }

  @Test
  public void testUpdateExternalIds3ProviderWithOrgId() {
    beforeEachTest();
    List<Map<String, String>> externalIds = new ArrayList<>();
    Map<String, String> extId2 = new HashMap<>();
    extId2.put(JsonKey.ORIGINAL_ID_TYPE, JsonKey.DECLARED_EMAIL);
    extId2.put(JsonKey.ORIGINAL_PROVIDER, "01234");
    extId2.put(JsonKey.ORIGINAL_EXTERNAL_ID, "abc@diksha.com");
    extId2.put(JsonKey.ID_TYPE, JsonKey.DECLARED_EMAIL);
    extId2.put(JsonKey.PROVIDER, "01234");
    extId2.put(JsonKey.EXTERNAL_ID, "abc@diksha.com");
    extId2.put(JsonKey.OPERATION, "add");
    externalIds.add(extId2);

    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put(JsonKey.USER_ID, "user1");
    requestMap.put(JsonKey.CHANNEL, "0123");
    requestMap.put(JsonKey.ROOT_ORG_ID, "012345678921");
    requestMap.put(JsonKey.EXTERNAL_IDS, externalIds);
    try {
      UserUtil.updateExternalIdsProviderWithOrgId(requestMap, null);
    } catch (Exception ex) {
      Assert.assertTrue(true);
      Assert.assertEquals(
          "Invalid value provider for parameter 01234. Please provide a valid value.",
          ex.getMessage());
    }
  }

  @Test
  public void testAddMaskEmailAndMaskPhone() {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put(JsonKey.PHONE, "9999999999");
    requestMap.put(JsonKey.EMAIL, "sunbird@example.com");
    UserUtil.addMaskEmailAndMaskPhone(requestMap);
    Assert.assertTrue(true);
  }

  @Test
  public void testRemoveEntryFromUserLookUp() {
    beforeEachTest();
    Map<String, Object> mergeeMap = new HashMap<>();
    mergeeMap.put(JsonKey.EMAIL, "someEmail");
    mergeeMap.put(JsonKey.PHONE, "somePhone");
    mergeeMap.put(JsonKey.USERNAME, "someUsername");
    List<String> userLookUpIdentifiers =
        Stream.of(JsonKey.EMAIL, JsonKey.PHONE, JsonKey.USERNAME).collect(Collectors.toList());
    UserUtil.removeEntryFromUserLookUp(mergeeMap, userLookUpIdentifiers, new RequestContext());
    Assert.assertTrue(true);
  }
}
