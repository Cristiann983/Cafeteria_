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
import entity.Receta;
import entity.Pedidoproducto;
import entity.Producto;
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
public class ProductoJpaController implements Serializable {

    public ProductoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Producto producto) {
        if (producto.getPedidoproductoCollection() == null) {
            producto.setPedidoproductoCollection(new ArrayList<Pedidoproducto>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Receta idReceta = producto.getIdReceta();
            if (idReceta != null) {
                idReceta = em.getReference(idReceta.getClass(), idReceta.getIdReceta());
                producto.setIdReceta(idReceta);
            }
            Collection<Pedidoproducto> attachedPedidoproductoCollection = new ArrayList<Pedidoproducto>();
            for (Pedidoproducto pedidoproductoCollectionPedidoproductoToAttach : producto.getPedidoproductoCollection()) {
                pedidoproductoCollectionPedidoproductoToAttach = em.getReference(pedidoproductoCollectionPedidoproductoToAttach.getClass(), pedidoproductoCollectionPedidoproductoToAttach.getIdDetalle());
                attachedPedidoproductoCollection.add(pedidoproductoCollectionPedidoproductoToAttach);
            }
            producto.setPedidoproductoCollection(attachedPedidoproductoCollection);
            em.persist(producto);
            if (idReceta != null) {
                idReceta.getProductoCollection().add(producto);
                idReceta = em.merge(idReceta);
            }
            for (Pedidoproducto pedidoproductoCollectionPedidoproducto : producto.getPedidoproductoCollection()) {
                Producto oldIdProductoOfPedidoproductoCollectionPedidoproducto = pedidoproductoCollectionPedidoproducto.getIdProducto();
                pedidoproductoCollectionPedidoproducto.setIdProducto(producto);
                pedidoproductoCollectionPedidoproducto = em.merge(pedidoproductoCollectionPedidoproducto);
                if (oldIdProductoOfPedidoproductoCollectionPedidoproducto != null) {
                    oldIdProductoOfPedidoproductoCollectionPedidoproducto.getPedidoproductoCollection().remove(pedidoproductoCollectionPedidoproducto);
                    oldIdProductoOfPedidoproductoCollectionPedidoproducto = em.merge(oldIdProductoOfPedidoproductoCollectionPedidoproducto);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Producto producto) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto persistentProducto = em.find(Producto.class, producto.getIdProducto());
            Receta idRecetaOld = persistentProducto.getIdReceta();
            Receta idRecetaNew = producto.getIdReceta();
            Collection<Pedidoproducto> pedidoproductoCollectionOld = persistentProducto.getPedidoproductoCollection();
            Collection<Pedidoproducto> pedidoproductoCollectionNew = producto.getPedidoproductoCollection();
            List<String> illegalOrphanMessages = null;
            for (Pedidoproducto pedidoproductoCollectionOldPedidoproducto : pedidoproductoCollectionOld) {
                if (!pedidoproductoCollectionNew.contains(pedidoproductoCollectionOldPedidoproducto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Pedidoproducto " + pedidoproductoCollectionOldPedidoproducto + " since its idProducto field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idRecetaNew != null) {
                idRecetaNew = em.getReference(idRecetaNew.getClass(), idRecetaNew.getIdReceta());
                producto.setIdReceta(idRecetaNew);
            }
            Collection<Pedidoproducto> attachedPedidoproductoCollectionNew = new ArrayList<Pedidoproducto>();
            for (Pedidoproducto pedidoproductoCollectionNewPedidoproductoToAttach : pedidoproductoCollectionNew) {
                pedidoproductoCollectionNewPedidoproductoToAttach = em.getReference(pedidoproductoCollectionNewPedidoproductoToAttach.getClass(), pedidoproductoCollectionNewPedidoproductoToAttach.getIdDetalle());
                attachedPedidoproductoCollectionNew.add(pedidoproductoCollectionNewPedidoproductoToAttach);
            }
            pedidoproductoCollectionNew = attachedPedidoproductoCollectionNew;
            producto.setPedidoproductoCollection(pedidoproductoCollectionNew);
            producto = em.merge(producto);
            if (idRecetaOld != null && !idRecetaOld.equals(idRecetaNew)) {
                idRecetaOld.getProductoCollection().remove(producto);
                idRecetaOld = em.merge(idRecetaOld);
            }
            if (idRecetaNew != null && !idRecetaNew.equals(idRecetaOld)) {
                idRecetaNew.getProductoCollection().add(producto);
                idRecetaNew = em.merge(idRecetaNew);
            }
            for (Pedidoproducto pedidoproductoCollectionNewPedidoproducto : pedidoproductoCollectionNew) {
                if (!pedidoproductoCollectionOld.contains(pedidoproductoCollectionNewPedidoproducto)) {
                    Producto oldIdProductoOfPedidoproductoCollectionNewPedidoproducto = pedidoproductoCollectionNewPedidoproducto.getIdProducto();
                    pedidoproductoCollectionNewPedidoproducto.setIdProducto(producto);
                    pedidoproductoCollectionNewPedidoproducto = em.merge(pedidoproductoCollectionNewPedidoproducto);
                    if (oldIdProductoOfPedidoproductoCollectionNewPedidoproducto != null && !oldIdProductoOfPedidoproductoCollectionNewPedidoproducto.equals(producto)) {
                        oldIdProductoOfPedidoproductoCollectionNewPedidoproducto.getPedidoproductoCollection().remove(pedidoproductoCollectionNewPedidoproducto);
                        oldIdProductoOfPedidoproductoCollectionNewPedidoproducto = em.merge(oldIdProductoOfPedidoproductoCollectionNewPedidoproducto);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = producto.getIdProducto();
                if (findProducto(id) == null) {
                    throw new NonexistentEntityException("The producto with id " + id + " no longer exists.");
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
            Producto producto;
            try {
                producto = em.getReference(Producto.class, id);
                producto.getIdProducto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The producto with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Pedidoproducto> pedidoproductoCollectionOrphanCheck = producto.getPedidoproductoCollection();
            for (Pedidoproducto pedidoproductoCollectionOrphanCheckPedidoproducto : pedidoproductoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Producto (" + producto + ") cannot be destroyed since the Pedidoproducto " + pedidoproductoCollectionOrphanCheckPedidoproducto + " in its pedidoproductoCollection field has a non-nullable idProducto field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Receta idReceta = producto.getIdReceta();
            if (idReceta != null) {
                idReceta.getProductoCollection().remove(producto);
                idReceta = em.merge(idReceta);
            }
            em.remove(producto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Producto> findProductoEntities() {
        return findProductoEntities(true, -1, -1);
    }

    public List<Producto> findProductoEntities(int maxResults, int firstResult) {
        return findProductoEntities(false, maxResults, firstResult);
    }

    private List<Producto> findProductoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Producto.class));
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

    public Producto findProducto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Producto.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Producto> rt = cq.from(Producto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
