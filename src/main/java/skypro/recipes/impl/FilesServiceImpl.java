package skypro.recipes.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import skypro.recipes.service.FilesService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FilesServiceImpl implements FilesService {

    @Value("${path.to.data.file}")
    private String dataFilePath;

    @Value("${name.of.data.file}")
    private String dataFileName;


    @Value("${name.of.data.file1}")
    private String dataFile1Name;

    @Override
    public boolean saveToFile(String json) { // метод принимает и сохраняет информацию, записывает строку в файл
        try {
            cleanDataFile();
            Files.writeString(Path.of(dataFilePath, dataFileName), json);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean saveToFile1(String json) { // метод принимает и сохраняет информацию, записывает строку в файл
        try {
            cleanDataFile1();
            Files.writeString(Path.of(dataFilePath, dataFile1Name), json);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }



    @Override
    public String readFromFile() {  /// метод читает из файла
        try {
            return Files.readString(Path.of(dataFilePath, dataFileName));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public String readFromFile1() {  /// метод читает из файла
        try {
            return Files.readString(Path.of(dataFilePath, dataFile1Name));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean cleanDataFile() { // метод удаляет файлы и создает пустые
        try {
            final Path path = Path.of(dataFilePath, dataFileName);
            Files.deleteIfExists(path);
            Files.createFile(path);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean cleanDataFile1() { // метод удаляет файлы и создает пустые
        try {
            final Path path = Path.of(dataFilePath, dataFile1Name);
            Files.deleteIfExists(path);
            Files.createFile(path);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public File getDataFile() {
        return new File(dataFilePath + "/" + dataFileName);
    }

    @Override
    public File getDataFile1() {
        return new File(dataFilePath + "/" + dataFile1Name);
    }


    @Override
    public Path createTempFile(String suffix) { // метод создает временные файлы
       try {
           return Files.createTempFile(Path.of(dataFilePath), "tempFile", suffix);
       } catch (IOException e) {
           throw new RuntimeException(e);
       }
    }



}
