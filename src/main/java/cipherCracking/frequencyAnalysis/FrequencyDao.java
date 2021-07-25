package cipherCracking.frequencyAnalysis;

import java.nio.file.Path;
import java.util.List;

public interface FrequencyDao {

    List<TextEntry> getAll(Path from);

    List<TextEntry> parseData(Path from, int groupLength);

    void saveAll(List<TextEntry> entries, Path to);

}
