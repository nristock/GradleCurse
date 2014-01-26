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
