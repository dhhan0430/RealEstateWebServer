package com.fastcampus.jpa.bookmanager2.domain.listener;

import com.fastcampus.jpa.bookmanager2.domain.User;
import com.fastcampus.jpa.bookmanager2.domain.UserHistory;
import com.fastcampus.jpa.bookmanager2.repository.UserHistoryRepository;
import com.fastcampus.jpa.bookmanager2.support.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

// @Component
public class UserEntityListener {

    /*
    @Autowired
    private UserHistoryRepository userHistoryRepository;
    */
    // prePersist로 하면 아직 db에 entity가 안 들어가서 id 값이 없다.
    // 그래서 userHistroy 저장할 때 null 값이 저장되게 된다.
    @PostPersist
    @PostUpdate
    public void preUpdateAndPreUpdate(Object obj) {
        UserHistoryRepository userHistoryRepository =
                BeanUtils.getBean(UserHistoryRepository.class);

        User user = (User) obj;

        UserHistory userHistory = new UserHistory();
        // userHistory.setUserId(user.getId());
        userHistory.setName(user.getName());
        userHistory.setEmail(user.getEmail());

        // UserHistory 에 @ManyToOne 추가로 인함.
        userHistory.setUser(user);
        userHistory.setHomeAddress(user.getHomeAddress());
        userHistory.setCompanyAddress(user.getCompanyAddress());

        userHistoryRepository.save(userHistory);

    }


}
