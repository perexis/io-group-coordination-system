package app.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import app.dao.RegisteredUserItemDao;
import app.model.db.RegisteredUserItem;

@Transactional
@Repository
public class HibernateRegisteredUserItemDao extends HibernateGenericDao<RegisteredUserItem> implements RegisteredUserItemDao {

}
