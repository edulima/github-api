package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.GitHubServiceImp;

import java.io.IOException;

/**
 * The GitHubController provides a method that returns the top 5 public GitHub
 * repositories of a given user sorted by size
 *
 * @author Eduardo Lima
 * @version 1.0
 * @since 2016-11-31
 */
public class GitHubController extends Controller {

  /**
   * Process income request and render response to view
   * @param userName
   * @return a Json response containing 5 or less repositories
   * @throws IOException
   * @throws JSONException
   */
  public Result getRepositories(String userName) throws IOException, JSONException {

    ObjectMapper mapper = new ObjectMapper();
    GitHubServiceImp gitHubServiceImp = new GitHubServiceImp();

    JSONArray response = gitHubServiceImp.getRepositories(
      "https://api.github.com/users/",
      userName,
      "/repos?sort=created",
      "GET"
    );

    // Parse response to Json
    JsonNode jsonResponse = mapper.readTree(response.toString());

    return ok(Json.toJson(jsonResponse));
  }
}
