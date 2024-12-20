package in.vishalit.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.vishalit.dto.LoginFormDTO;
import in.vishalit.dto.RegisterFormDTO;
import in.vishalit.dto.ResetPwdFormDTO;
import in.vishalit.dto.UserDTO;
import in.vishalit.entities.CityEntity;
import in.vishalit.entities.CountryEntity;
import in.vishalit.entities.StateEntity;
import in.vishalit.entities.UserEntity;
import in.vishalit.repo.CityRepo;
import in.vishalit.repo.CountryRepo;
import in.vishalit.repo.StateRepo;
import in.vishalit.repo.UserRepo;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private CountryRepo countryRepo;
	
	@Autowired
	private StateRepo stateRepo;
	
	@Autowired
	private CityRepo cityRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private EmailService emailService;
	
	Random random = new Random(); //after code review write here 

	@Override
	public Map<Integer, String> getCountries() {
		Map<Integer,String> countryMap = new HashMap<>();
		
		List<CountryEntity> countriesList = countryRepo.findAll();//countriesList is now convert into a MAP 
		
//		countriesList.forEach(c->{ 
//			countryMap.put(c.getCountryId(), c.getCountryName());
//			}); after code review
		countriesList.forEach(c-> countryMap.put(c.getCountryId(), c.getCountryName()));
			
		
		return countryMap;
	}

	@Override
	public Map<Integer, String> getStates(Integer countryId) {
		Map<Integer,String> statesMap = new HashMap<>();
		
		List<StateEntity> statesList = stateRepo.findByCountryId(countryId);
		//same here after code review 
		statesList.forEach(s->statesMap.put(s.getStateId(), s.getStateName()));
		
		
		return statesMap;
	}

	@Override
	public Map<Integer, String> getCities(Integer stateId) {
		Map<Integer,String> citiesMap = new HashMap<>();
		
		List<CityEntity> citiesList = cityRepo.findByStateId(stateId);
		
		//same here after code review
		citiesList.forEach(c->citiesMap.put(c.getCityId(),c.getCityName()));
		
		return citiesMap;
	}

	@Override
	public boolean duplicateEmailCheck(String email) {
		
		UserEntity byEmail = userRepo.findByEmail(email);
		// after code review
		
		return byEmail !=null;
		
//		if(byEmail !=null) {
//			return true;//true means record is already available
//		}else {
//			return false;
//		}
		
	}

	@Override
	public boolean saveUser(RegisterFormDTO regFormDTO) {
		
		UserEntity userEntity = new UserEntity();
		
		BeanUtils.copyProperties(regFormDTO, userEntity);
		
		CountryEntity country = countryRepo.findById(regFormDTO.getCountryId()).orElse(null);
		userEntity.setCountry(country);
		
		StateEntity state = stateRepo.findById(regFormDTO.getStateId()).orElse(null);
		userEntity.setState(state);
		
		CityEntity city = cityRepo.findById(regFormDTO.getCityId()).orElse(null);
		userEntity.setCity(city);
		
		String randomPwd = generateRandomPwd();
	
		userEntity.setPwd(randomPwd);
		userEntity.setPwdUpdated("No");
		
		UserEntity savedUser = userRepo.save(userEntity);
		
		if(null != savedUser.getUserId()) {
			String subject = "Your Account Created";
			String body ="Your Password To Logiin : "+ randomPwd;
			String to= regFormDTO.getEmail();
			
			emailService.sendEmail(subject, body, to);
			
			return true;
		}
		
		return false;
	}

	@Override
	public UserDTO login(LoginFormDTO loginFormDTO) {
		UserEntity userEntity = userRepo.findByEmailAndPwd(loginFormDTO.getEmail(), loginFormDTO.getPwd());
		if(userEntity!=null) {
			UserDTO userDTO = new UserDTO();
			BeanUtils.copyProperties(userEntity, userDTO);
			return userDTO;
		}
		return null;
	}

	@Override
	public boolean resetPwd(ResetPwdFormDTO resetPwdDTO) {
		
		String email = resetPwdDTO.getEmail();
		
		UserEntity entity = userRepo.findByEmail(email);
		
		//setting new PWD
		entity.setPwd(resetPwdDTO.getNewPwd());
		entity.setPwdUpdated("Yes");
		
		userRepo.save(entity);
		
		return true;
	}
	
	private String generateRandomPwd() {
		
		String upperCaseLetters="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String lowerCaseLetters="abcdefghijklmnopqrstuvwxyz";
		
		String alphabets= upperCaseLetters + lowerCaseLetters;
		
		
		// after code review
		//StringBuffer  generatedPwd = new StringBuffer();//StringBuffer to prepare the password
		StringBuilder  generatedPwd = new StringBuilder();
		
		for(int i =0; i<5; i++) {
			//give any no. from 0 to 51
			int randomIndex = random.nextInt(alphabets.length());
			
			generatedPwd.append(alphabets.charAt(randomIndex));
		}
		
		return generatedPwd.toString();//finally converted to StringBuffer to String object.
				
	}

}
