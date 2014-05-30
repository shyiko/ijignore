/*
 * Copyright 2014 Stanley Shyiko
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
package com.github.shyiko.ijignore;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.PathUtil;
import com.intellij.util.indexing.FileContentIndexingVoter;
import org.eclipse.jgit.ignore.IgnoreNode;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author <a href="mailto:stanley.shyiko@gmail.com">Stanley Shyiko</a>
 */
public class IjignoreFileContentIndexingVoter implements FileContentIndexingVoter {

    private static final Logger LOG =
            Logger.getInstance("#com.github.shyiko.ijignore.IjignoreFileContentIndexingVoter");

    private static final String FILENAME = ".ijignore";

    private String basePath;
    private IgnoreNode ijignore;

    public IjignoreFileContentIndexingVoter(Project project) {
        VirtualFile baseDir = project.getBaseDir();
        if (baseDir != null && baseDir.isInLocalFileSystem()) {
            basePath = baseDir.getPath();
            VirtualFile ideaIgnoreFile = baseDir.findChild(FILENAME);
            if (ideaIgnoreFile != null) {
                try {
                    ijignore = parseIgnoreFile(ideaIgnoreFile);
                } catch (IOException e) {
                    LOG.error("Failed to load " + ideaIgnoreFile.getPresentableUrl(), e);
                }
            }
        }
    }

    private IgnoreNode parseIgnoreFile(VirtualFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        try {
            IgnoreNode ignoreNode = new IgnoreNode();
            ignoreNode.parse(inputStream);
            if (!ignoreNode.getRules().isEmpty()) {
                return ignoreNode;
            }
        } finally {
            inputStream.close();
        }
        return null;
    }

    @Override
    public boolean isIndexable(@NotNull VirtualFile file) {
        if (ijignore != null) {
            String filePath = PathUtil.getLocalPath(file);
            if (filePath != null && filePath.startsWith(basePath)) {
                String candidate = filePath.substring(basePath.length() + 1);
                if (ijignore.isIgnored(candidate, file.isDirectory()) == IgnoreNode.MatchResult.IGNORED) {
                    return false;
                }
            }
        }
        return true;
    }
}

