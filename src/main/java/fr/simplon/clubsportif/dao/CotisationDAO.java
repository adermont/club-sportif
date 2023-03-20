package fr.simplon.clubsportif.dao;

import fr.simplon.clubsportif.jpa.CotisationDO;
import fr.simplon.clubsportif.jpa.LicenceDO;
import fr.simplon.clubsportif.jpa.RetardDeCotisation;
import fr.simplon.clubsportif.jpa.SportifDO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Classe d'accès aux données des cotisations.
 */
public class CotisationDAO
{
    private EntityManager mEntityManager;

    /**
     * Constructeur.
     *
     * @param pEntityManager L'entityManager à utiliser pour faire les requêtes dans la base de données.
     */
    public CotisationDAO(EntityManager pEntityManager)
    {
        mEntityManager = pEntityManager;
    }

    /**
     * @return La liste de toutes les Cotisations.
     */
    public Collection<CotisationDO> findAll()
    {
        TypedQuery<CotisationDO> query = mEntityManager.createQuery("from CotisationDO", CotisationDO.class);
        return query.getResultList();
    }

    /**
     * Trouve une cotisation par son identifiant.
     *
     * @param pCotisationId L'identifiant de la cotisation recherchée.
     * @return La cotisation correspondante.
     */
    public CotisationDO findById(int pCotisationId)
    {
        return mEntityManager.getReference(CotisationDO.class, pCotisationId);
    }

    /**
     * @return La liste des retards de paiement de cotisations sous forme d'objet RetardDePaiement.
     */
    public Collection<RetardDeCotisation> findCotisationsEnRetard()
    {
        // Création d'une NamedQuery "mauvaisPayeurs" qui est déclarée dans la classe CotisationDAO
        TypedQuery<Tuple> query = mEntityManager.createNamedQuery("retardsCotisations", Tuple.class);

        // Exécution de la requête : obtention d'une liste d'objets de type "Tuple"
        List<Tuple> resultList = query.getResultList();

        // On va convertir chaque Tuple en objet MauvaisPayeur pour
        // ensuite retourner une liste d'objets de type "MauvaisPayeur"
        // comme c'est indiqué dans la signature de la méthode.
        Collection<RetardDeCotisation> listeRetardDeCotisations = new ArrayList<>();

        // Pour chaque tuple de la liste  des résultats de la requête
        for (Tuple tuple : resultList)
        {
            // On crée un objet MauvaisPayeur qu'on initialise grâce au Tuple
            RetardDeCotisation retardDeCotisation = new RetardDeCotisation(tuple);

            // On l'ajoute à la liste des résultats
            listeRetardDeCotisations.add(retardDeCotisation);
        }

        // On retourne la liste des MauvaisPayeurs
        return listeRetardDeCotisations;
    }

    /**
     * Ajoute une nouvelle cotisation en BDD.
     *
     * @param pSportif Le sportif qui règle la cotisation.
     * @param pLicence Le sport ou la licence concernée.
     * @param pMontant Le montant réglé.
     * @return Le nouvel objet créé.
     */
    public CotisationDO insert(SportifDO pSportif, LicenceDO pLicence, int pMontant)
    {
        // Création d'un nouvel objet de type CotisationDO
        CotisationDO cotisation = new CotisationDO();
        cotisation.setSportif(pSportif);
        cotisation.setLicence(pLicence);
        cotisation.setMontantReglement(pMontant);
        cotisation.setDateReglement(new Date(System.currentTimeMillis()));

        // Ouverture d'une transaction, persistance puis commit.
        mEntityManager.getTransaction().begin();
        mEntityManager.persist(cotisation);
        mEntityManager.getTransaction().commit();

        return cotisation;
    }

    /**
     * Supprime une cotisation.
     *
     * @param pCotisation La cotisation à supprimer.
     */
    public void delete(CotisationDO pCotisation)
    {
        mEntityManager.getTransaction().begin();
        mEntityManager.remove(pCotisation);
        mEntityManager.getTransaction().commit();
    }
}
