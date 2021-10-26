package lucene;


import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.ru.RussianAnalyzer;
import org.apache.lucene.analysis.*;


import org.apache.lucene.analysis.Analyzer;


public class MyAnalyzer extends Analyzer {

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {

        Tokenizer src = new WhitespaceTokenizer();
        TokenStream  result = new LowerCaseFilter(src);

        CharArraySet enStopSet = EnglishAnalyzer.ENGLISH_STOP_WORDS_SET;
        result = new StopFilter(result, enStopSet);
        CharArraySet ruStopSet = RussianAnalyzer.getDefaultStopSet();
        result = new StopFilter(result, ruStopSet);
        return new TokenStreamComponents(src, result);

    }
}
