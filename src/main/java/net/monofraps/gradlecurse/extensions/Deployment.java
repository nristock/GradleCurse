/*
 * Copyright (C) 2014 Monofraps
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.monofraps.gradlecurse.extensions;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import net.monofraps.gradlecurse.FileType;
import net.monofraps.gradlecurse.MarkupType;
import org.gradle.api.Project;

import java.io.File;
import java.util.Set;

/**
 * @author monofraps
 */
public class Deployment
{
    private CurseDeploy parent;
    private Project     project;

    private boolean enabled = true;

    /**
     * The name of the project to upload to.
     */
    private String curseProjectName;

    /**
     * Defaults to $baseUrl$/projects/$projectName$/upload-file.json
     * <p/>
     * $baseUrl$ will be replaced with whatever baseUrl is set to.
     * $projectName$ will be replaced with whatever projectName is set to.
     */
    private String uploadUrl = "$baseUrl$/projects/$project-name$/upload-file.json";

    /**
     * Your API key - get one here: http://www.curseforge.com/home/api-key/ (required)
     */
    private String apiKey;

    /**
     * The name of the uploaded file. (defaults to $project.name$-$project.version$)
     */
    private String uploadFileName;

    /**
     * The change log to display on the uploads download page. (optional)
     */
    private String changeLog;

    /**
     * Known caveats to display on the uploads download page. (optional)
     */
    private String knownCaveats;

    /**
     * Markup to use for caveats. (optional, defaults to PLAIN)
     * See {@link net.monofraps.gradlecurse.MarkupType}.
     */
    private MarkupType caveatMarkup    = MarkupType.PLAIN;
    /**
     * Markup to use for change log. (optional, defaults to PLAIN)
     * See {@link net.monofraps.gradlecurse.MarkupType}.
     */
    private MarkupType changeLogMarkup = MarkupType.PLAIN;

    /**
     * The file to upload.
     */
    private Object file;

    /**
     * The type of the uploaded file. (optional, defaults to BETA)
     * See {@link net.monofraps.gradlecurse.FileType}.
     */
    private FileType fileType = FileType.BETA;

    private Set<String> gameVersions;

    public Deployment(final CurseDeploy parent, final Project project)
    {
        this.parent = parent;
        this.project = project;
    }

    public String getCurseProjectName()
    {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(curseProjectName), "Parameter projectName must not be null nor empty.");
        return curseProjectName;
    }

    public void setCurseProjectName(final String curseProjectName)
    {
        this.curseProjectName = curseProjectName;
    }

    public String getUploadUrl()
    {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(uploadUrl), "Parameter uploadUrl must not be null nor empty.");
        return uploadUrl.replace("$baseUrl$", parent.getBaseUrl()).replace("$projectName$", getCurseProjectName());
    }

    public void setUploadUrl(final String uploadUrl)
    {
        this.uploadUrl = uploadUrl;
    }

    public String getApiKey()
    {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(apiKey), "Parameter apiKey must not be null nor empty.");
        return apiKey;
    }

    public void setApiKey(final String apiKey)
    {
        this.apiKey = apiKey;
    }

    public String getUploadFileName()
    {
        return Strings.isNullOrEmpty(uploadFileName) ? String.format("%s-%s", getCurseProjectName(), project.getVersion()) : uploadFileName;
    }

    public void setUploadFileName(final String uploadFileName)
    {
        this.uploadFileName = uploadFileName;
    }

    public String getChangeLog()
    {
        return Strings.nullToEmpty(changeLog);
    }

    public void setChangeLog(final String changeLog)
    {
        this.changeLog = changeLog;
    }

    public String getKnownCaveats()
    {
        return Strings.nullToEmpty(knownCaveats);
    }

    public void setKnownCaveats(final String knownCaveats)
    {
        this.knownCaveats = knownCaveats;
    }

    public MarkupType getCaveatMarkup()
    {
        Preconditions.checkNotNull(caveatMarkup, "Parameter caveatMarkup must not be null.");
        return caveatMarkup;
    }

    public void setCaveatMarkup(final MarkupType caveatMarkup)
    {
        this.caveatMarkup = caveatMarkup;
    }

    public MarkupType getChangeLogMarkup()
    {
        Preconditions.checkNotNull(caveatMarkup, "Parameter changeLogMarkup must not be null.");
        return changeLogMarkup;
    }

    public void setChangeLogMarkup(final MarkupType changeLogMarkup)
    {
        this.changeLogMarkup = changeLogMarkup;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(final boolean enabled)
    {
        this.enabled = enabled;
    }

    public Object getFile()
    {
        Preconditions.checkNotNull(file, "Parameter file must not be null.");
        return file;
    }

    public void setFile(final Object file)
    {
        this.file = file;
    }

    public File getSourceFile()
    {
        return project.file(getFile());
    }

    public FileType getFileType()
    {
        Preconditions.checkNotNull(fileType, "Parameter fileType must not be null.");
        return fileType;
    }

    public void setFileType(final FileType fileType)
    {
        this.fileType = fileType;
    }

    public Set<String> getGameVersions()
    {
        Preconditions.checkNotNull(gameVersions, "Parameter gameVersions must not be null.");
        Preconditions.checkArgument(gameVersions.size() >= 1 && gameVersions.size() <= 3, "You have to specify 1 - 3 compatible game versions.");
        return gameVersions;
    }

    public void setGameVersions(final Set<String> gameVersions)
    {
        this.gameVersions = gameVersions;
    }
}
