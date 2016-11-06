package services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The GitHubServiceImp provides the logic that processes the response from the
 * GitHub service
 *
 * @author Eduardo Lima
 * @version 1.0
 * @since 2016-11-31
 */
public class GitHubServiceImp implements GitHubService {

  private URL url;
  private HttpURLConnection connection;

  /**
   * Get repositories
   * @param baseUrl
   * @param userName
   * @param urlParams
   * @param httpVerb
   * @return JSONArray containing 5 or less repositories
   * @throws IOException
   * @throws JSONException
   */
  @Override
  public JSONArray getRepositories(String baseUrl, String userName,
    String urlParams, String httpVerb) throws IOException, JSONException {

    URL url = createURL(baseUrl, userName, urlParams);
    HttpURLConnection connection = openConnection(url, httpVerb);

    return processResponse(connection);
  }

  /**
   * Create a URL
   * @param baseUrl
   * @param userName
   * @param urlParams
   * @return a new URL
   */
  @Override
  public URL createURL(String baseUrl, String userName, String urlParams)  {

    try {
      String uri = baseUrl + userName + urlParams;
      url = new URL(uri);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }

    return url;
  }

  /**
   * Open a HttpURLConnection
   * @param url
   * @param httpVerb
   * @return an openned HttpURLConnection connection
   */
  @Override
  public HttpURLConnection openConnection(URL url, String httpVerb) {

    try {
      connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod(httpVerb);
      connection.connect();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return connection;
  }

  /**
   * Process the response from the API
   * @param connection
   * @return JSONArray with a processed response
   * @throws IOException
   * @throws JSONException
   */
  @Override
  public JSONArray processResponse(HttpURLConnection connection)
    throws IOException, JSONException {

    String output;
    List<JSONObject> responseValues = new ArrayList<>();

    try {
      BufferedReader bufferedReader = new BufferedReader(
        new InputStreamReader(connection.getInputStream()));

      if (connection.getInputStream().available() > 2) {
        StringBuilder stringBuilder = new StringBuilder();

        while ((output = bufferedReader.readLine()) != null) {
          stringBuilder.append(output);
        }

        JSONArray jsonArr = new JSONArray(stringBuilder.toString());

        for (int i = 0; i < jsonArr.length(); i++) {
          responseValues.add(jsonArr.getJSONObject(i));
        }

        connection.disconnect();

        return sortResponse(responseValues);
      }
    } catch (FileNotFoundException e) {
      e.getMessage();
    }

    return processUserNotFoundResponse(connection);
  }

  /**
   * Sort response by size in DESC order
   * @param jsonValues
   * @return JSONArray order by size
   */
  @Override
  public JSONArray sortResponse(List<JSONObject> jsonValues) {

    JSONArray sortedJsonArray = new JSONArray();

    Collections.sort(jsonValues, (objA, objB) -> {
      int sizeA = 0;
      int sizeB = 0;

      try {
        sizeA = objA.getInt("size");
        sizeB = objB.getInt("size");
      } catch (JSONException e) {
        e.printStackTrace();
      }

      return new Integer(sizeB).compareTo(sizeA);
    });

    int counter = 0;
    int maxNumberOfRepositoriesToReturn = 5;

    if (jsonValues.size() <= maxNumberOfRepositoriesToReturn) {
      sortedJsonArray.put(jsonValues);
    } else {
      while (counter < maxNumberOfRepositoriesToReturn) {
        sortedJsonArray.put(jsonValues.get(counter));
        counter++;
      }
    }

    return sortedJsonArray;
  }

  /**
   * Create user not found response
   * @param connection
   * @return JSONArray with not found message and error code
   * @throws IOException
   * @throws JSONException
   */
  @Override
  public JSONArray processUserNotFoundResponse(HttpURLConnection connection)
    throws IOException, JSONException {

    JSONArray jsonArray = new JSONArray();
    JSONObject jsonNotFoundResponse = new JSONObject();

    jsonNotFoundResponse.put("message", "No user found");
    jsonNotFoundResponse.put("code", connection.getResponseCode());

    return jsonArray.put(jsonNotFoundResponse);
  }
}
