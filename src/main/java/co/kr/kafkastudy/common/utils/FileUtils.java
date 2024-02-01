package co.kr.kafkastudy.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

@Slf4j
public class FileUtils {

    public boolean moveFile(String filePath, String newFilePath) {
        boolean isMove = false;
        Path file = Paths.get(filePath);
        Path newFile = Paths.get(newFilePath);

        try {
            Files.move(file, newFile, StandardCopyOption.REPLACE_EXISTING);
            isMove = true;
        }catch (FileSystemException fe) {
            log.error("[FileUtils] 이동할 파일 경로 없음");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isMove;
    }
}
