package net.monofraps.gradlecurse.tasks;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.io.File;

/**
 * @author monofraps
 */
public class CurseDeployTask extends DefaultTask
{
    /**
     * The handle of the game. (e.g. wow for World of Warcraft or rom for Runes of Magic, this is the game's subdomain
     * on Curse.
     */
    private String gameHandle;

    /**
     * Defaults to http://$gameHandle$.curseforge.com
     */
    private String baseUrl;

    /**
     * The name of the project to upload to.
     */
    private String projectName;

    /**
     * Defaults to $baseUrl$/projects/$project-name$/upload-file.json
     */
    private String uploadUrl;

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


    @TaskAction
    public void uploadArtifact()
    {
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
        return baseUrl;
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
        return uploadUrl;
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

    public File getSourceFile() {
        return getProject().file(sourceObject);
    }

    public void setSourceObject(final Object sourceObject)
    {
        this.sourceObject = sourceObject;
    }
}
