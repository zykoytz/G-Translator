package translation;

import extension.TranslatorExtension;
import translation.translators.ArgosOpenTechTranslator;
import translation.translators.MicrosoftTranslator;
import translation.translators.DeeplTranslator;

public class TranslatorFactory {

    public static Translator get(TranslatorExtension t) {
        String api = t.getApi();

        if (api.equals("microsoft")) {
            return new MicrosoftTranslator(t.getMicrosoftKey(), t.getMicrosoftRegion());
        }
        else if (api.equals("deepl")) {
            return new DeeplTranslator(t.getDeeplKey());
        }
        else {
            return new ArgosOpenTechTranslator();
        }
    }

}
