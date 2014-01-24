package net.monofraps.gradlecurse.tasks;

import com.google.common.base.Strings;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * @author monofraps
 */
public class ObtainGameVersionsTask extends DefaultTask
{
    /**
     * The handle of the game. (e.g. wow for World of Warcraft or rom for Runes of Magic, this is the game's subdomain
     * on Curse.
     */
    private String gameHandle;

    /**
     * Defaults to http://$gameHandle$.curseforge.com
     * Set this to http://dev.bukkit.org to upload to DBO.
     * <p/>
     * $gameHandle$ will be replaced with whatever gameHandle is set to.
     */
    private String baseUrl = "http://$gameHandle$.curseforge.com";

    @TaskAction
    public void obtainGameVersions()
    {
        final String responseString;

        try
        {
            final HttpGet httpGet = new HttpGet(getBaseUrl() + "/game-versions.json");
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

        // TODO: Sort by date
        for(final Map.Entry<String, JsonElement> versionObject : versionAssList.entrySet()) {
            getLogger().lifecycle(String.format("Version %s, released on %s", versionObject.getKey(), versionObject.getValue().getAsJsonObject().get("release_date").getAsString()));
            getLogger().lifecycle(String.format("\tName: %s", versionObject.getValue().getAsJsonObject().get("name").getAsString()));
            getLogger().lifecycle(String.format("\tIsDevelopment: %b", versionObject.getValue().getAsJsonObject().get("is_development").getAsBoolean()));
            getLogger().lifecycle(String.format("\tBreaksCompatibility: %b", versionObject.getValue().getAsJsonObject().get("breaks_compatibility").getAsBoolean()));
        }
    }

    public String getGameHandle()
    {
        return Strings.nullToEmpty(gameHandle);
    }

    public void setGameHandle(final String gameHandle)
    {
        this.gameHandle = gameHandle;
    }

    public String getBaseUrl()
    {
        return baseUrl.replace("$gameHandle$", getGameHandle());
    }

    public void setBaseUrl(final String baseUrl)
    {
        this.baseUrl = baseUrl;
    }
}
