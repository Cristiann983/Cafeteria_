/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jpaController;

import entity.Insumo;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Tipocantidad;
import entity.Recetainsumo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpaController.exceptions.IllegalOrphanException;
import jpaController.exceptions.NonexistentEntityException;

/**
 *
 * @author crist
 */
public class InsumoJpaController implements Serializable {

    public InsumoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Insumo insumo) {
        if (insumo.getRecetainsumoCollection() == null) {
            insumo.setRecetainsumoCollection(new ArrayList<Recetainsumo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tipocantidad idTipocantidad = insumo.getIdTipocantidad();
            if (idTipocantidad != null) {
                idTipocantidad = em.getReference(idTipocantidad.getClass(), idTipocantidad.getIdTipo());
                insumo.setIdTipocantidad(idTipocantidad);
            }
            Collection<Recetainsumo> attachedRecetainsumoCollection = new ArrayList<Recetainsumo>();
            for (Recetainsumo recetainsumoCollectionRecetainsumoToAttach : insumo.getRecetainsumoCollection()) {
                recetainsumoCollectionRecetainsumoToAttach = em.getReference(recetainsumoCollectionRecetainsumoToAttach.getClass(), recetainsumoCollectionRecetainsumoToAttach.getRecetainsumoPK());
                attachedRecetainsumoCollection.add(recetainsumoCollectionRecetainsumoToAttach);
            }
            insumo.setRecetainsumoCollection(attachedRecetainsumoCollection);
            em.persist(insumo);
            if (idTipocantidad != null) {
                idTipocantidad.getInsumoCollection().add(insumo);
                idTipocantidad = em.merge(idTipocantidad);
            }
            for (Recetainsumo recetainsumoCollectionRecetainsumo : insumo.getRecetainsumoCollection()) {
                Insumo oldInsumoOfRecetainsumoCollectionRecetainsumo = recetainsumoCollectionRecetainsumo.getInsumo();
                recetainsumoCollectionRecetainsumo.setInsumo(insumo);
                recetainsumoCollectionRecetainsumo = em.merge(recetainsumoCollectionRecetainsumo);
                if (oldInsumoOfRecetainsumoCollectionRecetainsumo != null) {
                    oldInsumoOfRecetainsumoCollectionRecetainsumo.getRecetainsumoCollection().remove(recetainsumoCollectionRecetainsumo);
                    oldInsumoOfRecetainsumoCollectionRecetainsumo = em.merge(oldInsumoOfRecetainsumoCollectionRecetainsumo);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Insumo insumo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Insumo persistentInsumo = em.find(Insumo.class, insumo.getIdInsumo());
            Tipocantidad idTipocantidadOld = persistentInsumo.getIdTipocantidad();
            Tipocantidad idTipocantidadNew = insumo.getIdTipocantidad();
            Collection<Recetainsumo> recetainsumoCollectionOld = persistentInsumo.getRecetainsumoCollection();
            Collection<Recetainsumo> recetainsumoCollectionNew = insumo.getRecetainsumoCollection();
            List<String> illegalOrphanMessages = null;
            for (Recetainsumo recetainsumoCollectionOldRecetainsumo : recetainsumoCollectionOld) {
                if (!recetainsumoCollectionNew.contains(recetainsumoCollectionOldRecetainsumo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Recetainsumo " + recetainsumoCollectionOldRecetainsumo + " since its insumo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idTipocantidadNew != null) {
                idTipocantidadNew = em.getReference(idTipocantidadNew.getClass(), idTipocantidadNew.getIdTipo());
                insumo.setIdTipocantidad(idTipocantidadNew);
            }
            Collection<Recetainsumo> attachedRecetainsumoCollectionNew = new ArrayList<Recetainsumo>();
            for (Recetainsumo recetainsumoCollectionNewRecetainsumoToAttach : recetainsumoCollectionNew) {
                recetainsumoCollectionNewRecetainsumoToAttach = em.getReference(recetainsumoCollectionNewRecetainsumoToAttach.getClass(), recetainsumoCollectionNewRecetainsumoToAttach.getRecetainsumoPK());
                attachedRecetainsumoCollectionNew.add(recetainsumoCollectionNewRecetainsumoToAttach);
            }
            recetainsumoCollectionNew = attachedRecetainsumoCollectionNew;
            insumo.setRecetainsumoCollection(recetainsumoCollectionNew);
            insumo = em.merge(insumo);
            if (idTipocantidadOld != null && !idTipocantidadOld.equals(idTipocantidadNew)) {
                idTipocantidadOld.getInsumoCollection().remove(insumo);
                idTipocantidadOld = em.merge(idTipocantidadOld);
            }
            if (idTipocantidadNew != null && !idTipocantidadNew.equals(idTipocantidadOld)) {
                idTipocantidadNew.getInsumoCollection().add(insumo);
                idTipocantidadNew = em.merge(idTipocantidadNew);
            }
            for (Recetainsumo recetainsumoCollectionNewRecetainsumo : recetainsumoCollectionNew) {
                if (!recetainsumoCollectionOld.contains(recetainsumoCollectionNewRecetainsumo)) {
                    Insumo oldInsumoOfRecetainsumoCollectionNewRecetainsumo = recetainsumoCollectionNewRecetainsumo.getInsumo();
                    recetainsumoCollectionNewRecetainsumo.setInsumo(insumo);
                    recetainsumoCollectionNewRecetainsumo = em.merge(recetainsumoCollectionNewRecetainsumo);
                    if (oldInsumoOfRecetainsumoCollectionNewRecetainsumo != null && !oldInsumoOfRecetainsumoCollectionNewRecetainsumo.equals(insumo)) {
                        oldInsumoOfRecetainsumoCollectionNewRecetainsumo.getRecetainsumoCollection().remove(recetainsumoCollectionNewRecetainsumo);
                        oldInsumoOfRecetainsumoCollectionNewRecetainsumo = em.merge(oldInsumoOfRecetainsumoCollectionNewRecetainsumo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = insumo.getIdInsumo();
                if (findInsumo(id) == null) {
                    throw new NonexistentEntityException("The insumo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Insumo insumo;
            try {
                insumo = em.getReference(Insumo.class, id);
                insumo.getIdInsumo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The insumo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Recetainsumo> recetainsumoCollectionOrphanCheck = insumo.getRecetainsumoCollection();
            for (Recetainsumo recetainsumoCollectionOrphanCheckRecetainsumo : recetainsumoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Insumo (" + insumo + ") cannot be destroyed since the Recetainsumo " + recetainsumoCollectionOrphanCheckRecetainsumo + " in its recetainsumoCollection field has a non-nullable insumo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Tipocantidad idTipocantidad = insumo.getIdTipocantidad();
            if (idTipocantidad != null) {
                idTipocantidad.getInsumoCollection().remove(insumo);
                idTipocantidad = em.merge(idTipocantidad);
            }
            em.remove(insumo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Insumo> findInsumoEntities() {
        return findInsumoEntities(true, -1, -1);
    }

    public List<Insumo> findInsumoEntities(int maxResults, int firstResult) {
        return findInsumoEntities(false, maxResults, firstResult);
    }

    private List<Insumo> findInsumoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Insumo.class));
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

    public Insumo findInsumo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Insumo.class, id);
        } finally {
            em.close();
        }
    }

    public int getInsumoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Insumo> rt = cq.from(Insumo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
