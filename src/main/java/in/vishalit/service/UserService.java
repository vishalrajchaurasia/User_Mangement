package in.vishalit.service;

import java.util.Map;

import in.vishalit.dto.LoginFormDTO;
import in.vishalit.dto.RegisterFormDTO;
import in.vishalit.dto.ResetPwdFormDTO;
import in.vishalit.dto.UserDTO;

public interface UserService {
	
    public Map<Integer,String > getCountries();
	
	public Map<Integer,String > getStates(Integer countryId);
	
	public Map<Integer,String > getCities(Integer stateId);
	
	public boolean duplicateEmailCheck(String email);
	
	public boolean saveUser(RegisterFormDTO regFormDTO);
	
	public UserDTO login(LoginFormDTO loginFormDTO);
	
	public boolean resetPwd(ResetPwdFormDTO resetPwdDTO);
	
	//public UserDTO getUserByEmail(String email);
}
