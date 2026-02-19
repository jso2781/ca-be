package kr.or.kids.domain.ca.anyid;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * Any-ID(정부 통합인증) 중계형 SDK는 일부 API에서 "설정 파일 경로"(파일시스템 경로)를 요구합니다.
 * (예: kdist-api.json)
 *
 * Spring Boot는 classpath 리소스를 파일 경로로 바로 넘기기 어렵기 때문에,
 * 필요한 리소스를 임시 파일로 복사하여 경로를 제공합니다.
 */
@Component
public class AnyIdResourcePaths {

    private volatile Path kdistApiJsonPath;
    private volatile Path esignProviderKeyJsonPath;
    private volatile Path pidApiJsonPath;

    /**
     * WEB-INF/config/kdist/kdist-api.json (SDK 가이드 기준) 
     */
    public String kdistApiJsonFilePath() {
        if (kdistApiJsonPath != null) {
            return kdistApiJsonPath.toAbsolutePath().toString();
        }
        synchronized (this) {
            if (kdistApiJsonPath != null) {
                return kdistApiJsonPath.toAbsolutePath().toString();
            }
            try {
                kdistApiJsonPath = copyToTempFile("anyid/WEB-INF/config/kdist/kdist-api.json", "kdist-api", ".json");
            } catch (IOException e) {
                throw new IllegalStateException("Failed to prepare Any-ID kdist-api.json temp file", e);
            }
            return kdistApiJsonPath.toAbsolutePath().toString();
        }
    }

    /** WEB-INF/config/esign/provider-key.json */
    public String esignProviderKeyJsonFilePath() {
        if (esignProviderKeyJsonPath != null) {
            return esignProviderKeyJsonPath.toAbsolutePath().toString();
        }
        synchronized (this) {
            if (esignProviderKeyJsonPath != null) {
                return esignProviderKeyJsonPath.toAbsolutePath().toString();
            }
            try {
                esignProviderKeyJsonPath = copyToTempFile("anyid/WEB-INF/config/esign/provider-key.json", "provider-key", ".json");
            } catch (IOException e) {
                throw new IllegalStateException("Failed to prepare Any-ID provider-key.json temp file", e);
            }
            return esignProviderKeyJsonPath.toAbsolutePath().toString();
        }
    }

    /** resources/config/pid/pid_api.json */
    public String pidApiJsonFilePath() {
        if (pidApiJsonPath != null) {
            return pidApiJsonPath.toAbsolutePath().toString();
        }
        synchronized (this) {
            if (pidApiJsonPath != null) {
                return pidApiJsonPath.toAbsolutePath().toString();
            }
            try {
                pidApiJsonPath = copyToTempFile("anyid/resources/config/pid/pid_api.json", "pid_api", ".json");
            } catch (IOException e) {
                throw new IllegalStateException("Failed to prepare Any-ID pid_api.json temp file", e);
            }
            return pidApiJsonPath.toAbsolutePath().toString();
        }
    }

    private static Path copyToTempFile(String classpathLocation, String prefix, String suffix) throws IOException {
        ClassPathResource res = new ClassPathResource(classpathLocation);
        if (!res.exists()) {
            throw new IOException("Classpath resource not found: " + classpathLocation);
        }
        Path tmp = Files.createTempFile(prefix, suffix);
        tmp.toFile().deleteOnExit();
        try (InputStream in = res.getInputStream()) {
            Files.copy(in, tmp, StandardCopyOption.REPLACE_EXISTING);
        }
        return tmp;
    }
}
