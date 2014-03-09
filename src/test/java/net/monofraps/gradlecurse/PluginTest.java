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

package net.monofraps.gradlecurse;

import junit.framework.TestCase;
import net.monofraps.gradlecurse.extensions.CurseDeploy;
import net.monofraps.gradlecurse.tasks.CurseDeployTask;
import net.monofraps.gradlecurse.tasks.ObtainGameVersionsTask;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author monofraps
 */
public class PluginTest
{
    private Project project;

    @Before
    public void setupProject()
    {
        project = ProjectBuilder.builder().build();

        final Map<String, String> options = new HashMap<String, String>()
        {{
                put("plugin", "gradle-curse");
            }};
        project.apply(options);
    }

    @Test
    public void gradleCursePluginAddsDeployToCurseTask()
    {
        TestCase.assertTrue(project.getTasks().getByName("deployToCurse") instanceof CurseDeployTask);
    }

    @Test
    public void gradleCursePluginAddsShowGameVersionsTask()
    {
        TestCase.assertTrue(project.getTasks().getByName("showGameVersions") instanceof ObtainGameVersionsTask);
    }

    @Test
    public void gradleCursePluginAddsCurseDeployExtensionObject()
    {
        TestCase.assertTrue(project.getExtensions().getByName("curseDeploy") instanceof CurseDeploy);
    }

    @After
    public void cleanup()
    {
        project = null;
    }
}
