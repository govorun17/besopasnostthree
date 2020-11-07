import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public final class Algorithm {
    private static Algorithm instance;
    private Algorithm() {}
    public static Algorithm getInstance() {
        if (instance == null) {
            instance = new Algorithm();
        }
        return instance;
    }

    private File file;

    public void setFile(File file) {
        this.file = file;
    }

    public String code(String s) throws IOException {
        if (s == null || s.isEmpty()) {
            throw new IOException("Пустая строка!");
        }

        String[] words = makeValid(s).toLowerCase().split(" ");

        Map<String, Integer> wordCount = new LinkedHashMap<>();
        Map<String, ArrayList<String>> wordKeys = new LinkedHashMap<>();

        for (String word : words) {
            if (wordCount.get(word) == null) {
                wordCount.put(word, 1);
                wordKeys.put(word, new ArrayList<>());
            }
            else {
                int v = wordCount.get(word);
                v++;
                wordCount.replace(word, v);
            }
        }

        try (FileInputStream fis = new FileInputStream(file)) {

            XWPFDocument document = new XWPFDocument(fis);
            List<XWPFParagraph> paragraphs = document.getParagraphs();

            for (String word : words) {
                int pageIndex = 0;
                List<String> keys = wordKeys.get(word);
                for (XWPFParagraph para : paragraphs) {
                    String[] pageText = makeValid(para.getText()).toLowerCase().split(" ");

                    List<Integer> wordIndexes = getIndexOnPage(word, pageText);
                    if (wordIndexes.size() != 0) {
                        int i = pageIndex;
                        wordIndexes.forEach(index -> {
                            keys.add(i + "-" + index);
                        });
                    }

                    ++pageIndex;
                }
            }
        }

        StringBuilder res = new StringBuilder();

        for (String word : words) {
            int count = wordCount.get(word);
            List<String> keys = wordKeys.get(word);

            if (keys.size() == 0) {
                throw new IOException("Не удалось зашифровать слово: " + word);
            }

            res.append(keys.get(count % keys.size()));
            count--;
            wordCount.replace(word, count);
        }
        return res.toString();
    }

    public String decode(String k) {
        return null;
    }

    private String makeValid(String s) {
        StringBuilder result = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (!isNonCodeValue(c)){
                result.append(c);
            }
        }
        return result.toString();
    }

    private boolean isNonCodeValue(char c) {
        //ASCII
        return (c >= '{' && c <= '~') || (c >= '[' && c <= '`') || (c >= '!' && c <= '@');
    }

    private List<Integer> getIndexOnPage(String w, String[] p) {
        List<Integer> indexes = new ArrayList<>();
        int index = 0;
        for (String wp : p) {
            if (w.equals(wp)) {
                indexes.add(index);
            }
            index++;
        }
        return indexes;
    }
}
