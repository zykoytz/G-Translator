package translation.translators;

import extension.Language;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import translation.TranslationException;
import translation.Translator;

import java.util.*;


// based on Andy / Tripical's code
public class ArgosOpenTechTranslator extends Translator {

    private Set<Language> supportedLanguages = new HashSet<>(Arrays.asList(Language.ENGLISH, Language.FRENCH,
            Language.GERMAN, Language.ITALIAN, Language.PORTUGUESE, Language.SPANISH, Language.TURKISH));

    @Override
    protected String translate(String text, Language source, Language target) throws TranslationException {
        if (!supportedLanguages.contains(source)) {
            throw new TranslationException("Source language is not supported");
        }
        else if (!supportedLanguages.contains(target)) {
            throw new TranslationException("Target language is not supported");
        }

        try {
            Connection.Response response = Jsoup.connect("https://translate.argosopentech.com/translate")
                    .data("q", text)
                    .data("source", source.getLangCode())
                    .data("target", target.getLangCode())
                    .data("api_key", "")
                    .userAgent("Mozilla")
                    .header("Referer", "https://translate.argosopentech.com/")
                    .header("Origin", "https://translate.argosopentech.com")
                    .ignoreContentType(true)
                    .method(Connection.Method.POST)
                    .execute();

            if (response.statusCode() == 200) {
                JSONObject contents = new JSONObject(response.parse().text());
                return contents.getString("translatedText");
            }
            else {
                throw new TranslationException("Translation failed with code: " + response.statusCode());
            }

        } catch (Exception e) {
            throw new TranslationException("Translation failed");
        }
    }

    @Override
    protected List<String> translate(List<String> text, Language source, Language target) throws TranslationException {
        List<String> results = new ArrayList<>();
        for (String s : text) {
            results.add(translate(s, source, target));
        }

        return results;
    }

    @Override
    public boolean allowMultiLines() {
        return false;
    }
}
