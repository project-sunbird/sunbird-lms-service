package org.sunbird.learner.actors.notificationservice.dao.impl;

import static org.powermock.api.mockito.PowerMockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.sunbird.cassandra.CassandraOperation;
import org.sunbird.cassandraimpl.CassandraOperationImpl;
import org.sunbird.common.CassandraUtil;
import org.sunbird.common.models.response.Response;
import org.sunbird.common.models.util.JsonKey;
import org.sunbird.helper.ServiceFactory;
import org.sunbird.learner.actors.notificationservice.dao.EmailTemplateDao;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
  CassandraOperationImpl.class,
  ServiceFactory.class,
  CassandraOperation.class,
  CassandraUtil.class
})
@PowerMockIgnore({
  "javax.management.*",
  "javax.net.ssl.*",
  "javax.security.*",
  "jdk.internal.reflect.*"
})
public class EmailTemplateDaoImplTest {
  private CassandraOperation cassandraOperation;
  private static final String EMAIL_TEMPLATE = "email_template";
  private static final String DEFAULT_EMAIL_TEMPLATE_NAME = "default";
  private static final String TEMPLATE = "template";
  private EmailTemplateDao emailTemplateDao = new EmailTemplateDaoImpl();

  @Before
  public void setUp() throws Exception {
    emailTemplateDao = new EmailTemplateDaoImpl();
    cassandraOperation = PowerMockito.mock(CassandraOperation.class);
    PowerMockito.mockStatic(ServiceFactory.class);
    when(ServiceFactory.getInstance()).thenReturn(cassandraOperation);
  }

  @Test
  public void testGetTemplateWithBlankTemplateName() {
    List<String> idList = new ArrayList<>();
    idList.add(DEFAULT_EMAIL_TEMPLATE_NAME);
    Response response = new Response();
    List<Map<String, Object>> orgList = new ArrayList<>();
    Map<String, Object> map = new HashMap<>();
    orgList.add(map);
    response.put(JsonKey.RESPONSE, orgList);
    when(cassandraOperation.getRecordsByPrimaryKeys(
            JsonKey.SUNBIRD, EMAIL_TEMPLATE, idList, JsonKey.NAME))
        .thenReturn(response);
    String resp = emailTemplateDao.getTemplate(StringUtils.EMPTY);
    Assert.assertEquals(null, resp);
  }

  @Test
  public void testGetTemplateWithTemplateName() {
    List<String> idList = new ArrayList<>();
    idList.add("Sunbird_email_template");
    Response response = new Response();
    List<Map<String, Object>> orgList = new ArrayList<>();
    Map<String, Object> map = new HashMap<>();
    map.put(TEMPLATE, "Course is Been completed");
    orgList.add(map);
    response.put(JsonKey.RESPONSE, orgList);
    when(cassandraOperation.getRecordsByPrimaryKeys(
            JsonKey.SUNBIRD, EMAIL_TEMPLATE, idList, JsonKey.NAME))
        .thenReturn(response);
    String resp = emailTemplateDao.getTemplate("Sunbird_email_template");
    Assert.assertEquals("Course is Been completed", resp);
  }

  @Test
  public void testGetInstance() {
    Assert.assertEquals(
        emailTemplateDao.getClass().getSimpleName(),
        EmailTemplateDaoImpl.getInstance().getClass().getSimpleName());
  }
}
