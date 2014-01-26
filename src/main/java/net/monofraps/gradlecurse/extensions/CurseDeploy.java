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
import groovy.lang.Closure;
import org.gradle.api.Project;

import java.util.ArrayList;
import java.util.List;

/**
 * @author monofraps
 */
public class CurseDeploy
{
    private Project project;

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
    private String           baseUrl     = "http://$gameHandle$.curseforge.com";
    private List<Deployment> deployments = new ArrayList<Deployment>();

    public CurseDeploy(final Project project)
    {
        this.project = project;
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
        Preconditions.checkArgument(!Strings.isNullOrEmpty(baseUrl), "Parameter baseUrl must not be null nor empty.");
        return baseUrl.replace("$gameHandle$", getGameHandle());
    }

    public void setBaseUrl(final String baseUrl)
    {
        this.baseUrl = baseUrl;
    }

    public List<Deployment> getDeployments()
    {
        return deployments;
    }

    /**
     * Adds a deployment entry which will get executed by the deployToCurse task.
     * @param closure .
     * @return Returns itself
     */
    public CurseDeploy deployment(final Closure closure)
    {
        Deployment deployment = new Deployment(this, project);
        if (closure != null)
        {
            closure.setDelegate(deployment);
            closure.setResolveStrategy(Closure.DELEGATE_ONLY);
            closure.run();
        }

        deployments.add(deployment);
        return this;
    }
}
