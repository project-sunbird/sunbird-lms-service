package org.sunbird.learner.util;

import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.sunbird.common.models.util.JsonKey;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({
  "javax.management.*",
  "javax.net.ssl.*",
  "javax.security.*",
  "jdk.internal.reflect.*"
})
public class UserFlagUtilTest {

  @Test
  public void testGetFlagValue() {
    Assert.assertEquals(
        2, UserFlagUtil.getFlagValue(UserFlagEnum.EMAIL_VERIFIED.getUserFlagType(), true));
    Assert.assertEquals(
        1, UserFlagUtil.getFlagValue(UserFlagEnum.PHONE_VERIFIED.getUserFlagType(), true));
    Assert.assertEquals(
        4, UserFlagUtil.getFlagValue(UserFlagEnum.STATE_VALIDATED.getUserFlagType(), true));
  }

  @Test
  public void testAssignUserFlagValues() {
    Map<String, Boolean> userFlagMap = UserFlagUtil.assignUserFlagValues(1);
    Assert.assertEquals(true, userFlagMap.get(JsonKey.PHONE_VERIFIED));
    userFlagMap = UserFlagUtil.assignUserFlagValues(2);
    Assert.assertEquals(true, userFlagMap.get(JsonKey.EMAIL_VERIFIED));
    userFlagMap = UserFlagUtil.assignUserFlagValues(4);
    Assert.assertEquals(true, userFlagMap.get(JsonKey.STATE_VALIDATED));
  }
}
