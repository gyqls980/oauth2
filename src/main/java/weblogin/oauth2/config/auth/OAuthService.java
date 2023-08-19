package weblogin.oauth2.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import weblogin.oauth2.domain.Member;
import weblogin.oauth2.repository.MemberRepository;
import weblogin.oauth2.service.MemberService;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    /**
     * Spring Security가 access token 이용해서
     * OAuth2 Server 유저 정보를 가져온 후 loadUser()로 유저 정보 가져온다.
     * @param userRequest the user request
     * @return
     * @throws OAuth2AuthenticationException
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest); //oauth 서비스(Google..)에서 가져온 유저정보
        String registrationId = userRequest.getClientRegistration()
                                            .getRegistrationId();// oauth 서비스 이름 (ex. google)
        String userNameAttributeName = userRequest.getClientRegistration()
                                                    .getProviderDetails()
                                                    .getUserInfoEndpoint()
                                                    .getUserNameAttributeName(); //oauth 로그인 시 pk 되는 값
        Map<String, Object> attributes = oAuth2User.getAttributes(); //oauth 서비스의 유저 정보들

        Member member = new Member();
        member.setName((String)attributes.get("name"));
        member.setEmail((String)attributes.get("email"));
        member.setProvider(registrationId);
        Long memberId = memberService.join(member);

        Map<String, Object> customAttribute = customAttribute(attributes, userNameAttributeName, member, registrationId);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("USER")),
                customAttribute,
                userNameAttributeName
        );
    }

    private Map customAttribute(Map attributes, String userNameAttributeName, Member member, String registrationId){
        Map<String, Object> customAttribute = new LinkedHashMap<>();
        customAttribute.put(userNameAttributeName, attributes.get(userNameAttributeName));
        customAttribute.put("provider", registrationId);
        customAttribute.put("name", member.getName());
        customAttribute.put("email", member.getEmail());
        return customAttribute;
    }

}
