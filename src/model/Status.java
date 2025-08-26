package model;

public enum Status {
    ERLEDIGT, ANGEFANGEN, NEU;
    @Override
    public
    String toString() {
        switch (this) {
        case ERLEDIGT: return "erledigt";
        case ANGEFANGEN: return "begonnen";
        case NEU: return "noch nicht begonnen";
        default: return "Fehler!";
        }
    }
}
