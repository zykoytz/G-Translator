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

/**
 * Simple DeepL API translator using OkHttp.
 */
public class DeeplTranslator extends Translator {

    private final String apiKey;

    public DeeplTranslator(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    protected String translate(String text, Language source, Language target) throws TranslationException {
        return translate(Collections.singletonList(text), source, target).get(0);
    }

    @Override
    protected List<String> translate(List<String> texts, Language source, Language target) throws TranslationException {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("api-free.deepl.com")
                .addPathSegment("v2")
                .addPathSegment("translate")
                .build();

        FormEncodingBuilder formBuilder = new FormEncodingBuilder();
        formBuilder.add("auth_key", apiKey);
        formBuilder.add("target_lang", target.getLangCode().toUpperCase());
        if (source != null) {
            formBuilder.add("source_lang", source.getLangCode().toUpperCase());
        }
        for (String s : texts) {
            formBuilder.add("text", s);
        }

        RequestBody body = formBuilder.build();
        Request request = new Request.Builder().url(url).post(body).build();
        OkHttpClient client = new OkHttpClient();

        Response response;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new TranslationException("Something went wrong..");
        }

        if (response.code() == 403 || response.code() == 456) {
            throw new TranslationException("Invalid DeepL API key");
        }
        if (response.code() != 200) {
            throw new TranslationException("Translation failed with code: " + response.code());
        }

        try {
            String result = response.body().string();
            JSONArray arr = new JSONObject(result).getJSONArray("translations");
            List<String> res = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                res.add(arr.getJSONObject(i).getString("text"));
            }
            return res;
        } catch (Exception e) {
            throw new TranslationException("Translation failed");
        }
    }

    @Override
    public boolean allowMultiLines() {
        return true;
    }
}
