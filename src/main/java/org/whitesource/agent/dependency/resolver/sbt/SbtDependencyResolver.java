package org.whitesource.agent.dependency.resolver.sbt;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.whitesource.agent.Constants;
import org.whitesource.agent.api.model.DependencyInfo;
import org.whitesource.agent.api.model.DependencyType;
import org.whitesource.agent.dependency.resolver.AbstractDependencyResolver;
import org.whitesource.agent.dependency.resolver.ResolutionResult;
import org.whitesource.agent.hash.ChecksumUtils;
import org.whitesource.agent.utils.Cli;
import org.whitesource.agent.utils.FilesScanner;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;

public class SbtDependencyResolver extends AbstractDependencyResolver {

    private static final String BUILD_SBT = "build.sbt";
    private static final String SCALA = "scala";
    private static final String SBT = "sbt";
    private static final String SCALA_EXTENSION = Constants.DOT + SCALA;
    private static final List<String> SCALA_SCRIPT_EXTENSION = Arrays.asList(SCALA_EXTENSION, Constants.DOT + SBT);
    private static final String COMPILE = "compile";
    protected static final String TARGET = "target";
    protected static final String RESOLUTION_CACHE = "resolution-cache";
    protected static final String REPORTS = "reports";
    protected static final String SUCCESS = "success";


    private final Logger logger = LoggerFactory.getLogger(SbtDependencyResolver.class);

    @Override
    protected ResolutionResult resolveDependencies(String projectFolder, String topLevelFolder, Set<String> bomFiles) {
        List<DependencyInfo> dependencies = collectDependencies(topLevelFolder);
        return new ResolutionResult(dependencies, getExcludes(), getDependencyType(), topLevelFolder);
    }

    private List<DependencyInfo> collectDependencies(String folderPath){
        List<File> xmlFiles = findXmlReport(folderPath);
        if (xmlFiles.size()== 0){
            xmlFiles = loadXmlReport(folderPath);
        }
        if (xmlFiles.size() != 0){
            return parseXmlReport(xmlFiles);
        }
        return new ArrayList<>();
    }

    /* looking for an xml file ending with '-compile.xml', which should be either in 'target/scala-{version number}/resolution-cache/reports'
     or 'target/scala-{version number}/sbt-{version-number}/resolution-cache/reports'.
     There are some cases where 2 files inside that folder end with '-compile.xml'.  in such case, the way to find the relevant is if its name
     contains the scala-version (which is part of the scala folder name), and that's relevant only if the xml file isn't inside 'sbt-{}' folder.
     */
    private List<File> findXmlReport(String folderPath){
        FilesScanner filesScanner = new FilesScanner();
        String[] includes = {"**" + fileSeparator + "target" + fileSeparator + "**" + fileSeparator + "" + RESOLUTION_CACHE + fileSeparator + REPORTS + fileSeparator + "*-compile.xml"};
        String[] excludes = {"**" + fileSeparator + "project" + fileSeparator + "**"};
        String[] directoryContent = filesScanner.getDirectoryContent(folderPath, includes, excludes, false, false);
        List<File> files = new LinkedList<>();
        for (String filePath : directoryContent){
            boolean add = true;
            File reportFile = new File(folderPath + fileSeparator + filePath);
            for (File file : files){
                if (file.getParent().equals(reportFile.getParent())){
                    add = false;
                    if (reportFile.getName().length() < file.getName().length()){
                        files.remove(file);
                        files.add(reportFile);
                        break;
                    }
                }
            }
            if (add){
                files.add(reportFile);
            }
        }
        return files;
        /*File targetFolder = new File(folderPath + fileSeparator + TARGET);
        if (targetFolder.isDirectory()){
            File scalaFolder = findFolder(targetFolder, SCALA);
            String pathToReports = fileSeparator + RESOLUTION_CACHE + fileSeparator + REPORTS;
            File reportsFolder = new File(scalaFolder.getAbsolutePath() + pathToReports);
            boolean insideSbtFolder = false;
            if (!reportsFolder.isDirectory()) {
                File sbtFolder = findFolder(scalaFolder, SBT);
                if (sbtFolder != null) {
                    reportsFolder = new File(sbtFolder.getAbsolutePath() + pathToReports);
                    if (!reportsFolder.isDirectory()) {
                        logger.warn("Can't find '*-compile.xml' report file");
                        return null;
                    }
                } else {
                    logger.warn("Can't find '*-compile.xml' report file");
                    return null;
                }
                insideSbtFolder = true;
            }
            String scalaVersion = scalaFolder.getName().split(Constants.DASH)[1];
            File[] xmlFiles = reportsFolder.listFiles(new XmlFileNameFilter(insideSbtFolder, scalaVersion));
            if (xmlFiles.length > 0) {
                return xmlFiles[0];
            }
        }
        logger.warn("Can't find '*-compile.xml' report file");
        return null;*/
    }

    private File findFolder(File parentFolder, String folderName){
        if (parentFolder != null) {
            File[] files = parentFolder.listFiles(new ScalaFileNameFilter(folderName));
            if (files.length > 0) {
                Arrays.sort(files, Collections.reverseOrder());
                File folder = files[0];
                if (folder.isDirectory()) {
                    return folder;
                }
            }
        }
        return null;
    }

    // creating the xml report using 'sbt "compile"' command
    private List<File> loadXmlReport(String folderPath){
        Cli cli = new Cli();
        List<String> compileOutput = cli.runCmd(folderPath, cli.getCommandParams(SBT, COMPILE));
        if (compileOutput != null){
            if (compileOutput.get(compileOutput.size()-1).contains(SUCCESS)){
                return findXmlReport(folderPath);
            }
        }
        logger.warn("Can't run '{} {}'", SBT, COMPILE);
        return new LinkedList<>();
    }

    private List<DependencyInfo> parseXmlReport(List<File> xmlReportFiles){
        List<DependencyInfo> dependencyInfoList = new LinkedList<>();
        Serializer serializer = new Persister();
        for (File xmlReportFile : xmlReportFiles) {
            Map<String, DependencyInfo> parentsMap = new HashMap<>();
            Map<String, List<String>> childrenMap = new HashMap<>();
            try {
                IvyReport ivyReport = serializer.read(IvyReport.class, xmlReportFile);
                // using these properties to identify root dependencies (having the project's root as their parent)
                String projectGroupId = ivyReport.getInfo().getGroupId();
                String projectArtifactId = ivyReport.getInfo().getArtifactId();
                for (Module dependency : ivyReport.getDependencies()) {
                    String groupId = dependency.getGroupId();
                    String artifactId = dependency.getArtifactId();
                    for (Revision revision : dependency.getRevisions()) {
                        // making sure this dependency's version is used (and not over-written by a newer version)
                        if (!revision.isIgnored()) {
                            String version = revision.getVersion();
                            //Artifact artifact = revision.getArtifacts().get(0); // resolving path to jar file
                            if (revision.getArtifacts().size() > 0 && revision.getArtifacts().get(0) != null) {
                                File jarFile = new File(revision.getArtifacts().get(0).getPathToJar());
                                if (jarFile.isFile()) {
                                    String sha1 = ChecksumUtils.calculateSHA1(jarFile);
                                    if (sha1 != null) {
                                        DependencyInfo dependencyInfo = new DependencyInfo(groupId, artifactId, version);
                                        dependencyInfo.setSha1(sha1);
                                        //dependencyInfo.setDependencyType(DependencyType.GRADLE);
                                        dependencyInfo.setFilename(jarFile.getName());
                                        dependencyInfo.setSystemPath(jarFile.getPath());
                                        String dependencyName = groupId + Constants.COLON + artifactId + Constants.COLON + version;
                                        parentsMap.put(dependencyName, dependencyInfo);
                                        for (Caller parent : revision.getParentsList()) {
                                            String parentGroupId = parent.getGroupId();
                                            String parentArtifactId = parent.getArtifactId();
                                            // if this dependency's parent is the root - no need to add is as a child...
                                            if (parentGroupId.equals(projectGroupId) == false && parentArtifactId.equals(projectArtifactId) == false) {
                                                String parentVersion = parent.getVersion();
                                                String parentName = parentGroupId + Constants.COLON + parentArtifactId + Constants.COLON + parentVersion;
                                                DependencyInfo parentDependencyInfo = parentsMap.get(parentName);
                                                if (parentDependencyInfo != null) { // the parent was already created - add it as a child
                                                    parentDependencyInfo.getChildren().add(dependencyInfo);
                                                } else { // add this dependency to the children map
                                                    if (childrenMap.get(dependencyName) == null) {
                                                        childrenMap.put(dependencyName, new ArrayList<>());
                                                    }
                                                    childrenMap.get(dependencyName).add(parentName);
                                                }
                                            } else { //... add it directly to the dependency info list
                                                dependencyInfoList.add(dependencyInfo);
                                            }
                                        }
                                    } else {
                                        logger.warn("Could not find SHA1 for {}-{}-{}", groupId, revision.getArtifacts().get(0), version);
                                    }
                                } else {
                                    logger.warn("Could not find jar file {}", jarFile.getPath());
                                }
                            } else {
                                logger.warn("Could not find artifact ID for {}-{}", groupId, version);
                            }
                        }
                    }
                }
                // building dependencies tree
                for (String child : childrenMap.keySet()) {
                    List<String> parents = childrenMap.get(child);
                    for (String parent : parents) {
                        if (!isDescendant(parentsMap.get(child), parentsMap.get(parent))) {
                            parentsMap.get(parent).getChildren().add(parentsMap.get(child));
                        }
                    }
                }
            } catch (Exception e) {
                logger.warn("Could not read {}: {}", xmlReportFile.getPath(), e.getMessage());
                logger.debug("stacktrace {}", e.getStackTrace());
            }
        }
        return dependencyInfoList;
    }

    // preventing circular dependencies by making sure the dependency is not a descendant of its own
    private boolean isDescendant(DependencyInfo ancestor, DependencyInfo descendant){
        for (DependencyInfo child : ancestor.getChildren()){
            if (child.equals(descendant)){
                return true;
            }
            return isDescendant(child, descendant);
        }
        return false;
    }

    @Override
    protected Collection<String> getExcludes() {
        Set<String> excludes = new HashSet<>();
        excludes.add(Constants.PATTERN + SCALA_EXTENSION);
        return excludes;
    }

    @Override
    public Collection<String> getSourceFileExtensions() {
        return SCALA_SCRIPT_EXTENSION;
    }

    @Override
    protected DependencyType getDependencyType() {
        return null;
    }

    @Override
    protected String getDependencyTypeName() {
        return SBT.toUpperCase();
    }

    @Override
    protected String[] getBomPattern() {
        return new String[]{Constants.PATTERN + BUILD_SBT};
    }

    @Override
    protected Collection<String> getLanguageExcludes() {
        return null;
    }
}

class ScalaFileNameFilter implements FilenameFilter {
    private String folderName;

    public ScalaFileNameFilter(String name){
        folderName = name;
    }

    @Override
    public boolean accept(File dir, String name) {
        return name.toLowerCase().startsWith(folderName);
    }
}

class XmlFileNameFilter implements FilenameFilter{
    private boolean insideSbt;
    private String scalaVersion;

    public XmlFileNameFilter(boolean insideSbt, String scalaVersion){
        this.insideSbt = insideSbt;
        this.scalaVersion = scalaVersion;
    }
    @Override
    public boolean accept(File file, String name){
        return name.toLowerCase().endsWith("-compile.xml") && (insideSbt || name.contains(scalaVersion));
    }
}