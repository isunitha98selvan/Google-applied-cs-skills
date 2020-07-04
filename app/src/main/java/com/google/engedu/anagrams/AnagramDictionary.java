/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    protected static  int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private ArrayList<String> wordList = new ArrayList<String>();
    private HashMap<Integer,List<String>> sizeToWords = new HashMap<Integer, List<String>>();
    private HashMap <String,List<String>> lettersToWord = new HashMap<String, List<String>>();
    private HashSet<String> wordSet = new HashSet<String>();

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while ((line = in.readLine()) != null) {
            String word = line.trim();
            if (sizeToWords.get(word.length()) == null){
                sizeToWords.put(word.length(),new ArrayList<String>());
            }
            sizeToWords.get(word.length()).add(word);
            wordList.add(word);
            wordSet.add(word);
            String key = sortLetters(word);
            if (lettersToWord.get(key) ==null){
                lettersToWord.put(key,new ArrayList<String>());
            }
            lettersToWord.get(key).add(word);
        }
    }

    public boolean isGoodWord(String word, String base) {
        if(wordSet.contains(word) && !word.contains(base)){
            return true;
        }
        return false;
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        String sortedWord = sortLetters(targetWord);
        for (String anagram : wordList) {
            if (sortLetters(anagram).equals(sortedWord)) {
                result.add(anagram);
            }
        }
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        int ascii_a = (int)'a';
        for(int i=0;i<26;i++) {
            String newWord = word + Character.toString((char) (ascii_a + i));
            String key = sortLetters(newWord);
            if (lettersToWord.get(key) != null) {
                for (String anagram : lettersToWord.get(key)) {
                    result.add(anagram);
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        String starter;
        List<String> words = new ArrayList<String>();
        words = sizeToWords.get(DEFAULT_WORD_LENGTH);
        Random rand = new Random();
        int index = rand.nextInt(words.size());
        for(int i=index;i<words.size();i++)
        {
            String word = words.get(i);
            String sortedWord = sortLetters(word);
            if(lettersToWord.get(sortedWord).size()>MIN_NUM_ANAGRAMS)
                return word;
            if(i == wordList.size()){
                i=0;
            }
            if(i==index-1)
                break;
        }
        DEFAULT_WORD_LENGTH+=1;
        return words.get(index);
    }

    public String sortLetters(String word) {
        char tempArray[] = word.toCharArray();
        Arrays.sort(tempArray);
        return new String(tempArray);
    }
}