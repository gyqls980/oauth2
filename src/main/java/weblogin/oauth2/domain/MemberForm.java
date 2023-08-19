package weblogin.oauth2.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class MemberForm {

    @NotEmpty(message="이름은 필수입니다.")
    private String name;
    @NotEmpty(message="이메일은 필수입니다.")
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private MemberRole role;

}
