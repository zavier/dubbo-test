package com.zavier.dubbotest.service.clientpackage;

import lombok.Data;

/**
 * api包信息
 *
 * @date 2020-01-15 12:42
 * @author zhengwei20
 */
@Data
public class PackageInfo {

    /**
     * The Group id.
     */
    private String groupId;

    /**
     * The Artifact id.
     */
    private String artifactId;

    /**
     * The Version.
     */
    private String version;

    /**
     * Instantiates a new Package info.
     *
     * @param groupId    the group id
     * @param artifactId the artifact id
     * @param version    the version
     */
    public PackageInfo(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    /**
     * Gets coords.
     *
     * @return the coords
     */
    public String getCoords() {
        return String.join(":", groupId, artifactId, version);
    }
}
