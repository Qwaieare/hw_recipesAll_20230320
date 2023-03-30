package skypro.recipes.service;

import java.io.File;
import java.nio.file.Path;

public interface FilesService {

    boolean saveToFile(String json);

    boolean saveToFile1(String json);

    String readFromFile();

    String readFromFile1();

    boolean cleanDataFile();

    boolean cleanDataFile1();


    File getDataFile();

    File getDataFile1();

    Path createTempFile(String suffix);
}
