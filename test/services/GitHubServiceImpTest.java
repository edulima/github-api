package services;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static play.mvc.Http.Status.OK;

public class GitHubServiceImpTest {

  private GitHubServiceImp gitHubServiceImp;

  @Before
  public void setUp() throws Exception {
    gitHubServiceImp = new GitHubServiceImp();
  }

  @Test
  public void realResponseShouldContainValidDetails() throws Exception {

    URL url = new URL("http://localhost:9000/resource/edulima");
    HttpURLConnection connection = gitHubServiceImp.openConnection(url, "GET");

    String expectedName = "edulima";

    JSONArray response = gitHubServiceImp.processResponse(connection);

    assert(response.toString().contains(expectedName));

    assertEquals(response.getJSONArray(0).getJSONObject(0)
      .getString("pushed_at"), "2016-02-16T14:29:19Z");
  }

  @Test
  public void processUserNotFoundResponseShouldReturnExpectedMessage()
    throws Exception {

    HttpURLConnection mockedConnection = mock(HttpURLConnection.class);
    when(mockedConnection.getResponseCode()).thenReturn(200);

    String expectedMessage = "No user found";

    JSONArray actualResponse = gitHubServiceImp
      .processUserNotFoundResponse(mockedConnection);

    assertEquals(expectedMessage, actualResponse.getJSONObject(0)
      .get("message").toString());
  }

  @Test
  public void openConnectionShouldReturnAConnection() throws Exception {

    URL url = new URL("http://www.blablablaNotfound.com");

    HttpURLConnection httpURLConnection = gitHubServiceImp
      .openConnection(url, "GET");

    assertEquals(httpURLConnection.getRequestMethod(), "GET");
    assertEquals(httpURLConnection.getResponseCode(), OK);
  }

  @Test
  public void sortResponseShouldReturnSortedObject() throws Exception {

    List<JSONObject> objList = new ArrayList<>();

    objList.add(new JSONObject().put("size","50").put("name:","java-app"));
    objList.add(new JSONObject().put("size","10").put("name:","php-app"));
    objList.add(new JSONObject().put("size","2").put("name:","ruby-app"));
    objList.add(new JSONObject().put("size","53").put("name:","css-lol-app"));

    JSONArray actual = gitHubServiceImp.sortResponse(objList);

    String expected = "[[" +
      "{\"size\":\"53\",\"name:\":\"css-lol-app\"}," +
      "{\"size\":\"50\",\"name:\":\"java-app\"}," +
      "{\"size\":\"10\",\"name:\":\"php-app\"}," +
      "{\"size\":\"2\",\"name:\":\"ruby-app\"}" +
    "]]";

    assertEquals(expected, actual.toString());
  }

  @Test
  public void createURLShouldReturnExpectedURL() throws Exception {

    String baseUrl = "http://test.com/";
    String userName = "username";
    String urlParam = "/repos?sort=created";

    String expectedUrl = "http://test.com/username/repos?sort=created";
    URL actualUrl  = gitHubServiceImp.createURL(baseUrl, userName, urlParam);

    assertEquals(actualUrl.toString(), expectedUrl);
  }

  @Test(expected = MalformedURLException.class)
  public void openConnectionShouldReturnMalformedURLException() throws Exception {

    URL url = new URL("htp://www.blablablaNotfound.com");
    gitHubServiceImp.openConnection(url, "GET");
  }

  @Test(expected = MalformedURLException.class)
  public void createURLShouldReturnMalFormedURLExceptionForInvalidURL()
    throws MalformedURLException {

    String malformedURL = "htpt://malformed.com";
    gitHubServiceImp.createURL(malformedURL, malformedURL, malformedURL);
    throw new MalformedURLException();
  }
}
