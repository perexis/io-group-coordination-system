package app.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import app.dao.GenericDao;
import app.model.db.Model;
import app.util.CustomHibernateDaoSupport;

public abstract class HibernateGenericDao<T extends Model> 
		extends CustomHibernateDaoSupport implements GenericDao<T> {

	private Class<T> type;

	@SuppressWarnings("unchecked")
	public HibernateGenericDao() {
		Type t = getClass().getGenericSuperclass();
		ParameterizedType pt = (ParameterizedType) t;
		type = (Class<T>) pt.getActualTypeArguments()[0];
	}

	@Override
	public T save(T t) {
		if (t == null) {
			return null;
		}
		if (this.find(t.getId()) != null) {
			return null;
		}
		else {
			getHibernateTemplate().save(t);
			return t;
		}
	}
	
	@Override
	public T find(Serializable id) {
		if (id == null) {
			return null;
		}
		return (T) getHibernateTemplate().get(type, id);
	}

	@Override
	public T update(T t) {
		if (t == null) {
			return null;
		}
		if (this.find(t.getId()) == null) {
			return null;
		}
		return getHibernateTemplate().merge(t);
	}

	@Override
	public void delete(Serializable id) {
		if (id == null) {
			return;
		}
		T tmp = this.find(id);
		getHibernateTemplate().delete(tmp);
	}

	@Override
	public List<T> getAll() {
		return getHibernateTemplate().loadAll(type);
	}
	
	@Override
	public void deleteAll() {
		getHibernateTemplate().deleteAll(getHibernateTemplate().loadAll(type));
	}
}
