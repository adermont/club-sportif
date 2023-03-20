package fr.simplon.clubsportif.dao;

import fr.simplon.clubsportif.jpa.CotisationDO;
import fr.simplon.clubsportif.jpa.LicenceDO;
import fr.simplon.clubsportif.jpa.SportifDO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe d'accès aux données des sportifs.
 */
public class SportifDAO
{
    public EntityManager mEntityManager;

    /**
     * Constructeur.
     *
     * @param pEntityManager L'entityManager à utiliser pour faire les requêtes dans la base de données.
     */
    public SportifDAO(EntityManager pEntityManager)
    {
        mEntityManager = pEntityManager;
    }

    /**
     * @return Récupère tous les sportifs dans la BDD.
     */
    public Collection<SportifDO> findAll()
    {
        TypedQuery<SportifDO> query = mEntityManager.createQuery("from SportifDO", SportifDO.class);
        return query.getResultList();
    }

    /**
     * Trouve un sportif par son identifiant.
     *
     * @param pId L'identifiant du sportif recherché.
     */
    public SportifDO findById(int pId)
    {
        return mEntityManager.getReference(SportifDO.class, pId);
    }

    /**
     * Retourne la liste des sportifs qui ont fait au moins une cotisation pour une licence donnée.
     *
     * @param pLicenceDO La licence pour laquelle on cherche les sportifs qui pratiquent ce sport.
     * @return la liste des sportifs qui ont fait au moins une cotisation pour une licence donnée.
     */
    public Collection<SportifDO> findByLicence(LicenceDO pLicenceDO)
    {
        Collection<CotisationDO> cotisations = pLicenceDO.getCotisations();
        List<SportifDO> sportifsByLicence = cotisations.stream().map(cotisation -> cotisation.getSportif()).collect(Collectors.toList());
        return sportifsByLicence;
    }

    /**
     * Trouve toutes les personnes qui sont en retard de cotisation.
     *
     * @return La liste des sportifs qui n'ont pas réglé l'intégralité des cotisations de leurs licences.
     */
    public Collection<SportifDO> findAllRetardsDeCotisations()
    {
        TypedQuery<SportifDO> query = mEntityManager.createNamedQuery("sportifDO.retardsDePaiement", SportifDO.class);
        List<SportifDO> listSportifs = query.getResultList();
        return listSportifs;
    }

    /**
     * Ajoute un(e) nouvel(le) adhérent(e) en BDD.
     *
     * @param pNom    Nom de l'adhérent(e).
     * @param pPrenom Prénom de l'adhérent(e).
     * @return Le nouvel objet créé.
     */
    public SportifDO insert(String pNom, String pPrenom)
    {
        SportifDO sportif = new SportifDO();
        sportif.setNom(pNom);
        sportif.setPrenom(pPrenom);

        mEntityManager.getTransaction().begin();
        mEntityManager.persist(sportif);
        mEntityManager.getTransaction().commit();

        return sportif;
    }

    /**
     * Supprime un(e) adhérent(e) de la BDD.
     *
     * @param pSportif L'adhérent(e) à supprimer.
     */
    public void delete(SportifDO pSportif)
    {
        mEntityManager.getTransaction().begin();
        mEntityManager.remove(pSportif);
        mEntityManager.getTransaction().commit();
    }
}
