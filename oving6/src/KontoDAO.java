import javax.persistence.*;
import java.util.List;

public class KontoDAO {
    private EntityManagerFactory emf;

    public KontoDAO(EntityManagerFactory emf, boolean reset) {
        this.emf = emf;
        EntityManager em = getEM();
        if (reset) {
            try {
                em.getTransaction().begin();
                em.createNativeQuery("TRUNCATE TABLE KONTO").executeUpdate();
            } finally {
                lukkEM(em);
            }
        }
    }

    private EntityManager getEM() {
        return emf.createEntityManager();
    }

    private void lukkEM(EntityManager em) {
        if (em != null && em.isOpen()) em.close();
    }

    public void lagreNyKonto(Konto konto) {
        EntityManager em = getEM();
        try {
            em.getTransaction().begin();
            em.persist(konto);
            em.getTransaction().commit();
        } catch (EntityExistsException e) {
            System.out.println(e);
        } finally {
            lukkEM(em);
        }
    }

    public List<Konto> getAlleKontoer() {
        EntityManager em = getEM();
        try {
            Query q = em.createQuery("SELECT OBJECT(o) FROM Konto o");
            return q.getResultList();
        } finally {
            lukkEM(em);
        }
    }

    public void endreKonto(Konto konto) {
        EntityManager em = getEM();
        try {
            em.getTransaction().begin();
            Konto k = em.merge(konto);
            em.getTransaction().commit();
        } finally {
            lukkEM(em);
        }
    }

    public Konto finnKonto(String eier) {
        EntityManager em = getEM();
        try {
            return em.find(Konto.class, eier);
        } finally {
            lukkEM(em);
        }
    }

    public List<Konto> listAlleKontoer() {
        EntityManager em = getEM();
        try {
            Query q = em.createQuery("SELECT OBJECT(o) FROM Konto o");
            return q.getResultList();
        } finally {
            lukkEM(em);
        }
    }

    public List<Konto> listKontoerMedStørreSaldo(double saldo) {
        EntityManager em = getEM();
        try {
            Query q = em.createQuery("SELECT OBJECT(k) FROM Konto k WHERE k.saldo>= :saldo");
            q.setParameter("saldo", saldo);
            return q.getResultList();
        } finally {
            lukkEM(em);
        }
    }

    public void slettKonto(String eier) {
        EntityManager em = getEM();
        try {
            Konto b = finnKonto(eier);
            em.getTransaction().begin();
            em.remove(b);
            em.getTransaction().commit();
        } finally {
            lukkEM(em);
        }
    }

    public static void main(String args[]) throws Exception {
        EntityManagerFactory emf = null;
        KontoDAO fasade = null;
        System.out.println("starter...");
        try {
            emf = Persistence.createEntityManagerFactory("MyPersistence");
            System.out.println("konstruktor ferdig " + emf);
            fasade = new KontoDAO(emf, true);
            System.out.println("konstruktor ferdig");

            System.out.println("---Lager Kontoer---\n");
            Konto kont1 = new Konto("2222 5555 2222 5555", 100, "Saitama");
            Konto kont2 = new Konto("3333 4444 3333 4444", 0, "Genos");
            fasade.lagreNyKonto(kont1);
            fasade.lagreNyKonto(kont2);

        } finally {
            if (emf == null) {
                System.out.println("?");
            } else {
                emf.close();
            }
        }
    }
}