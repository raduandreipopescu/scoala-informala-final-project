package ro.siit.tripscatalog.service;

/**
 * Files utility class.
 * See method description.
 *
 * @author Radu Popescu
 *
 */

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Service
public class FileService {
    private static final String ROOT_DIR = "D:/Docs/Trip Catalog/Photos/";

    /**
     * Saves file on disk.
     *
     * @author Radu Popescu
     *
     */

    public void store(String photoName, MultipartFile file) {

        try (OutputStream outputStream = new FileOutputStream(new File(ROOT_DIR + photoName));
             InputStream inputStream = file.getInputStream();) {
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error saving file", e);
        }
    }

    /**
     * Reads file from disk and creates the a FileSystemResource.
     *
     * @author Radu Popescu
     *
     */

    public Resource loadAsResource(String filename) {
        return new FileSystemResource(ROOT_DIR + filename);
    }

    /**
     * Delete the file from disk.
     *
     * @author Radu Popescu
     *
     */

    public void deleteResource(String filename) {
        File file = new File(ROOT_DIR + filename);
        file.delete();
    }
}
