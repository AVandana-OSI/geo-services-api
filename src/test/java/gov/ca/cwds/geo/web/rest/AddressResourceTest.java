package gov.ca.cwds.geo.web.rest;

import static gov.ca.cwds.geo.Constants.ADDRESS;
import static gov.ca.cwds.geo.Constants.DISTANCE;
import static gov.ca.cwds.geo.Constants.LOOKUP_ZIP_CODE;
import static gov.ca.cwds.geo.Constants.SUGGEST;
import static gov.ca.cwds.geo.Constants.VALIDATE_SINGLE;
import static gov.ca.cwds.geo.web.rest.AssertFixtureUtils.assertResponseByFixturePath;
import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import gov.ca.cwds.geo.BaseApiIntegrationTest;
import java.io.IOException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONException;
import org.junit.Ignore;
import org.junit.Test;

public class AddressResourceTest extends BaseApiIntegrationTest {

  @Ignore
  @Test
  public void testPostAddressValidate() throws Exception {
    WebTarget target = clientTestRule.target(ADDRESS + "/" + VALIDATE_SINGLE);
    Invocation.Builder invocation = target.request(MediaType.APPLICATION_JSON);
    Response postResponse =
        invocation.post(
            Entity.entity(
                fixture("fixtures/addressValidateRequest.json"), MediaType.APPLICATION_JSON_TYPE),
            Response.class);
    assertResponseByFixturePath(postResponse, "fixtures/addressValidateResponse.json");
  }

  @Ignore
  @Test
  public void postAddressValidate_unprocessableEntityResponse_whenValidationFailed() {
    // given
    final Entity input = Entity.entity(
        fixture("fixtures/addressValidateFailRequest.json"),
        MediaType.APPLICATION_JSON_TYPE
    );

    // when
    final Response response = clientTestRule
        .target(ADDRESS + "/" + VALIDATE_SINGLE)
        .request(MediaType.APPLICATION_JSON)
        .post(input, Response.class);

    // then
    assertThat(response.getStatus(), is(equalTo(422)));
  }

  @Test
  public void testPostAddressValidateJsonProcessingException() throws Exception {
    WebTarget target = clientTestRule.target(ADDRESS + "/" + VALIDATE_SINGLE);
    Invocation.Builder invocation = target.request(MediaType.APPLICATION_JSON);
    Response postResponse =
        invocation.post(
            Entity.entity(
                fixture("fixtures/addressValidation/validation/jsonProcessingException/request.json"),
                MediaType.APPLICATION_JSON_TYPE),
            Response.class);
    assertResponseByFixturePath(
        postResponse, "fixtures/addressValidation/validation/jsonProcessingException/response.json");
  }

  @Test
  public void testPostAddressValidateStreetTooLong() throws Exception {
    WebTarget target = clientTestRule.target(ADDRESS + "/" + VALIDATE_SINGLE);
    Invocation.Builder invocation = target.request(MediaType.APPLICATION_JSON);
    Response postResponse =
        invocation.post(
            Entity.entity(
                fixture("fixtures/addressValidation/validation/streetTooLong/request.json"),
                MediaType.APPLICATION_JSON_TYPE),
            Response.class);
    assertResponseByFixturePath(
        postResponse, "fixtures/addressValidation/validation/streetTooLong/response.json");
  }

  @Test
  public void testPostAddressValidateCityTooLong() throws Exception {
    WebTarget target = clientTestRule.target(ADDRESS + "/" + VALIDATE_SINGLE);
    Invocation.Builder invocation = target.request(MediaType.APPLICATION_JSON);
    Response postResponse =
        invocation.post(
            Entity.entity(
                fixture("fixtures/addressValidation/validation/cityTooLong/request.json"),
                MediaType.APPLICATION_JSON_TYPE),
            Response.class);
    assertResponseByFixturePath(
        postResponse, "fixtures/addressValidation/validation/cityTooLong/response.json");
  }

  @Test
  public void testPostAddressValidateZipTooLong() throws Exception {
    WebTarget target = clientTestRule.target(ADDRESS + "/" + VALIDATE_SINGLE);
    Invocation.Builder invocation = target.request(MediaType.APPLICATION_JSON);
    Response postResponse =
        invocation.post(
            Entity.entity(
                fixture("fixtures/addressValidation/validation/zipTooLong/request.json"),
                MediaType.APPLICATION_JSON_TYPE),
            Response.class);
    assertResponseByFixturePath(
        postResponse, "fixtures/addressValidation/validation/zipTooLong/response.json");
  }

  @Test
  public void testPostAddressValidateStateTooLong() throws Exception {
    WebTarget target = clientTestRule.target(ADDRESS + "/" + VALIDATE_SINGLE);
    Invocation.Builder invocation = target.request(MediaType.APPLICATION_JSON);
    Response postResponse =
        invocation.post(
            Entity.entity(
                fixture("fixtures/addressValidation/validation/stateTooLong/request.json"),
                MediaType.APPLICATION_JSON_TYPE),
            Response.class);
    assertResponseByFixturePath(
        postResponse, "fixtures/addressValidation/validation/stateTooLong/response.json");
  }

  @Ignore
  @Test
  public void calculateDistance_success_whenValidInput() throws Exception {
    calculateDistanceAndAssert(
        "fixtures/calculateDistance/calculateDistance_success_request.json",
        "fixtures/calculateDistance/calculateDistance_success_response.json"
    );
  }

  @Test
  public void calculateDistance_errorMessage_whenNoSecondAddress() throws Exception {
    calculateDistanceAndAssert(
        "fixtures/calculateDistance/calculateDistance_noSecondAddress_request.json",
        "fixtures/calculateDistance/calculateDistance_noSecondAddress_response.json"
    );
  }

  @Test
  public void calculateDistance_errorMessage_whenNotValidFirstAddress() throws Exception {
    calculateDistanceAndAssert(
        "fixtures/calculateDistance/calculateDistance_notValidFirstAddress_request.json",
        "fixtures/calculateDistance/calculateDistance_notValidFirstAddress_response.json"
    );
  }

  private void calculateDistanceAndAssert(String inputFixture, String expectedResultFixture)
      throws IOException, JSONException {
    // given
    final Entity<String> input = Entity.entity(
        fixture(inputFixture),
        MediaType.APPLICATION_JSON_TYPE
    );

    // when
    final Response postResponse = clientTestRule.target(ADDRESS + "/" + DISTANCE)
        .request(MediaType.APPLICATION_JSON)
        .post(input, Response.class);

    // then
    assertResponseByFixturePath(postResponse, expectedResultFixture);
  }

  @Ignore
  @Test
  public void testAddressLookup() throws Exception {
    String zipCode = "95747";
    WebTarget target = clientTestRule.target(ADDRESS + "/" + LOOKUP_ZIP_CODE + "/" + zipCode);
    Invocation.Builder invocation = target.request(MediaType.APPLICATION_JSON);
    Response response = invocation.get(Response.class);
    assertResponseByFixturePath(response, "fixtures/addressLookupResponse.json");
  }

  @Ignore
  @Test
  public void testAddressSuggestion() throws Exception {
    String suggestion = "1489 Black Be";
    WebTarget target = clientTestRule.target(ADDRESS + "/" + SUGGEST + "/" + suggestion);
    Invocation.Builder invocation = target.request(MediaType.APPLICATION_JSON);
    Response response = invocation.get(Response.class);
    assertResponseByFixturePath(response, "fixtures/addressSuggestResponse.json");
  }

  @Ignore
  @Test
  public void testAddressDuplicationBug() throws Exception {
    String suggestion = "6458 Altama";
    WebTarget target = clientTestRule.target(ADDRESS + "/" + SUGGEST + "/" + suggestion);
    Invocation.Builder invocation = target.request(MediaType.APPLICATION_JSON);
    Response response = invocation.get(Response.class);
    assertResponseByFixturePath(response, "fixtures/addressSuggestDuplicationResponse.json");
  }
}
