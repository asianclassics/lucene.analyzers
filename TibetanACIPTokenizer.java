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

package  org.acip.analysis.tb;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
//import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.Version;
//import org.apache.lucene.util.Version;

/** A grammar-based tokenizer constructed with JFlex
 *
 * <p> This should be a good tokenizer for most European-language documents:
 *
 * <ul>
 *   <li>Splits words at punctuation characters, removing punctuation. However, a
 *     dot that's not followed by whitespace is considered part of a token.
 *   <li>Splits words at hyphens, unless there's a number in the token, in which case
 *     the whole token is interpreted as a product number and is not split.
 *   <li>Recognizes email addresses and internet hostnames as one token.
 * </ul>
 *
 * <p>Many applications have specific tokenizer needs.  If this tokenizer does
 * not suit your application, please consider copying this source code
 * directory to your project and maintaining your own grammar-based tokenizer.
 */

public class TibetanACIPTokenizer extends Tokenizer
	{
	/** A private instance of the JFlex-constructed scanner */
	private TibetanACIPTokenizerImpl scanner;

	public static final int WORD          = 0;
	public static final int COMMENT		  = 1;
	public static final int FOLIONO		  = 2;
	public static final int NUMBER		  = 3;
	public static final int PUNCTUATION   = 4;

	/** String token types that correspond to token type int constants */
	public static final String [] TOKEN_TYPES = new String []
		{
		"<WORD>",
		"<COMMENT>",
		"<FOLIONO>",
		"<NUMBER>",
		"<PUNCTUATION>"
		};

	private int skippedPositions;

	private int maxTokenLength = TibetanACIPAnalyzer.DEFAULT_MAX_TOKEN_LENGTH;

	/** Set the max allowed token length.  Any token longer
	*  than this is skipped.
	 * @param length */
	public void setMaxTokenLength( int length )
		{
		this.maxTokenLength = length;
		}	// End of setMaxTokenLength
	

	/**
	 * @return  *  @see #setMaxTokenLength */
	public int getMaxTokenLength()
		{
		return maxTokenLength;
		}	// End of getMaxTokenLength
	

  /**
   * Creates a new instance of the {@link org.apache.lucene.analysis.standard.TibetanACIPTokenizer}.  Attaches
   * the <code>input</code> to the newly created JFlex scanner.
   *
	 * @param matchVersion
   * @param input The input reader
   *
   * See http://issues.apache.org/jira/browse/LUCENE-1068
   */
	public TibetanACIPTokenizer( Version matchVersion, Reader input )
		{
		super( input );
		this.typeAtt	 = addAttribute( TypeAttribute.class);
		this.posIncrAtt = addAttribute( PositionIncrementAttribute.class);
		this.offsetAtt  = addAttribute( OffsetAttribute.class);
		this.termAtt	 = addAttribute( CharTermAttribute.class);
		init( matchVersion );
		}

	
	public TibetanACIPTokenizer( Version matchVersion, AttributeFactory factory, Reader input )
		{
		super( factory, input );
		this.typeAtt	 = addAttribute( TypeAttribute.class);
		this.posIncrAtt = addAttribute( PositionIncrementAttribute.class);
		this.offsetAtt  = addAttribute( OffsetAttribute.class);
		this.termAtt	 = addAttribute( CharTermAttribute.class);
		}

	
	private void init( Version matchVersion )
		{
		this.scanner = new TibetanACIPTokenizerImpl(input);
		}


	// this tokenizer generates three attributes:
	// term offset, positionIncrement and type
	private final CharTermAttribute			  termAtt;
	private final OffsetAttribute				  offsetAtt;
	private final PositionIncrementAttribute posIncrAtt;
	private final TypeAttribute				  typeAtt;

  /*
   * (non-Javadoc)
   *
   * @see org.apache.lucene.analysis.TokenStream#next()
   */
	@Override
	public final boolean incrementToken() throws IOException
		{
		clearAttributes();
		skippedPositions = 0;

		while( true )
			{
			int tokenType = scanner.getNextToken();

			if ( tokenType == TibetanACIPTokenizerImpl.YYEOF )
				{
				return false;
				}

			if ( scanner.yylength() <= maxTokenLength )
				{
				posIncrAtt.setPositionIncrement( skippedPositions+1 );
				scanner.getText( termAtt );
				final int start = scanner.yychar();
				offsetAtt.setOffset( correctOffset(start), correctOffset(start+termAtt.length()) );
				if ( tokenType == TibetanACIPTokenizerImpl.WORD )
					{
					typeAtt.setType( TibetanACIPTokenizerImpl.TOKEN_TYPES[tokenType] );
					return true;
					}
				}
			else
				{
				// When we skip a too-long term, we still increment the
				// position increment
				skippedPositions++;
				}
			}
		}	// End of incrementToken
	
    /*
     * (non-Javadoc)
     *
     * @see org.apache.lucene.analysis.TokenStream#reset()
     */
	
	@Override
	public final void end() throws IOException
		{
		int finalOffset = correctOffset( scanner.yychar() + scanner.yylength() );
		offsetAtt.setOffset( finalOffset,  finalOffset );
		// adjust any skipped tokens
		posIncrAtt.setPositionIncrement(posIncrAtt.getPositionIncrement()+skippedPositions);
		}
	
	
	@Override
	public void close() throws IOException
		{
		super.close();
		}	// End of close
	
	
	@Override
	public void reset() throws IOException
		{
		super.reset();
		scanner.yyreset(input);
		skippedPositions = 0;
		}	// End of reset

	}
