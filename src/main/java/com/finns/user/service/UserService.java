package com.finns.user.service;

import com.finns.Mbti;
import com.finns.amountByCategory.dto.AmountByCategory;
import com.finns.amountByCategory.mapper.AmountByCategoryMapper;
import com.finns.user.dto.SetMbtiDTO;
import com.finns.user.dto.User;
import com.finns.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@PropertySource({"classpath:/application.properties"})
@Transactional(readOnly = true)
public class UserService {

    private final UserMapper userMapper;
    private final AmountByCategoryMapper amountByCategoryMapper;

    public User getUser(Long userNo) {
        return Optional.ofNullable(userMapper.selectOne(userNo))
                .orElseThrow(NoSuchElementException::new);
    }

    @Transactional
    public void setMbtiByCategory(Long userNo) {
        List<AmountByCategory> amountByCategories = amountByCategoryMapper.selectAllByUser(userNo);

        String topCategory = null;
        double maxPoint = 0;
        for(AmountByCategory amountByCategory : amountByCategories) {
            double point = Mbti.calculatePoints(amountByCategory.getCategory(), amountByCategory.getAmount());

            if(maxPoint < point){
                maxPoint = point;
                topCategory = amountByCategory.getCategory();
            }
        }

        String mbtiName = Mbti.getMbtiNameByCategory(topCategory);
        SetMbtiDTO setMbtiDTO = new SetMbtiDTO(userNo, mbtiName);
        userMapper.updateMbti(setMbtiDTO);
    }

}
