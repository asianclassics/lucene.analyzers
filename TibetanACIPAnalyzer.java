package org.acip.analysis.tb;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.lucene.analysis.*;
//import org.apache.lucene.analysis.LowerCaseFilter;
//import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
//import org.apache.lucene.analysis.WordlistLoader;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

import java.util.Set;
import org.apache.lucene.util.Version;

/**
 * Filters {@link StandardTokenizer} with {@link StandardFilter}, {@link
 * LowerCaseFilter} and {@link StopFilter}, using a list of
 * English stop words.
 *
 * <a name="version"/>
 * <p>You must specify the required {@link Version}
 * compatibility when creating StandardAnalyzer:
 * <ul>
 *   <li> As of 3.4, Hiragana and Han characters are no longer wrongly split
 *        from their combining characters. If you use a previous version number,
 *        you get the exact broken behavior for backwards compatibility.
 *   <li> As of 3.1, StandardTokenizer implements Unicode text segmentation,
 *        and StopFilter correctly handles Unicode 4.0 supplementary characters
 *        in stopwords.  {@link ClassicTokenizer} and {@link ClassicAnalyzer} 
 *        are the pre-3.1 implementations of StandardTokenizer and
 *        StandardAnalyzer.
 *   <li> As of 2.9, StopFilter preserves position increments
 *   <li> As of 2.4, Tokens incorrectly identified as acronyms
 *        are corrected (see <a href="https://issues.apache.org/jira/browse/LUCENE-1068">LUCENE-1068</a>)
 * </ul>
 */
public class TibetanACIPAnalyzer extends Analyzer 
	{
	private Set stopSet;

	/**
	 * Builds an analyzer with the default stop words ({@link #STOP_WORDS}).
	 */
	public TibetanACIPAnalyzer() {}
	
	
	/**
	 * Builds an analyzer with the given stop words.
	 * @param stopWords
	 */
//	public TibetanACIPAnalyzer(String[] stopWords) 
//		{
//		stopSet = StopFilter.makeStopSet(stopWords);
//		}

	/**
	 * Builds an analyzer with the stop words from the given file.
	 *
	 * @param stopwords
	 * @throws java.io.IOException
	 * @see WordlistLoader#getWordSet(File)
	 */
	public TibetanACIPAnalyzer(File stopwords) throws IOException 
		{
//		stopSet = WordlistLoader.getWordSet(stopwords);
		}

	/**
	 * Builds an analyzer with the stop words from the given reader.
	 *
	 * @param stopWords
	 * @throws java.io.IOException
	 * @see WordlistLoader#getWordSet(Reader)
	 */
	public TibetanACIPAnalyzer(Reader stopWords) throws IOException 
		{
//		stopSet = WordlistLoader.getWordSet(stopWords);
		} // End of TibetanACIPAnalyzer CONSTRUCTOR

	/**
	 * Constructs a {@link TibetanACIPTokenizer} filtered by a {@link
	 * TibetanACIPFilter}, a {@link LowerCaseFilter} and a {@link StopFilter}.
	 * @param fieldName
	 * @param reader
	 * @return 
	 */
//	public TokenStream tokenStream( String fieldName, Reader reader ) 
//		{
//		TibetanACIPTokenizer tokenStream = new TibetanACIPTokenizer( reader );
//		tokenStream.setMaxTokenLength( maxTokenLength );
//		TokenStream result = new TibetanACIPFilter( tokenStream );
//		result = new LowerCaseFilter(result);
//		result = new StopFilter(result, stopSet);
//		return result;
//		}	// End of tokenStream

	@Override
   protected TokenStreamComponents createComponents(String fieldName, Reader reader) 
		{
		Tokenizer source = new TibetanACIPTokenizer( Version.LUCENE_36, reader );
		TokenStream filter = new TibetanACIPFilter(source);
		return new TokenStreamComponents( source, filter );
		}
	
	
	private static final class SavedStreams 
		{
		TibetanACIPTokenizer tokenStream;
		TokenStream filteredTokenStream;
		} // End of class SavedStreams


	/** Default maximum allowed token length */
	public static final int DEFAULT_MAX_TOKEN_LENGTH = 255;

	private int maxTokenLength = DEFAULT_MAX_TOKEN_LENGTH;

	/**
	 * Set maximum allowed token length. If a token is seen that exceeds this
	 * length then it is discarded. This setting only takes effect the next time
	 * tokenStream or reusableTokenStream is called.
	 * @param length
	 */
	
	public void setMaxTokenLength(int length)
		{
		maxTokenLength = length;
		} // End of setMaxTokenLength
	

	/**
	 * @return 
	* @see #setMaxTokenLength
	*/
	public int getMaxTokenLength()
		{
		return maxTokenLength;
		} // End of getMaxTokenLength
	
	
//	@Override
//	public TokenStream reusableTokenStream(String fieldName, Reader reader) throws IOException
//		{
//		SavedStreams streams = (SavedStreams) getPreviousTokenStream();
//		if (streams == null)
//			{
//			streams = new SavedStreams();
//			setPreviousTokenStream(streams);
//			streams.tokenStream = new TibetanACIPTokenizer(reader);
//			streams.filteredTokenStream = new TibetanACIPFilter(streams.tokenStream);
//			streams.filteredTokenStream = new LowerCaseFilter(streams.filteredTokenStream);
//			streams.filteredTokenStream = new StopFilter(streams.filteredTokenStream, stopSet);
//			}
//		else
//			{
//			streams.tokenStream.reset(reader);
//			}
//		streams.tokenStream.setMaxTokenLength(maxTokenLength);

//		return streams.filteredTokenStream;
//		} // End of reusableTokenStream
	
}	// End of class TibetanACIPAnalyzer
