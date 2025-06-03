/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jpaController;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Insumo;
import entity.Tipocantidad;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpaController.exceptions.NonexistentEntityException;

/**
 *
 * @author crist
 */
public class TipocantidadJpaController implements Serializable {

    public TipocantidadJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tipocantidad tipocantidad) {
        if (tipocantidad.getInsumoCollection() == null) {
            tipocantidad.setInsumoCollection(new ArrayList<Insumo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Insumo> attachedInsumoCollection = new ArrayList<Insumo>();
            for (Insumo insumoCollectionInsumoToAttach : tipocantidad.getInsumoCollection()) {
                insumoCollectionInsumoToAttach = em.getReference(insumoCollectionInsumoToAttach.getClass(), insumoCollectionInsumoToAttach.getIdInsumo());
                attachedInsumoCollection.add(insumoCollectionInsumoToAttach);
            }
            tipocantidad.setInsumoCollection(attachedInsumoCollection);
            em.persist(tipocantidad);
            for (Insumo insumoCollectionInsumo : tipocantidad.getInsumoCollection()) {
                Tipocantidad oldIdTipocantidadOfInsumoCollectionInsumo = insumoCollectionInsumo.getIdTipocantidad();
                insumoCollectionInsumo.setIdTipocantidad(tipocantidad);
                insumoCollectionInsumo = em.merge(insumoCollectionInsumo);
                if (oldIdTipocantidadOfInsumoCollectionInsumo != null) {
                    oldIdTipocantidadOfInsumoCollectionInsumo.getInsumoCollection().remove(insumoCollectionInsumo);
                    oldIdTipocantidadOfInsumoCollectionInsumo = em.merge(oldIdTipocantidadOfInsumoCollectionInsumo);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tipocantidad tipocantidad) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tipocantidad persistentTipocantidad = em.find(Tipocantidad.class, tipocantidad.getIdTipo());
            Collection<Insumo> insumoCollectionOld = persistentTipocantidad.getInsumoCollection();
            Collection<Insumo> insumoCollectionNew = tipocantidad.getInsumoCollection();
            Collection<Insumo> attachedInsumoCollectionNew = new ArrayList<Insumo>();
            for (Insumo insumoCollectionNewInsumoToAttach : insumoCollectionNew) {
                insumoCollectionNewInsumoToAttach = em.getReference(insumoCollectionNewInsumoToAttach.getClass(), insumoCollectionNewInsumoToAttach.getIdInsumo());
                attachedInsumoCollectionNew.add(insumoCollectionNewInsumoToAttach);
            }
            insumoCollectionNew = attachedInsumoCollectionNew;
            tipocantidad.setInsumoCollection(insumoCollectionNew);
            tipocantidad = em.merge(tipocantidad);
            for (Insumo insumoCollectionOldInsumo : insumoCollectionOld) {
                if (!insumoCollectionNew.contains(insumoCollectionOldInsumo)) {
                    insumoCollectionOldInsumo.setIdTipocantidad(null);
                    insumoCollectionOldInsumo = em.merge(insumoCollectionOldInsumo);
                }
            }
            for (Insumo insumoCollectionNewInsumo : insumoCollectionNew) {
                if (!insumoCollectionOld.contains(insumoCollectionNewInsumo)) {
                    Tipocantidad oldIdTipocantidadOfInsumoCollectionNewInsumo = insumoCollectionNewInsumo.getIdTipocantidad();
                    insumoCollectionNewInsumo.setIdTipocantidad(tipocantidad);
                    insumoCollectionNewInsumo = em.merge(insumoCollectionNewInsumo);
                    if (oldIdTipocantidadOfInsumoCollectionNewInsumo != null && !oldIdTipocantidadOfInsumoCollectionNewInsumo.equals(tipocantidad)) {
                        oldIdTipocantidadOfInsumoCollectionNewInsumo.getInsumoCollection().remove(insumoCollectionNewInsumo);
                        oldIdTipocantidadOfInsumoCollectionNewInsumo = em.merge(oldIdTipocantidadOfInsumoCollectionNewInsumo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipocantidad.getIdTipo();
                if (findTipocantidad(id) == null) {
                    throw new NonexistentEntityException("The tipocantidad with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tipocantidad tipocantidad;
            try {
                tipocantidad = em.getReference(Tipocantidad.class, id);
                tipocantidad.getIdTipo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipocantidad with id " + id + " no longer exists.", enfe);
            }
            Collection<Insumo> insumoCollection = tipocantidad.getInsumoCollection();
            for (Insumo insumoCollectionInsumo : insumoCollection) {
                insumoCollectionInsumo.setIdTipocantidad(null);
                insumoCollectionInsumo = em.merge(insumoCollectionInsumo);
            }
            em.remove(tipocantidad);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tipocantidad> findTipocantidadEntities() {
        return findTipocantidadEntities(true, -1, -1);
    }

    public List<Tipocantidad> findTipocantidadEntities(int maxResults, int firstResult) {
        return findTipocantidadEntities(false, maxResults, firstResult);
    }

    private List<Tipocantidad> findTipocantidadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tipocantidad.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Tipocantidad findTipocantidad(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tipocantidad.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipocantidadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tipocantidad> rt = cq.from(Tipocantidad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
