package edu.kh.comm.member.controller;

 

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.comm.member.model.service.MemberService;
import edu.kh.comm.member.model.vo.Member;
@Controller
@RequestMapping("/member")
@SessionAttributes({"loginMember"})

public class MemberController {

	private Logger logger = LoggerFactory.getLogger(MemberController.class);
	 
	@Autowired
	private MemberService service;
	
	@PostMapping("/login")
	public String login( @ModelAttribute Member inputMember 
						, Model model
						, RedirectAttributes ra
						, HttpServletResponse resp
						, HttpServletRequest req
						, @RequestParam(value="saveId", required = false) String saveId) {
							
		logger.info("로그인 기능 수행됨~");
		
		Member loginMember = service.login(inputMember);
		
		if(loginMember != null) {
			model.addAttribute("loginMember", loginMember);
			
			Cookie cookie = new Cookie("saveId", loginMember.getMemberEmail());
			
			if(saveId != null) {
				cookie.setMaxAge(60 * 60 * 24 * 365);
				
			} else {
				cookie.setMaxAge(0);
			}
			
			cookie.setPath(req.getContextPath());
			
			resp.addCookie(cookie);
		
		} else {
			ra.addFlashAttribute("message", "아이디 또는 비밀번호가 일치하지 않습니다.");
		}
		
		return "redirect:/";
		
	}
	
	@GetMapping("/logout")
	public String logout(SessionStatus status) {
		logger.info("로그아웃 수행됨ㅋㅋ");
		
		status.setComplete();
		return "redirect:/";
	}
	
	@ResponseBody
	@GetMapping("/emailDupCheck")
	public int emailDupCheck(String memberEmail) {
		int result = service.emailDupCheck(memberEmail);
		return result;
	}
	
	
	@ResponseBody
	@GetMapping("/nicknameDupCheck")
	public int nicknameDupCheck(String memberNickname) {
		int result = service.nicknameDupCheck(memberNickname);
		return result;
	}
	
	
	
	@GetMapping("/signUp")
	public String signUp() {
		return "member/signUp";
	}
	
	
	@PostMapping("/signUp")
	public String signUp(Member inputMember
					, String[] memberAddress
					, RedirectAttributes ra) {
						
		inputMember.setMemberAddress(String.join(",,", memberAddress));
		
		if(inputMember.getMemberAddress().equals(",,,,")) {
			inputMember.setMemberAddress(null);
		}
		
		int result = service.signUp(inputMember);
		
		String message = null;
		String path = null;
		
		if(result > 0) {
			message = "회원 가입 성공!ㅋ";
			path = "redirect:/";
			
		}else {
			message = "회원 가입 실패ㅋ";
			path = "redirect:/member/signUp";
	 
		}
		ra.addFlashAttribute("message", message); 
		return path;
		
		}
	
	
	
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
 
