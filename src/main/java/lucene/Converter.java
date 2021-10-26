package lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Converter {

        public Converter(){


        }


        public Set<String> parseKeywords(Analyzer analyzer, String field, String keywords) throws IOException {

            Set<String> result = new HashSet<>();
            TokenStream stream  = analyzer.tokenStream(field, new StringReader(keywords));

            try {
                stream.reset();
                while(stream.incrementToken()) {
                    result.add(stream.getAttribute(CharTermAttribute.class).toString());
                }
            }
            catch(IOException e) {

            }

            return result;
        }

}
