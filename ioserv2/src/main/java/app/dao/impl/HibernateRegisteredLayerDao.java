package app.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import app.dao.RegisteredLayerDao;
import app.model.db.RegisteredLayer;

@Transactional
@Repository
public class HibernateRegisteredLayerDao extends HibernateGenericDao<RegisteredLayer> implements RegisteredLayerDao {

}
