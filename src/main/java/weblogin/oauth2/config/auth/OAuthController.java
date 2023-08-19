package weblogin.oauth2.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import weblogin.oauth2.domain.MemberRole;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/oauth")
public class OAuthController {
    @GetMapping("/loginInfo") //localhost:8080/oauth/loginInfo
    public String oauthLoginInfo (Authentication authentication, HttpServletRequest request){ //인증토큰 방식 활
        //oAuth2User.toString() 예시 :
        // Name: [2346930276],
        // Granted Authorities: [[USER]],
        // User Attributes: [{id=2346930276, provider=kakao, name=김준우, email=bababoll@naver.com}]
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        //attributes.toString() 예시 :
        // {id=2346930276, provider=kakao, name=김준우, email=bababoll@naver.com}
        Map<String, Object> attributes = oAuth2User.getAttributes();

        HttpSession session = request.getSession();
        session.setAttribute("loginName", attributes.get("name"));
        session.setAttribute("loginRole", MemberRole.valueOf("USER").getTitle());

        return "redirect:/";
        //return attributes.toString();
    }
}
