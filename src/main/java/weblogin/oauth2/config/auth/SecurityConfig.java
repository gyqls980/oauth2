package weblogin.oauth2.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity //spring security 설정 활성화
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final OAuthService oAuthService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .logout().logoutSuccessUrl("/") //logout요청 시 홈으로 이동 , 디폴트=/logout
                .and()
                .oauth2Login() //oauth2 로그인 설정 시작
                .defaultSuccessUrl("/oauth/loginInfo", true)
                .userInfoEndpoint() //oauth2 로그인 성공 이후 사용자 정보 가져올 때 설정 담당
                .userService(oAuthService); //oauth2 로그인 성공 시 작업 진행할 MemberService

    }
}
