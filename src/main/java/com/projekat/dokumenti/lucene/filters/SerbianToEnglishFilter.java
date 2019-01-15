package com.projekat.dokumenti.lucene.filters;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class SerbianToEnglishFilter extends TokenFilter {
	
	private CharTermAttribute termAttribute;

	public SerbianToEnglishFilter(TokenStream input) {
		super(input);
		termAttribute = (CharTermAttribute) input.addAttribute(CharTermAttribute.class);
	}

	@Override
	public boolean incrementToken() throws IOException {
		if(input.incrementToken()) {
			String text = termAttribute.toString();
			termAttribute.setEmpty();
			termAttribute.append(SerbianEnglishConverter.srb2eng(text));
			return true;
		}
		return false;
	}

}
