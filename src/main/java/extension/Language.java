package extension;

public enum Language {
    ENGLISH("English", "en"),
    FRENCH("French", "fr"),
    GERMAN("German", "de"),
    ITALIAN("Italian", "it"),
    PORTUGUESE("Portuguese", "pt"),
    SPANISH("Spanish", "es"),
    TURKISH("Turkish", "tr"),
    FINNISH("Finnish", "fi"),
    DUTCH("Dutch", "nl");

    private String name;
    private String langCode;

    Language(String name, String langCode) {
        this.name = name;
        this.langCode = langCode;
    }

    public String getName() {
        return name;
    }

    public String getLangCode() {
        return langCode;
    }

    @Override
    public String toString() {
        return name;
    }
}
