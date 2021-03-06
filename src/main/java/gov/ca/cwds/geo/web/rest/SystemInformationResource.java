package gov.ca.cwds.geo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import gov.ca.cwds.geo.Constants.API;
import gov.ca.cwds.rest.api.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author CWDS TPT2 Team
 */
@Api(value = API.SYSTEM_INFORMATION)
@Path(API.SYSTEM_INFORMATION)
@Produces(MediaType.APPLICATION_JSON)
public class SystemInformationResource {

  private static final String VERSION_PROPERTIES_FILE = "version.properties";
  private static final String BUILD_VERSION = "build.version";
  private static final String BUILD_NUMBER = "build.number";

  private String applicationName;
  private String version;
  private String buildNumber;

  private static final ObjectMapper MAPPER = new ObjectMapper();

  /**
   * Constructor
   *
   * @param applicationName The name of the application
   */
  @Inject
  public SystemInformationResource(@Named("app.name") String applicationName) {
    this.applicationName = applicationName;
    Properties versionProperties = getVersionProperties();
    this.version = versionProperties.getProperty(BUILD_VERSION);
    this.buildNumber = versionProperties.getProperty(BUILD_NUMBER);
  }

  private Properties getVersionProperties() {
    Properties versionProperties = new Properties();
    try {
      InputStream is = ClassLoader.getSystemResourceAsStream(VERSION_PROPERTIES_FILE);
      versionProperties.load(is);
    } catch (IOException e) {
      throw new ApiException("Can't read version.properties", e);
    }
    return versionProperties;
  }

  /**
   * Get the name of the application.
   *
   * @return the application data
   */
  @GET
  @Timed
  @ApiOperation(value = "Returns Application information")
  public String get() {
    ImmutableMap<String, String> map =
        ImmutableMap.<String, String>builder()
            .put("Application", applicationName)
            .put("Version", version)
            .put("BuildNumber", buildNumber)
            .build();
    try {
      return MAPPER.writeValueAsString(map);
    } catch (JsonProcessingException e) {
      throw new ApiException("Unable to parse application data", e);
    }
  }
}
