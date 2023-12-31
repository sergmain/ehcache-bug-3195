/*
 * Metaheuristic, Copyright (C) 2017-2023, Innovation platforms, LLC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.example.ehcache3195;

import jakarta.annotation.PostConstruct;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.DeprecatedConfigurationProperty;
import org.springframework.boot.convert.DataSizeUnit;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.boot.convert.PeriodUnit;
import org.springframework.lang.Nullable;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.PublicKey;
import java.time.Duration;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties("mh")
@Getter
@Setter
@Slf4j
public class Globals {
    public static final Duration SECONDS_3 = Duration.ofSeconds(3);
    public static final Duration SECONDS_5 = Duration.ofSeconds(5);
    public static final Duration SECONDS_6 = Duration.ofSeconds(6);
    public static final Duration SECONDS_9 = Duration.ofSeconds(9);
    public static final Duration SECONDS_10 = Duration.ofSeconds(10);
    public static final Duration SECONDS_11 = Duration.ofSeconds(11);
    public static final Duration SECONDS_19 = Duration.ofSeconds(19);
    public static final Duration SECONDS_23 = Duration.ofSeconds(23);
    public static final Duration SECONDS_29 = Duration.ofSeconds(29);
    public static final Duration SECONDS_31 = Duration.ofSeconds(31);
    public static final Duration SECONDS_60 = Duration.ofSeconds(60);
    public static final Duration SECONDS_120 = Duration.ofSeconds(120);
    public static final Duration SECONDS_300 = Duration.ofSeconds(300);
    public static final Duration SECONDS_3600 = Duration.ofSeconds(3600);
    public static final Duration DAYS_14 = Duration.ofDays(14);
    public static final Period DAYS_90 = Period.ofDays(90);
    public static final Period DAYS_IN_YEARS_3 = Period.ofDays(365*3);

    public static final String METAHEURISTIC_PROJECT = "Metaheuristic project";

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KbPath {
        public String evals;
        public String data;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KbFile {
        public String url;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Max {
        public int consoleOutputLines = 1000;

        // in unicode units. i.e. String.length()
        public int promptLength = 4096;
        public int errorsPerPart = 1;
        // has effect only with a local executor of requests
        public int errorsPerEvaluation = 5;
        public int promptsPerPart = 10000;
    }

    @Getter
    @Setter
    public static class RowsLimit {
        public int defaultLimit = 20;
        //        @Value("#{ T(ai.metaheuristic.ai.utils.EnvProperty).minMax( environment.getProperty('mh.dispatcher.global-variable-table-rows-limit'), 5, 100, 20) }")
        public int globalVariableTable = 20;

        //        @Value("#{ T(ai.metaheuristic.ai.utils.EnvProperty).minMax( environment.getProperty('mh.dispatcher.experiment-table-rows-limit'), 5, 30, 10) }")
        public int experiment = 10;

        //        @Value("#{ T(ai.metaheuristic.ai.utils.EnvProperty).minMax( environment.getProperty('mh.dispatcher.experiment-result-table-rows-limit'), 5, 100, 20) }")
        public int experimentResult = 20;

        //        @Value("#{ T(ai.metaheuristic.ai.utils.EnvProperty).minMax( environment.getProperty('mh.dispatcher.source-code-table-rows-limit'), 5, 50, 25) }")
        public int sourceCode = 25;

        //        @Value("#{ T(ai.metaheuristic.ai.utils.EnvProperty).minMax( environment.getProperty('mh.dispatcher.exec-context-table-rows-limit'), 5, 50, 20) }")
        public int execContext = 20;

        //        @Value("#{ T(ai.metaheuristic.ai.utils.EnvProperty).minMax( environment.getProperty('mh.dispatcher.processor-table-rows-limit'), 5, 100, 50) }")
        public int processor = 50;

        //        @Value("#{ T(ai.metaheuristic.ai.utils.EnvProperty).minMax( environment.getProperty('mh.dispatcher.account-table-rows-limit'), 5, 100, 20) }")
        public int account = 20;
    }

    @Setter
    public static class DispatcherTimeout {

        @DurationUnit(ChronoUnit.SECONDS)
        public Duration gc = Duration.ofSeconds(3600);

        @DurationUnit(ChronoUnit.SECONDS)
        public Duration artifactCleaner = SECONDS_60;

        @DurationUnit(ChronoUnit.SECONDS)
        public Duration updateBatchStatuses = Duration.ofSeconds(5);

        /**
         * period of time after which a virtually deleted batch will be deleted from db
         */
        @DurationUnit(ChronoUnit.DAYS)
        public Duration batchDeletion = DAYS_14;

        public Duration getBatchDeletion() {
            return batchDeletion.toSeconds() >= 7 && batchDeletion.toSeconds() <= 180 ? batchDeletion : DAYS_14;
        }

        public Duration getArtifactCleaner() {
            return artifactCleaner.toSeconds() >= 60 && artifactCleaner.toSeconds() <=600 ? artifactCleaner : SECONDS_300;
        }

        public Duration getGc() {
            return gc.toSeconds() >= 600 && gc.toSeconds() <= 3600*24*7 ? gc : SECONDS_3600;
        }

        public Duration getUpdateBatchStatuses() {
            return updateBatchStatuses.toSeconds() >= 5 && updateBatchStatuses.toSeconds() <=60 ? updateBatchStatuses : SECONDS_23;
        }

        public void setGc(Duration gc) {
            this.gc = gc;
        }

        public void setArtifactCleaner(Duration artifactCleaner) {
            this.artifactCleaner = artifactCleaner;
        }

        public void setUpdateBatchStatuses(Duration updateBatchStatuses) {
            this.updateBatchStatuses = updateBatchStatuses;
        }

        public void setBatchDeletion(Duration batchDeletion) {
            this.batchDeletion = batchDeletion;
        }
    }

    @Getter
    @Setter
    public static class Dispatcher {
        public RowsLimit rowsLimit = new RowsLimit();
        public DispatcherTimeout timeout = new DispatcherTimeout();

        @PeriodUnit(ChronoUnit.DAYS)
        public Period keepEventsInDb = Period.ofDays(90);

        public boolean functionSignatureRequired = true;
        public boolean enabled = false;

        @Nullable
        public String masterUsername;

        @Nullable
        public String masterPassword;

        @Nullable
        public PublicKey publicKey;

        public int maxTriesAfterError = 3;

        @DataSizeUnit(DataUnit.MEGABYTES)
        public DataSize chunkSize = DataSize.ofMegabytes(10);

        public Period getKeepEventsInDb() {
            return keepEventsInDb.getDays() >= 7 && keepEventsInDb.getDays() <= DAYS_IN_YEARS_3.getDays() ? keepEventsInDb : DAYS_90;
        }

        @DeprecatedConfigurationProperty(replacement = "mh.dispatcher.rows-limit.global-variable-table")
        @Deprecated
        public int getGlobalVariableRowsLimit() {
            return rowsLimit.globalVariableTable;
        }

        public void setExperimentRowsLimit(int experimentRowsLimit) {
            this.rowsLimit.experiment = experimentRowsLimit;
        }

        public void setExperimentResultRowsLimit(int experimentResultRowsLimit) {
            this.rowsLimit.experimentResult = experimentResultRowsLimit;
        }

        public void setSourceCodeRowsLimit(int sourceCodeRowsLimit) {
            this.rowsLimit.sourceCode = sourceCodeRowsLimit;
        }

        public void setExecContextRowsLimit(int execContextRowsLimit) {
            this.rowsLimit.execContext = execContextRowsLimit;
        }

        public void setProcessorRowsLimit(int processorRowsLimit) {
            this.rowsLimit.processor = processorRowsLimit;
        }

        public void setAccountRowsLimit(int accountRowsLimit) {
            this.rowsLimit.account = accountRowsLimit;
        }

        @DeprecatedConfigurationProperty(replacement = "mh.dispatcher.rows-limit.experiment")
        @Deprecated
        public int getExperimentRowsLimit() {
            return rowsLimit.experiment;
        }

        @DeprecatedConfigurationProperty(replacement = "mh.dispatcher.rows-limit.experiment-result")
        @Deprecated
        public int getExperimentResultRowsLimit() {
            return rowsLimit.experimentResult;
        }

        @DeprecatedConfigurationProperty(replacement = "mh.dispatcher.rows-limit.source-code")
        @Deprecated
        public int getSourceCodeRowsLimit() {
            return rowsLimit.sourceCode;
        }

        @DeprecatedConfigurationProperty(replacement = "mh.dispatcher.rows-limit.exec-context")
        @Deprecated
        public int getExecContextRowsLimit() {
            return rowsLimit.execContext;
        }

        @DeprecatedConfigurationProperty(replacement = "mh.dispatcher.rows-limit.processor")
        @Deprecated
        public int getProcessorRowsLimit() {
            return rowsLimit.processor;
        }

        @DeprecatedConfigurationProperty(replacement = "mh.dispatcher.rows-limit.account")
        @Deprecated
        public int getAccountRowsLimit() {
            return rowsLimit.account;
        }
    }

    @Getter
    @Setter
    public static class ProcessorTimeout {
        @DurationUnit(ChronoUnit.SECONDS)
        public Duration requestDispatcher = SECONDS_6;

        @DurationUnit(ChronoUnit.SECONDS)
        public Duration taskAssigner = SECONDS_5;

        @DurationUnit(ChronoUnit.SECONDS)
        public Duration taskProcessor = SECONDS_9;

        @DurationUnit(ChronoUnit.SECONDS)
        public Duration downloadFunction = SECONDS_11;

        @DurationUnit(ChronoUnit.SECONDS)
        public Duration prepareFunctionForDownloading = SECONDS_31;

        @DurationUnit(ChronoUnit.SECONDS)
        public Duration downloadResource = SECONDS_3;

        @DurationUnit(ChronoUnit.SECONDS)
        public Duration uploadResultResource = SECONDS_3;

        @DurationUnit(ChronoUnit.SECONDS)
        public Duration dispatcherContextInfo = SECONDS_19;

        @DurationUnit(ChronoUnit.SECONDS)
        public Duration artifactCleaner = SECONDS_29;

        public Duration getRequestDispatcher() {
            return requestDispatcher.toSeconds() >= 6 && requestDispatcher.toSeconds() <= 30 ? requestDispatcher : SECONDS_10;
        }

        public Duration getTaskAssigner() {
            return taskAssigner.toSeconds() >= 3 && taskAssigner.toSeconds() <= 20 ? taskAssigner : SECONDS_5;
        }

        public Duration getTaskProcessor() {
            return taskProcessor.toSeconds() >= 3 && taskProcessor.toSeconds() <= 20 ? taskProcessor : SECONDS_9;
        }

        public Duration getDownloadFunction() {
            return downloadFunction.toSeconds() >= 3 && downloadFunction.toSeconds() <= 20 ? downloadFunction : SECONDS_11;
        }

        public Duration getPrepareFunctionForDownloading() {
            return prepareFunctionForDownloading.toSeconds() >= 20 && prepareFunctionForDownloading.toSeconds() <= 60 ? prepareFunctionForDownloading : SECONDS_31;
        }

        public Duration getDownloadResource() {
            return downloadResource.toSeconds() >= 3 && downloadResource.toSeconds() <= 20 ? downloadResource : SECONDS_3;
        }

        public Duration getUploadResultResource() {
            return uploadResultResource.toSeconds() >= 3 && uploadResultResource.toSeconds() <= 20 ? uploadResultResource : SECONDS_3;
        }

        public Duration getDispatcherContextInfo() {
            return dispatcherContextInfo.toSeconds() >= 10 && dispatcherContextInfo.toSeconds() <= 60 ? dispatcherContextInfo : SECONDS_19;
        }

        public Duration getArtifactCleaner() {
            return artifactCleaner.toSeconds() >= 10 && artifactCleaner.toSeconds() <= 60 ? artifactCleaner : SECONDS_29;
        }

        @DeprecatedConfigurationProperty(replacement = "mh.processor.timeout.dispatcher-context-info")
        @Deprecated
        public Duration getGetDispatcherContextInfo() {
            return getDispatcherContextInfo();
        }

        @DeprecatedConfigurationProperty(replacement = "mh.processor.timeout.dispatcher-context-info")
        @Deprecated
        public void setGetDispatcherContextInfo(Duration getDispatcherContextInfo) {
            this.dispatcherContextInfo = getDispatcherContextInfo;
        }

    }

    @Getter
    @Setter
    public static class Processor {
        public ProcessorTimeout timeout = new ProcessorTimeout();

        public boolean enabled = false;

        @Nullable
        public Path defaultDispatcherYamlFile = null;

        @Nullable
        public Path defaultEnvYamlFile = null;

        public int taskConsoleOutputMaxLines = 1000;

        public int initCoreNumber = 1;
    }

    public static class ThreadNumber {
        public int scheduler = 10;
        public int event =  Math.max(10, Runtime.getRuntime().availableProcessors()/2);
        public int queryApi =  2;

        public void setScheduler(int scheduler) {
            this.scheduler = scheduler;
        }

        public void setEvent(int event) {
            this.event = event;
        }
    }

    @Value("${spring.profiles.active}")
    public String[] activeProfiles;

    public final Dispatcher dispatcher = new Dispatcher();
    public final Processor processor = new Processor();
    public final ThreadNumber threadNumber = new ThreadNumber();

    @Nullable
    public String systemOwner = null;

    public String branding = METAHEURISTIC_PROJECT;

    @Nullable
    public List<String> corsAllowedOrigins = new ArrayList<>(List.of("*"));

    public boolean testing = false;
    public boolean eventEnabled = false;

    public boolean sslRequired = true;

    public Path home;

    public Path getHome() {
        if (home==null) {
            throw new IllegalArgumentException("property mh.home isn't specified");
        }
        return home;
    }

    // some fields, will be inited in postConstruct()
    public Path dispatcherTempPath;
    public Path dispatcherResourcesPath;
    public Path dispatcherPath;
    public Path dispatcherStoragePath;
    public Path dispatcherStorageVariablesPath;
    public Path dispatcherStorageGlobalVariablesPath;
    public Path dispatcherStorageFunctionsPath;
    public Path dispatcherStorageCacheVariablessPath;

    public Path processorPath;

    @SneakyThrows
    @PostConstruct
    public void postConstruct() {
        dispatcherPath = getHome().resolve("dispatcher");
        Files.createDirectories(dispatcherPath);
        processorPath = getHome().resolve("processor");
        Files.createDirectories(processorPath);

        if (processor.enabled) {

            // TODO 2019.04.26 right now the change of ownership is disabled
            //  but maybe will be required in future
//            checkOwnership(processorEnvHotDeployDir);
        }

        if (dispatcher.enabled) {
            dispatcherTempPath = dispatcherPath.resolve("temp");
            Files.createDirectories(dispatcherTempPath);

            dispatcherResourcesPath = dispatcherPath.resolve(Consts.RESOURCES_DIR);
            Files.createDirectories(dispatcherResourcesPath);

            dispatcherStoragePath = dispatcherPath.resolve(Consts.STORAGE_DIR);
            Files.createDirectories(dispatcherStoragePath);

            dispatcherStorageVariablesPath = dispatcherStoragePath.resolve(Consts.VARIABLES_DIR);
            Files.createDirectories(dispatcherStorageVariablesPath);

            dispatcherStorageGlobalVariablesPath = dispatcherStoragePath.resolve(Consts.GLOBAL_VARIABLES_DIR);
            Files.createDirectories(dispatcherStorageGlobalVariablesPath);

            dispatcherStorageFunctionsPath = dispatcherStoragePath.resolve(Consts.FUNCTIONS_DIR);
            Files.createDirectories(dispatcherStorageFunctionsPath);

            dispatcherStorageCacheVariablessPath = dispatcherStoragePath.resolve(Consts.CACHE_VARIABLES_DIR);
            Files.createDirectories(dispatcherStorageCacheVariablessPath);
        }

        logGlobals();
        logGarbageCollectors();

    }

    private static void logGarbageCollectors() {
        log.info("Garbage collectors:");
        List<GarbageCollectorMXBean> beans = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean bean : beans) {
            log.info("'\t"+ bean.getName());
        }
    }

    public void setCorsAllowedOrigins(List<String> corsAllowedOrigins) {
        if (corsAllowedOrigins.isEmpty()) {
            return;
        }
        this.corsAllowedOrigins = corsAllowedOrigins;
    }

    private void logGlobals() {
        final Runtime rt = Runtime.getRuntime();
        log.warn("Memory, free: {}, max: {}, total: {}", rt.freeMemory(), rt.maxMemory(), rt.totalMemory());
        log.info("Current globals:");
        log.info("'\tmh.home: {}", getHome());
        log.info("'\tcorsAllowedOrigins: {}", corsAllowedOrigins);
        log.info("'\tbranding: {}", branding);
        log.info("'\ttesting: {}", testing);
        log.info("'\tsslRequired: {}", sslRequired);
        log.info("'\tthreadNumber.scheduler: {}", threadNumber.scheduler);
        log.info("'\tthreadNumber.event: {}", threadNumber.event);
        log.info("'\tdispatcher.enabled: {}", dispatcher.enabled);
        log.info("'\tdispatcher.dir: {}", dispatcherPath.toAbsolutePath().normalize());
        log.info("'\tdispatcher.functionSignatureRequired: {}", dispatcher.functionSignatureRequired);
        log.info("'\tdispatcher.masterUsername: {}", dispatcher.masterUsername);
        log.info("'\tdispatcher.publicKey: {}", dispatcher.publicKey!=null ? "provided" : "wasn't provided");
        log.info("'\tdispatcher.chunkSize: {}", dispatcher.chunkSize);
        log.info("'\tdispatcher.keepEventsInDb: {}", dispatcher.keepEventsInDb);
        log.info("'\tdispatcher.maxTriesAfterError: {}", dispatcher.maxTriesAfterError);

        log.info("'\tdispatcher.timeout.gc: {}", dispatcher.timeout.gc);
        log.info("'\tdispatcher.timeout.artifactCleaner: {}", dispatcher.timeout.artifactCleaner);
        log.info("'\tdispatcher.timeout.updateBatchStatuses: {}", dispatcher.timeout.updateBatchStatuses);

        log.info("'\tdispatcher.rowsLimit.globalVariableTable: {}", dispatcher.rowsLimit.globalVariableTable);
        log.info("'\tdispatcher.rowsLimit.experiment: {}", dispatcher.rowsLimit.experiment);
        log.info("'\tdispatcher.rowsLimit.sourceCode: {}", dispatcher.rowsLimit.sourceCode);
        log.info("'\tdispatcher.rowsLimit.execContext: {}", dispatcher.rowsLimit.execContext);
        log.info("'\tdispatcher.rowsLimit.processor: {}", dispatcher.rowsLimit.processor);
        log.info("'\tdispatcher.rowsLimit.account: {}", dispatcher.rowsLimit.account);
        log.info("'\tdispatcher.rowsLimit.processor: {}", dispatcher.rowsLimit.processor);

        log.info("'\tprocessor.enabled: {}", processor.enabled);
        log.info("'\tprocessor.dir: {}", processorPath.toAbsolutePath().normalize());
        log.info("'\tprocessor.taskConsoleOutputMaxLines: {}", processor.taskConsoleOutputMaxLines);
        log.info("'\tprocessor.timeout.artifactCleaner: {}", processor.timeout.artifactCleaner);
        log.info("'\tprocessor.timeout.downloadFunction: {}", processor.timeout.downloadFunction);
        log.info("'\tprocessor.timeout.downloadResource: {}", processor.timeout.downloadResource);
        log.info("'\tprocessor.timeout.prepareFunctionForDownloading: {}", processor.timeout.prepareFunctionForDownloading);
        log.info("'\tprocessor.timeout.requestDispatcher: {}", processor.timeout.requestDispatcher);
        log.info("'\tprocessor.timeout.taskAssigner: {}", processor.timeout.taskAssigner);
        log.info("'\tprocessor.timeout.taskProcessor: {}", processor.timeout.taskProcessor);
        log.info("'\tprocessor.timeout.dispatcherContextInfo: {}", processor.timeout.dispatcherContextInfo);
    }
}
