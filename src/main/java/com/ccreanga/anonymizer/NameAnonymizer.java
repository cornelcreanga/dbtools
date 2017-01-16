package com.ccreanga.anonymizer;


import com.ccreanga.random.Language;
import com.ccreanga.random.RandomNameGenerator;
import com.ccreanga.random.RandomNameGeneratorFactory;

public class NameAnonymizer implements Anonymizer {

    private RandomNameGenerator generator = RandomNameGeneratorFactory.generator(Language.FANTASY);

    private int sylNumber = 2;
    private int wordNumber = 1;
    private String store;

    public Object anonymize(Object original) {
        String cachedValue = null;
        if (store != null) {
            cachedValue = (String) Store.get(store, original);
            if (cachedValue != null)
                return cachedValue;
        }
        if (wordNumber == 1)
            return generator.compose(sylNumber);
        //compose the text from multiple words
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < wordNumber; i++) {
            sb.append(generator.compose(sylNumber));
            if (i != (wordNumber - 1))
                sb.append(" ");
        }
        if ((store != null))
            Store.put(store, (String) original, sb.toString());
        return sb.toString();
    }

    public NameAnonymizer() {
    }

    public void setSylNumber(int sylNumber) {
        this.sylNumber = sylNumber;
    }


    public void setWordNumber(int wordNumber) {
        this.wordNumber = wordNumber;
    }

    public int getSylNumber() {
        return sylNumber;
    }

    public int getWordNumber() {
        return wordNumber;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }
}
