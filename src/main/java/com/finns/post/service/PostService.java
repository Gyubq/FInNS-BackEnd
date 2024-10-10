package com.finns.post.service;

import com.finns.amountByCategory.mapper.AmountByCategoryMapper;
import com.finns.post.dto.*;
import com.finns.amountByDate.mapper.AmountByDateMapper;
import com.finns.post.mapper.PostMapper;
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
public class PostService {

    private final PostMapper postMapper;
    private final AmountByDateMapper amountByDateMapper;
    private final AmountByCategoryMapper amountByCategoryMapper;
    private final UserMapper userMapper;

    public PostResponseDTO getPost(Long no) {
        // 게시글 정보 가져오기
        PostResponseDTO postResponse = Optional.ofNullable(postMapper.selectOne(no))
                .orElseThrow(NoSuchElementException::new);

        // 이미지 URL 리스트 가져오기
        List<String> imgUrls = postMapper.selectImagesByPostNo(no);
        postResponse.setImgUrls(imgUrls); // 이미지 URL 리스트 설정

        return postResponse;
    }

    public List<PostResponseDTO> getPostsByUserAndDateAndIsPublic(PostRequestByDateDTO postRequestByDateDTO) {
        return Optional.ofNullable(postMapper.selectAllByUserAndDateAndIsPublic(postRequestByDateDTO))
                .orElseThrow(NoSuchElementException::new);
    }

    public List<PostResponseDTO> getPostsByUserAndCategoryAndIsPublic(PostRequestByCategoryDTO postRequestByCategoryDTO) {
        return Optional.ofNullable(postMapper.selectAllByUserAndCategoryAndIsPublic(postRequestByCategoryDTO))
                .orElseThrow(NoSuchElementException::new);
    }

    @Transactional
    public void updateRenewStatusAndAmount(ChangeRenewStatusDTO changeRenewStatusDTO) {
        // 1. 갱신된 post의 date, category, userNo, amount 가져오기
        List<UpdateAmountDTO> updatedPosts = postMapper.selectUpdatedRenewPost(changeRenewStatusDTO);

        // 2. 갱신된 각 데이터에 대해 날짜별, 카테고리별 amount 업데이트
        for (UpdateAmountDTO post : updatedPosts) {
            amountByDateMapper.upsertByDateAndUser(post);
            amountByCategoryMapper.upsertByCategoryAndUser(post);
        }

        // 3. renew_status 업데이트
        postMapper.updateRenewStatusByUser(changeRenewStatusDTO);

        // 4. user의 renew_time 업데이트
        userMapper.updateRenewTime(changeRenewStatusDTO);
    }

    public Long getCountByUser(Long userNo) {
        return Optional.of(postMapper.selectCountByUser(userNo))
                .orElseThrow(NoSuchElementException::new);
    }

    @Transactional
    public void reversePublicStatus(Long no) {
        postMapper.updatePublicStatus(no);
    }
    // 전체 중복 제거된 post_no 가져오기
    public List<Long> getDistinctPostNos() {
        return postMapper.selectDistinctPostNos();
    }
}
