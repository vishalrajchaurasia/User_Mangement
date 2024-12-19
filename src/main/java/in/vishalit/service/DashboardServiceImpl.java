package in.vishalit.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import in.vishalit.dto.QuoteApiResponseDTO;

@Service
public class DashboardServiceImpl implements DashboardService {
	
	private String quoteApiURL= "https://dummyjson.com/quotes/random";
	
	@Override
	public QuoteApiResponseDTO getQuote() {
		
		RestTemplate rt = new RestTemplate();
		
		//send HTTP Get req and store Response into QuoteApiResponseDTO object
		ResponseEntity<QuoteApiResponseDTO> forEntity =
				rt.getForEntity(quoteApiURL, QuoteApiResponseDTO.class);
		
		
//		QuoteApiResponseDTO body = forEntity.getBody();
//		return body; after code review
		return forEntity.getBody();
	}

}
