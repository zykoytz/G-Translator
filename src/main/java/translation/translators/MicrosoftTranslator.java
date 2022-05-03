package translation.translators;

import com.squareup.okhttp.*;
import extension.Language;
import org.json.JSONArray;
import org.json.JSONObject;
import translation.TranslationException;
import translation.Translator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MicrosoftTranslator extends Translator {

    // https://docs.microsoft.com/en-us/azure/cognitive-services/translator/quickstart-translator?tabs=java
    private final String subscriptionKey;
    private final String location;

    public MicrosoftTranslator(String subscriptionKey, String location) {
        this.subscriptionKey = subscriptionKey;
        this.location = location;
    }

    @Override
    protected String translate(String text, Language source, Language target) throws TranslationException {
        return translate(Collections.singletonList(text), source, target).get(0);
    }



    @Override
    protected List<String> translate(List<String> texts, Language source, Language target) throws TranslationException {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("api.cognitive.microsofttranslator.com")
                .addPathSegment("/translate")
                .addQueryParameter("api-version", "3.0")
                .addQueryParameter("from", source.getLangCode())
                .addQueryParameter("to", target.getLangCode())
                .build();

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");

        JSONArray bodyContents = new JSONArray();

        for(String s : texts) {
            JSONObject translateText = new JSONObject();
            translateText.put("Text", s);
            bodyContents.put(translateText);
        }

        RequestBody body = RequestBody.create(mediaType, bodyContents.toString(1));
        Request request = new Request.Builder().url(url).post(body)
                .addHeader("Ocp-Apim-Subscription-Key", subscriptionKey)
                .addHeader("Ocp-Apim-Subscription-Region", location)
                .addHeader("Content-type", "application/json")
                .build();
        Response response;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new TranslationException("Something went wrong..");
        }
        if (response.code() != 200) {
            try {
                String msg = response.body().string();
                throw new TranslationException("Translation failed with code: " + response.code() + ", " + msg);
            } catch (IOException ignore) {
            }
            throw new TranslationException("Translation failed with code: " + response.code());
        }

        try {
            String result = response.body().string();
            List<String> translations = new ArrayList<>();
            for (int i = 0; i < texts.size(); i++) {
                translations.add(new JSONArray(result).getJSONObject(i).getJSONArray("translations").getJSONObject(0).getString("text"));
            }
            return translations;
        } catch (IOException e) {
            throw new TranslationException("Something went wrong 2");
        }
    }

    @Override
    public boolean allowMultiLines() {
        return true;
    }
}
