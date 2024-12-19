package in.vishalit.dto;

public class QuoteApiResponseDTO {
	
	private Integer id;
	private String Quote;
	private String author;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getQuote() {
		return Quote;
	}
	public void setQuote(String quote) {
		Quote = quote;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	
}
