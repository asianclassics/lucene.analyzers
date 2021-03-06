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

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
//import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
//import org.apache.lucene.util.Version;

/** Normalizes tokens extracted with {@link StandardTokenizer}. */

public final class TibetanACIPFilter extends TokenFilter
	{
	// this filters uses attribute type
	private TypeAttribute typeAtt;
//	private TermAttribute termAtt;

	/** Construct filtering <i>in</i>. */
	public TibetanACIPFilter(  TokenStream in )
		{
		super( in );
//		termAtt = addAttribute(TermAttribute.class);
		typeAtt = addAttribute(TypeAttribute.class);
		}	// End of TibetanACIPFilter CONSTRUCTOR

	
	/** Returns the next token in the stream, or null at EOS.
	 * <p>Removes <tt>'s</tt> from the end of words.
	 * <p>Removes dots from acronyms.
	 */
  @Override
	public final boolean incrementToken() throws java.io.IOException
		{
		if ( !input.incrementToken() )
			{
			return false;
			}

		return true;
		}

}	// End of public final class TibetanACIPFilter
