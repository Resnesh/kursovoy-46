package kz.edu.java46.library.service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.*;
import java.util.stream.Collectors;


public class JsonFileRepository implements DataRepository<String, Long> {

    private final Path filePath;
    private final Map<Long, String> storage = new LinkedHashMap<>();
    private long nextId = 1L;

    public JsonFileRepository(String fileName) {
        this.filePath = Paths.get(fileName);
        loadFromFile();
    }

    private void loadFromFile() {
        try {
            if (Files.exists(filePath)) {
                List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
                for (String line : lines) {
                    // формат: id|value
                    int sep = line.indexOf('|');
                    if (sep > 0) {
                        Long id = Long.parseLong(line.substring(0, sep));
                        String value = line.substring(sep + 1);
                        storage.put(id, value);
                        if (id >= nextId) nextId = id + 1;
                    }
                }
            } else {
                Files.createDirectories(filePath.getParent() == null ? Paths.get("") : filePath.getParent());
                Files.createFile(filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void persistToFile() {
        try (BufferedWriter bw = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
            for (Map.Entry<Long, String> e : storage.entrySet()) {
                bw.write(e.getKey() + "|" + e.getValue());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String save(String entity) throws Exception {
        Long id = nextId++;
        storage.put(id, entity);
        persistToFile();
        return entity;
    }

    @Override
    public Optional<String> findById(Long id) throws Exception {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<String> findAll() throws Exception {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void deleteById(Long id) throws Exception {
        storage.remove(id);
        persistToFile();
    }
}