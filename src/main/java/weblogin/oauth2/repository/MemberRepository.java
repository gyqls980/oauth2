package weblogin.oauth2.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import weblogin.oauth2.domain.Member;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MemberRepository
{
    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public List<Member> findByEmail(String email) {
        return em.createQuery("select m from Member m where m.email = :email", Member.class)
                .setParameter("email", email)
                .getResultList();
    }

    public List<Member> findMember(String email, String password){
        return em.createQuery("select m from Member m where m.email = :email and m.password = :password", Member.class)
                            .setParameter("email", email)
                            .setParameter("password", password)
                            .getResultList();
    }

    public List<Member> findAll(){
        return em.createQuery("select m from Member m")
                .getResultList();
    }

}
