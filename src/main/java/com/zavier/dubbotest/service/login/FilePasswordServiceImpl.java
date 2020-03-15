package com.zavier.dubbotest.service.login;

import com.zavier.dubbotest.common.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The type File password service.
 *
 * @date 2020-01-15 12:41
 * @author zhengwei20
 */
@Slf4j
@Service
public class FilePasswordServiceImpl implements PasswordService {

    /**
     * The Pwd file.
     */
    private String pwdFile = "passwd";

    /**
     * 确保已创建密码文件
     */
    @PostConstruct
    public void init() {
        File file = new File(pwdFile);
        if (!file.exists()) {
            try {
                // 因为只在当前目录创建，所以不用考虑创建路径文件夹的情况
                file.createNewFile();
            } catch (IOException e) {
                log.error("init pwd file error", e);
                throw new BusinessException(e.getMessage());
            }

            // 创建管理员账号
            try {
                FileUtils.writeStringToFile(file, "admin:$6$nimda$cZfIwnfKLlEX0JnxYFeV75vsTIFjDB2e/vKBcCCHo.UhvrQk3vwtJIY9ktGOo49MVfwxB8NYNYaueBF4ide030");
            } catch (IOException e) {
                log.error("write admin user error", e);
            }
        }

    }

    @Override
    public String getEncryptedPwd(String userName) {
        if (StringUtils.isBlank(userName)) {
            return null;
        }
        List<String> lines = new ArrayList<>();
        try {
            lines = FileUtils.readLines(new File(pwdFile));
        } catch (IOException e) {
            log.error("getEncryptedPwd error", e);
            return null;
        }
        for (String line : lines) {
            String[] split = line.split(":");
            if (Objects.equals(userName, split[0])) {
                return split[1];
            }
        }
        return null;
    }

}
