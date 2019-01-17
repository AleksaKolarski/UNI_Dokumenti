package com.projekat.dokumenti.dto;


public class ResultDataDTO {

	private EBookDTO ebookDTO;
	private String highlight;
	
	public ResultDataDTO(EBookDTO ebookDTO, String highlight) {
		this.ebookDTO = ebookDTO;
		this.highlight = highlight;
	}
	
	
	public EBookDTO getEbookDTO() {
		return ebookDTO;
	}

	public void setEbookDTO(EBookDTO ebookDTO) {
		this.ebookDTO = ebookDTO;
	}

	public String getHighlight() {
		return highlight;
	}

	public void setHighlight(String highlight) {
		this.highlight = highlight;
	}
}
