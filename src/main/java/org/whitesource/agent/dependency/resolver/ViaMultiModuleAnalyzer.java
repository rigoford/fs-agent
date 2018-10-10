package org.whitesource.agent.dependency.resolver;

import org.slf4j.Logger;
import org.whitesource.agent.Constants;
import org.whitesource.agent.utils.FilesScanner;
import org.whitesource.agent.utils.LoggerFactory;

import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * @author raz.nitzan
 */
public class ViaMultiModuleAnalyzer {

    /* --- Static Members --- */

    private static final String APP_PATH = "appPath";
    private static final String DEPENDENCY_MANAGER_PATH = "dependencyManagerPath";
    private static final String PROJECT_NAME = "projectName";

    /* --- Members --- */

    private final Logger logger = LoggerFactory.getLogger(ViaMultiModuleAnalyzer.class);
    private final Collection<String> buildExtensions = new HashSet<>(Arrays.asList(".jar", ".war", ".zip"));
    private AbstractDependencyResolver dependencyResolver;
    private String suffixOfBuild;
    private String scanDirectory;
    private String contentFileAppPaths;
    private Collection<String> bomFiles = new HashSet<>();

    /* --- Constructor --- */

    public ViaMultiModuleAnalyzer(String scanDirectory, AbstractDependencyResolver dependencyResolver, String suffixOfBuild, String contentFileAppPaths) {
        this.dependencyResolver = dependencyResolver;
        this.suffixOfBuild = suffixOfBuild;
        this.scanDirectory = scanDirectory;
        this.contentFileAppPaths = contentFileAppPaths;
        findBomFiles();
    }

    private void findBomFiles() {
        Collection<String> scanDirectoryCollection = new LinkedList<>();
        scanDirectoryCollection.add(scanDirectory);
        Collection<ResolvedFolder> topFolders = new FilesScanner().findTopFolders(scanDirectoryCollection, dependencyResolver.getBomPattern(), dependencyResolver.getExcludes());
        topFolders.forEach(topFolder -> topFolder.getTopFoldersFound().forEach((folder, bomFilesFound) -> this.bomFiles.addAll(bomFilesFound)));
    }

    public void writeFile() {
        try {
            File outputFile = new File(this.contentFileAppPaths);
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            bufferedWriter.write(DEPENDENCY_MANAGER_PATH + Constants.EQUALS + this.scanDirectory);
            bufferedWriter.write(System.lineSeparator());
            int counter = 1;
            for (String bomFile : bomFiles) {
                File parentFileOfBom = new File(bomFile).getParentFile();
                File buildFolder = new File(parentFileOfBom.getPath() + File.separator + this.suffixOfBuild);
                if (buildFolder.exists() && buildFolder.isDirectory() && buildFolder.listFiles() != null) {
                    Collection<File> filesWithBuildExtensions = Arrays.stream(buildFolder.listFiles()).filter(file -> {
                        for (String extension : buildExtensions) {
                            if (file.getName().endsWith(extension)) {
                                return true;
                            }
                        }
                        return false;
                    }).collect(Collectors.toList());
                    try {
                        if (filesWithBuildExtensions.size() >= 1) {
                            bufferedWriter.write(PROJECT_NAME + counter + Constants.EQUALS + parentFileOfBom.getAbsolutePath());
                            bufferedWriter.write(System.lineSeparator());
                            String appPathProperty = APP_PATH + counter + Constants.EQUALS;
                            if (filesWithBuildExtensions.size() == 1) {
                                File appPath = filesWithBuildExtensions.stream().findFirst().get();
                                appPathProperty += appPath.getAbsolutePath();
                            }
                            bufferedWriter.write(appPathProperty);
                            bufferedWriter.write(System.lineSeparator());
                            counter++;
                        }
                    } catch (IOException e) {
                        logger.warn("Failed to write to file: {}", this.contentFileAppPaths);
                    }
                }
            }
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            logger.warn("Failed to write to file: {}", this.contentFileAppPaths);
        }
    }

    public Collection<String> getBomFiles() {
        return this.bomFiles;
    }
}
