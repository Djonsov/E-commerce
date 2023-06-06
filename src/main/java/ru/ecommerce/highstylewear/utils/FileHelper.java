package ru.ecommerce.highstylewear.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import static ru.ecommerce.highstylewear.constants.FileDirectoriesConstants.FILES_UPLOAD_DIRECTORY;

@Slf4j
public class FileHelper {
    private FileHelper() {

    }

    public static String createFile(final MultipartFile file) {
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String resultName = "";
        try {
            Path path = Paths.get(FILES_UPLOAD_DIRECTORY + "/" + filename).toAbsolutePath().normalize();
            if (!path.toFile().exists()) {
                Files.createDirectories(path);
            }
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            //Будет сохранен полный путь до файла от самого корня
            resultName = path.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return resultName;
    }
}
