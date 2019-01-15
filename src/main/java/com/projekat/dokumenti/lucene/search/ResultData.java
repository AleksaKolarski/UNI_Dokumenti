package com.projekat.dokumenti.lucene.search;

public class ResultData {
	
	private String filename;
	private String highlight;
	
	
	public ResultData(String filename, String highlight) {
		this.filename = filename;
		this.highlight = highlight;
	}

	
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getHighlight() {
		return highlight;
	}

	public void setHighlight(String highlight) {
		this.highlight = highlight;
	}
}
