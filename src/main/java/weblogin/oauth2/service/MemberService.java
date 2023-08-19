package weblogin.oauth2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weblogin.oauth2.security.SecurityConfiguration;
import weblogin.oauth2.domain.Member;
import weblogin.oauth2.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final SecurityConfiguration pwEncoder;

    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);

        String rawPw = "";
        String encodePw = "";
        rawPw = member.getPassword();
        encodePw = pwEncoder.passwordEncoder().encode(rawPw);
        member.setPassword(encodePw);


        memberRepository.save(member);

        return member.getId();
    }

    @Transactional
    public Long oauthJoin(Member member) {

        memberRepository.save(member);

        return member.getId();
    }

    public void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByEmail(member.getEmail());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    public Optional<Member> isMember(Member member) {
        List<Member> findMembers = memberRepository.findByEmail(member.getEmail());
        if (findMembers.isEmpty()) {
            throw new IllegalStateException("존재하지 않는 회원입니다.");
        }

        Optional<Member> memberInfo = memberRepository.findByEmail(member.getEmail()).stream().findAny();

        return memberInfo;
    }

    public List<Member> findMembers(){
        List<Member> members = memberRepository.findAll();
        return members;

    }
}
