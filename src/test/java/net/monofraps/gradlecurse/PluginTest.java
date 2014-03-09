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
