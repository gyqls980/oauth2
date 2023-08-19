package weblogin.oauth2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import weblogin.oauth2.domain.Member;
import weblogin.oauth2.domain.MemberForm;
import weblogin.oauth2.security.SecurityConfiguration;
import weblogin.oauth2.service.MemberService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final SecurityConfiguration pwEncoder;

    @GetMapping("/member/new")
    public String createForm(Model model){
        model.addAttribute("memberForm", new MemberForm());
        return "member/createMemberForm";
    }

    @PostMapping("/member/new")
    public String create(@Valid MemberForm form, BindingResult result) {
        //@Valid 다음에 BindingResult => error 사항을 담아준다
        if(result.hasErrors()){
            return "member/createMemberForm";
        }

        //form 화면 validation check : 그대로 entity 에 받아서 하기보다
        //form에 맞는 formdata entity 생성 후 그거에 대한 validation 체크 후
        //실제 entity에 저장하는 것을 추천한다
        Member member = new Member();
        member.setName(form.getName());
        member.setEmail(form.getEmail());
        member.setPassword(form.getPassword());
        member.setRole(form.getRole());

        memberService.join(member);

        return "redirect:/";
    }

    @GetMapping("member/login")
    public String linkLoginPage(Model model){
        model.addAttribute("memberLoginForm", new Member());
        return "member/loginMemberForm";
    }

    @PostMapping("member/login")
    public String login(@Valid Member form, Model model, HttpServletRequest request){
        Member member = new Member();
        member.setEmail(form.getEmail());
        member.setPassword(form.getPassword());

        Optional<Member> memberInfo = memberService.isMember(member);


        String rawPw = member.getPassword();
        String decodePw = memberInfo.get().getPassword();

        if(pwEncoder.passwordEncoder().matches(rawPw, decodePw)){
            HttpSession session = request.getSession();
            session.setAttribute("loginName", memberInfo.get().getName());
            session.setAttribute("loginRole", memberInfo.get().getRole());
            return "home";
        }

        return "member/loginMemberForm";
    }

    @GetMapping("/members")
    public String list(Model model){
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "member/memberList";
    }


}