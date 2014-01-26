package net.monofraps.gradlecurse.models;

import com.google.gson.JsonObject;

/**
 * @author monofraps
 */
public class GameVersion implements Comparable<GameVersion>
{
    private int     versionNumber;
    private boolean isDevelopment;
    private boolean breaksCompatibility;
    private String  releaseDate;
    private String  name;
    private String  internalVersionId;

    public GameVersion(final String versionNumber, final JsonObject dataObject)
    {
        this.versionNumber = Integer.valueOf(versionNumber);
        isDevelopment = dataObject.get("is_development").getAsBoolean();
        breaksCompatibility = dataObject.get("breaks_compatibility").getAsBoolean();
        releaseDate = dataObject.get("release_date").getAsString();
        name = dataObject.get("name").getAsString();
        internalVersionId = dataObject.get("internal_id").getAsString();
    }

    public int getVersionNumber()
    {
        return versionNumber;
    }

    public boolean isDevelopment()
    {
        return isDevelopment;
    }

    public boolean isBreaksCompatibility()
    {
        return breaksCompatibility;
    }

    public String getReleaseDate()
    {
        return releaseDate;
    }

    public String getName()
    {
        return name;
    }

    public String getInternalVersionId()
    {
        return internalVersionId;
    }

    @Override
    public int compareTo(final GameVersion other)
    {
        if (other.getVersionNumber() < getVersionNumber())
        {
            return 1;
        }
        else if (other.getVersionNumber() > getVersionNumber())
        {
            return -1;
        }
        else
        {
            return 0;
        }
    }
}
