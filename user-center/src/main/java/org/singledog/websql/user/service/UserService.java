package org.singledog.websql.user.service;

import com.alibaba.fastjson.JSON;
import ognl.Ognl;
import ognl.OgnlException;
import org.apache.ibatis.features.jpa.domain.Page;
import org.apache.ibatis.features.jpa.domain.PageRequest;
import org.apache.ibatis.features.jpa.domain.Sort;
import org.apache.ibatis.utils.CollectionUtils;
import org.omg.PortableInterceptor.USER_EXCEPTION;
import org.singledog.websql.user.auth.PasswordValidator;
import org.singledog.websql.user.entity.SoftDelete;
import org.singledog.websql.user.entity.UserEntity;
import org.singledog.websql.user.event.EventType;
import org.singledog.websql.user.event.LoggerEventBuilder;
import org.singledog.websql.user.exceptions.LoginException;
import org.singledog.websql.user.exceptions.ServiceException;
import org.singledog.websql.user.mapper.UserMapper;
import org.singledog.websql.user.param.HttpInfoHolder;
import org.singledog.websql.user.param.UserParam;
import org.singledog.websql.user.param.UserSearchParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Adam on 2017/9/8.
 */
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SaltGenerator saltGenerator;
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * create a user
     * @param userParam
     * @return
     */
    @Transactional
    public UserEntity addUser(UserParam userParam) {
        HttpInfoHolder.getAdmin();

        PasswordValidator.validate(userParam.getPassword());

        if (getUserByUserName(userParam.getUserName()) != null)
            throw new ServiceException("User name already exists !");

        if (getUserByEmail(userParam.getEmail()) != null)
            throw new ServiceException("Email already exists !");

        UserEntity userEntity = new UserEntity();
        userEntity.defaultProperty();
        userEntity.setSalt(saltGenerator.randomSalt());
        userEntity.setUserName(userParam.getUserName());
        userEntity.setPassword(passwordEncoder.encode(
                saltGenerator.wrapSalt(userParam.getPassword(), userEntity.getSalt())));
        userEntity.setEmail(userParam.getEmail());
        userEntity.setAdmin(userParam.getAdmin());
        userEntity.setDepartId(userParam.getDepartId());
        try {
            this.userMapper.saveAutoIncrementKey(userEntity);
        } catch (DuplicateKeyException e) {
            logger.error(e.getMessage(), e);
            throw  new ServiceException("数据重复!");
        }

        userParam.setPassword("");
        applicationContext.publishEvent(LoggerEventBuilder
                .getInstance(DatabaseService.class)
                .eventType(EventType.CREATE_USER)
                .userEntity(HttpInfoHolder.getUserEntity())
                .params(JSON.toJSONString(userParam))
                .remark("user id: ".concat(String.valueOf(userEntity.getId())))
                .build());

        return userEntity;
    }

    public UserEntity getUserByUserName(String userName) {
        if (StringUtils.isEmpty(userName))
            return null;
        return userMapper.findByUserName(userName);
    }

    public UserEntity getByUserId(Long id) {
        return userMapper.findById(id);
    }

    public UserEntity getUserByEmail(String email) {
        if (StringUtils.isEmpty(email))
            return null;

        return userMapper.findByEmail(email);
    }

    public boolean checkStatus(UserEntity entity) {
        if (entity.getStatus() != null && entity.getStatus() == UserEntity.STATUS_LOCKED) {
            throw new ServiceException("用户已被锁定!");
        }

        if (entity.getDelete() != null && entity.getDelete() == SoftDelete.deleted) {
            throw new ServiceException("用户已被删除!");
        }

        return true;
    }

    /**
     * update user info
     * @param userParam
     * @return
     */
    @Transactional
    public UserEntity updateUser(UserParam userParam) {
        HttpInfoHolder.getAdmin();
        UserEntity entity = userMapper.findById(userParam.getId());
        if (entity != null) {
            if (!StringUtils.isEmpty(userParam.getUserName()))
                entity.setUserName(userParam.getUserName());
            if (userParam.getAdmin() != null)
                entity.setAdmin(userParam.getAdmin());
            if (userParam.getDepartId() != null)
                entity.setDepartId(userParam.getDepartId());
            if (!StringUtils.isEmpty(userParam.getEmail()))
                entity.setEmail(userParam.getEmail());

            if (!StringUtils.isEmpty(userParam.getPassword())) {
                PasswordValidator.validate(userParam.getPassword());
                entity.setSalt(saltGenerator.randomSalt());
                entity.setPassword(passwordEncoder.encode(
                        saltGenerator.wrapSalt(userParam.getPassword(), entity.getSalt())));
            }

            entity.setUpdateTime(new Date());
            entity.setUpdateUserId(HttpInfoHolder.getAdmin().getId());
            try {
                this.userMapper.updateByPrimaryKeySelective(entity);
            } catch (DuplicateKeyException e) {
                logger.error(e.getMessage(), e);
                throw  new ServiceException("数据重复!");
            }

            userParam.setPassword("");
            applicationContext.publishEvent(LoggerEventBuilder
                    .getInstance(DatabaseService.class)
                    .eventType(EventType.UPDATE_USER)
                    .userEntity(HttpInfoHolder.getUserEntity())
                    .params(JSON.toJSONString(userParam))
                    .remark("user id: ".concat(String.valueOf(entity.getId())))
                    .build());
            return entity;
        }

        throw new RuntimeException("用户不存在!");
    }

    /**
     * query user by page
     * @param userSearchParam
     * @return
     */
    public Page<UserEntity> listUsers(UserSearchParam userSearchParam) {
        return userMapper.searchUsers(userSearchParam,
                PageRequest.of(userSearchParam.getPage(), userSearchParam.getRows(),
                        new Sort(Sort.Direction.DESC, "createTime")));
    }

    /**
     * lock user
     * @param userId
     * @return
     */
    @Transactional
    public int lockUser(Long userId, byte state) {
        UserEntity entity = HttpInfoHolder.getAdmin();

        int i = this.userMapper.updateUserLock(userId, entity.getId(), state);
        applicationContext.publishEvent(LoggerEventBuilder
                .getInstance(DatabaseService.class)
                .eventType(EventType.BLOCK_UNBLOCK_USER)
                .userEntity(HttpInfoHolder.getUserEntity())
                .params(JSON.toJSONString(state))
                .remark("user id: ".concat(String.valueOf(userId)))
                .build());
        return i;
    }

    /**
     * delete user
     * @param userId
     * @return
     */
    @Transactional
    public int deleteUser(Long userId) {
        return this.userMapper.updateUserDelete(userId, SoftDelete.normal, HttpInfoHolder.getAdmin().getId());
    }

    @Transactional
    public void logout() {
        UserEntity entity = HttpInfoHolder.getUserEntityNotNull();
        applicationContext.publishEvent(LoggerEventBuilder
                .getInstance(DatabaseService.class)
                .eventType(EventType.LOGOUT)
                .userEntity(HttpInfoHolder.getUserEntity())
                .remark("user id: ".concat(String.valueOf(entity.getId())))
                .build());
    }

    @Transactional
    public void updatePassword(String oldPassword, String newPassword) {
        UserEntity entity = HttpInfoHolder.getUserEntityNotNull();
        PasswordValidator.validate(newPassword);
        entity = userMapper.findById(entity.getId());
        if (!passwordEncoder.matches(saltGenerator.wrapSalt(oldPassword, entity.getSalt()), entity.getPassword())) {
            logger.error("update failed ! wrong password !");
            throw new LoginException("当前密码错误!");
        }

        entity.setSalt(saltGenerator.randomSalt());
        entity.setPassword(passwordEncoder.encode(
                saltGenerator.wrapSalt(newPassword, entity.getSalt())));
        this.userMapper.updateByPrimaryKey(entity);
        applicationContext.publishEvent(LoggerEventBuilder
                .getInstance(DatabaseService.class)
                .eventType(EventType.UPDATE_PASSWORD)
                .userEntity(HttpInfoHolder.getUserEntity())
                .remark("user id: ".concat(String.valueOf(entity.getId())))
                .build());
    }

    public void loadUserName(List<? extends UserNameRequired> userNameRequireds) {
        Set<Long> idSet = new HashSet<>();
        userNameRequireds.forEach(u -> {
            String[][] mapping = u.idNameFieldMapping();
            if (mapping != null && mapping.length > 0) {
              for (String[] m : mapping) {
                  try {
                      idSet.add((Long) Ognl.getValue(m[0], u));
                  } catch (Exception e) {
                      logger.error(e.getMessage(), e);
                  }
              }
            }
        });

        if (CollectionUtils.isEmpty(idSet))
            return;

        List<UserEntity> userEntities = userMapper.findAllById(new ArrayList<>(idSet));
        Map<Long, String> userNameMap = userEntities
                .stream()
                .filter(u -> u != null)
                .collect(Collectors.toMap(UserEntity::getId, UserEntity::getUserName));

        for (UserNameRequired r : userNameRequireds) {
            String[][] mapping = r.idNameFieldMapping();
            if (mapping != null && mapping.length > 0) {
                for (String[] m : mapping) {
                    try {
                        Long id = (Long) Ognl.getValue(m[0], r);
                        String value = userNameMap.get(id);
                        if (value != null) {
                            Ognl.setValue(m[1], r, value);
                        }
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
        }
    }

}
