package cipherCracking.frequencyAnalysis;

import java.util.Objects;

public class TextEntry implements Comparable<TextEntry>{

    private final String group;
    private double occurrencePercentage;

    public TextEntry(String group, double occurrencePercentage) {
        this.group = group;
        this.occurrencePercentage = occurrencePercentage;
    }

    public String getGroup() {
        return group;
    }

    public double getOccurrencePercentage() {
        return occurrencePercentage;
    }

    public void setOccurrencePercentage(double occurrencePercentage) {
        this.occurrencePercentage = occurrencePercentage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextEntry textEntry = (TextEntry) o;
        return occurrencePercentage == textEntry.occurrencePercentage && Objects.equals(group, textEntry.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(group, occurrencePercentage);
    }

    @Override
    public String toString() {
        return "TextEntry{" +
                "group='" + group + '\'' +
                ", occurrencePercentage=" + occurrencePercentage +
                '}';
    }

    @Override
    public int compareTo(TextEntry o) {
        return Double.compare(this.occurrencePercentage, o.occurrencePercentage);
    }
}
