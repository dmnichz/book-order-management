package service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import service.WriterService;

public class WriterServiceImpl implements WriterService {
    @Override
    public void writeToFile(String filePath, String data) {
        if (filePath == null) {
            throw new RuntimeException("File path can not be null");
        }
        if (data == null) {
            throw new RuntimeException("Data can not be null");
        }
        File file = new File(filePath);
        try {
            Files.write(file.toPath(), data.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Can't write data to file: " + filePath, e);
        }
    }
}
