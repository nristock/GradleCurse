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

package net.monofraps.gradlecurse.tasks;

import com.google.common.base.Preconditions;
import groovy.lang.Closure;
import net.monofraps.gradlecurse.extensions.CurseDeploy;
import net.monofraps.gradlecurse.extensions.Deployment;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.util.EntityUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.io.IOException;

/**
 * This task uploads either all enabled deployments specified in the curseDeploy extension. If you want to upload a
 * specific artifact only create a task of this type and set the deployment property just as you would in the curseDeploy
 * extension.
 * <p/>
 * The GradleCurse plugin automatically creates a default CurseDeployTask which uploads all deployments defined in curseDeploy.
 * This task is named deployToCurse.
 *
 * @author monofraps
 */
public class CurseDeployTask extends DefaultTask
{
    private boolean isSingleUpload = true;
    private Deployment deployment;

    @TaskAction
    public void doWork()
    {
        if (isSingleUpload)
        {
            uploadArtifact(getDeployment());
        }
        else
        {
            for (final Deployment deployment : ((CurseDeploy) getProject().getExtensions().getByName("curseDeploy")).getDeployments())
            {
                if (deployment.isEnabled())
                {
                    uploadArtifact(deployment);
                }
            }
        }
    }

    private void uploadArtifact(final Deployment deployment)
    {
        getLogger().lifecycle("Curse Upload: " + "Uploading to Curse...");
        getLogger().lifecycle("Curse Upload: " + deployment.toString());

        //TODO: binary or app/zip, maybe an option or auto-detect from file extension ?!
        final FileBody fileBody = new FileBody(deployment.getSourceFile(), ContentType.DEFAULT_BINARY, deployment.getUploadFileName());

        final MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.addTextBody("name", deployment.getUploadFileName());
        multipartEntityBuilder.addTextBody("file_type", deployment.getFileType().toString());
        multipartEntityBuilder.addTextBody("change_log", deployment.getChangeLog());
        multipartEntityBuilder.addTextBody("change_markup_type", deployment.getChangeLogMarkup().toString());
        multipartEntityBuilder.addTextBody("known_caveats", deployment.getKnownCaveats());
        multipartEntityBuilder.addTextBody("caveats_markup_type", deployment.getCaveatMarkup().toString());
        multipartEntityBuilder.addPart("file", fileBody);
        multipartEntityBuilder.addTextBody("game_versions", StringUtils.join(deployment.getGameVersions(), ","));

        try
        {
            final HttpPost httpPost = new HttpPost(probeForRedirect(deployment));
            httpPost.addHeader("User-Agent", "GradleCurse Uploader/1.0");
            httpPost.addHeader("X-API-Key", deployment.getApiKey());
            httpPost.setEntity(multipartEntityBuilder.build());

            final HttpClient httpClient = HttpClientBuilder.create().build();
            final HttpResponse httpResponse = httpClient.execute(httpPost);

            getLogger().lifecycle("Curse Upload: " + httpResponse.getStatusLine());
            getLogger().debug(EntityUtils.toString(httpResponse.getEntity()));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private String probeForRedirect(final Deployment deployment) throws IOException
    {
        getLogger().lifecycle("Curse Upload: Checking for HTTP301/redirect on " + deployment.getUploadUrl());

        final HttpPost httpPost = new HttpPost(deployment.getUploadUrl());
        httpPost.addHeader("User-Agent", "GradleCurse Uploader/1.0");
        httpPost.addHeader("X-API-Key", deployment.getApiKey());

        final HttpClient httpClient = HttpClientBuilder.create().build();
        final HttpResponse httpResponse = httpClient.execute(httpPost);
        getLogger().lifecycle("Curse Upload: " + httpResponse.getStatusLine().toString());

        if (httpResponse.getStatusLine().getStatusCode() == 301)
        {
            final String newLocation = httpResponse.getHeaders("Location")[0].getValue();
            getLogger().lifecycle("Curse Upload: " + "Detected redirect to " + newLocation);
            return newLocation;
        }
        else
        {
            getLogger().lifecycle("Curse Upload: " + "No redirect.");
            return deployment.getUploadUrl();
        }
    }

    public Deployment getDeployment()
    {
        Preconditions.checkNotNull(deployment, "CurseDeployTask parameter deployment must not be null if task is in single upload mode.");
        return deployment;
    }

    public CurseDeployTask deployment(final Closure closure)
    {
        deployment = new Deployment((CurseDeploy) getProject().getExtensions().getByName("curseDeploy"), getProject());
        if (closure != null)
        {
            closure.setDelegate(deployment);
            closure.setResolveStrategy(Closure.DELEGATE_ONLY);
            closure.run();
        }

        return this;
    }

    public boolean isSingleUpload()
    {
        return isSingleUpload;
    }

    public void setSingleUpload(final boolean isSingleUpload)
    {
        this.isSingleUpload = isSingleUpload;
    }
}
