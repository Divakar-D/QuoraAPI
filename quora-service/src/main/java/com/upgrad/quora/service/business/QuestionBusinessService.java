package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthenticationEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class QuestionBusinessService {

    @Autowired
    QuestionDao questionDao;

    @Autowired
    UserDao userDao;

  public QuestionEntity createQuestion(String accessToken,QuestionEntity questionEntity)
          throws AuthorizationFailedException {
      UserAuthenticationEntity userByToken = userDao.getUserByToken(accessToken);

      if (userByToken == null) {

          throw new AuthorizationFailedException("ATHR-001", "User has not signed in");

      } else if (userByToken.getLogoutAt() != null) {

          throw new AuthorizationFailedException(
                  "ATHR-002", "User is signed out.Sign in first to get user details");

      }
      questionEntity.setUserEntity(userByToken.getUserEntity());

      return questionDao.createQuestion(questionEntity);

  }


    public QuestionEntity CheckValidQuestion(String questionId) throws InvalidQuestionException {
        QuestionEntity questionEntity = questionDao.getQuestionByUuid(questionId);
        if(questionEntity==null) {
            throw new InvalidQuestionException("QUES-001","The question entered is invalid");
        }
        return questionEntity;
    }

  public List<QuestionEntity> getAllQuestion(){

        return questionDao.getAllQuestion();

  }


}
