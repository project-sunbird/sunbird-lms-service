package org.sunbird.common.models.util;

import java.util.Arrays;
import java.util.List;

/**
 * This class will contains all the key related to request and response.
 *
 * @author Manzarul
 */
public final class JsonKey {
  public static final String ANONYMOUS = "Anonymous";
  public static final String UNAUTHORIZED = "Unauthorized";
  public static final String MW_SYSTEM_HOST = "sunbird_mw_system_host";
  public static final String MW_SYSTEM_PORT = "sunbird_mw_system_port";
  public static final String ACCOUNT_KEY = "sunbird_account_key";
  public static final String ACCOUNT_NAME = "sunbird_account_name";
  public static final String DOWNLOAD_LINK_EXPIRY_TIMEOUT = "download_link_expiry_timeout";
  public static final String ACTION_GROUP = "action_group";
  public static final String ACTION_GROUPS = "actionGroups";
  public static final String ACTION_NAME = "actionName";
  public static final String ACTION_URL = "actionUrl";
  public static final String ACTIONS = "actions";
  public static final String ACTIVE = "active";
  public static final String ACTOR_ID = "actorId";
  public static final String ACTOR_SERVICE = "Actor service";
  public static final String ACTOR_TYPE = "actorType";
  public static final String ADDED_BY = "addedBy";
  public static final String ADDITIONAL_INFO = "ADDITIONAL_INFO";
  public static final String ADDRESS = "address";
  public static final String ALLOWED_LOGIN = "allowedLogin";
  public static final String API_ACCESS = "api_access";
  public static final String API_ACTOR_PROVIDER = "api_actor_provider";
  public static final String API_CALL = "API_CALL";
  public static final String APPROVED_BY = "approvedBy";
  public static final String APPROVED_DATE = "approvedDate";
  public static final String ATTEMPTED_COUNT = "attemptedCount";
  public static final String AUTH_WITH_MASTER_KEY = "authWithMasterKey";
  public static final String AUTHORIZATION = "Authorization";
  public static final String BACKGROUND_ACTOR_PROVIDER = "background_actor_provider";
  public static final String BEARER = "Bearer ";
  public static final String BODY = "body";
  public static final String BULK_OP_DB = "BulkOpDb";
  public static final String BULK_UPLOAD_ORG_DATA_SIZE = "bulk_upload_org_data_size";
  public static final String BULK_UPLOAD_USER_DATA_SIZE = "sunbird_user_bulk_upload_size";
  public static final String BULK_USER_UPLOAD = "bulkUserUpload";
  public static final String CASSANDRA_SERVICE = "Cassandra service";
  public static final String CATEGORIES = "categories";
  public static final String CHANNEL = "channel";
  public static final String CHANNEL_REG_STATUS = "channelRegStatus";
  public static final String CHANNEL_REG_STATUS_ID = "003";
  public static final String CHANNELS = "channels";
  public static final String CHECKS = "checks";
  public static final String CLASS = "class";
  public static final String CODE = "code";
  public static final String COMPLETENESS = "completeness";
  public static final String CONSUMER = "consumer";
  public static final String CONTACT_DETAILS = "contactDetail";
  public static final String CONTAINER = "container";
  public static final String CONTENT = "content";
  public static final String CONTENT_ID = "contentId";
  public static final String CONTEXT = "context";
  public static final String CORRELATED_OBJECTS = "correlatedObjects";
  public static final String COUNT = "count";
  public static final String COUNTRY_CODE = "countryCode";
  public static final String COURSE_ID = "courseId";
  public static final String COURSE_NAME = "courseName";
  public static final String CREATE = "create";
  public static final String CREATED_BY = "createdBy";
  public static final String CREATED_DATE = "createdDate";
  public static final String CURRENT_STATE = "CURRENT_STATE";
  public static final String DATA = "data";
  public static final String KEY = "key";
  public static final String KEYS = "keys";
  public static final String DATE_HISTOGRAM = "DATE_HISTOGRAM";
  public static final String DEFAULT_CONSUMER_ID = "internal";
  public static final String DEFAULT_ROOT_ORG_ID = "ORG_001";
  public static final String DELETE = "delete";
  public static final String DESCRIPTION = "description";
  public static final String DOB = "dob";
  public static final String EDUCATION = "education";
  public static final String EKS = "eks";
  public static final String ANALYTICS_API_BASE_URL = "sunbird_analytics_api_base_url";
  public static final String EKSTEP_AUTHORIZATION = "ekstep_authorization";
  public static final String EKSTEP_BASE_URL = "ekstep_api_base_url";
  public static final String EKSTEP_CHANNEL_REG_API_URL = "ekstep.channel.reg.api.url";
  public static final String EKSTEP_CHANNEL_UPDATE_API_URL = "ekstep.channel.update.api.url";
  public static final String EKSTEP_GET_CHANNEL_LIST = "ekstep.channel.list.api.url";
  public static final String EKSTEP_TAG_API_URL = "ekstep.tag.api.url";
  public static final String EMAIL = "email";
  public static final String EMAIL_REQUEST = "emailReq";
  public static final String EMAIL_SERVER_FROM = "sunbird_mail_server_from_email";
  public static final String EMAIL_SERVER_HOST = "sunbird_mail_server_host";
  public static final String EMAIL_SERVER_PASSWORD = "sunbird_mail_server_password";
  public static final String EMAIL_SERVER_PORT = "sunbird_mail_server_port";
  public static final String EMAIL_SERVER_USERNAME = "sunbird_mail_server_username";
  public static final String EMAIL_TEMPLATE_TYPE = "emailTemplateType";
  public static final String EMAIL_UNIQUE = "emailUnique";
  public static final String EMAIL_VERIFIED = "emailVerified";
  public static final String EMBEDDED = "embedded";
  public static final String ENC_EMAIL = "encEmail";
  public static final String ENC_PHONE = "encPhone";
  public static final String ENCRYPTION_KEY = "sunbird_encryption_key";
  public static final String END_DATE = "endDate";
  public static final String ENV = "env";
  public static final String ERR_TYPE = "errtype";
  public static final String ERROR = "err";
  public static final String ERROR_MSG = "err_msg";
  public static final String ERRORMSG = "errmsg";
  public static final String ES_SERVICE = "Elastic search service";
  public static final String EXISTS = "exists";
  public static final String EXTERNAL_ID = "externalId";
  public static final String FACETS = "facets";
  public static final String FAILED = "FAILED";
  public static final String FAILURE_RESULT = "failureResult";
  public static final String FIELD = "field";
  public static final String FIELDS = "fields";
  public static final String FILE = "file";
  public static final String FILE_NAME = "fileName";
  public static final String FILTERS = "filters";
  public static final String FIRST_NAME = "firstName";
  public static final String FRAMEWORK = "framework";
  public static final String FROM_EMAIL = "fromEmail";
  public static final String GROUP_QUERY = "groupQuery";
  public static final String HASHTAGID = "hashTagId";
  public static final String HEADER = "header";
  public static final String Healthy = "healthy";
  public static final String HOME_URL = "homeUrl";
  public static final String ID = "id";
  public static final String IDENTIFIER = "identifier";
  public static final String INACTIVE = "inactive";
  public static final String INFO = "info";
  public static final String INSERT = "insert";
  public static final String IS_APPROVED = "isApproved";
  public static final String IS_AUTH_REQ = "isAuthReq";
  public static final String IS_DELETED = "isDeleted";
  public static final String JOB_PROFILE = "jobProfile";
  public static final String LANGUAGE = "language";
  public static final String LAST_LOGIN_TIME = "lastLoginTime";
  public static final String LAST_NAME = "lastName";
  public static final String LEARNER_SERVICE = "Learner service";
  public static final String LEVEL = "level";
  public static final String LIMIT = "limit";
  public static final String LIST = "List";
  public static final String LOC_ID = "locationId";
  public static final String LOCATION = "location";
  public static final String LOCATION_NAME = "locationName";
  public static final String LOCATION_ID = "locationId";
  public static final String LOCATION_IDS = "locationIds";
  public static final String LOCATIONS = "locations";
  public static final String LOG_LEVEL = "logLevel";
  public static final String LOG_TYPE = "logType";
  public static final String LOGIN_ID = "loginId";
  public static final String MAIL_NOTE = "mail_note";
  public static final String MANDATORY_FIELDS = "mandatoryFields";
  public static final String MAP = "map";
  public static final String MASKED_EMAIL = "maskedEmail";
  public static final String MASKED_PHONE = "maskedPhone";
  public static final String MESSAGE = "message";
  public static final String MESSAGE_ID = "X-msgId";
  public static final String METHOD = "method";
  public static final String MISSING_FIELDS = "missingFields";
  public static final String MOBILE = "mobile";
  public static final String NAME = "name";
  public static final String NOT_EXISTS = "not_exists";
  public static final String NOTE = "note";
  public static final String NOTE_ID = "noteId";
  public static final String NOTIFICATION = "notification";
  public static final String OBJECT_IDS = "objectIds";
  public static final String OBJECT_TYPE = "objectType";
  public static final String OFFSET = "offset";
  public static final String ON = "ON";
  public static final String ONBOARDING_WELCOME_MAIL_BODY = "onboarding_welcome_mail_body";
  public static final String OPERATION = "operation";
  public static final String OPERATION_FOR = "operationFor";
  public static final String OPERATION_TYPE = "operationType";
  public static final String ORDER = "order";
  public static final String ORG_EXT_ID_DB = "org_external_identity";
  public static final String ORG_DB = "org_db";
  public static final String ORG_ID = "orgId";
  public static final String ORG_IMAGE_URL = "orgImageUrl";
  public static final String ORG_JOIN_DATE = "orgJoinDate";
  public static final String ORG_NAME = "orgName";
  public static final String ORG_TYPE = "organisationType";
  public static final String ORGANISATION = "organisation";
  public static final String ORGANISATION_ID = "organisationId";
  public static final String ORGANISATION_NAME = "orgName";
  public static final String ORGANISATIONS = "organisations";
  public static final String OTP = "otp";
  public static final String OTP_EMAIL_RESET_PASSWORD_TEMPLATE = "otpEmailResetPasswordTemplate";
  public static final String OTP_PHONE_RESET_PASSWORD_TEMPLATE = "otpPhoneResetPasswordTemplate";
  public static final String VERIFY_PHONE_OTP_TEMPLATE = "verifyPhoneOtpTemplate";
  public static final String PARAMS = "params";
  public static final String PASSWORD = "password";
  public static final String PDATA = "pdata";

  public static final String PHONE = "phone";
  public static final String PHONE_UNIQUE = "phoneUnique";
  public static final String PHONE_VERIFIED = "phoneVerified";
  public static final String POSITION = "position";
  public static final String PREV_STATE = "PREV_STATE";
  public static final String PRIVATE = "private";
  public static final String PROCESS_ID = "processId";
  public static final String PROFILE_CONFIG = "profileConfig";
  public static final String PROCESS_START_TIME = "processStartTime";
  public static final String PDATA_ID = "telemetry_pdata_id";
  public static final String PDATA_PID = "telemetry_pdata_pid";
  public static final String PDATA_VERSION = "telemetry_pdata_ver";
  public static final String PROFILE_SUMMARY = "profileSummary";
  public static final String PROFILE_VISIBILITY = "profileVisibility";
  public static final String PROPS = "props";
  public static final String PROVIDER = "provider";
  public static final String PUBLIC = "public";
  public static final String QUERY = "query";
  public static final String QUERY_FIELDS = "queryFields";
  public static final String RECIPIENT_EMAILS = "recipientEmails";
  public static final String RECIPIENT_USERIDS = "recipientUserIds";
  public static final String REGISTERED_ORG = "registeredOrg";
  public static final String REGISTERED_ORG_ID = "regOrgId";
  public static final String RELATION = "relation";
  public static final String REPLACE_WITH_ASTERISK = "*";
  public static final String REQUEST = "request";
  public static final String REQUEST_TYPE = "requestType";
  public static final String REQUESTED_BY = "requestedBy";
  public static final String RESPONSE = "response";
  public static final String RESULT = "result";
  public static final String RETRY_COUNT = "retryCount";
  public static final String ROLE = "role";
  public static final String ROLE_GROUP = "role_group";
  public static final String ROLES = "roles";
  public static final String ROLLUP = "rollup";
  public static final String ROOT_ORG = "rootOrg";
  public static final String ROOT_ORG_ID = "rootOrgId";
  public static final String SEARCH = "search";
  public static final String SEARCH_TOP_N = "searchTopN";
  public static final String SIZE = "size";
  public static final String SLUG = "slug";
  public static final String SNAPSHOT = "snapshot";
  public static final String SORT = "sort";
  public static final String SORT_BY = "sort_by";
  public static final String SOURCE = "source";
  public static final String SSO_CLIENT_ID = "sso.client.id";
  public static final String SSO_CLIENT_SECRET = "sso.client.secret";
  public static final String SSO_PASSWORD = "sso.password";
  public static final String SSO_POOL_SIZE = "sso.connection.pool.size";
  public static final String SSO_PUBLIC_KEY = "sunbird_sso_publickey";
  public static final String SSO_REALM = "sso.realm";
  public static final String SSO_URL = "sso.url";
  public static final String SSO_USERNAME = "sso.username";
  public static final String STACKTRACE = "stacktrace";
  public static final String START_DATE = "startDate";
  public static final String START_TIME = "startTime";
  public static final String STATE = "state";
  public static final String STATUS = "status";
  public static final String SUBJECT = "subject";
  public static final String SUCCESS = "SUCCESS";
  public static final String SUCCESS_RESULT = "successResult";
  public static final String SUNBIRD = "sunbird";
  public static final String SUNBIRD_ALLOWED_LOGIN = "sunbird_allowed_login";
  public static final String SUNBIRD_API_BASE_URL = "sunbird_api_base_url";
  public static final String SUNBIRD_CASSANDRA_IP = "sunbird_cassandra_host";
  public static final String SUNBIRD_ENCRYPTION = "sunbird_encryption";
  public static final String SUNBIRD_ENV_LOGO_URL = "sunbird_env_logo_url";
  public static final String SUNBIRD_ES_CHANNEL = "es.channel.name";
  public static final String SUNBIRD_ES_CLUSTER = "sunbird_es_cluster";
  public static final String SUNBIRD_ES_IP = "sunbird_es_host";
  public static final String SUNBIRD_ES_PORT = "sunbird_es_port";
  public static final String SUNBIRD_INSTALLATION = "sunbird_installation";
  public static final String SUNBIRD_PG_DB = "sunbird_pg_db";
  public static final String SUNBIRD_PG_HOST = "sunbird_pg_host";
  public static final String SUNBIRD_PG_PASSWORD = "sunbird_pg_password";
  public static final String SUNBIRD_PG_PORT = "sunbird_pg_port";
  public static final String SUNBIRD_PG_USER = "sunbird_pg_user";
  public static final String SUNBIRD_QUARTZ_MODE = "sunbird_quartz_mode";
  public static final String SUNBIRD_SSO_CLIENT_ID = "sunbird_sso_client_id";
  public static final String SUNBIRD_SSO_CLIENT_SECRET = "sunbird_sso_client_secret";
  public static final String SUNBIRD_SSO_PASSWORD = "sunbird_sso_password";
  public static final String SUNBIRD_SSO_RELAM = "sunbird_sso_realm";
  public static final String SUNBIRD_SSO_URL = "sunbird_sso_url";
  public static final String SUNBIRD_SSO_USERNAME = "sunbird_sso_username";
  public static final String SUNBIRD_FRAMEWORK_READ_API = "sunbird_framework_read_api";
  public static final String SUNBIRD_USERNAME_NUM_DIGITS = "sunbird_username_num_digits";
  public static final String SYSTEM = "system";
  public static final String SYSTEM_SETTINGS_DB = "system_settings";
  public static final String TAGS = "tags";
  public static final String TARGET_OBJECT = "targetObject";
  public static final String TELEMETRY_EVENT_TYPE = "telemetryEventType";
  public static final String TEMPORARY_PASSWORD = "tempPassword";
  public static final String TENANT_PREFERENCE = "tenantPreference";
  public static final String TENANT_PREFERENCE_DB = "tenantPreferenceDb";
  public static final String TERM_AND_CONDITION_STATUS = "tcStatus";
  public static final String TERMS = "terms";
  public static final String TITLE = "title";
  public static final String TOKEN = "token";
  public static final String TOPIC = "topic";
  public static final String TOPICS = "topics";
  public static final String TOPN = "topn";
  public static final String TYPE = "type";
  public static final String SUB_TYPE = "subType";
  public static final String TNC_TYPE = "tncType";
  public static final String UNDEFINED_IDENTIFIER = "Undefined column name ";
  public static final String UNKNOWN_IDENTIFIER = "Unknown identifier ";
  public static final String UPDATE = "update";
  public static final String UPDATED_BY = "updatedBy";
  public static final String UPDATED_DATE = "updatedDate";
  public static final String UPLOADED_BY = "uploadedBy";
  public static final String UPLOADED_DATE = "uploadedDate";
  public static final String URL = "url";
  public static final String URL_ACTION = "url_action";
  public static final String URL_ACTION_ID = "url_action_ids";
  public static final String USER = "user";
  public static final String USER_ACTION_ROLE = "user_action_role";
  public static final String USER_DB = "user_db";
  public static final String USER_ID = "userId";
  public static final String USER_IDs = "userIds";
  public static final String USER_NAME = "username";
  public static final String USER_NOTES_DB = "userNotes_db";
  public static final String USER_ORG = "user_organisation";
  public static final String USER_ORG_DB = "user_org_db";
  public static final String USER_ROLES = "user_roles";
  public static final String USERIDS = "userIds";
  public static final String USERNAME = "userName";
  public static final String USER_DECLARATION_DB = "user_declarations";
  public static final String VALUE = "value";
  public static final String VER = "ver";
  public static final String VERSION = "version";
  public static final String WELCOME_MESSAGE = "welcomeMessage";
  public static final String SUNBIRD_HEALTH_CHECK_ENABLE = "sunbird_health_check_enable";
  public static final String HEALTH = "health";
  public static final String SERVICE = "service";
  public static final String SOFT_CONSTRAINTS = "softConstraints";
  public static final String SUNBIRD_AUTHORIZATION = "sunbird_authorization";
  public static final String DURATION = "duration";
  public static final String LOCATION_CODE = "locationCode";
  public static final String UPLOAD_FILE_MAX_SIZE = "file_upload_max_size";
  public static final String PRIMARY_KEY = "PK";
  public static final String NON_PRIMARY_KEY = "NonPK";
  public static final String PARENT_ID = "parentId";
  public static final String CREATED_ON = "createdOn";
  public static final String UPDATED_ON = "updatedOn";
  public static final String LAST_UPDATED_ON = "lastUpdatedOn";
  public static final String LAST_UPDATED_BY = "lastUpdatedBy";
  public static final String SUNBIRD_DEFAULT_CHANNEL = "sunbird_default_channel";
  public static final String CASSANDRA_WRITE_BATCH_SIZE = "cassandra_write_batch_size";
  public static final String ORG_EXTERNAL_ID = "orgExternalId";
  public static final String ORG_PROVIDER = "orgProvider";
  public static final String EXTERNAL_IDS = "externalIds";
  public static final String EXTERNAL_ID_TYPE = "externalIdType";
  public static final String ID_TYPE = "idType";
  public static final String ADD = "add";
  public static final String REMOVE = "remove";
  public static final String EDIT = "edit";
  public static final String DEFAULT_FRAMEWORK = "defaultFramework";
  public static final String EXTERNAL_ID_PROVIDER = "externalIdProvider";
  public static final String SUNBIRD_INSTALLATION_DISPLAY_NAME =
      "sunbird_installation_display_name_for_sms";
  public static final String USR_EXT_IDNT_TABLE = "usr_external_identity";
  public static final String RESPONSE_CODE = "responseCode";
  public static final String OK = "ok";
  public static final String SUNBIRD_DEFAULT_COUNTRY_CODE = "sunbird_default_country_code";
  public static final String ONBOARDING_MAIL_SUBJECT = "onboarding_mail_subject";
  public static final String ONBOARDING_MAIL_MESSAGE = "onboarding_welcome_message";
  public static final String SUNBIRD_DEFAULT_WELCOME_MSG = "sunbird_default_welcome_sms";
  public static final String RECIPIENT_SEARCH_QUERY = "recipientSearchQuery";
  public static final String SUNBIRD_EMAIL_MAX_RECEPIENT_LIMIT =
      "sunbird_email_max_recipients_limit";
  public static final String ORIGINAL_EXTERNAL_ID = "originalExternalId";
  public static final String ORIGINAL_ID_TYPE = "originalIdType";
  public static final String ORIGINAL_PROVIDER = "originalProvider";
  public static final String SUNBIRD_CASSANDRA_CONSISTENCY_LEVEL =
      "sunbird_cassandra_consistency_level";
  public static final String VERSION_2 = "v2";
  public static final String CUSTODIAN_ORG_CHANNEL = "custodianOrgChannel";
  public static final String CUSTODIAN_ORG_ID = "custodianOrgId";
  public static final String APP_ID = "appId";
  public static final String REDIRECT_URI = "redirectUri";
  public static final String SET_PASSWORD_LINK = "set_password_link";
  public static final String VERIFY_EMAIL_LINK = "verify_email_link";
  public static final String LINK = "link";
  public static final String SET_PW_LINK = "setPasswordLink";
  public static final String SUNBIRD_URL_SHORTNER_ENABLE = "sunbird_url_shortner_enable";
  public static final String USER_PROFILE_CONFIG = "userProfileConfig";
  public static final String PUBLIC_FIELDS = "publicFields";
  public static final String PROFILE_USERTYPE = "profileUserType";
  public static final String PROFILE_LOCATION = "profileLocation";
  public static final String PRIVATE_FIELDS = "privateFields";
  public static final String BATCH_START_DATE = "batchStartDate";
  public static final String BATCH_END_DATE = "batchEndDate";
  public static final String BATCH_NAME = "batchName";
  public static final String SUNBIRD_API_REQUEST_LOWER_CASE_FIELDS =
      "sunbird_api_request_lower_case_fields";
  public static final String ATTRIBUTE = "attribute";
  public static final String ERRORS = "errors";
  public static final String ROLE_LIST = "roleList";
  public static final String CALLER_ID = "callerId";
  public static final String USER_TYPE = "userType";
  public static final String USER_SUB_TYPE = "userSubType";
  public static final String MANAGED_BY = "managedBy";
  public static final String MANAGED_FOR = "managedFor";
  public static final String COURSE_BATCH_URL = "courseBatchUrl";
  public static final String SIGNATURE = "signature";
  public static final String TTL = "ttl";
  public static final String FILE_TYPE_CSV = "csv";
  public static final String MODE = "mode";
  public static final String TNC_ACCEPTED_ON = "tncAcceptedOn";
  public static final String TNC_ACCEPTED_VERSION = "tncAcceptedVersion";
  public static final String ALL_TNC_ACCEPTED = "allTncAccepted";
  public static final String TNC_LATEST_VERSION_URL = "tncLatestVersionUrl";
  public static final String PROMPT_TNC = "promptTnC";
  public static final String TNC_LATEST_VERSION = "tncLatestVersion";
  public static final String BULK_ORG_UPLOAD = "bulkOrgUpload";
  public static final String LATEST_VERSION = "latestVersion";
  public static final String TNC_CONFIG = "tncConfig";
  public static final String ACCEPT = "accept";
  public static final String ROOT_ORG_NAME = "rootOrgName";
  public static final String SUNBIRD_OTP_EXPIRATION = "sunbird_otp_expiration";
  public static final String SUNBIRD_OTP_LENGTH = "sunbird_otp_length";
  public static final String OTP_EXPIRATION_IN_MINUTES = "otpExpiryInMinutes";
  public static final String SUNBIRD_RATE_LIMIT_ENABLED = "sunbird_rate_limit_enabled";
  public static final String RATE_LIMIT = "rate_limit";
  public static final String RATE_LIMIT_UNIT = "unit";
  public static final String RATE = "rate";
  public static final String INSTALLATION_NAME = "installationName";
  public static final String LOCATION_CODES = "locationCodes";
  public static final String USER_LOCATIONS = "userLocations";
  public static final String USER_EXTERNAL_ID = "userExternalId";
  public static final String USER_ID_TYPE = "userIdType";
  public static final String USER_PROVIDER = "userProvider";
  public static final String SORTBY = "sortBy";
  public static final String TERM = "term";
  public static final String SUNBIRD_KEYCLOAK_USER_FEDERATION_PROVIDER_ID =
      "sunbird_keycloak_user_federation_provider_id";
  public static final String DEVICE_ID = "did";
  public static final String SUNBIRD_GZIP_ENABLE = "sunbird_gzip_enable";
  public static final String SUNBIRD_SYNC_READ_WAIT_TIME = "sunbird_sync_read_wait_time";
  public static final String SUNBIRD_GZIP_SIZE_THRESHOLD = "sunbird_gzip_size_threshold";
  public static final String ANALYTICS_ACCOUNT_NAME = "sunbird_analytics_blob_account_name";
  public static final String ANALYTICS_ACCOUNT_KEY = "sunbird_analytics_blob_account_key";
  public static final String SIGNUP_TYPE = "signupType";
  public static final String REQUEST_SOURCE = "source";

  public static final String RECIPIENT_PHONES = "recipientPhones";
  public static final String REST = "rest";
  public static final String ES_OR_OPERATION = "$or";
  public static final String PREV_USED_EMAIL = "prevUsedEmail";
  public static final String PREV_USED_PHONE = "prevUsedPhone";
  public static final String MERGE_USER = "mergeUser";
  public static final String MIGRATE_USER = "migrateUser";
  public static final String FROM_ACCOUNT_ID = "fromAccountId";
  public static final String TO_ACCOUNT_ID = "toAccountId";
  public static final String USER_MERGEE_ACCOUNT = "userMergeeAccount";
  public static final String SEARCH_FUZZY = "fuzzy";
  public static final String SUNBIRD_FUZZY_SEARCH_THRESHOLD = "sunbird_fuzzy_search_threshold";
  public static final String USER_CERT = "user_cert";
  public static final String SIGN_KEYS = "signKeys";
  public static final String ENC_KEYS = "encKeys";
  public static final String SUNBIRD_STATE_IMG_URL = "sunbird_state_img_url";
  public static final String SUNBIRD_DIKSHA_IMG_URL = "sunbird_diksha_img_url";
  public static final String SUNBIRD_CERT_COMPLETION_IMG_URL = "sunbird_cert_completion_img_url";
  public static final String stateImgUrl = "stateImgUrl";
  public static final String dikshaImgUrl = "dikshaImgUrl";
  public static final String certificateImgUrl = "certificateImgUrl";
  public static final String SUNBIRD_RESET_PASS_MAIL_SUBJECT = "sunbird_reset_pass_mail_subject";
  public static final String X_AUTHENTICATED_USER_TOKEN = "x-authenticated-user-token";
  public static final String X_SOURCE_USER_TOKEN = "x-source-user-token";
  public static final String SUNBIRD_SUBDOMAIN_KEYCLOAK_BASE_URL =
      "sunbird_subdomain_keycloak_base_url";
  public static final String ACTION = "action";
  public static final String ITERATION = "iteration";
  public static final String TELEMETRY_TARGET_USER_MERGE_TYPE = "MergeUserCoursesAndCert";
  public static final String TELEMETRY_PRODUCER_USER_MERGE_ID = "org.sunbird.platform";
  public static final String TELEMETRY_EDATA_USER_MERGE_ACTION = "merge-user-courses-and-cert";
  public static final String BE_JOB_REQUEST = "BE_JOB_REQUEST";
  public static final String TELEMETRY_ACTOR_USER_MERGE_ID = "Merge User Courses and Cert";
  public static final String RECOVERY_EMAIL = "recoveryEmail";
  public static final String RECOVERY_PHONE = "recoveryPhone";
  public static final String SUPPORTED_COlUMNS = "supportedColumns";
  public static final String INPUT_STATUS = "input status";
  public static final String EXTERNAL_USER_ID = "ext user id";
  public static final String EXTERNAL_ORG_ID = "ext org id";
  public static final String MIGRATION_USER_OBJECT = "MigrationUser";
  public static final String TASK_COUNT = "taskCount";
  public static final String ERROR_VISUALIZATION_THRESHOLD =
      "sunbird_user_upload_error_visualization_threshold";
  public static final String NESTED_KEY_FILTER = "nestedFilters";
  public static final String SHADOW_USER = "shadow_user";
  public static final String USER_EXT_ID = "userExtId";
  public static final String ORG_EXT_ID = "orgExtId";
  public static final String STATE_VALIDATED = "stateValidated";
  public static final String FLAGS_VALUE = "flagsValue";
  public static final String USER_STATUS = "userStatus";
  public static final String CLAIM_STATUS = "claimStatus";
  public static final String CLAIMED_ON = "claimedOn";
  public static final String SUNBIRD_MIGRATE_USER_BODY = "sunbird_migrate_user_body";
  public static final String SMS = "sms";
  public static final String SUNBIRD_ACCOUNT_MERGE_SUBJECT = "sunbird_account_merge_subject";
  public static final String CONTEXT_TELEMETRY = "telemetryContext";
  public static final String MAX_ATTEMPT = "maxAttempt";
  public static final String REMAINING_ATTEMPT = "remainingAttempt";
  public static final String IS_SSO_ROOTORG_ENABLED = "isSSOEnabled";
  public static final String USER_FEED_DB = "user_feed";
  public static final String USER_FEED = "userFeed";
  public static final String FEED_DATA = "data";
  public static final String REJECT = "reject";
  public static final String FEED_ID = "feedId";
  public static final String LICENSE = "license";
  public static final String DEFAULT_LICENSE = "defaultLicense";
  public static final String SUNBIRD_PASS_REGEX = "sunbird_pass_regex";
  public static final String NESTED_EXISTS = "nested_exists";
  public static final String NESTED_NOT_EXISTS = "nested_not_exists";
  public static final String PROSPECT_CHANNELS = "prospectChannels";
  public static final String PROSPECT_CHANNELS_IDS = "prospectChannelsIds";
  public static final String CATEGORY = "category";
  public static final String TEMPLATE_ID = "templateId";
  public static final String TEMPLATE_OPTIONS = "templateOptions";
  public static final String TEMPLATE_ID_VALUE = "resetPasswordWithOtp";
  public static final String VERSION_3 = "v3";
  public static final String VERSION_4 = "v4";
  public static final String WARD_LOGIN_OTP_TEMPLATE_ID = "wardLoginOTP";
  public static final String OTP_PHONE_WARD_LOGIN_TEMPLATE = "verifyPhoneOtpTemplateWard";
  public static final String OTP_EMAIL_WARD_LOGIN_TEMPLATE = "verifyEmailOtpTemplateWard";
  public static final String LIMIT_MANAGED_USER_CREATION = "limit_managed_user_creation";
  public static final String MANAGED_USER_LIMIT = "managed_user_limit";
  public static final String ACCESS_TOKEN_PUBLICKEY_BASEPATH = "accesstoken.publickey.basepath";
  public static final String SHA_256_WITH_RSA = "SHA256withRSA";
  public static final String SUB = "sub";
  public static final String DOT_SEPARATOR = ".";
  public static final List<String> USER_UNAUTH_STATES =
      Arrays.asList(JsonKey.UNAUTHORIZED, JsonKey.ANONYMOUS);
  public static final String EKSTEP_SIGNING_SIGN_PAYLOAD = "ekstep.signing.sign.payload";
  public static final String EKSTEP_SIGNING_SIGN_PAYLOAD_VER = "ekstep.signing.sign.payload.ver";
  public static final String ADMINUTIL_BASE_URL = "adminutil_base_url";
  public static final String ADMINUTIL_SIGN_ENDPOINT = "adminutil_sign_endpoint";
  public static final String FORM_API_ENDPOINT = "form_api_endpoint";
  public static final String MANAGED_TOKEN = "managedToken";
  public static final String WITH_TOKENS = "withTokens";
  public static final String DECLARED_EMAIL = "declared-email";
  public static final String DECLARED_PHONE = "declared-phone";
  public static final String DECLARED_SCHOOL_UDISE_CODE = "declared-school-udise-code";
  public static final String DECLARED_SCHOOL_NAME = "declared-school-name";
  public static final String GOOGLE_CAPTCHA_PRIVATE_KEY = "google_captcha_private_key";
  public static final String GOOGLE_CAPTCHA_MOBILE_PRIVATE_KEY =
      "google_captcha_mobile_private_key";
  public static final String MOBILE_APP = "app";
  public static final String CAPTCHA_RESPONSE = "captchaResponse";
  public static final String ENABLE_CAPTCHA = "enable_captcha";
  public static final String DECLARED_STATE = "declared-state";
  public static final String DECLARED_DISTRICT = "declared-district";
  public static final String SUBMITTED = "SUBMITTED";
  public static final String VALIDATED = "VALIDATED";
  public static final String REJECTED = "REJECTED";
  public static final String SELF_DECLARED_ERROR = "ERROR";
  public static final String SELF_DECLARED_MANDATORY_FIELDS = "self_declared_mandatory_fields";
  public static final String SELF_DECLARED_OPTIONAL_FIELDS = "self_declared_optional_fields";
  public static final String SELF_DECLARED_USER_OBJECT = "SELF_DECLARED_USER";
  public static final String DIKSHA_UUID = "Diksha UUID";
  public static final String STATE_PROVIDED_EXT_ID = "State provided ext. ID";
  public static final String USER_INFO = "userInfo";
  public static final String USR_DECLARATION_TABLE = "user_declarations";
  public static final String ERROR_TYPE = "errorType";
  public static final String DECLARATIONS = "declarations";
  public static final String PERSONA = "persona";
  public static final String SUB_PERSONA = "subPersona";
  // This denotes the persona of the user in self declaration and
  // is different from role or user type = TEACHER
  public static final String TEACHER_PERSONA = "teacher";
  public static final String DEFAULT_PERSONA = "default";
  public static final String TENANT_PREFERENCE_V2 = "tenantPreferenceV2";
  public static final String X_Session_ID = "x-session-id";
  public static final String X_APP_VERSION = "x-app-ver";
  public static final String X_TRACE_ENABLED = "x-trace-enabled";
  public static final String X_REQUEST_ID = "x-request-id";
  public static final String USER_LOOKUP = "user_lookup";
  // this fields are being stored in type column in user_lookup table
  public static final String USER_LOOKUP_FILED_USER_NAME = "username";
  public static final String USER_LOOKUP_FILED_EXTERNAL_ID = "externalid";
  public static final String CONSENT_EXPIRY_IN_DAYS = "consent_expiry_in_days";
  public static final String CONSENT_EXPIRY = "expiry";
  public static final String CONSENT_BODY = "consent";
  public static final String CONSENT_RESPONSE = "consents";
  public static final String CONSENT_SUCCESS_MESSAGE = "User Consent updated successfully.";
  // user consent req-response attributes listing - started
  public static final String CONSENT_CONSUMERID = "consumerId";
  public static final String CONSENT_OBJECTID = "objectId";
  public static final String CONSENT_CONSUMERTYPE = "consumerType";
  public static final String CONSENT_OBJECTTYPE = "objectType";
  // user consent req-response attributes listing - ended
  // user consent table columns listing - started
  public static final String CONSENT_CONSUMER_ID = "consumer_id";
  public static final String CONSENT_OBJECT_ID = "object_id";
  public static final String CONSENT_USER_ID = "user_id";
  public static final String CONSENT_CONSUMER_TYPE = "consumer_type";
  public static final String CONSENT_OBJECT_TYPE = "object_type";
  public static final String CONSENT_LAST_UPDATED_ON = "last_updated_on";
  public static final String CONSENT_CREATED_ON = "created_on";
  public static final String CONSENT_OBJECT = "object";
  // user consent table columns listing - ended
  public static final String PRIORITY = "priority";
  public static final String FEED_LIMIT = "feed_limit";
  public static final String ORG_ADMIN = "ORG_ADMIN";
  public static final String ORG_ADMIN_TNC = "orgAdminTnc";
  public static final String REQUEST_ID = "requestid";
  public static final String LOCATION_TYPE_SCHOOL = "school";
  public static final String UPDATE_USER_SCHOOL_ORG = "updateUserSchoolOrg";
  public static final String GET = "get";
  public static final String FORM = "form";
  public static final String CHILDREN = "children";
  public static final String OPTIONS = "options";
  public static final String DISTRICT = "district";
  public static final String PORTAL_SERVICE_PORT = "PORTAL_SERVICE_PORT";
  public static final String LOCATION_TYPE = "type";
  public static final String SUNBIRD_VALID_LOCATION_TYPES = "sunbird_valid_location_types";
  public static final String PARENT_CODE = "parentCode";
  public static final String PROPERTY_NAME = "name";
  public static final String PROPERTY_VALUE = "value";
  public static final String SMS_TEMPLATE_CONFIG = "smsTemplateConfig";
  public static final String IS_MINOR = "isMinor";
  public static final String DEFAULT_MONTH_DATE = "defaultMonthDate";
  public static final String DOB_VALIDATION_DONE = "dobValidationDone";
  public static final String IS_TENANT = "isTenant";
  public static final String ORG_LOCATION = "orgLocation";
  public static final String IS_SCHOOL = "isSchool";
  public static final String ORGANISATION_TYPE = "organisationType";
  public static final String SYNC = "sync";
  public static final String ES_SYNC_RESPONSE = "esSyncResponse";

  private JsonKey() {}
}
