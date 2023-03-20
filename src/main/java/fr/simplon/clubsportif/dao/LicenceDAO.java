package fr.simplon.clubsportif.dao;

import fr.simplon.clubsportif.jpa.LicenceDO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.sql.Date;
import java.util.List;

/**
 * Classe d'accès aux données des licences du club.
 */
public class LicenceDAO
{
    private EntityManager mEntityManager;

    /**
     * Constructeur.
     *
     * @param pEntityManager L'entityManager à utiliser pour faire les requêtes dans la base de données.
     */
    public LicenceDAO(EntityManager pEntityManager)
    {
        mEntityManager = pEntityManager;
    }

    /**
     * @return La liste de toutes les licences disponibles.
     */
    public List<LicenceDO> findAll()
    {
        TypedQuery<LicenceDO> query = mEntityManager.createNamedQuery("tousLesSports", LicenceDO.class);
        return query.getResultList();
    }

    /**
     * Trouve un objet par son identifiant.
     *
     * @param pIdLicence L'id de la licence recherchée.
     * @return La licence trouvée.
     */
    public LicenceDO findById(int pIdLicence)
    {
        return mEntityManager.getReference(LicenceDO.class, pIdLicence);
    }

    /**
     * Ajoute un nouveau sport en BDD.
     *
     * @param pSport        Le nom du sport.
     * @param pAnneeLicence L'année de validité de la licence.
     * @param pPrixLicence  Le prix de la licence.
     * @return Le nouvel objet créé.
     */
    public LicenceDO insert(String pSport, int pAnneeLicence, int pPrixLicence)
    {
        // Création d'une nouvelle LicenceDO
        LicenceDO licence = new LicenceDO();

        // On modifie ses attributs en appelant les setters
        licence.setAnnee(pAnneeLicence);
        licence.setDateDebut(Date.valueOf(pAnneeLicence + "-01-01"));
        licence.setDateFin(Date.valueOf(pAnneeLicence + "-12-31"));
        licence.setSport(pSport);
        licence.setPrix(pPrixLicence);

        mEntityManager.getTransaction().begin();
        mEntityManager.persist(licence);
        mEntityManager.getTransaction().commit();
        return licence;
    }

}
