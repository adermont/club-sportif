import fr.simplon.clubsportif.jpa.SportifDO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

import java.util.List;

public class TestDatabase
{
    public static void main(String[] args)
    {
        String schema = "club-sportif";

        // Récupère la factory permettant d'obtenir les EntityManagers JPA
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(schema);

        // Création de l'EntityManager
        EntityManager entityManager = factory.createEntityManager();

        // Création & exécution de la requête JPQL
        Query queryAllSportifs = entityManager.createQuery("select sportif from SportifDO sportif");
        List<SportifDO> resultList = queryAllSportifs.getResultList();

        // Affichage des résultats
        for (SportifDO sportifDO : resultList)
        {
            System.out.printf("[%d] %s %s%n", sportifDO.getId(), sportifDO.getNom().toUpperCase(), sportifDO.getPrenom());
        }
    }
}
