package com.finns.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.finns.member.dto.MemberDTO;
import com.finns.member.dto.MemberJoinDTO;
import com.finns.member.dto.MemberUpdateDTO;
import com.finns.member.exception.PasswordMissmatchException;
import com.finns.member.mapper.MemberMapper;
import com.finns.security.account.domain.AuthVO;
import com.finns.security.account.domain.MemberVO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    final PasswordEncoder passwordEncoder;
    final MemberMapper mapper;

    @Override
    public boolean checkDuplicate(String username) {
        MemberVO member = mapper.checkUsername(username);
        return member != null;
    }

    @Override
    public MemberDTO get(String username) {
        MemberVO member = Optional.ofNullable(mapper.get(username))
                .orElseThrow(() -> new NoSuchElementException("회원 정보를 찾을 수 없습니다."));
        return MemberDTO.of(member);
    }

    private void saveAvatar(MultipartFile avatar, String username) {
        // 아바타 업로드
        if (avatar != null && !avatar.isEmpty()) {
            File dest = new File("c:/upload/avatar", username + ".png");
            try {
                avatar.transferTo(dest);
            } catch (IOException e) {
                log.error("아바타 파일 저장 중 오류 발생: {}", e.getMessage());
                throw new RuntimeException("아바타 파일 저장 중 오류가 발생했습니다.", e);
            }
        }
    }

    @Transactional
    @Override
    public MemberDTO join(MemberJoinDTO dto) {
        MemberVO member = dto.toVO();
        member.setPassword(passwordEncoder.encode(member.getPassword())); // 비밀번호 암호화
        mapper.insert(member);
        AuthVO authority = new AuthVO();
        authority.setUsername(member.getUsername());
        authority.setAuthority("ROLE_MEMBER");
        mapper.insertAuth(authority);
        saveAvatar(dto.getAvatar(), member.getUsername());
        return get(member.getUsername());
    }

    @Transactional
    @Override
    public MemberDTO update(MemberUpdateDTO member) {
        // 기존 회원 정보 가져오기
        MemberVO vo = Optional.ofNullable(mapper.get(member.getUsername()))
                .orElseThrow(() -> new NoSuchElementException("회원 정보를 찾을 수 없습니다."));

        // 현재 비밀번호 확인 (비밀번호가 변경되는 경우에만 확인)
        if (member.getOldPassword() != null && !member.getOldPassword().isEmpty() && !passwordEncoder.matches(member.getOldPassword(), vo.getPassword())) {
            throw new PasswordMissmatchException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 새로운 비밀번호가 존재하면 암호화된 비밀번호 사용, 그렇지 않으면 기존 비밀번호 사용
        String encodedPassword = (member.getNewPassword() != null && !member.getNewPassword().isEmpty())
                ? passwordEncoder.encode(member.getNewPassword())
                : vo.getPassword(); // 기존 비밀번호 유지

        // 업데이트할 `MemberVO` 객체 생성
        MemberVO updatedVO = member.toVO(encodedPassword);

        // 데이터베이스 업데이트
        mapper.update(updatedVO);

        // 아바타 업데이트
        saveAvatar(member.getAvatar(), member.getUsername());

        return get(member.getUsername());
    }
}