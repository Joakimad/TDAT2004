import javax.persistence.EntityManagerFactory;
import javax.persistence.OptimisticLockException;
import javax.persistence.Persistence;
import javax.persistence.RollbackException;
import java.util.List;

public class Oppgave4 {

    public static void main(String[] args) {
        EntityManagerFactory emf = null;
        KontoDAO fasade;

        try {
            emf = Persistence.createEntityManagerFactory("MyPersistence");
            fasade = new KontoDAO(emf, false);

            List<Konto> liste = fasade.listAlleKontoer();

            System.out.println("---Før---");
            for (Konto k : liste) {
                System.out.println("\t" + k.toString() + "\n");
            }

            liste.get(0).trekkBelop(100);
            liste.get(1).leggTilBeløp(100);

            Thread.sleep(5000);

            fasade.endreKonto(liste.get(0));
            fasade.endreKonto(liste.get(1));

            System.out.println("---Etter---");
            for (Konto k : liste) {
                System.out.println("\t" + k.toString() + "\n");
            }

        } catch (OptimisticLockException | RollbackException e) {
            System.out.println("Den andre instansen ble ferdig først. Denne ble kansellert.");
        } catch (Exception e) {
            System.out.println("Other exceptions");
        } finally {
            assert emf != null;
            emf.close();
        }
    }
}

