package com.finalproject.dontbeweak.service;

import com.finalproject.dontbeweak.dto.CatResponseDto;
import com.finalproject.dontbeweak.model.Cat;
import com.finalproject.dontbeweak.model.CatImage;
import com.finalproject.dontbeweak.model.User;
import com.finalproject.dontbeweak.repository.CatImageRepository;
import com.finalproject.dontbeweak.repository.CatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CatService {
    private final CatRepository catRepository;
    private final CatImageRepository catImageRepository;

    // 최소 레벨
    public static final Integer MIN_LEVEL = 1;


    // 새 고양이 생성
    @Transactional
    public void createNewCat(User user) {
        CatImage catImage = catImageRepository.findCatImageByChangeLevel(MIN_LEVEL);
        String firstCatImage = catImage.getCatImage();

        Cat cat = new Cat(user, firstCatImage);

        catRepository.save(cat);
    }


    // 내 고양이 조회
    @Transactional
    public CatResponseDto getMyCatStatus(UserDetails userDetails) {
        String username = userDetails.getUsername();
        Cat cat = catRepository.findByUser_Username(username)
                .orElseThrow(() -> new IllegalArgumentException("내 고양이가 존재하지 않습니다."));

        return new CatResponseDto(cat);
    }

    // 친구 고양이 조회
    @Transactional
    public CatResponseDto getFriendCatStatus(String username) {
        Cat friendCat = catRepository.findByUser_Username(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 고양이입니다."));

        return new CatResponseDto(friendCat);
    }

    // 고양이 경험치 상승
    public void addExp(Cat cat) {
        cat.addExpAndLevel();
        int resultLevel = cat.getLevel();
        if (resultLevel >= 10 && resultLevel % 10 == 0) {
            changeCatImage(cat, resultLevel);
        }
    }

    // 고양이 이미지 변경
    public void changeCatImage(Cat cat, int level) {
        CatImage catImage = catImageRepository.findCatImageByChangeLevel(level);
        int changelevel = catImage.getChangeLevel();
        String changeImage = catImage.getCatImage();

        if (level == changelevel) {
            cat.setCatImage(changeImage);
        }
    }
}
