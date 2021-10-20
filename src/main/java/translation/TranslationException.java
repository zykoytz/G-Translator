package translation;

public class TranslationException extends Exception {

    private String reason;

    public TranslationException(String reason) {
        super();
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
