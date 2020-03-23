import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class Oppgave3 {

    /**
     * Eksempel på overføring med feil resultat
     * Krever at det er to kontoer i databasen fra før av. Jeg bruker KontoDao sin main for å lage disse.
     * Gjort før version ble lagt til i Konto.
     *
     * Her burde resultatet bli 0kr på konto 1 og 100kr på konto 2.
     * Se vedlagt bilde for feilen.
     */

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

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            assert emf != null;
            emf.close();
        }
    }
}

