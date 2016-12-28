package com.nekkitest.dao;

import com.nekkitest.entity.EntryXml;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by Alex on 27.12.2016.
 */
@Repository
@Transactional
public class EntryXmlDao {
    @PersistenceContext
    private EntityManager entityManager;

    public void create(EntryXml entryXml) {
        if (entryXml == null) return;
        entityManager.persist(entryXml);
    }

    public void delete(EntryXml entryXml) {
        if (entityManager.contains(entryXml))
            entityManager.remove(entryXml);
        else
            entityManager.remove(entityManager.merge(entryXml));
    }

    @SuppressWarnings("unchecked")
    public List<EntryXml> getAll() {
        return entityManager.createQuery("select ex from EntryXml ex").getResultList();
    }

    public EntryXml getById(long id) {
        return entityManager.find(EntryXml.class, id);
    }

    public void update(EntryXml entryXml) {
        entityManager.merge(entryXml);
    }
}
