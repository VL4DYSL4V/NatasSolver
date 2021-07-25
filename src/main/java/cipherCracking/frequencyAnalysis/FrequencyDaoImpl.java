package cipherCracking.frequencyAnalysis;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class FrequencyDaoImpl implements FrequencyDao {

    private static final String DELIMITER = " - ";

    @Override
    public List<TextEntry> getAll(Path from) {
        try {
            List<String> lines = Files.readAllLines(from);
            return lines.stream()
                    .map(s -> {
                        String[] parts = s.split(DELIMITER);
                        return new TextEntry(parts[0], Double.parseDouble(parts[1]));
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<TextEntry> parseData(Path from, int groupLength) {
        try {
            List<String> lines = Files.readAllLines(from);
            String text = String.join(" ", lines);
            return FrequencyCounter
                    .getLetterGroupToOccurrenceCount(text, groupLength, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveAll(List<TextEntry> entries, Path to) {
        List<String> lines = entries.stream()
                .map(e -> e.getGroup().concat(DELIMITER).concat(String.valueOf(e.getOccurrencePercentage())))
                .collect(Collectors.toList());
        try {
            if (! Files.exists(to)){
                Files.createFile(to);
            }
            Files.write(to, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
