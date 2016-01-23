package com.ccreanga.random;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class RandomNameGenerator {

    private String[] pre = null;
    private String[] mid = null;
    private String[] sur = null;

    final private static char[] VOWELS = {'a', 'e', 'i', 'o', 'u', 'ä', 'ö', 'õ', 'ü', 'y'};
    final private static char[] CONSONANTS = {'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p','q', 'r', 's', 't', 'v', 'w', 'x', 'y'};

    private String fileName;

    public RandomNameGenerator(String fileName) throws IOException {
        this.fileName = fileName;
        refresh();
    }


    private void refresh() throws IOException {

        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))){
            String line;
            ArrayList<String> pList=new ArrayList<>(),sList=new ArrayList<>(),mList=new ArrayList<>();
            while((line=reader.readLine())!=null){
                line = line.trim();
                if (line.isEmpty())
                    continue;
                if(line.charAt(0) == '-'){
                    pList.add(line.substring(1).toLowerCase());
                }
                else if(line.charAt(0) == '+'){
                    sList.add(line.substring(1).toLowerCase());
                }
                else{
                    mList.add(line.toLowerCase());
                }
            }
            pre = new String[pList.size()];
            mid = new String[mList.size()];
            sur = new String[sList.size()];
            if ((pList.size()==0))
                throw new RuntimeException("prefix list is empty");
            if (sList.size()==0)
                throw new RuntimeException("suffix list is empty");

            pre = pList.toArray(pre);
            mid = pList.toArray(mid);
            sur = pList.toArray(sur);
        }

    }

    private boolean containsConsFirst(String[] array){
        for(String s: array){
            if(consonantFirst(s)) return true;
        }
        return false;
    }

    private boolean containsVocFirst(String[] array){
        for(String s: array){
            if(vowelFirst(s)) return true;
        }
        return false;
    }

    private boolean allowCons(String[] array){
        for(String s: array){
            if(hatesPreviousVowels(s) || !hatesPreviousConsonants(s)) return true;
        }
        return false;
    }

    private boolean allowVocs(String[] array){
        for(String s: array){
            if(hatesPreviousConsonants(s) || !hatesPreviousVowels(s)) return true;
        }
        return false;
    }

    private boolean expectsVowel(String s){
        return s.indexOf("+v",1)>-1;
    }
    private boolean expectsConsonant(String s){
        return s.indexOf("+c",1)>-1;
    }
    private boolean hatesPreviousVowels(String s){
        return s.indexOf("-c",1)>-1;
    }
    private boolean hatesPreviousConsonants(String s){
        return s.indexOf("-v",1)>-1;
    }

    private String pureSyl(String s){
        char first = s.charAt(0);
        if(first == '+' || first == '-')
            s = s.substring(1);
        StringBuilder sb = new StringBuilder(s.length());
        int i=0;
        while(i<s.length() && s.charAt(i)!=' '){
            sb.append(s.charAt(i++));
        }
        if (s.length()==sb.length())
            return s;
        return sb.toString();
    }

    private boolean vowelFirst(String s){
        char first = s.charAt(0);
        return (Arrays.binarySearch(VOWELS,first)>-1);
    }

    private boolean consonantFirst(String s){
        char first = s.charAt(0);
        return (Arrays.binarySearch(CONSONANTS,first)>-1);
    }

    private boolean vowelLast(String s){
        char first = s.charAt(0);
        return (Arrays.binarySearch(VOWELS,first)>-1);
    }

    private boolean consonantLast(String s){
        char first = s.charAt(0);
        return (Arrays.binarySearch(CONSONANTS,first)>-1);
    }

    public String compose(int syls){
        if(syls < 1)
            throw new RuntimeException("no of sylabs should be greater than 1");
        if(syls > 2 && mid.length == 0)
            throw new RuntimeException("can't compose a name with more than 3 parts without middle parts, which requires middle parts");
        int expecting = 0; // 1 for Vowel, 2 for consonant
        int last = 0; // 1 for Vowel, 2 for consonant
        int a = (int)(Math.random() * pre.length);

        if(vowelLast(pureSyl(pre[a]))) last = 1;
        else last = 2;

        if(syls > 2){
            if(expectsVowel(pre[a])){
                expecting = 1;
                if(!containsVocFirst(mid))
                    throw new RuntimeException("Expecting \"middle\" part starting with vowel but there is none");
            }
            if(expectsConsonant(pre[a])){
                expecting = 2;
                if(!containsConsFirst(mid))
                    throw new RuntimeException("Expecting \"middle\" part starting with consonant but there is none");
            }
        }
        else{
            if(expectsVowel(pre[a])){
                expecting = 1;
                if(!containsVocFirst(sur))
                    throw new RuntimeException("Expecting \"suffix\" part starting with vowel but there is none");
            }
            if(expectsConsonant(pre[a])){
                expecting = 2;
                if(!containsConsFirst(sur))
                    throw new RuntimeException("Expecting \"suffix\" part starting with consonant but there is none");
            }
        }
        if(vowelLast(pureSyl(pre[a])) && !allowVocs(mid))
            throw new RuntimeException("Expecting \"middle\" part that allows last character of prefix to be a Vowel but there is none. the prefix used  was : \""+pre[a]+"\", which" +
                    "means there should be a part available, that has \"-v\" requirement or no requirements for previous syllables at all.");

        if(consonantLast(pureSyl(pre[a])) && !allowCons(mid))
            throw new RuntimeException("Expecting \"middle\" part that allows last character of prefix to be a consonant but there is none. the prefix used, was : \""+pre[a]+"\", which" +
                    "means there should be a part available, that has \"-c\" requirement or no requirements for previous syllables at all.");

        int b[] = new int[syls];
        for(int i = 0; i<b.length-2; i++){

            do{
                b[i] = (int)(Math.random() * mid.length);
            }
            while(expecting == 1 && !vowelFirst(pureSyl(mid[b[i]])) || expecting == 2 && !consonantFirst(pureSyl(mid[b[i]]))
                    || last == 1 && hatesPreviousVowels(mid[b[i]]) || last == 2 && hatesPreviousConsonants(mid[b[i]]));

            expecting = 0;
            if(expectsVowel(mid[b[i]])){
                expecting = 1;
                if(i < b.length-3 && !containsVocFirst(mid))
                    throw new RuntimeException("Expecting \"middle\" part starting with vowel but there is none");
                if(i == b.length-3 && !containsVocFirst(sur))
                    throw new RuntimeException("Expecting \"suffix\" part starting with vowel but there is none");
            }
            if(expectsConsonant(mid[b[i]])){
                expecting = 2;
                if(i < b.length-3 && !containsConsFirst(mid))
                    throw new RuntimeException("Expecting \"middle\" part starting with consonant but there is none");
                if(i == b.length-3 && !containsConsFirst(sur))
                    throw new RuntimeException("Expecting \"suffix\" part starting with consonant but there is none");
            }
            if(vowelLast(pureSyl(mid[b[i]])) && !allowVocs(mid) && syls > 3)
                throw new RuntimeException("Expecting \"middle\" part that allows last character of last syllable to be a Vowel, " +
                    "but there is none. the part used, was : \""+mid[b[i]]+"\", which " +
                    "means there should be a part available, that has \"-v\" requirement or no requirements for previous syllables at all.");

            if(consonantLast(pureSyl(mid[b[i]])) && !allowCons(mid) && syls > 3)
                throw new RuntimeException("Expecting \"middle\" part that allows last character of last syllable to be a consonant, " +
                    "but there is none. the part used, was : \""+mid[b[i]]+"\", which " +
                    "means there should be a part available, that has \"-c\" requirement or no requirements for previous syllables at all.");
            if(i == b.length-3){
                if(vowelLast(pureSyl(mid[b[i]])) && !allowVocs(sur))
                    throw new RuntimeException("Expecting \"suffix\" part that allows last character of last syllable to be a Vowel, " +
                        "but there is none. the part used, was : \""+mid[b[i]]+"\", which " +
                        "means there should be a suffix available, that has \"-v\" requirement or no requirements for previous syllables at all.");

                if(consonantLast(pureSyl(mid[b[i]])) && !allowCons(sur))
                    throw new RuntimeException("Expecting \"suffix\" part that allows last character of last syllable to be a consonant, " +
                        "but there is none. the part used, was : \""+mid[b[i]]+"\", which " +
                        "means there should be a suffix available, that has \"-c\" requirement or no requirements for previous syllables at all.");
            }
            if(vowelLast(pureSyl(mid[b[i]]))) last = 1;
            else last = 2;
        }

        int c;
        do{
            c = (int)(Math.random() * sur.length);
        }
        while(expecting == 1 && !vowelFirst(pureSyl(sur[c])) ||
                expecting == 2 && !consonantFirst(pureSyl(sur[c])) ||
                last == 1 && hatesPreviousVowels(sur[c]) ||
                last == 2 && hatesPreviousConsonants(sur[c]));

        StringBuilder sb = new StringBuilder();
        String first = pureSyl(pre[a]);
        sb.append(Character.toUpperCase(first.charAt(0)));
        for (int i = 1; i < first.length(); i++) {
            sb.append(first.charAt(i));
        }

        for(int i = 0; i<b.length-2; i++){
            sb.append(pureSyl(mid[b[i]]));
        }
        if(syls > 1)
            sb.append(pureSyl(sur[c]));
        return sb.toString();
    }


}
