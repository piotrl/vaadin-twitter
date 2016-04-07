package net.piotrl.analyser.summary;

import java.time.LocalDate;

public class DaySummary extends Summary {
    private LocalDate day;

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }
}
