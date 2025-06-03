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
import entity.EnumStatus;
import entity.Factura;
import entity.Pedido;
import java.util.ArrayList;
import java.util.Collection;
import entity.Pedidoproducto;
import entity.Venta;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpaController.exceptions.IllegalOrphanException;
import jpaController.exceptions.NonexistentEntityException;

/**
 *
 * @author crist
 */
public class PedidoJpaController implements Serializable {

    public PedidoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pedido pedido) {
        if (pedido.getFacturaCollection() == null) {
            pedido.setFacturaCollection(new ArrayList<Factura>());
        }
        if (pedido.getPedidoproductoCollection() == null) {
            pedido.setPedidoproductoCollection(new ArrayList<Pedidoproducto>());
        }
        if (pedido.getVentaCollection() == null) {
            pedido.setVentaCollection(new ArrayList<Venta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EnumStatus idStatus = pedido.getIdStatus();
            if (idStatus != null) {
                idStatus = em.getReference(idStatus.getClass(), idStatus.getIdStatus());
                pedido.setIdStatus(idStatus);
            }
            Collection<Factura> attachedFacturaCollection = new ArrayList<Factura>();
            for (Factura facturaCollectionFacturaToAttach : pedido.getFacturaCollection()) {
                facturaCollectionFacturaToAttach = em.getReference(facturaCollectionFacturaToAttach.getClass(), facturaCollectionFacturaToAttach.getIdFactura());
                attachedFacturaCollection.add(facturaCollectionFacturaToAttach);
            }
            pedido.setFacturaCollection(attachedFacturaCollection);
            Collection<Pedidoproducto> attachedPedidoproductoCollection = new ArrayList<Pedidoproducto>();
            for (Pedidoproducto pedidoproductoCollectionPedidoproductoToAttach : pedido.getPedidoproductoCollection()) {
                pedidoproductoCollectionPedidoproductoToAttach = em.getReference(pedidoproductoCollectionPedidoproductoToAttach.getClass(), pedidoproductoCollectionPedidoproductoToAttach.getIdDetalle());
                attachedPedidoproductoCollection.add(pedidoproductoCollectionPedidoproductoToAttach);
            }
            pedido.setPedidoproductoCollection(attachedPedidoproductoCollection);
            Collection<Venta> attachedVentaCollection = new ArrayList<Venta>();
            for (Venta ventaCollectionVentaToAttach : pedido.getVentaCollection()) {
                ventaCollectionVentaToAttach = em.getReference(ventaCollectionVentaToAttach.getClass(), ventaCollectionVentaToAttach.getIdVenta());
                attachedVentaCollection.add(ventaCollectionVentaToAttach);
            }
            pedido.setVentaCollection(attachedVentaCollection);
            em.persist(pedido);
            if (idStatus != null) {
                idStatus.getPedidoCollection().add(pedido);
                idStatus = em.merge(idStatus);
            }
            for (Factura facturaCollectionFactura : pedido.getFacturaCollection()) {
                Pedido oldIdPedidoOfFacturaCollectionFactura = facturaCollectionFactura.getIdPedido();
                facturaCollectionFactura.setIdPedido(pedido);
                facturaCollectionFactura = em.merge(facturaCollectionFactura);
                if (oldIdPedidoOfFacturaCollectionFactura != null) {
                    oldIdPedidoOfFacturaCollectionFactura.getFacturaCollection().remove(facturaCollectionFactura);
                    oldIdPedidoOfFacturaCollectionFactura = em.merge(oldIdPedidoOfFacturaCollectionFactura);
                }
            }
            for (Pedidoproducto pedidoproductoCollectionPedidoproducto : pedido.getPedidoproductoCollection()) {
                Pedido oldIdPedidoOfPedidoproductoCollectionPedidoproducto = pedidoproductoCollectionPedidoproducto.getIdPedido();
                pedidoproductoCollectionPedidoproducto.setIdPedido(pedido);
                pedidoproductoCollectionPedidoproducto = em.merge(pedidoproductoCollectionPedidoproducto);
                if (oldIdPedidoOfPedidoproductoCollectionPedidoproducto != null) {
                    oldIdPedidoOfPedidoproductoCollectionPedidoproducto.getPedidoproductoCollection().remove(pedidoproductoCollectionPedidoproducto);
                    oldIdPedidoOfPedidoproductoCollectionPedidoproducto = em.merge(oldIdPedidoOfPedidoproductoCollectionPedidoproducto);
                }
            }
            for (Venta ventaCollectionVenta : pedido.getVentaCollection()) {
                Pedido oldIdPedioOfVentaCollectionVenta = ventaCollectionVenta.getIdPedio();
                ventaCollectionVenta.setIdPedio(pedido);
                ventaCollectionVenta = em.merge(ventaCollectionVenta);
                if (oldIdPedioOfVentaCollectionVenta != null) {
                    oldIdPedioOfVentaCollectionVenta.getVentaCollection().remove(ventaCollectionVenta);
                    oldIdPedioOfVentaCollectionVenta = em.merge(oldIdPedioOfVentaCollectionVenta);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pedido pedido) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pedido persistentPedido = em.find(Pedido.class, pedido.getIdPedido());
            EnumStatus idStatusOld = persistentPedido.getIdStatus();
            EnumStatus idStatusNew = pedido.getIdStatus();
            Collection<Factura> facturaCollectionOld = persistentPedido.getFacturaCollection();
            Collection<Factura> facturaCollectionNew = pedido.getFacturaCollection();
            Collection<Pedidoproducto> pedidoproductoCollectionOld = persistentPedido.getPedidoproductoCollection();
            Collection<Pedidoproducto> pedidoproductoCollectionNew = pedido.getPedidoproductoCollection();
            Collection<Venta> ventaCollectionOld = persistentPedido.getVentaCollection();
            Collection<Venta> ventaCollectionNew = pedido.getVentaCollection();
            List<String> illegalOrphanMessages = null;
            for (Factura facturaCollectionOldFactura : facturaCollectionOld) {
                if (!facturaCollectionNew.contains(facturaCollectionOldFactura)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Factura " + facturaCollectionOldFactura + " since its idPedido field is not nullable.");
                }
            }
            for (Pedidoproducto pedidoproductoCollectionOldPedidoproducto : pedidoproductoCollectionOld) {
                if (!pedidoproductoCollectionNew.contains(pedidoproductoCollectionOldPedidoproducto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Pedidoproducto " + pedidoproductoCollectionOldPedidoproducto + " since its idPedido field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idStatusNew != null) {
                idStatusNew = em.getReference(idStatusNew.getClass(), idStatusNew.getIdStatus());
                pedido.setIdStatus(idStatusNew);
            }
            Collection<Factura> attachedFacturaCollectionNew = new ArrayList<Factura>();
            for (Factura facturaCollectionNewFacturaToAttach : facturaCollectionNew) {
                facturaCollectionNewFacturaToAttach = em.getReference(facturaCollectionNewFacturaToAttach.getClass(), facturaCollectionNewFacturaToAttach.getIdFactura());
                attachedFacturaCollectionNew.add(facturaCollectionNewFacturaToAttach);
            }
            facturaCollectionNew = attachedFacturaCollectionNew;
            pedido.setFacturaCollection(facturaCollectionNew);
            Collection<Pedidoproducto> attachedPedidoproductoCollectionNew = new ArrayList<Pedidoproducto>();
            for (Pedidoproducto pedidoproductoCollectionNewPedidoproductoToAttach : pedidoproductoCollectionNew) {
                pedidoproductoCollectionNewPedidoproductoToAttach = em.getReference(pedidoproductoCollectionNewPedidoproductoToAttach.getClass(), pedidoproductoCollectionNewPedidoproductoToAttach.getIdDetalle());
                attachedPedidoproductoCollectionNew.add(pedidoproductoCollectionNewPedidoproductoToAttach);
            }
            pedidoproductoCollectionNew = attachedPedidoproductoCollectionNew;
            pedido.setPedidoproductoCollection(pedidoproductoCollectionNew);
            Collection<Venta> attachedVentaCollectionNew = new ArrayList<Venta>();
            for (Venta ventaCollectionNewVentaToAttach : ventaCollectionNew) {
                ventaCollectionNewVentaToAttach = em.getReference(ventaCollectionNewVentaToAttach.getClass(), ventaCollectionNewVentaToAttach.getIdVenta());
                attachedVentaCollectionNew.add(ventaCollectionNewVentaToAttach);
            }
            ventaCollectionNew = attachedVentaCollectionNew;
            pedido.setVentaCollection(ventaCollectionNew);
            pedido = em.merge(pedido);
            if (idStatusOld != null && !idStatusOld.equals(idStatusNew)) {
                idStatusOld.getPedidoCollection().remove(pedido);
                idStatusOld = em.merge(idStatusOld);
            }
            if (idStatusNew != null && !idStatusNew.equals(idStatusOld)) {
                idStatusNew.getPedidoCollection().add(pedido);
                idStatusNew = em.merge(idStatusNew);
            }
            for (Factura facturaCollectionNewFactura : facturaCollectionNew) {
                if (!facturaCollectionOld.contains(facturaCollectionNewFactura)) {
                    Pedido oldIdPedidoOfFacturaCollectionNewFactura = facturaCollectionNewFactura.getIdPedido();
                    facturaCollectionNewFactura.setIdPedido(pedido);
                    facturaCollectionNewFactura = em.merge(facturaCollectionNewFactura);
                    if (oldIdPedidoOfFacturaCollectionNewFactura != null && !oldIdPedidoOfFacturaCollectionNewFactura.equals(pedido)) {
                        oldIdPedidoOfFacturaCollectionNewFactura.getFacturaCollection().remove(facturaCollectionNewFactura);
                        oldIdPedidoOfFacturaCollectionNewFactura = em.merge(oldIdPedidoOfFacturaCollectionNewFactura);
                    }
                }
            }
            for (Pedidoproducto pedidoproductoCollectionNewPedidoproducto : pedidoproductoCollectionNew) {
                if (!pedidoproductoCollectionOld.contains(pedidoproductoCollectionNewPedidoproducto)) {
                    Pedido oldIdPedidoOfPedidoproductoCollectionNewPedidoproducto = pedidoproductoCollectionNewPedidoproducto.getIdPedido();
                    pedidoproductoCollectionNewPedidoproducto.setIdPedido(pedido);
                    pedidoproductoCollectionNewPedidoproducto = em.merge(pedidoproductoCollectionNewPedidoproducto);
                    if (oldIdPedidoOfPedidoproductoCollectionNewPedidoproducto != null && !oldIdPedidoOfPedidoproductoCollectionNewPedidoproducto.equals(pedido)) {
                        oldIdPedidoOfPedidoproductoCollectionNewPedidoproducto.getPedidoproductoCollection().remove(pedidoproductoCollectionNewPedidoproducto);
                        oldIdPedidoOfPedidoproductoCollectionNewPedidoproducto = em.merge(oldIdPedidoOfPedidoproductoCollectionNewPedidoproducto);
                    }
                }
            }
            for (Venta ventaCollectionOldVenta : ventaCollectionOld) {
                if (!ventaCollectionNew.contains(ventaCollectionOldVenta)) {
                    ventaCollectionOldVenta.setIdPedio(null);
                    ventaCollectionOldVenta = em.merge(ventaCollectionOldVenta);
                }
            }
            for (Venta ventaCollectionNewVenta : ventaCollectionNew) {
                if (!ventaCollectionOld.contains(ventaCollectionNewVenta)) {
                    Pedido oldIdPedioOfVentaCollectionNewVenta = ventaCollectionNewVenta.getIdPedio();
                    ventaCollectionNewVenta.setIdPedio(pedido);
                    ventaCollectionNewVenta = em.merge(ventaCollectionNewVenta);
                    if (oldIdPedioOfVentaCollectionNewVenta != null && !oldIdPedioOfVentaCollectionNewVenta.equals(pedido)) {
                        oldIdPedioOfVentaCollectionNewVenta.getVentaCollection().remove(ventaCollectionNewVenta);
                        oldIdPedioOfVentaCollectionNewVenta = em.merge(oldIdPedioOfVentaCollectionNewVenta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pedido.getIdPedido();
                if (findPedido(id) == null) {
                    throw new NonexistentEntityException("The pedido with id " + id + " no longer exists.");
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
            Pedido pedido;
            try {
                pedido = em.getReference(Pedido.class, id);
                pedido.getIdPedido();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pedido with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Factura> facturaCollectionOrphanCheck = pedido.getFacturaCollection();
            for (Factura facturaCollectionOrphanCheckFactura : facturaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pedido (" + pedido + ") cannot be destroyed since the Factura " + facturaCollectionOrphanCheckFactura + " in its facturaCollection field has a non-nullable idPedido field.");
            }
            Collection<Pedidoproducto> pedidoproductoCollectionOrphanCheck = pedido.getPedidoproductoCollection();
            for (Pedidoproducto pedidoproductoCollectionOrphanCheckPedidoproducto : pedidoproductoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pedido (" + pedido + ") cannot be destroyed since the Pedidoproducto " + pedidoproductoCollectionOrphanCheckPedidoproducto + " in its pedidoproductoCollection field has a non-nullable idPedido field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            EnumStatus idStatus = pedido.getIdStatus();
            if (idStatus != null) {
                idStatus.getPedidoCollection().remove(pedido);
                idStatus = em.merge(idStatus);
            }
            Collection<Venta> ventaCollection = pedido.getVentaCollection();
            for (Venta ventaCollectionVenta : ventaCollection) {
                ventaCollectionVenta.setIdPedio(null);
                ventaCollectionVenta = em.merge(ventaCollectionVenta);
            }
            em.remove(pedido);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pedido> findPedidoEntities() {
        return findPedidoEntities(true, -1, -1);
    }

    public List<Pedido> findPedidoEntities(int maxResults, int firstResult) {
        return findPedidoEntities(false, maxResults, firstResult);
    }

    private List<Pedido> findPedidoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pedido.class));
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

    public Pedido findPedido(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pedido.class, id);
        } finally {
            em.close();
        }
    }

    public int getPedidoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pedido> rt = cq.from(Pedido.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
