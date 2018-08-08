package com.hr.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hr.model.ResponseResult;
import com.hr.model.User;
import com.hr.service.CompanyService;
import com.hr.service.ResumeService;
import com.hr.service.UserService;

@Controller
public class SecurityAction {
	private final static Logger logger = LoggerFactory.getLogger(SecurityAction.class);
	@Resource
	private ResumeService resumeService;
	@Resource
	private CompanyService companyService;
	@Resource
	private UserService userService;

	@RequestMapping("/index")
	public String index(Model model,HttpSession session) {
		session.setAttribute("name", "haha");
		System.out.println(session.getId());
		return "index";
	}

	/**
	 * 登录页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/login")
	public String login() {
		logger.debug("登录了");
		return "login";
	}

	/**
	 * 登出
	 */
	@RequestMapping(value = "/logout", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ResponseResult<Void> logout() {
		ResponseResult<Void> result = new ResponseResult<>();
		Subject subject = SecurityUtils.getSubject();
		if (subject != null) {
			subject.logout();
		}
		result.setStatus(ResponseResult.STATE_OK);
		result.setMessage("登出成功");
		return result;
	}

	/**
	 * 普通用户登录处理
	 * 
	 * @param username
	 * @param password
	 * @param session
	 * @return
	 */
	@PostMapping("/generalLoginHandler")
	@ResponseBody
	public ResponseResult<Map<String, String>> generalLoginHandler(@RequestParam("username") String username,
			@RequestParam("password") String password, HttpSession session) {
		ResponseResult<Map<String, String>> result = new ResponseResult<>();
		Map<String, String> resultMap = new HashMap<>();
		// shiro登录获得用户信息
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(token);
			User user = (User) subject.getPrincipal();
			session.setAttribute("user", user);
			// 检查用户简历是否填写完整
			boolean ifFull = resumeService.checkResumeFull();// checkResumeFull()放到userService里面会不会好点
			String resumeFull = ifFull ? "yes" : "no";
			resultMap.put("resumeFull", resumeFull);

			result.setData(resultMap);
			result.setStatus(ResponseResult.STATE_OK);
			result.setMessage("成功登录");
			return result;
		} catch (Exception e) {
			result.setStatus(ResponseResult.STATE_ERROR);
			result.setMessage("用户名或密码错误");
			return result;
		}
	}

	/**
	 * 企业登录
	 * 
	 * @param username
	 * @param password
	 * @param session
	 * @return
	 */
	@PostMapping("/companyLoginHandler")
	@ResponseBody
	public ResponseResult<Map<String, String>> companyLoginHandler(@RequestParam("username") String username,
			@RequestParam("password") String password, HttpSession session) {
		ResponseResult<Map<String, String>> result = new ResponseResult<>();
		Map<String, String> resultMap = new HashMap<>();
		// shiro登录获得用户信息
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(token);
			User user = (User) subject.getPrincipal();
			session.setAttribute("user", user);
			// 检查用户简历是否填写完整
			boolean ifFull = companyService.checkResumeFull();
			String resumeFull = ifFull ? "yes" : "no";
			resultMap.put("resumeFull", resumeFull);

			result.setData(resultMap);
			result.setStatus(ResponseResult.STATE_OK);
			result.setMessage("成功登录");
			return result;
		} catch (Exception e) {
			result.setStatus(ResponseResult.STATE_ERROR);
			result.setMessage("用户名或密码错误");
			return result;
		}
	}
}
