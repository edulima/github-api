package services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * GitHubService interface
 *
 * @author Eduardo Lima
 * @version 1.0
 * @since 2016-11-31
 */
public interface GitHubService {

  /**
   * Creates a new URL
   * @param baseUrl
   * @param userName
   * @param urlParams
   */
  URL createURL(String baseUrl, String userName, String urlParams);

  /**
   * Opens a new HttpURLConnection
   * @param url
   * @param httpVerb
   */
  HttpURLConnection openConnection(URL url, String httpVerb);

  /**
   * Processes  api response
   * @param connection
   * @throws IOException
   * @throws JSONException
   */
  JSONArray processResponse(HttpURLConnection connection)
    throws IOException, JSONException;

  /**
   * Sorts response order by size
   * @param jsonValues
   */
  JSONArray sortResponse(List<JSONObject> jsonValues);

  /**
   *
   * @param connection
   * @return
   * @throws IOException
   * @throws JSONException
   */
  JSONArray processUserNotFoundResponse(HttpURLConnection connection)
    throws IOException, JSONException;

  /**
   * Gets the repositories from api
   * @param baseUrl
   * @param userName
   * @param urlParams
   * @param httpVerb
   * @throws IOException
   * @throws JSONException
   */
  JSONArray getRepositories(String baseUrl, String userName, String urlParams, String httpVerb)
    throws IOException, JSONException;
}
