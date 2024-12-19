package in.vishalit.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import in.vishalit.constants.AppConstants;
import in.vishalit.dto.LoginFormDTO;
import in.vishalit.dto.QuoteApiResponseDTO;
import in.vishalit.dto.RegisterFormDTO;
import in.vishalit.dto.ResetPwdFormDTO;
import in.vishalit.dto.UserDTO;
import in.vishalit.service.DashboardService;
import in.vishalit.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DashboardService dashboardService;
	
	@GetMapping("/register")
	public String loadRegisterPage(Model model) {
		
		Map<Integer, String> countriesMap = userService.getCountries();
		model.addAttribute("countries", countriesMap);
		
		RegisterFormDTO registerFormDTO = new RegisterFormDTO();
		model.addAttribute("registerForm", registerFormDTO);
		
		return "register";
		
	}
	
	@GetMapping("/states/{countryId}")
	@ResponseBody
	public Map<Integer,String> getStates(@PathVariable Integer countryId) {
		
//		Map<Integer, String> statesMap = userService.getStates(countryId);
//		return statesMap; after code review write this type
		return userService.getStates(countryId);
		
	}
	
	
	@GetMapping("/cities/{stateId}")
	@ResponseBody
	public Map<Integer,String> getCities(@PathVariable Integer stateId) {
		
		//Map<Integer, String> citiesMap = userService.getCities(stateId);
		//return citiesMap; or write the code below after the code review
		return userService.getCities(stateId);
		
	}
	
	@PostMapping("/register")
	public String handleRegistration(RegisterFormDTO registerFormDTO,Model model) {
		
		boolean status = userService.duplicateEmailCheck(registerFormDTO.getEmail());
		if(status) {
			model.addAttribute("emsg", "Duplicate Email Found");
		}else {
			boolean saveUser = userService.saveUser(registerFormDTO);
			if(saveUser) {
				//user saved
				model.addAttribute("smsg", "Registration Success, Please check your email..!!");
			}else {
				//failed to save
				model.addAttribute("emsg", "Registration Failed!");
			}
		}
		model.addAttribute("registerForm",new RegisterFormDTO());
		model.addAttribute("countries", userService.getCountries());
		return "register";
	}
	
	@GetMapping("/")
	public String index(Model model) {
		
		LoginFormDTO loginFormDTO = new LoginFormDTO();
		
		model.addAttribute("loginForm", loginFormDTO);
		
		return "login";
	}
	
	@PostMapping("/login")
	public String handleUserLogin(LoginFormDTO loginFormDTO,Model model) {
		
		UserDTO userDTO = userService.login(loginFormDTO);
		
		if(userDTO == null) {
			model.addAttribute("emsg", "Invalid Credentials");
			model.addAttribute("loginForm", new LoginFormDTO());
		}else {
			String pwdUpdated = userDTO.getPwdUpdated();
			if("Yes".equals(pwdUpdated)) {
				//display dashboard
				return "redirect:dashboard";
			}else {
				//display reset pwd page
				return "redirect:rest-pwd-page?email=" +userDTO.getEmail();
			}
		}
		
		return "login";
	}
	
	@GetMapping("/dashboard")
	public String dashboard(Model model) {
		
		QuoteApiResponseDTO quoteApiResponseDTO = dashboardService.getQuote();
		
		model.addAttribute("quote", quoteApiResponseDTO);
		
		return "dashboard";
	}
	
	@GetMapping("/rest-pwd-page")
	public String loadResetPwdPage(@RequestParam String email,Model model) {
		
		ResetPwdFormDTO resetPwdFormDTO = new ResetPwdFormDTO();
		resetPwdFormDTO.setEmail(email);
		
		model.addAttribute(AppConstants.RESET_PWD, resetPwdFormDTO);
		
		return AppConstants.RESET_PWD;
	}
	
	@PostMapping("/resetPwd")
	public String handlePwdReset(ResetPwdFormDTO resetPwdFormDTO ,Model model) {
		
		boolean resetPwd = userService.resetPwd(resetPwdFormDTO);
		
		if(resetPwd) {
			return "redirect:dashboard";
		}
		
		return AppConstants.RESET_PWD;
	}
}
