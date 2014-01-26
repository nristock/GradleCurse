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

import net.monofraps.gradlecurse.extensions.CurseDeploy;
import net.monofraps.gradlecurse.tasks.CurseDeployTask;
import net.monofraps.gradlecurse.tasks.ObtainGameVersionsTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * Main plugin class.
 *
 * The plugin automagiclly adds the curseDeploy extension to your project. It will also create two tasks.
 *
 * Created Tasks:<br />
 * deployToCurse - uploads all enabled deployments defined in curseDeploy extension. See {@link net.monofraps.gradlecurse.tasks.CurseDeployTask}
 * for more information.<br />
 * showGameVersions - outputs the 3 latest versions of the game. See {@link net.monofraps.gradlecurse.tasks.ObtainGameVersionsTask}
 * for more information.
 *
 * @author monofraps
 */
public class GradleCursePlugin implements Plugin<Project>
{
    @Override
    public void apply(final Project project)
    {
        project.getExtensions().create("curseDeploy", CurseDeploy.class, project);

        project.getTasks().create("deployToCurse", CurseDeployTask.class).setSingleUpload(true);
        project.getTasks().create("showGameVersions", ObtainGameVersionsTask.class);
    }
}
