package ru.netcracker.chinagram.services.impl;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.netcracker.chinagram.model.AbstractEntity;
import ru.netcracker.chinagram.services.interfaces.ChinaDAO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.UUID;

@Repository
public class ChinaDAOImpl implements ChinaDAO {

    @PersistenceContext
    private EntityManager entityManager;


    @Transactional
    public <T extends AbstractEntity> List<T> findAll(Class<T> clazz) {
        return findAll(clazz, null, null);
    }

    @Transactional
    public <T extends AbstractEntity> List<T> findAll(Class<T> clazz, Integer from, Integer max) {
        Query query = entityManager.createQuery("from " + clazz.getSimpleName() + " e");
        if (from != null) {
            query.setFirstResult(from);
        }
        if (max != null) {
            query.setMaxResults(max);
        }
        return query.getResultList();
    }

    @Transactional
    public <T extends AbstractEntity> List<T> findAll(Class<T> clazz, Pageable page) {
       return  findAll(clazz, toFirstResult(page),
               page.getPageSize());
    }

    @Transactional
    public <T extends AbstractEntity> List<T> findAllByFieldLike(Class<T> clazz, String field, Object value) {
        Query query = entityManager.createQuery(String.format("from %s where %s like :%s", clazz.getSimpleName(), field, field));
        query.setParameter(field, "%" + value + "%");
        return query.getResultList();
    }

    @Transactional
    public <T extends AbstractEntity> List<T> findAllByField(Class<T> clazz, String field, Object value) {
        Query query = entityManager.createQuery(String.format("from %s where %s = :%s", clazz.getSimpleName(), field, field));
        query.setParameter(field, value);
        return query.getResultList();
    }

    @Transactional
    public <T extends AbstractEntity> T get(Class<T> clazz, String field, Object value) {
        List<T> result = findAllByField(clazz, field, value);
        if (!result.isEmpty()) {
            return result.get(0);
        } else {
            return null;
        }
    }

    @Transactional
    public <T extends AbstractEntity> T get(Class<T> clazz, UUID id) {
        return get(clazz, "id", id);
    }

    @Transactional
    public <T extends AbstractEntity> void remove(T object) {
        entityManager.remove(object);
    }

    @Transactional
    public <T extends AbstractEntity> void merge(T object) {
        entityManager.merge(object);
    }

    @Transactional
    public <T extends AbstractEntity> void persist(T object) {
        entityManager.persist(object);
    }

    public <T extends AbstractEntity> List executeSqlQuery(String sqlQuery, String param, Class<T> clazz, Integer maxResults, Integer firstResult) {
        return entityManager.createNativeQuery(String.format(sqlQuery, param), clazz).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    public <T extends AbstractEntity> List executeSqlQuery(String sqlQuery, String param, Class<T> clazz, Pageable page) {
        return entityManager.createNativeQuery(String.format(sqlQuery, param), clazz).setFirstResult(toFirstResult(page)).setMaxResults(page.getPageSize()).getResultList();
    }

    private Integer toFirstResult(Pageable page){
       return  (page.getPageNumber()-1)*page.getPageSize();
    }

}
