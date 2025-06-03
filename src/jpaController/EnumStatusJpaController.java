/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jpaController;

import entity.EnumStatus;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Pedido;
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
public class EnumStatusJpaController implements Serializable {

    public EnumStatusJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EnumStatus enumStatus) {
        if (enumStatus.getPedidoCollection() == null) {
            enumStatus.setPedidoCollection(new ArrayList<Pedido>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Pedido> attachedPedidoCollection = new ArrayList<Pedido>();
            for (Pedido pedidoCollectionPedidoToAttach : enumStatus.getPedidoCollection()) {
                pedidoCollectionPedidoToAttach = em.getReference(pedidoCollectionPedidoToAttach.getClass(), pedidoCollectionPedidoToAttach.getIdPedido());
                attachedPedidoCollection.add(pedidoCollectionPedidoToAttach);
            }
            enumStatus.setPedidoCollection(attachedPedidoCollection);
            em.persist(enumStatus);
            for (Pedido pedidoCollectionPedido : enumStatus.getPedidoCollection()) {
                EnumStatus oldIdStatusOfPedidoCollectionPedido = pedidoCollectionPedido.getIdStatus();
                pedidoCollectionPedido.setIdStatus(enumStatus);
                pedidoCollectionPedido = em.merge(pedidoCollectionPedido);
                if (oldIdStatusOfPedidoCollectionPedido != null) {
                    oldIdStatusOfPedidoCollectionPedido.getPedidoCollection().remove(pedidoCollectionPedido);
                    oldIdStatusOfPedidoCollectionPedido = em.merge(oldIdStatusOfPedidoCollectionPedido);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EnumStatus enumStatus) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EnumStatus persistentEnumStatus = em.find(EnumStatus.class, enumStatus.getIdStatus());
            Collection<Pedido> pedidoCollectionOld = persistentEnumStatus.getPedidoCollection();
            Collection<Pedido> pedidoCollectionNew = enumStatus.getPedidoCollection();
            List<String> illegalOrphanMessages = null;
            for (Pedido pedidoCollectionOldPedido : pedidoCollectionOld) {
                if (!pedidoCollectionNew.contains(pedidoCollectionOldPedido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Pedido " + pedidoCollectionOldPedido + " since its idStatus field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Pedido> attachedPedidoCollectionNew = new ArrayList<Pedido>();
            for (Pedido pedidoCollectionNewPedidoToAttach : pedidoCollectionNew) {
                pedidoCollectionNewPedidoToAttach = em.getReference(pedidoCollectionNewPedidoToAttach.getClass(), pedidoCollectionNewPedidoToAttach.getIdPedido());
                attachedPedidoCollectionNew.add(pedidoCollectionNewPedidoToAttach);
            }
            pedidoCollectionNew = attachedPedidoCollectionNew;
            enumStatus.setPedidoCollection(pedidoCollectionNew);
            enumStatus = em.merge(enumStatus);
            for (Pedido pedidoCollectionNewPedido : pedidoCollectionNew) {
                if (!pedidoCollectionOld.contains(pedidoCollectionNewPedido)) {
                    EnumStatus oldIdStatusOfPedidoCollectionNewPedido = pedidoCollectionNewPedido.getIdStatus();
                    pedidoCollectionNewPedido.setIdStatus(enumStatus);
                    pedidoCollectionNewPedido = em.merge(pedidoCollectionNewPedido);
                    if (oldIdStatusOfPedidoCollectionNewPedido != null && !oldIdStatusOfPedidoCollectionNewPedido.equals(enumStatus)) {
                        oldIdStatusOfPedidoCollectionNewPedido.getPedidoCollection().remove(pedidoCollectionNewPedido);
                        oldIdStatusOfPedidoCollectionNewPedido = em.merge(oldIdStatusOfPedidoCollectionNewPedido);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = enumStatus.getIdStatus();
                if (findEnumStatus(id) == null) {
                    throw new NonexistentEntityException("The enumStatus with id " + id + " no longer exists.");
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
            EnumStatus enumStatus;
            try {
                enumStatus = em.getReference(EnumStatus.class, id);
                enumStatus.getIdStatus();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The enumStatus with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Pedido> pedidoCollectionOrphanCheck = enumStatus.getPedidoCollection();
            for (Pedido pedidoCollectionOrphanCheckPedido : pedidoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This EnumStatus (" + enumStatus + ") cannot be destroyed since the Pedido " + pedidoCollectionOrphanCheckPedido + " in its pedidoCollection field has a non-nullable idStatus field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(enumStatus);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EnumStatus> findEnumStatusEntities() {
        return findEnumStatusEntities(true, -1, -1);
    }

    public List<EnumStatus> findEnumStatusEntities(int maxResults, int firstResult) {
        return findEnumStatusEntities(false, maxResults, firstResult);
    }

    private List<EnumStatus> findEnumStatusEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EnumStatus.class));
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

    public EnumStatus findEnumStatus(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EnumStatus.class, id);
        } finally {
            em.close();
        }
    }

    public int getEnumStatusCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EnumStatus> rt = cq.from(EnumStatus.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
