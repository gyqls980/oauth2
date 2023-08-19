package weblogin.oauth2.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String name;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private MemberRole role; //ADMIN_ROLE, USER_ROLE
    private String provider;

    public Member(){
    }
    @Builder
    public Member(Long id, String name, String email, String provider){
        this.id = id;
        this.name = name;
        this.email = email;
        this.provider = provider;
    }

    public Member update(String name, String email){
        this.name = name;
        this.email = email;
        return this;
    }


}
