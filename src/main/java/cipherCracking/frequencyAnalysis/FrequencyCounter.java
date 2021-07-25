package cipherCracking.frequencyAnalysis;

import java.util.*;
import java.util.stream.Collectors;

public class FrequencyCounter {

    public static List<TextEntry> getLetterGroupToOccurrenceCount(String text, int groupLength, boolean omitWhitespaces) {
        if (groupLength <= 0 || groupLength > text.length()) {
            throw new IllegalArgumentException("Group length is not within bounds");
        }
        Map<String, Integer> groupsToOccurrence = new HashMap<>();
        char[] chars = text.toCharArray();
        StringBuilder stringBuilder = new StringBuilder(groupLength);
        outer:
        for (int i = 0; i <= chars.length - groupLength; i++) {
            for (int j = i; j < i + groupLength; j++) {
                if (omitWhitespaces && Character.isWhitespace(chars[j])) {
                    stringBuilder.delete(0, stringBuilder.length());
                    continue outer;
                }
                stringBuilder.append(chars[j]);
            }

            String group = stringBuilder.toString();
            stringBuilder.delete(0, stringBuilder.length());
            Integer occurrenceCount = groupsToOccurrence.getOrDefault(group, 0);
            groupsToOccurrence.put(group, occurrenceCount + 1);
        }
        return groupsToOccurrence.entrySet()
                .stream()
                .map(e -> new TextEntry(e.getKey(), (double) e.getValue() * 100 / groupLength / text.length()))
                .sorted(((Comparator<TextEntry>) TextEntry::compareTo).reversed())
                .collect(Collectors.toList());
    }

}
