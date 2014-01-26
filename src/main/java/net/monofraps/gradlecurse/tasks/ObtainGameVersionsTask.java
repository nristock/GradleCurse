package net.monofraps.gradlecurse.tasks;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.monofraps.gradlecurse.extensions.CurseDeploy;
import net.monofraps.gradlecurse.models.GameVersion;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This task will show the available game versions by sending an HTTP GET request to $baseUrl$/game-versions.json.
 * By default it will only show the latest 3 versions. If you would like to see all available versions, create a task of
 * this type and set the showAll property to true.
 * <p/>
 * The GradleCurse plugin will automatically create a default ObtainGameVersionsTask named showGameVersions.
 *
 * @author monofraps
 */
public class ObtainGameVersionsTask extends DefaultTask
{
    /**
     * Number of version to display. (e.g. 3 will show the 3 most recent versions)
     * Set to -1 to show all versions.
     * Defaults to 3.
     */
    private int numVersionsToDisplay = 3;

    @TaskAction
    public void obtainGameVersions()
    {
        final String responseString;

        try
        {
            final HttpGet httpGet = new HttpGet(((CurseDeploy) getProject().getExtensions().getByName("curseDeploy")).getBaseUrl() + "/game-versions.json");
            getLogger().info("Obtaining game versions from " + httpGet.getURI().toString());

            final HttpClient httpClient = new DefaultHttpClient();
            final HttpResponse httpResponse = httpClient.execute(httpGet);
            final BufferedReader responseReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

            final StringBuilder response = new StringBuilder();
            String line;
            while ((line = responseReader.readLine()) != null)
            {
                response.append(line);
            }

            responseString = response.toString();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }

        final JsonElement response = (new JsonParser()).parse(responseString);
        final JsonObject versionAssList = response.getAsJsonObject();
        final List<GameVersion> gameVersions = new ArrayList<GameVersion>();

        for (final Map.Entry<String, JsonElement> versionObject : versionAssList.entrySet())
        {
            gameVersions.add(new GameVersion(versionObject.getKey(), versionObject.getValue().getAsJsonObject()));
        }
        Collections.sort(gameVersions);

        for (final GameVersion gameVersion : (numVersionsToDisplay == -1)? gameVersions : gameVersions.subList(gameVersions.size() - 3, gameVersions.size()))
        {

            getLogger().lifecycle(String.format("Version %s, released on %s", gameVersion.getVersionNumber(), gameVersion.getReleaseDate()));
            getLogger().lifecycle(String.format("\tName: %s", gameVersion.getName()));
            getLogger().lifecycle(String.format("\tIsDevelopment: %b", gameVersion.isDevelopment()));
            getLogger().lifecycle(String.format("\tBreaksCompatibility: %b", gameVersion.isBreaksCompatibility()));
        }
    }

    public int getNumVersionsToDisplay()
    {
        return numVersionsToDisplay;
    }

    public void setNumVersionsToDisplay(final int numVersionsToDisplay)
    {
        this.numVersionsToDisplay = numVersionsToDisplay;
    }
}
