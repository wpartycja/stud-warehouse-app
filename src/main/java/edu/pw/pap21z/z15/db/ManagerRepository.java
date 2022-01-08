package edu.pw.pap21z.z15.db;

import edu.pw.pap21z.z15.db.model.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.stream.Collectors;

public class ManagerRepository {

    private final EntityManager session;

    public ManagerRepository(EntityManager session) {
        this.session = session;
    }

    private <T> List<T> getAll(Class<T> cls) {
        CriteriaQuery<T> criteria = session.getCriteriaBuilder().createQuery(cls);
        criteria.from(cls);
        return session.createQuery(criteria).getResultList();
    }


}
