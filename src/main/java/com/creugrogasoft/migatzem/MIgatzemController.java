package com.creugrogasoft.migatzem;

import javax.annotation.Resource;
import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class MIgatzemController {

	@Resource(name = "userInfoBean")
	private UserInfo userInfo;
	
	@Autowired
	private Environment  env;
	
	Logger logger = LoggerFactory.getLogger(LoggingController.class);
	
	@RequestMapping("/")
    public String home(Model model) {
		if (!userInfo.isLogged())
			return "login";
		
        return "redirect:/articles";
    }
	
	@RequestMapping("/login")
    public String login(ModelMap model, @RequestParam (value = "username", required = true) String username,
    		@RequestParam (value = "password", required = true) String password) throws NamingException {
		
		try{
			LdapContext ctx = ActiveDirectory.getConnection(username, password);
			User user = ActiveDirectory.getUser(username, ctx);
		    ctx.close();
		    userInfo.setName(user.getUserPrincipal());
		    userInfo.setFullName(user.getCommonName());
		    // user logged ok, check if app is userRestricted
		    userInfo.setLogged(true);
		    return "redirect:/";
		}
		catch(Exception e) {
		    //Failed to authenticate user!
			logger.error(e.getLocalizedMessage());
			userInfo.setLogged(false);
			model.addAttribute("message", "Login incorrecte. Recordi que és el mateix usuari i contrasenya amb el que ha accedit a l'ordinador.");
		}
		
		return "login";
	}

}
