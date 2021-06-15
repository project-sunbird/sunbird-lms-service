package org.sunbird.learner.actors.bulkupload.dao.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.sunbird.cassandra.CassandraOperation;
import org.sunbird.common.CassandraUtil;
import org.sunbird.common.models.response.Response;
import org.sunbird.common.models.util.JsonKey;
import org.sunbird.common.models.util.LoggerUtil;
import org.sunbird.common.request.RequestContext;
import org.sunbird.helper.ServiceFactory;
import org.sunbird.learner.actors.bulkupload.dao.BulkUploadProcessTaskDao;
import org.sunbird.learner.actors.bulkupload.model.BulkUploadProcessTask;

/**
 * Data access implementation for BulkUploadProcessTask entity.
 *
 * @author arvind
 */
public class BulkUploadProcessTaskDaoImpl implements BulkUploadProcessTaskDao {
  private static LoggerUtil logger = new LoggerUtil(BulkUploadProcessTaskDaoImpl.class);

  private CassandraOperation cassandraOperation = ServiceFactory.getInstance();
  private ObjectMapper mapper = new ObjectMapper();
  private static final String KEYSPACE_NAME = "sunbird";
  private static final String TABLE_NAME = "bulk_upload_process_task";

  public static BulkUploadProcessTaskDao bulkUploadProcessTaskDao = null;

  public static synchronized BulkUploadProcessTaskDao getInstance() {
    if (bulkUploadProcessTaskDao == null)
      bulkUploadProcessTaskDao = new BulkUploadProcessTaskDaoImpl();
    return bulkUploadProcessTaskDao;
  }

  @Override
  public String create(BulkUploadProcessTask bulkUploadProcessTask, RequestContext context) {
    Map<String, Object> map = mapper.convertValue(bulkUploadProcessTask, Map.class);
    cassandraOperation.insertRecord(KEYSPACE_NAME, TABLE_NAME, map, context);
    return JsonKey.SUCCESS;
  }

  @Override
  public String update(BulkUploadProcessTask bulkUploadProcessTask, RequestContext context) {
    Map<String, Map<String, Object>> map = CassandraUtil.batchUpdateQuery(bulkUploadProcessTask);
    Response response =
        cassandraOperation.updateRecord(
            KEYSPACE_NAME,
            TABLE_NAME,
            map.get(JsonKey.NON_PRIMARY_KEY),
            map.get(JsonKey.PRIMARY_KEY),
            context);
    return (String) response.get(JsonKey.RESPONSE);
  }

  @Override
  public BulkUploadProcessTask read(
      BulkUploadProcessTask bulkUploadProcessTask, RequestContext context) {
    Response response =
        cassandraOperation.getRecordById(
            KEYSPACE_NAME, TABLE_NAME, CassandraUtil.getPrimaryKey(bulkUploadProcessTask), context);
    List<Map<String, Object>> list = (List<Map<String, Object>>) response.get(JsonKey.RESPONSE);
    if (CollectionUtils.isEmpty(list)) {
      return null;
    }
    try {
      String jsonString = mapper.writeValueAsString((Map<String, Object>) list.get(0));
      return mapper.readValue(jsonString, BulkUploadProcessTask.class);
    } catch (IOException e) {
      logger.error(context, e.getMessage(), e);
    }
    return null;
  }

  @Override
  public List<BulkUploadProcessTask> readByPrimaryKeys(
      Map<String, Object> id, RequestContext context) {
    Response response = cassandraOperation.getRecordById(KEYSPACE_NAME, TABLE_NAME, id, context);
    List<Map<String, Object>> list = (List<Map<String, Object>>) response.get(JsonKey.RESPONSE);
    if (CollectionUtils.isEmpty(list)) {
      return null;
    }
    TypeReference<List<BulkUploadProcessTask>> mapType =
        new TypeReference<List<BulkUploadProcessTask>>() {};
    List<BulkUploadProcessTask> taskList = mapper.convertValue(list, mapType);
    return taskList;
  }

  @Override
  public String insertBatchRecord(List<BulkUploadProcessTask> records, RequestContext context) {
    TypeReference<List<Map<String, Object>>> tRef =
        new TypeReference<List<Map<String, Object>>>() {};
    List<Map<String, Object>> list = mapper.convertValue(records, tRef);
    Response response = cassandraOperation.batchInsert(KEYSPACE_NAME, TABLE_NAME, list, context);
    return (String) response.get(JsonKey.RESPONSE);
  }

  @Override
  public String updateBatchRecord(List<BulkUploadProcessTask> records, RequestContext context) {
    List<Map<String, Map<String, Object>>> list = new ArrayList<>();
    for (BulkUploadProcessTask bulkUploadProcessTask : records) {
      list.add(CassandraUtil.batchUpdateQuery(bulkUploadProcessTask));
    }
    Response response = cassandraOperation.batchUpdate(KEYSPACE_NAME, TABLE_NAME, list, context);
    return (String) response.get(JsonKey.RESPONSE);
  }
}
