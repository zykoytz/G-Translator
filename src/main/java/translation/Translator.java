package translation;

import extension.Language;
import misc.MaybeConsumer;

import java.util.List;

public abstract class Translator {

    protected abstract String translate(String text, Language source, Language target) throws TranslationException;
    protected abstract List<String> translate(List<String> text, Language source, Language target) throws TranslationException;

    public abstract boolean allowMultiLines();

    public void translate(List<String> text, Language source, Language target, MaybeConsumer<List<String>, TranslationException> callback) {
        new Thread(() -> {
            try {
                List<String> result = translate(text, source, target);
                callback.accept(result);
            } catch (TranslationException error) {
                callback.except(error);
            }
        }).start();
    }

    public void translate(String text, Language source, Language target, MaybeConsumer<String, TranslationException> callback) {
        new Thread(() -> {
            try {
                String result = translate(text, source, target);
                callback.accept(result);
            } catch (TranslationException error) {
                callback.except(error);
            }
        }).start();
    }

}
