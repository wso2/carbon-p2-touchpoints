/*
* Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.wso2.carbon.extensions.touchpoint.actions;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.p2.engine.spi.ProvisioningAction;
import org.wso2.carbon.extensions.touchpoint.utils.Constants;

import java.io.File;
import java.util.Map;

/**
 * Create a directory.
 * The {runtime} placeholder in the path is replaced with the profile,
 * This is basically a copy of natives MkdirAction class(@see org.eclipse.equinox.internal.p2.touchpoint.natives
 * .actions.MkdirAction) with {runtime} placeholder support.
 *
 * @since 1.1.0
 */
public class MkdirAction extends ProvisioningAction {

    @Override
    public IStatus execute(Map<String, Object> parameters) {
        String path = (String) parameters.get(Constants.PARM_PATH);
        if (path == null) {
            return new Status(IStatus.ERROR, Constants.PLUGIN_ID, "Path is not defined");
        }

        String profile = parameters.get(Constants.PROFILE).toString();
        String runtime = profile.substring(profile.indexOf(Constants.PROFILE_END_CHAR) + 1, profile.length() - 1);
        path = path.replaceAll(Constants.RUNTIME_KEY, runtime);

        File dir = new File(path);
        // A created or existing directory is ok
        boolean isCreated = dir.mkdir();
        if (dir.isDirectory()) {
            return Status.OK_STATUS;
        }

        // mkdir could have failed because of permissions, or because of an existing file
        return new Status(IStatus.ERROR, Constants.PLUGIN_ID,
                          "mkdir " + isCreated + " due to permissions or because of an existing file");
    }

    @Override
    public IStatus undo(Map<String, Object> parameters) {
        String path = (String) parameters.get(Constants.PARM_PATH);
        if (path == null) {
            return new Status(IStatus.ERROR, Constants.PLUGIN_ID, "Path is not defined");
        }

        String profile = parameters.get(Constants.PROFILE).toString();
        String runtime = profile.substring(profile.indexOf(Constants.PROFILE_END_CHAR) + 1, profile.length() - 1);

        path = path.replaceAll(Constants.RUNTIME_KEY, runtime);

        File dir = new File(path);
        // although not perfect, it at least prevents a faulty mkdir to delete a file on undo
        // worst case is that an empty directory could be deleted
        boolean isDeleted = false;
        if (dir.isDirectory()) {
            isDeleted = dir.delete();
        }

        return new Status(IStatus.OK, Constants.PLUGIN_ID, "Undo successful. Deletion " + isDeleted);
    }
}
