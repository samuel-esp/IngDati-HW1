package lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class Converter {

        public Converter(){


        }


        public List<String> parseKeywords(Analyzer analyzer, String field, String keywords) throws IOException {

            List<String> result = new ArrayList<String>();
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
