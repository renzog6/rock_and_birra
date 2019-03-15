package ar.nex.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Renzo
 */
public class GetPK {

    private final EntityManagerFactory presitence;

    public GetPK() {
        presitence = Persistence.createEntityManagerFactory("SysControl-PU");

    }

    public Integer Ultimo(Class clazz) {
        System.out.println("ar.nex.util.GetPK.Ultimo() : " + clazz.toString());

        EntityManager em = this.presitence.createEntityManager();

        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(clazz));

            Root<?> c = cq.from(clazz);
            cq.select(c.get("id")).distinct(true);

            Query q = em.createQuery(cq);

            return (Integer) q.getResultList().get(q.getResultList().size() - 1);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return -1;
    }

    public Integer Nuevo(Class clazz) {
        System.out.println("ar.nex.util.GetPK.Nuevo() : " + clazz.toString());

        EntityManager em = this.presitence.createEntityManager();

        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(clazz));

            Root<?> c = cq.from(clazz);
            cq.select(c.get("id")).distinct(true);

            Query q = em.createQuery(cq);

            if (!q.getResultList().isEmpty()) {
                return (Integer) q.getResultList().get(q.getResultList().size() - 1) + 1;
            }
//            return (Integer) q.getResultList().get(q.getResultList().size() - 1) + 1;
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return -1;
    }

}
