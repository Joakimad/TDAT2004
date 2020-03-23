import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class Oppgave2 {

    public static void main(String[] args) {
        EntityManagerFactory emf = null;
        KontoDAO fasade = null;

        try {
            emf = Persistence.createEntityManagerFactory("MyPersistence");
            fasade = new KontoDAO(emf, true);

            System.out.println("---Lager Kontoer---\n");
            Konto kont1 = new Konto("2222 5555 2222 5555", 250, "Saitama");
            Konto kont2 = new Konto("3333 4444 3333 4444", 100, "Genos");
            fasade.lagreNyKonto(kont1);
            fasade.lagreNyKonto(kont2);

            //lister ut alle kontoer med saldo større enn 50
            List<Konto> liste = fasade.listKontoerMedStørreSaldo(200);
            System.out.println("---Kontoer med saldo større enn 200kr---");
            if (liste.size() > 0) {
                for (Konto k : liste) {
                    System.out.println("\t" + k.toString() + "\n");
                }
            } else {
                System.out.println("-");
            }
            System.out.println("---Endre navn---");
            System.out.println("Endrer: " + liste.get(0).getEier());
            liste.get(0).setEier("One Punch Man");
            fasade.endreKonto(liste.get(0));

            System.out.print("Endret til: ");
            System.out.println(liste.get(0).getEier());
        } catch (
                Exception e) {
            e.printStackTrace();
        } finally {
            assert emf != null;
            emf.close();
        }
    }
}
