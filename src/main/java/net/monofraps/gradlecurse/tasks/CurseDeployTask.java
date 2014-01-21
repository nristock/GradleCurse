package net.monofraps.gradlecurse.tasks;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * @author monofraps
 */
public class CurseDeployTask extends DefaultTask
{
    public enum FileType
    {
        RELEASE("r"), BETA("b"), ALPHA("a");

        private String stringRepresentation;

        FileType(final String stringRepresentation)
        {
            this.stringRepresentation = stringRepresentation;
        }

        @Override
        public String toString()
        {
            return stringRepresentation;
        }
    }

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

    /**
     * The name of the project to upload to.
     */
    private String projectName;

    /**
     * Defaults to $baseUrl$/projects/$projectName$/upload-file.json
     * <p/>
     * $baseUrl$ will be replaced with whatever baseUrl is set to.
     * $projectName$ will be replaced with whatever projectName is set to.
     */
    private String uploadUrl = "$baseUrl$/projects/$project-name$/upload-file.json";

    /**
     * Your API key - get one here: http://www.curseforge.com/home/api-key/
     */
    private String apiKey;

    /**
     * Defaults to $project.name$-$project.version$
     */
    private String uploadFileName;

    private String changeLog;

    /**
     * The file to upload
     */
    private Object sourceObject;

    private FileType fileType;

    private Set<String> gameVersions;

    @TaskAction
    public void uploadArtifact()
    {
        Preconditions.checkNotNull(Strings.emptyToNull(getApiKey()), "You must specify a Curse Forge API Key. You can get one here: http://www.curseforge.com/home/api-key/");
        Preconditions.checkNotNull(Strings.emptyToNull(getGameHandle()), "You must specify a game handle. (e.g. wow for World of Warcraft or rom for Runes of magic)");
        Preconditions.checkNotNull(Strings.emptyToNull(getProjectName()), "You must specify a project name.");
        Preconditions.checkNotNull(Strings.emptyToNull(getBaseUrl()), "baseUrl cannot be null or empty.");
        Preconditions.checkNotNull(Strings.emptyToNull(getUploadUrl()), "uploadUrl cannot be null or empty.");
        Preconditions.checkNotNull(getFileType(), "fileType must not be null.");
        Preconditions.checkArgument(getSourceFile().exists(), "The file you wish to upload to Curse does not exist.");
        Preconditions.checkNotNull(getGameVersions(), "gameVersions must not be null.");
        Preconditions.checkArgument(getGameVersions().size() >= 1 && getGameVersions().size() <= 3, "You must specify between 1 to 3 game versions.");

        getLogger().lifecycle("Uploading to Curse...");

        final StringBody name = new StringBody(uploadFileName, ContentType.DEFAULT_TEXT);
        final StringBody fileType = new StringBody(getFileType().toString(), ContentType.DEFAULT_TEXT);
        final StringBody changeLog = new StringBody(getChangeLog(), ContentType.DEFAULT_TEXT);

        //TODO: binary or app/zip, maybe an option or auto-detect from file extension ?!
        final FileBody fileBody = new FileBody(getSourceFile(), ContentType.DEFAULT_BINARY);

        final MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.addPart("name", name);
        multipartEntityBuilder.addPart("file_type", fileType);
        multipartEntityBuilder.addPart("change_log", changeLog);
        multipartEntityBuilder.addPart("file", fileBody);

        for (final String gameVersion : gameVersions)
        {
            multipartEntityBuilder.addTextBody("game_version", gameVersion);
        }

        final HttpPost httpPost = new HttpPost(getUploadUrl());
        httpPost.addHeader("User-Agent", "GradleCurse Uploader");
        httpPost.addHeader("X-API-Key", getApiKey());
        httpPost.setEntity(multipartEntityBuilder.build());

        try
        {
            final HttpClient httpClient = new DefaultHttpClient();
            final HttpResponse httpResponse = httpClient.execute(httpPost);

            getLogger().lifecycle("Curse Upload: " + httpResponse.getStatusLine());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public String getGameHandle()
    {
        return gameHandle;
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

    public String getProjectName()
    {
        return projectName;
    }

    public void setProjectName(final String projectName)
    {
        this.projectName = projectName;
    }

    public String getUploadUrl()
    {
        return uploadUrl.replace("$baseUrl$", getBaseUrl()).replace("$projectName$", getProjectName());
    }

    public void setUploadUrl(final String uploadUrl)
    {
        this.uploadUrl = uploadUrl;
    }

    public String getApiKey()
    {
        return apiKey;
    }

    public void setApiKey(final String apiKey)
    {
        this.apiKey = apiKey;
    }

    public String getUploadFileName()
    {
        if (Strings.nullToEmpty(uploadFileName).isEmpty())
        {
            return String.format("%s-%s", getProjectName(), getProject().getVersion());
        }
        return uploadFileName;
    }

    public void setUploadFileName(final String uploadFileName)
    {
        this.uploadFileName = uploadFileName;
    }

    public String getChangeLog()
    {
        return changeLog;
    }

    public void setChangeLog(final String changeLog)
    {
        this.changeLog = changeLog;
    }

    public Object getSourceObject()
    {
        return sourceObject;
    }

    public File getSourceFile()
    {
        return getProject().file(sourceObject);
    }

    public void setSourceObject(final Object sourceObject)
    {
        this.sourceObject = sourceObject;
    }

    public FileType getFileType()
    {
        return fileType;
    }

    public void setFileType(final FileType fileType)
    {
        this.fileType = fileType;
    }

    public Set<String> getGameVersions()
    {
        return gameVersions;
    }

    public void setGameVersions(final Set<String> gameVersions)
    {
        this.gameVersions = gameVersions;
    }
}
