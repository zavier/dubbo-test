package com.zavier.dubbotest.common.util;

import com.zavier.dubbotest.common.Constants;
import com.zavier.dubbotest.service.clientpackage.PackageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.transport.file.FileTransporterFactory;
import org.eclipse.aether.transport.http.HttpTransporterFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 * maven工具类，提供下载服务等
 *
 * @date 2020-01-15 12:42
 * @author zhengwei20
 */
@Slf4j
@Component
public class MavenUtil {

    /**
     * The Default download path.
     */
    private String defaultDownloadPath = "repo";

    /**
     * The Maven repo url.
     */
    @Value("${maven.repository.url}")
    private String mavenRepoUrl = "<私服地址>";

    /**
     * Clear all boolean.
     *
     * @return the boolean
     */
    public boolean clearAll() {
        try {
            FileUtils.deleteDirectory(new File(defaultDownloadPath));
        } catch (IOException e) {
            log.error("delete repo error", e);
            return false;
        }
        return true;
    }

    /**
     * 从maven仓库下载jar包
     *
     * @param packageInfo  包信息
     * @param forceRefresh 快照版本是否强制刷新
     * @return string string
     */
    public String download(PackageInfo packageInfo, boolean forceRefresh) {
        String downloadBasePath = defaultDownloadPath;
        File file = new File(downloadBasePath);

        File packageFilePath = new File(defaultDownloadPath, generatePackagePath(packageInfo));

        if (packageFilePath.exists() && !forceRefresh) {
            return packageFilePath.getAbsolutePath();
        }

        // 正式版，如果已存在则直接返回
        if (!packageInfo.getVersion().endsWith(Constants.SNAPSHOT)) {
            if (packageFilePath.exists()) {
                log.info("path:{} is exist", downloadBasePath);
                return file.getAbsolutePath();
            }
        }

        // 清空历史包
        clear(packageFilePath.getAbsolutePath());

        try {
            RepositorySystem repositorySystem = newRepositorySystem();
            RepositorySystemSession session = newSession(repositorySystem, downloadBasePath);
            Dependency dependency = new Dependency(new DefaultArtifact(packageInfo.getCoords()), "compile");
            RemoteRepository central = new RemoteRepository.Builder("central", "default", mavenRepoUrl)
//                    .setAuthentication(new AuthenticationBuilder().addUsername("").addPassword("").build())
                    .build();

            String coords = packageInfo.getCoords();
            Artifact artifact = new DefaultArtifact(coords);
            ArtifactRequest artifactRequest = new ArtifactRequest();
            artifactRequest.addRepository(central);
            artifactRequest.setArtifact(artifact);
            repositorySystem.resolveArtifact(session, artifactRequest);

            log.info("download {} successfule", packageFilePath.getAbsolutePath());
            return packageFilePath.getAbsolutePath();
        } catch (Exception e) {
            log.error("download jar error", e);
        }
        return null;
    }

    /**
     * Generate package path string.
     *
     * @param packageInfo the package info
     * @return the string
     */
    private String generatePackagePath(PackageInfo packageInfo) {
        String groupId = packageInfo.getGroupId();
        String artifactId = packageInfo.getArtifactId();
        String version = packageInfo.getVersion();

        String groupIdPath = groupId.replace(".", File.separator);
        return String.join(File.separator, groupIdPath, artifactId, version);
    }

    /**
     * Clear.
     *
     * @param path the path
     */
    private void clear(String path) {
        if (StringUtils.isEmpty(path)) {
            return;
        }
        log.info("clear path:{}", path);
        try {
            FileUtils.deleteDirectory(new File(path));
        } catch (IOException e) {
            log.error("clear maven resource error", e);
        }
    }

    /**
     * New session repository system session.
     *
     * @param system    the system
     * @param storePath the store path
     * @return the repository system session
     */
    private RepositorySystemSession newSession(RepositorySystem system, String storePath) {
        DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();
        LocalRepository localRepository = new LocalRepository(storePath);
        session.setLocalRepositoryManager(system.newLocalRepositoryManager(session, localRepository));
        return session;
    }

    /**
     * New repository system repository system.
     *
     * @return the repository system
     */
    private RepositorySystem newRepositorySystem() {
        DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
        locator.addService( RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class );
        locator.addService( TransporterFactory.class, FileTransporterFactory.class );
        locator.addService( TransporterFactory.class, HttpTransporterFactory.class );
        return locator.getService( RepositorySystem.class );
    }
}
