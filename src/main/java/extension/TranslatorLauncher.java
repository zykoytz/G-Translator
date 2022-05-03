package extension;

import gearth.extensions.ThemedExtensionFormCreator;

import java.net.URL;

public class TranslatorLauncher extends ThemedExtensionFormCreator {

    public static void main(String[] args) {
        runExtensionForm(args, TranslatorLauncher.class);
    }

    @Override
    protected String getTitle() {
        return "G-Translator";
    }

    @Override
    protected URL getFormResource() {
        return getClass().getResource("translator.fxml");
    }
}
