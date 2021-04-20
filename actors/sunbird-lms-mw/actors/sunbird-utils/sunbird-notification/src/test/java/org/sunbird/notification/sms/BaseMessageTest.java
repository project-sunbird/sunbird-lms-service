package org.sunbird.notification.sms;

import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;

import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.sunbird.notification.sms.providerimpl.Msg91SmsProvider;
import org.sunbird.notification.utils.PropertiesCache;
import org.sunbird.notification.utils.SMSFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(PowerMockRunner.class)
@PowerMockIgnore({
  "javax.management.*",
  "javax.net.ssl.*",
  "javax.security.*",
  "jdk.internal.reflect.*"
})
@PrepareForTest({
  HttpClients.class,
  CloseableHttpClient.class,
  PropertiesCache.class,
  Msg91SmsProvider.class,
  SMSFactory.class
})
public abstract class BaseMessageTest {

  @Before
  public void initMockRules() throws Exception {
    CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
    CloseableHttpResponse httpResp = mock(CloseableHttpResponse.class);
    PropertiesCache propertiesCache = mock(PropertiesCache.class);
    StatusLine statusLine = mock(StatusLine.class);
    PowerMockito.mockStatic(HttpClients.class);
    try {
      doReturn(httpClient).when(HttpClients.class, "createDefault");
      doReturn(httpResp).when(httpClient).execute(Mockito.any(HttpPost.class));
      doReturn(statusLine).when(httpResp).getStatusLine();
      doReturn(200).when(statusLine).getStatusCode();
    } catch (Exception e) {
      Assert.fail("Exception while mocking static " + e.getLocalizedMessage());
    }

    try {
      // PowerMockito.when(PropertiesCache.getInstance()).thenReturn(propertiesCache);
      PowerMockito.when(propertiesCache.getProperty(Mockito.anyString())).thenReturn("anyString");
    } catch (Exception e) {
      Assert.fail("Exception while mocking static " + e.getLocalizedMessage());
    }
    // doReturn("randomString").when(PropertiesCache.getInstance().getProperty(Mockito.anyString()));*/

  }
}
