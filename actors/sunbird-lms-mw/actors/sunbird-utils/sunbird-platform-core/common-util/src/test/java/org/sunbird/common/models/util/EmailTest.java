/** */
package org.sunbird.common.models.util;

import javax.mail.PasswordAuthentication;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.jvnet.mock_javamail.Mailbox;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.sunbird.common.models.util.mail.GMailAuthenticator;
import org.sunbird.common.models.util.mail.SendMail;

/** @author Manzarul */
@PowerMockIgnore({
  "javax.management.*",
  "javax.net.ssl.*",
  "javax.security.*",
  "jdk.internal.reflect.*"
})
public class EmailTest {

  private static GMailAuthenticator authenticator = null;

  @BeforeClass
  public static void setUp() {
    authenticator = new GMailAuthenticator("test123", "test");
    // clear Mock JavaMail box
    Mailbox.clearAll();
  }

  @Test
  public void createGmailAuthInstance() {
    GMailAuthenticator authenticator = new GMailAuthenticator("test123", "test");
    Assert.assertNotEquals(null, authenticator);
  }

  @Test
  public void passwordAuthTest() {
    PasswordAuthentication authentication = authenticator.getPasswordAuthentication();
    Assert.assertEquals("test", authentication.getPassword());
  }

  @Test
  public void initialiseFromPropertyTest() {
    SendMail.initialiseFromProperty();
    Assert.assertTrue(true);
  }

  @AfterClass
  public static void tearDown() {
    authenticator = null;
    Mailbox.clearAll();
  }
}
