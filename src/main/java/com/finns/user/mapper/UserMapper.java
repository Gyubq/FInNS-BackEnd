package com.finns.user.mapper;

import com.finns.post.dto.ChangeRenewStatusDTO;
import com.finns.user.dto.SetMbtiDTO;
import com.finns.user.dto.User;

public interface UserMapper {
    User selectOne(Long userNo);
    void updateRenewTime(ChangeRenewStatusDTO changeRenewStatusDTO);
    void updateMbti(SetMbtiDTO setMbtiDTO);
}
