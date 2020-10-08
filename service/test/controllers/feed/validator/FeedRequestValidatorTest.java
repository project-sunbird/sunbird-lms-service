package controllers.feed.validator;

import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import org.sunbird.common.exception.ProjectCommonException;
import org.sunbird.common.models.util.ActorOperations;
import org.sunbird.common.models.util.JsonKey;
import org.sunbird.common.request.Request;

public class FeedRequestValidatorTest {

  @Test
  public void userIdValidationTestSuccess() {
    Assert.assertTrue(FeedRequestValidator.userIdValidation("123-456-789", "123-456-789"));
  }

  @Test(expected = ProjectCommonException.class)
  public void userIdValidationTestFailure() {
    Assert.assertTrue(FeedRequestValidator.userIdValidation("123-456-7890", "123-456-789"));
  }

  @Test(expected = ProjectCommonException.class)
  public void validateFeedRequestTestFailure() {
    Request reqObj = new Request();
    Map<String, Object> requestMap = new HashMap<>();
    Map<String, Object> dataMap = new HashMap<>();
    reqObj.setOperation(ActorOperations.CREATE_USER_FEED.getValue());
    requestMap.put(JsonKey.USER_ID, "someUserId");
    requestMap.put(JsonKey.CATEGORY, "someCategory");
    requestMap.put(JsonKey.DATA, dataMap);
    reqObj.setRequest(requestMap);
    FeedRequestValidator.validateFeedRequest(reqObj);
  }
}
