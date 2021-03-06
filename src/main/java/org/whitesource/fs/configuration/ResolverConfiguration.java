/**
 * Copyright (C) 2014 WhiteSource Ltd.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.whitesource.fs.configuration;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.whitesource.agent.ConfigPropertyKeys;
import org.whitesource.agent.dependency.resolver.go.GoDependencyManager;

import java.util.Arrays;

import static org.whitesource.agent.ConfigPropertyKeys.*;

public class ResolverConfiguration {


    /* --- Constructors --- */

    @JsonCreator
    public ResolverConfiguration(
            @JsonProperty(NPM_RUN_PRE_STEP) boolean npmRunPreStep,
            @JsonProperty(NPM_RESOLVE_DEPENDENCIES) boolean npmResolveDependencies,
            @JsonProperty(NPM_IGNORE_SCRIPTS) boolean npmIgnoreScripts,
            @JsonProperty(NPM_INCLUDE_DEV_DEPENDENCIES) boolean npmIncludeDevDependencies,
            @JsonProperty(NPM_IGNORE_SOURCE_FILES) boolean npmIgnoreSourceFiles,
            @JsonProperty(NPM_TIMEOUT_DEPENDENCIES_COLLECTOR_SECONDS) long npmTimeoutDependenciesCollector,
            @JsonProperty(NPM_ACCESS_TOKEN) String npmAccessToken,
            @JsonProperty(NPM_IGNORE_NPM_LS_ERRORS) boolean npmIgnoreNpmLsErrors,
            @JsonProperty(NPM_YARN_PROJECT) boolean npmYarnProject,

            @JsonProperty(BOWER_RESOLVE_DEPENDENCIES) boolean bowerResolveDependencies,
            @JsonProperty(BOWER_RUN_PRE_STEP) boolean bowerRunPreStep,
            @JsonProperty(BOWER_IGNORE_SOURCE_FILES) boolean bowerIgnoreSourceFiles,

            @JsonProperty(NUGET_RESOLVE_DEPENDENCIES) boolean nugetResolveDependencies,
            @JsonProperty(NUGET_RESTORE_DEPENDENCIES) boolean nugetRestoreDependencies,
            @JsonProperty(NUGET_RUN_PRE_STEP) boolean nugetRunPreStep,
            @JsonProperty(NUGET_IGNORE_SOURCE_FILES) boolean nugetIgnoreSourceFiles,

            @JsonProperty(MAVEN_RESOLVE_DEPENDENCIES) boolean mavenResolveDependencies,
            @JsonProperty(MAVEN_IGNORED_SCOPES) String[] mavenIgnoredScopes,
            @JsonProperty(MAVEN_AGGREGATE_MODULES) boolean mavenAggregateModules,
            @JsonProperty(MAVEN_IGNORE_POM_MODULES) boolean mavenIgnorePomModules,
            @JsonProperty(MAVEN_IGNORE_SOURCE_FILES) boolean mavenIgnoreSourceFiles,
            @JsonProperty(MAVEN_RUN_PRE_STEP) boolean mavenRunPreStep,
            @JsonProperty(MAVEN_IGNORE_DEPENDENCY_TREE_ERRORS) boolean mavenIgnoreDependencyTreeErrors,

            @JsonProperty(PYTHON_RESOLVE_DEPENDENCIES) boolean pythonResolveDependencies,
            @JsonProperty(PYTHON_PIP_PATH) String pipPath,
            @JsonProperty(PYTHON_PATH) String pythonPath,
            @JsonProperty(PYTHON_IS_WSS_PLUGIN_INSTALLED) boolean pythonIsWssPluginInstalled,
            @JsonProperty(PYTHON_UNINSTALL_WSS_PLUGIN) boolean pythonUninstallWssPlugin,
            @JsonProperty(PYTHON_IGNORE_PIP_INSTALL_ERRORS) boolean pythonIgnorePipInstallErrors,
            @JsonProperty(PYTHON_INSTALL_VIRTUALENV) boolean pythonInstallVirtualenv,
            @JsonProperty(PYTHON_RESOLVE_HIERARCHY_TREE) boolean pythonResolveHierarchyTree,
            @JsonProperty(PYTHON_REQUIREMENTS_FILE_INCLUDES) String[] pythonRequirementsFileIncludes,
            @JsonProperty(PYTHON_RESOLVE_SETUP_PY_FILES) boolean pythonResolveSetupPyFiles,
            @JsonProperty(PYTHON_IGNORE_SOURCE_FILES) boolean pythonIgnoreSourceFiles,
            @JsonProperty(PYTHON_IGNORE_PIPENV_INSTALL_ERRORS) boolean ignorePipEnvInstallErrors,
            @JsonProperty(PYTHON_RUN_PIPENV_PRE_STEP) boolean runPipenvPreStep,
            @JsonProperty(PYTHON_PIPENV_DEV_DEPENDENCIES) boolean pipenvInstallDevDependencies,
            @JsonProperty(IGNORE_SOURCE_FILES) boolean ignoreSourceFiles,
//            @JsonProperty(DEPENDENCIES_ONLY) boolean dependenciesOnly,
            @JsonProperty(WHITESOURCE_CONFIGURATION) String whitesourceConfiguration,

            @JsonProperty(GRADLE_RESOLVE_DEPENDENCIES) boolean gradleResolveDependencies,
            @JsonProperty(GRADLE_RUN_ASSEMBLE_COMMAND) boolean gradleRunAssembleCommand,
            @JsonProperty(GRADLE_AGGREGATE_MODULES) boolean gradleAggregateModules,
            @JsonProperty(GRADLE_PREFERRED_ENVIRONMENT) String gradlePreferredEnvironment,
            @JsonProperty(GRADLE_IGNORE_SOURCE_FILES) boolean gradleIgnoreSourceFiles,
            @JsonProperty(GRADLE_RUN_PRE_STEP) boolean gradleRunPreStep,
            @JsonProperty(GRADLE_IGNORE_SCOPES) String[] gradleIgnoredScopes,

            @JsonProperty(PAKET_RESOLVE_DEPENDENCIES) boolean paketResolveDependencies,
            @JsonProperty(PAKET_IGNORED_GROUPS) String[] paketIgnoredScopes,
            @JsonProperty(PAKET_RUN_PRE_STEP) boolean paketRunPreStep,
            @JsonProperty(PAKET_EXE_PATH) String paketPath,
            @JsonProperty(PAKET_IGNORE_SOURCE_FILES) boolean paketIgnoreSourceFiles,

            @JsonProperty(GO_RESOLVE_DEPENDENCIES) boolean goResolveDependencies,
            @JsonProperty(GO_DEPENDENCY_MANAGER) String goDependencyManager,
            @JsonProperty(GO_COLLECT_DEPENDENCIES_AT_RUNTIME) boolean goCollectDependenciesAtRuntime,
            @JsonProperty(GO_GLIDE_IGNORE_TEST_PACKAGES) boolean goIgnoreTestPackages,
            @JsonProperty(GO_IGNORE_SOURCE_FILES) boolean goIgnoreSourceFiles,
            @JsonProperty(GO_GRADLE_ENABLE_TASK_ALIAS) boolean goGradleEnableTaskAlias,

            @JsonProperty(RUBY_RESOLVE_DEPENDENCIES) boolean rubyResolveDependencies,
            @JsonProperty(RUBY_RUN_BUNDLE_INSTALL) boolean rubyRunBundleInstall,
            @JsonProperty(RUBY_OVERWRITE_GEM_FILE) boolean rubyOverwriteGemFile,
            @JsonProperty(RUBY_INSTALL_MISSING_GEMS) boolean rubyInstallMissingGems,
            @JsonProperty(RUBY_IGNORE_SOURCE_FILES) boolean rubyIgnoreSourceFiles,

            @JsonProperty(PHP_RESOLVE_DEPENDENCIES) boolean phpResolveDependencies,
            @JsonProperty(PHP_RUN_PRE_STEP) boolean phpRunPreStep,
            @JsonProperty(PHP_INCLUDE_DEV_DEPENDENCIES) boolean phpIncludeDevDependencies,

            @JsonProperty(SBT_RESOLVE_DEPENDENCIES) boolean sbtResolveDependencies,
            @JsonProperty(SBT_AGGREGATE_MODULES) boolean sbtAggregateModules,
            @JsonProperty(SBT_RUN_PRE_STEP) boolean sbtRunPreStep,
            @JsonProperty(SBT_TARGET_FOLDER) String sbtTargetFolder,
            @JsonProperty(SBT_IGNORE_SOURCE_FILES) boolean sbtIgnoreSourceFiles,

            @JsonProperty(HTML_RESOLVE_DEPENDENCIES) boolean htmlResolveDependencies,
            @JsonProperty(COCOAPODS_RESOLVE_DEPENDENCIES) boolean cocoapodsResolveDependencies,
            @JsonProperty(COCOAPODS_RUN_PRE_STEP) boolean cocoapodsRunPreStep,
            @JsonProperty(COCOAPODS_IGNORE_SOURCE_FILES) boolean cocoapodsIgnoreSourceFiles,
            @JsonProperty(HEX_RESOLVE_DEPENDENECIES) boolean hexResolveDependencies,
            @JsonProperty(HEX_RUN_PRE_STEP) boolean hexRunPreStep,
            @JsonProperty(HEX_IGNORE_SOURCE_FILES) boolean hexIgnoreSourceFiles,
            @JsonProperty(HEX_AGGREGATE_MODULES) boolean hexAggregateModules,
            @JsonProperty("addSha1") boolean addSha1) {
        this.npmRunPreStep                      = npmRunPreStep;
        this.npmIgnoreScripts                   = npmIgnoreScripts;
        this.npmResolveDependencies             = npmResolveDependencies;
        this.npmIncludeDevDependencies          = npmIncludeDevDependencies;
        this.npmTimeoutDependenciesCollector    = npmTimeoutDependenciesCollector;
        this.npmAccessToken                     = npmAccessToken;
        this.npmIgnoreNpmLsErrors               = npmIgnoreNpmLsErrors;
        this.npmYarnProject                     = npmYarnProject;
        this.npmIgnoreSourceFiles               = npmIgnoreSourceFiles;

        this.bowerResolveDependencies   = bowerResolveDependencies;
        this.bowerRunPreStep            = bowerRunPreStep;
        this.bowerIgnoreSourceFiles               = bowerIgnoreSourceFiles;

        this.nugetResolveDependencies = nugetResolveDependencies;
        this.nugetRestoreDependencies = nugetRestoreDependencies;
        this.nugetRunPreStep          = nugetRunPreStep;
        this.nugetIgnoreSourceFiles   = nugetIgnoreSourceFiles;

        this.mavenResolveDependencies   = mavenResolveDependencies;
        this.mavenIgnoredScopes         = mavenIgnoredScopes;
        this.mavenAggregateModules      = mavenAggregateModules;
        this.mavenIgnorePomModules      = mavenIgnorePomModules;
        this.mavenIgnoreSourceFiles     = mavenIgnoreSourceFiles;
        this.mavenRunPreStep            = mavenRunPreStep;
        this.mavenIgnoreDependencyTreeErrors = mavenIgnoreDependencyTreeErrors;

        this.pythonResolveDependencies      = pythonResolveDependencies;
        this.pipPath                        = pipPath;
        this.pythonPath                     = pythonPath;
        this.pythonIsWssPluginInstalled     = pythonIsWssPluginInstalled;
        this.pythonUninstallWssPlugin       = pythonUninstallWssPlugin;
        this.pythonIgnorePipInstallErrors   = pythonIgnorePipInstallErrors;
        this.pythonInstallVirtualenv        = pythonInstallVirtualenv;
        this.pythonResolveHierarchyTree     = pythonResolveHierarchyTree;
        this.pythonRequirementsFileIncludes = pythonRequirementsFileIncludes;
        this.pythonResolveSetupPyFiles      = pythonResolveSetupPyFiles;
        this.pythonIgnoreSourceFiles        = pythonIgnoreSourceFiles;
        this.ignorePipEnvInstallErrors      = ignorePipEnvInstallErrors;
        this.runPipenvPreStep               = runPipenvPreStep;
        this.pipenvInstallDevDependencies   = pipenvInstallDevDependencies;
        this.ignoreSourceFiles          = ignoreSourceFiles;
        this.whitesourceConfiguration   = whitesourceConfiguration;

        this.gradleResolveDependencies  = gradleResolveDependencies;
        this.gradleAggregateModules     = gradleAggregateModules;
        this.gradleRunAssembleCommand   = gradleRunAssembleCommand;
        this.gradlePreferredEnvironment = gradlePreferredEnvironment;
        this.gradleIgnoreSourceFiles    = gradleIgnoreSourceFiles;
        this.gradleRunPreStep           = gradleRunPreStep;
        this.gradleIgnoreSourceFiles    = gradleIgnoreSourceFiles;
        this.gradleIgnoredScopes        = gradleIgnoredScopes;

        this.paketResolveDependencies   = paketResolveDependencies;
        this.paketIgnoredScopes         = paketIgnoredScopes;
        this.paketRunPreStep            = paketRunPreStep;
        this.paketPath                  = paketPath;
        this.paketIgnoreSourceFiles     = paketIgnoreSourceFiles;

        this.goResolveDependencies = goResolveDependencies;
        if (goDependencyManager != null && !goDependencyManager.isEmpty()) {
            this.goDependencyManager = GoDependencyManager.getFromType(goDependencyManager);
        }
        this.goCollectDependenciesAtRuntime = goCollectDependenciesAtRuntime;
        this.goIgnoreTestPackages           = goIgnoreTestPackages;
        this.goIgnoreSourceFiles            = goIgnoreSourceFiles;
        this.goGradleEnableTaskAlias        = goGradleEnableTaskAlias;

        this.rubyResolveDependencies    = rubyResolveDependencies;
        this.rubyRunBundleInstall       = rubyRunBundleInstall;
        this.rubyOverwriteGemFile       = rubyOverwriteGemFile;
        this.rubyInstallMissingGems     = rubyInstallMissingGems;
        this.rubyIgnoreSourceFiles      = rubyIgnoreSourceFiles;

        this.phpResolveDependencies     = phpResolveDependencies;
        this.phpRunPreStep              = phpRunPreStep;
        this.phpIncludeDevDependencies  = phpIncludeDevDependencies;

        this.sbtResolveDependencies = sbtResolveDependencies;
        this.sbtAggregateModules    = sbtAggregateModules;
        this.sbtRunPreStep          = sbtRunPreStep;
        this.sbtTargetFolder        = sbtTargetFolder;
        this.sbtIgnoreSourceFiles   = sbtIgnoreSourceFiles;

        this.htmlResolveDependencies = htmlResolveDependencies;

        this.cocoapodsResolveDependencies   = cocoapodsResolveDependencies;
        this.cocoapodsRunPreStep            = cocoapodsRunPreStep;
        this.cocoapodsIgnoreSourceFiles     = cocoapodsIgnoreSourceFiles;

        this.hexResolveDependencies = hexResolveDependencies;
        this.hexRunPreStep          = hexRunPreStep;
        this.hexIgnoreSourceFiles   = hexIgnoreSourceFiles;
        this.hexAggregateModules    = hexAggregateModules;

        this.addSha1 = addSha1;
    }

    /* --- Members --- */

    private boolean npmRunPreStep;
    private boolean npmIgnoreScripts;
    private boolean npmResolveDependencies;
    private boolean npmIncludeDevDependencies;
    private String npmAccessToken;
    private long npmTimeoutDependenciesCollector;
    private boolean npmIgnoreNpmLsErrors;
    private boolean npmYarnProject;
    private boolean npmIgnoreSourceFiles;


    private boolean bowerResolveDependencies;
    private boolean bowerRunPreStep;
    private boolean bowerIgnoreSourceFiles;

    private boolean nugetResolveDependencies;
    private boolean nugetRestoreDependencies;
    private boolean nugetRunPreStep;
    private boolean nugetIgnoreSourceFiles;

    private boolean mavenResolveDependencies;
    private String[] mavenIgnoredScopes;
    private boolean mavenAggregateModules;
    private boolean mavenIgnorePomModules;
    private boolean mavenIgnoreSourceFiles;
    private boolean mavenRunPreStep;

    private boolean mavenIgnoreDependencyTreeErrors;

//    private boolean dependenciesOnly;
    private boolean ignoreSourceFiles;
    private String whitesourceConfiguration;
    private boolean pythonResolveDependencies;
    private String pipPath;
    private String pythonPath;
    private boolean pythonIgnorePipInstallErrors;
    private boolean pythonInstallVirtualenv;
    private boolean pythonResolveHierarchyTree;
    private String[] pythonRequirementsFileIncludes;
    private boolean pythonResolveSetupPyFiles;
    private boolean pythonIgnoreSourceFiles;
    private boolean ignorePipEnvInstallErrors;


    private boolean pipenvInstallDevDependencies;
    private boolean runPipenvPreStep;
    private boolean gradleResolveDependencies;
    private boolean gradleRunAssembleCommand;
    private boolean gradleAggregateModules;
    private String gradlePreferredEnvironment;
    private boolean gradleIgnoreSourceFiles;
    private boolean gradleRunPreStep;
    private String[] gradleIgnoredScopes;

    private final boolean pythonIsWssPluginInstalled;
    private final boolean pythonUninstallWssPlugin;

    private boolean paketResolveDependencies;
    private String[] paketIgnoredScopes;
    private boolean paketRunPreStep;
    private String paketPath;
    private boolean paketIgnoreSourceFiles;

    private boolean goResolveDependencies;
    private GoDependencyManager goDependencyManager;
    private boolean goCollectDependenciesAtRuntime;
    private boolean goIgnoreTestPackages;
    private boolean goIgnoreSourceFiles;
    private boolean goGradleEnableTaskAlias;

    private boolean rubyResolveDependencies;
    private boolean rubyRunBundleInstall;
    private boolean rubyOverwriteGemFile;
    private boolean rubyInstallMissingGems;
    private boolean rubyIgnoreSourceFiles;

    private boolean phpResolveDependencies;
    private boolean phpRunPreStep;
    private boolean phpIncludeDevDependencies;

    private boolean sbtResolveDependencies;
    private boolean sbtAggregateModules;
    private boolean sbtRunPreStep;
    private String sbtTargetFolder;
    private boolean sbtIgnoreSourceFiles;

    private boolean htmlResolveDependencies;

    private boolean cocoapodsResolveDependencies;
    private boolean cocoapodsRunPreStep;
    private boolean cocoapodsIgnoreSourceFiles;

    private boolean hexResolveDependencies;
    private boolean hexRunPreStep;
    private boolean hexAggregateModules;
    private boolean hexIgnoreSourceFiles;

    private boolean addSha1;

    /* --- Public getters --- */

    @JsonProperty(NPM_RUN_PRE_STEP)
    public boolean isNpmRunPreStep() {
        return npmRunPreStep;
    }

    @JsonProperty(NPM_IGNORE_SCRIPTS)
    public boolean isNpmIgnoreScripts() {
        return npmIgnoreScripts;
    }

    @JsonProperty(NPM_RESOLVE_DEPENDENCIES)
    public boolean isNpmResolveDependencies() {
        return npmResolveDependencies;
    }

    @JsonProperty(NPM_INCLUDE_DEV_DEPENDENCIES)
    public boolean isNpmIncludeDevDependencies() {
        return npmIncludeDevDependencies;
    }

    @JsonProperty(NPM_IGNORE_SOURCE_FILES)
    public boolean isNpmIgnoreSourceFiles() { return npmIgnoreSourceFiles; }

    @JsonProperty(NPM_TIMEOUT_DEPENDENCIES_COLLECTOR_SECONDS)
    public long getNpmTimeoutDependenciesCollector() {
        return npmTimeoutDependenciesCollector;
    }

    @JsonProperty(NPM_ACCESS_TOKEN)
    public String getNpmAccessToken() {
        return npmAccessToken;
    }

    @JsonProperty(NPM_IGNORE_NPM_LS_ERRORS)
    public boolean getNpmIgnoreNpmLsErrors() {
        return npmIgnoreNpmLsErrors;
    }

    @JsonProperty(NPM_YARN_PROJECT)
    public boolean getNpmYarnProject() {
        return npmYarnProject;
    }

    @JsonProperty(BOWER_RESOLVE_DEPENDENCIES)
    public boolean isBowerResolveDependencies() {
        return bowerResolveDependencies;
    }

    @JsonProperty(BOWER_RUN_PRE_STEP)
    public boolean isBowerRunPreStep() {
        return bowerRunPreStep;
    }

    @JsonProperty(BOWER_IGNORE_SOURCE_FILES)
    public boolean isBowerIgnoreSourceFiles() { return bowerIgnoreSourceFiles; }

    @JsonProperty(NUGET_RESOLVE_DEPENDENCIES)
    public boolean isNugetResolveDependencies() {
        return nugetResolveDependencies;
    }

    @JsonProperty(NUGET_RESTORE_DEPENDENCIES)
    public boolean isNugetRestoreDependencies() {
        return nugetRestoreDependencies;
    }

    @JsonProperty(NUGET_RUN_PRE_STEP)
    public boolean isNugetRunPreStep() {
        return nugetRunPreStep;
    }

    @JsonProperty(NUGET_IGNORE_SOURCE_FILES)
    public boolean isNugetIgnoreSourceFiles() { return nugetIgnoreSourceFiles; }

    @JsonProperty(MAVEN_RESOLVE_DEPENDENCIES)
    public boolean isMavenResolveDependencies() {
        return mavenResolveDependencies;
    }

    @JsonProperty(MAVEN_IGNORED_SCOPES)
    public String[] getMavenIgnoredScopes() {
        return mavenIgnoredScopes;
    }

    @JsonProperty(MAVEN_AGGREGATE_MODULES)
    public boolean isMavenAggregateModules() {
        return mavenAggregateModules;
    }

    @JsonProperty(MAVEN_IGNORE_POM_MODULES)
    public boolean isMavenIgnorePomModules() { return mavenIgnorePomModules; }

    @JsonProperty(MAVEN_IGNORE_SOURCE_FILES)
    public boolean isMavenIgnoreSourceFiles() { return mavenIgnoreSourceFiles; }

    @JsonProperty(MAVEN_RUN_PRE_STEP)
    public boolean isMavenRunPreStep() { return mavenRunPreStep; }

    @JsonProperty(MAVEN_IGNORE_DEPENDENCY_TREE_ERRORS)
    public boolean isMavenIgnoreDependencyTreeErrors() { return mavenIgnoreDependencyTreeErrors; }

    @JsonProperty(IGNORE_SOURCE_FILES)
    public boolean isIgnoreSourceFiles() {
        return ignoreSourceFiles;
    }

    @JsonProperty(WHITESOURCE_CONFIGURATION)
    public String getWhitesourceConfiguration() {
        return whitesourceConfiguration;
    }

    @JsonProperty(PYTHON_RESOLVE_DEPENDENCIES)
    public boolean isPythonResolveDependencies() {
        return pythonResolveDependencies;
    }

    @JsonProperty(PYTHON_PIP_PATH)
    public String getPipPath() {
        return pipPath;
    }

    @JsonProperty(PYTHON_PATH)
    public String getPythonPath() {
        return pythonPath;
    }

    @JsonProperty(PYTHON_IGNORE_PIP_INSTALL_ERRORS)
    public boolean isPythonIgnorePipInstallErrors() {
        return pythonIgnorePipInstallErrors;
    }

    @JsonProperty(PYTHON_IS_WSS_PLUGIN_INSTALLED)
    public boolean isPythonIsWssPluginInstalled() {
        return pythonIsWssPluginInstalled;
    }

    @JsonProperty(PYTHON_UNINSTALL_WSS_PLUGIN)
    public boolean getPythonUninstallWssPlugin() {
        return pythonUninstallWssPlugin;
    }

    @JsonProperty(PYTHON_INSTALL_VIRTUALENV)
    public boolean isPythonInstallVirtualenv() {
        return pythonInstallVirtualenv;
    }

    @JsonProperty(PYTHON_RESOLVE_HIERARCHY_TREE)
    public boolean isPythonResolveHierarchyTree() {
        return pythonResolveHierarchyTree;
    }

    @JsonProperty(PYTHON_RESOLVE_SETUP_PY_FILES)
    public boolean isPythonResolveSetupPyFiles() {
        return this.pythonResolveSetupPyFiles;
    }

    public String[] getPythonRequirementsFileIncludes() {
        return pythonRequirementsFileIncludes;
    }

    @JsonProperty(PYTHON_IGNORE_SOURCE_FILES)
    public boolean isPythonIgnoreSourceFiles() { return pythonIgnoreSourceFiles; }

    @JsonProperty(PYTHON_RUN_PIPENV_PRE_STEP)
    public boolean IsRunPipenvPreStep() {
        return this.runPipenvPreStep;
    }
    @JsonProperty(PYTHON_IGNORE_PIPENV_INSTALL_ERRORS)
    public boolean isIgnorePipEnvInstallErrors() {
        return this.ignorePipEnvInstallErrors;
    }
    @JsonProperty(PYTHON_PIPENV_DEV_DEPENDENCIES)
    public boolean isPipenvInstallDevDependencies() {
        return pipenvInstallDevDependencies;
    }
    @JsonProperty(GRADLE_RESOLVE_DEPENDENCIES)
    public boolean isGradleResolveDependencies() {
        return gradleResolveDependencies;
    }

    @JsonProperty(GRADLE_AGGREGATE_MODULES)
    public boolean isGradleAggregateModules() {
        return gradleAggregateModules;
    }

    @JsonProperty(GRADLE_RUN_ASSEMBLE_COMMAND)
    public boolean isGradleRunAssembleCommand() {
        return gradleRunAssembleCommand;
    }

    @JsonProperty(GRADLE_PREFERRED_ENVIRONMENT)
    public String getGradlePreferredEnvironment() { return gradlePreferredEnvironment; }

    @JsonProperty(GRADLE_IGNORE_SOURCE_FILES)
    public boolean isGradleIgnoreSourceFiles() { return gradleIgnoreSourceFiles; }

    @JsonProperty(GRADLE_RUN_PRE_STEP)
    public boolean isGradleRunPreStep() { return gradleRunPreStep; }

    @JsonProperty(PAKET_RESOLVE_DEPENDENCIES)
    public boolean isPaketResolveDependencies() {
        return paketResolveDependencies;
    }

    @JsonProperty(PAKET_IGNORED_GROUPS)
    public String[] getPaketIgnoredScopes() {
        return paketIgnoredScopes;
    }

    @JsonProperty(PAKET_RUN_PRE_STEP)
    public boolean isPaketRunPreStep() {
        return paketRunPreStep;
    }

    @JsonProperty(PAKET_EXE_PATH)
    public String getPaketPath() {
        return paketPath;
    }

    @JsonProperty(PAKET_IGNORE_SOURCE_FILES)
    public boolean isPaketIgnoreSourceFiles() { return paketIgnoreSourceFiles; }

    @JsonProperty(GO_RESOLVE_DEPENDENCIES)
    public boolean isGoResolveDependencies() {
        return goResolveDependencies;
    }

    @JsonProperty(GO_DEPENDENCY_MANAGER)
    public GoDependencyManager getGoDependencyManager() {
        return goDependencyManager;
    }

    @JsonProperty(GO_COLLECT_DEPENDENCIES_AT_RUNTIME)
    public boolean isGoCollectDependenciesAtRuntime() {
        return goCollectDependenciesAtRuntime;
    }

    @JsonProperty(GO_GLIDE_IGNORE_TEST_PACKAGES)
    public boolean isGoIgnoreTestPackages() {
        return goIgnoreTestPackages;
    }

    @JsonProperty(GO_IGNORE_SOURCE_FILES)
    public boolean isGoIgnoreSourceFiles() { return goIgnoreSourceFiles; }

    @JsonProperty(GO_GRADLE_ENABLE_TASK_ALIAS)
    public boolean isGoGradleEnableTaskAlias(){  return goGradleEnableTaskAlias;    }

    @JsonProperty(RUBY_RESOLVE_DEPENDENCIES)
    public boolean isRubyResolveDependencies() {
        return rubyResolveDependencies;
    }

    @JsonProperty(RUBY_RUN_BUNDLE_INSTALL)
    public boolean isRubyRunBundleInstall() {
        return rubyRunBundleInstall;
    }

    @JsonProperty(RUBY_OVERWRITE_GEM_FILE)
    public boolean isRubyOverwriteGemFile() {
        return rubyOverwriteGemFile;
    }

    @JsonProperty(RUBY_INSTALL_MISSING_GEMS)
    public boolean isRubyInstallMissingGems() {
        return rubyInstallMissingGems;
    }

    @JsonProperty(RUBY_IGNORE_SOURCE_FILES)
    public boolean isRubyIgnoreSourceFiles() { return rubyIgnoreSourceFiles; }

    @JsonProperty(PHP_RESOLVE_DEPENDENCIES)
    public boolean isPhpResolveDependencies() {
        return phpResolveDependencies;
    }

    @JsonProperty(PHP_RUN_PRE_STEP)
    public boolean isPhpRunPreStep() {
        return phpRunPreStep;
    }

    @JsonProperty(PHP_INCLUDE_DEV_DEPENDENCIES)
    public boolean isPhpIncludeDevDependencies() {
        return phpIncludeDevDependencies;
    }

    @JsonProperty(SBT_RESOLVE_DEPENDENCIES)
    public boolean isSbtResolveDependencies() {
        return sbtResolveDependencies;
    }

    @JsonProperty(SBT_AGGREGATE_MODULES)
    public boolean isSbtAggregateModules() {
        return sbtAggregateModules;
    }

    @JsonProperty(SBT_RUN_PRE_STEP)
    public boolean isSbtRunPreStep() {
        return sbtRunPreStep;
    }

    @JsonProperty(SBT_TARGET_FOLDER)
    public String getSbtTargetFolder() {
        return sbtTargetFolder;
    }

    @JsonProperty(SBT_IGNORE_SOURCE_FILES)
    public boolean isSbtIgnoreSourceFiles() { return sbtIgnoreSourceFiles; }

    @JsonProperty(HTML_RESOLVE_DEPENDENCIES)
    public boolean isHtmlResolveDependencies() {
        return htmlResolveDependencies;
    }

    @JsonProperty(COCOAPODS_RESOLVE_DEPENDENCIES)
    public boolean isCocoapodsResolveDependencies() {
        return cocoapodsResolveDependencies;
    }

    @JsonProperty(COCOAPODS_RUN_PRE_STEP)
    public boolean isCocoapodsRunPreStep() {
        return cocoapodsRunPreStep;
    }

    @JsonProperty(COCOAPODS_IGNORE_SOURCE_FILES)
    public boolean isCocoapodsIgnoreSourceFiles() { return cocoapodsIgnoreSourceFiles; }

    @JsonProperty(HEX_RESOLVE_DEPENDENECIES)
    public boolean isHexResolveDependencies(){
        return hexResolveDependencies;
    }

    @JsonProperty(HEX_IGNORE_SOURCE_FILES)
    public boolean isHexIgnoreSourceFiles(){
        return hexIgnoreSourceFiles;
    }

    @JsonProperty(HEX_RUN_PRE_STEP)
    public boolean isHexRunPreStep(){
        return hexRunPreStep;
    }

    @JsonProperty(HEX_AGGREGATE_MODULES)
    public boolean isHexAggregateModules(){
        return hexAggregateModules;
    }

    public String[] getGradleIgnoredScopes() {
        return this.gradleIgnoredScopes;
    }

    public boolean isAddSha1(){ return addSha1; }

    @Override
    public String toString() {
        return "ignoreSourceFiles/dependenciesOnly=" + ignoreSourceFiles + '\n' +
//                ", dependenciesOnly=" + dependenciesOnly +
                "NPM:\n" +
                "npm.runPreStep=" + npmRunPreStep +
                ", npm.ignoreScripts=" + npmIgnoreScripts +
                ", npm.resolveDependencies= " + npmResolveDependencies +
                ", npm.includeDevDependencies= " + npmIncludeDevDependencies + '\n' +
                "npm.ignoreSourceFiles=" + npmIgnoreSourceFiles +
                ", npm.timeoutDependenciesCollectorInSeconds=" + npmTimeoutDependenciesCollector +
                ", npm.ignoreNpmLsErrors=" + npmIgnoreNpmLsErrors +
                ", npm.yarnProject=" + npmYarnProject + '\n' +

                "BOWER:\n" +
                "bower.resolveDependencies=" + bowerResolveDependencies +
                ", bower.runPreStep=" + bowerRunPreStep +
                ", bower.ignoreSourceFiles=" + bowerIgnoreSourceFiles + '\n' +

                "NUGET:\n" +
                "nuget.resolveDependencies=" + nugetResolveDependencies +
                ", nuget.ignoreSourceFiles=" + nugetIgnoreSourceFiles +
                ", nuget.restoreDependencies=" + nugetRestoreDependencies +
                ", nuget.runPreStep=" + nugetRunPreStep + '\n' +

                "MAVEN:\n" +
                "maven.resolveDependencies=" + mavenResolveDependencies +
                ", maven.ignoredScopes=" + Arrays.toString(mavenIgnoredScopes) +
                ", maven.aggregateModules=" + mavenAggregateModules +
                ", maven.ignoreSourceFiles=" + mavenIgnoreSourceFiles + '\n' +
                "maven.runPreStep=" + mavenRunPreStep +
                ", maven.ignorePomModules=" + mavenIgnorePomModules +
                ", maven.ignoreMvnTreeErrors=" + mavenIgnoreDependencyTreeErrors + '\n' +

                "PYTHON:\n" +
                "python.resolveDependencies=" + pythonResolveDependencies +
                ", python.ignorePipInstallErrors=" + pythonIgnorePipInstallErrors +
                ", python.installVirtualenv=" + pythonInstallVirtualenv +
                ", python.resolveHierarchyTree=" + pythonResolveHierarchyTree + '\n' +
                "python.resolveSetupPyFiles=" + pythonResolveSetupPyFiles +
                ", python.requirementsFileIncludes=" + Arrays.toString(pythonRequirementsFileIncludes) +
                ", python.ignoreSourceFiles=" + pythonIgnoreSourceFiles +
                ", python.ignorePipInstallErrors=" + ignorePipEnvInstallErrors + '\n' +
                "python.runPipenvPreStep=" + runPipenvPreStep +
                ", python.pipPath=" + pipPath +
                ", python.path=" + pythonPath +
                ", python.pipenvDevDependencies=" + pipenvInstallDevDependencies + '\n' +
                "python.isWssPluginInstalled=" + pythonIsWssPluginInstalled +
                ", python.uninstallWssPlugin=" + pythonUninstallWssPlugin + '\n' +

                "GRADLE:\n" +
                "gradle.resolveDependencies=" + gradleResolveDependencies +
                ", gradle.runAssembleCommand=" + gradleRunAssembleCommand +
                ", gradle.aggregateModules=" + gradleAggregateModules +
                ", gradle.ignoreSourceFiles=" + gradleIgnoreSourceFiles + '\n' +
                "gradle.runPreStep=" + gradleRunPreStep +
                ", gradle.ignoredScopes=" + Arrays.toString(gradleIgnoredScopes) +
                ", gradle.preferredEnvironment=" + gradlePreferredEnvironment + '\n' +

                "PAKET:\n" +
                "paket.resolveDependencies=" + paketResolveDependencies +
                ", paket.ignoredScopes=" + Arrays.toString(paketIgnoredScopes) +
                ", paket.runPreStep=" + paketRunPreStep + '\n' +
                "paket.exePath=" +paketPath +
                ", paket.ignoreSourceFiles =" + paketIgnoreSourceFiles + '\n' +

                "GO:\n" +
                "go.resolveDependencies=" + goResolveDependencies +
                ", go.dependencyManager=" + goDependencyManager +
                ", go.collectDependenciesAtRuntime=" + goCollectDependenciesAtRuntime + '\n' +
                "go.glide.ignoreTestPackages=" + goIgnoreTestPackages +
                ", go.glide.ignoreSourceFiles=" + goIgnoreSourceFiles +
                ", go.gogradle.enableTaskAlias=" + goGradleEnableTaskAlias + '\n' +

                "RUBY:\n" +
                "ruby.resolveDependencies=" + rubyResolveDependencies +
                ", ruby.runBundleInstall=" + rubyRunBundleInstall +
                ", ruby.overwriteGemFile=" + rubyOverwriteGemFile + '\n' +
                "ruby.installMissingGems=" + rubyInstallMissingGems +
                ", ruby.ignoreSourceFiles=" + rubyIgnoreSourceFiles + '\n' +

                "PHP:\n" +
                "php.resolveDependencies=" + phpResolveDependencies +
                ", php.runPreStep=" + phpRunPreStep +
                ", php.includeDevDependenices=" + phpIncludeDevDependencies + '\n' +

                "SBT:\n" +
                "sbt.resolveDependencies=" + sbtResolveDependencies +
                ", sbt.aggregateModules=" + sbtAggregateModules +
                ", sbt.runPreStep=" + sbtRunPreStep + '\n' +
                "sbt.TargetFolder=" + sbtTargetFolder +
                ", sbt.ignoreSourceFiles=" + sbtIgnoreSourceFiles + '\n' +

                "HTML:\n" +
                "html.resolveDependencies=" + htmlResolveDependencies + '\n' +

                "COCOAPODS:\n" +
                "cocoapods.resolveDependencies=" + cocoapodsResolveDependencies +
                ", cocoapods.ignoreSourceFiles=" + cocoapodsIgnoreSourceFiles +
                ", cocoapods.runPreStep=" + cocoapodsRunPreStep + '\n' +

                "HEX:\n" +
                "hex.resolveDependencies=" + hexResolveDependencies +
                ", hex.runPreStep=" + hexRunPreStep +
                ", hex.ignoreSourceFiles=" + hexIgnoreSourceFiles +
                ", hex.aggregateModules=" + hexAggregateModules;
    }
}