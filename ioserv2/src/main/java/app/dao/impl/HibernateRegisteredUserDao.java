package app.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import app.dao.RegisteredUserDao;
import app.model.db.RegisteredUser;

@Transactional
@Repository
public class HibernateRegisteredUserDao extends HibernateGenericDao<RegisteredUser> implements RegisteredUserDao {
}
