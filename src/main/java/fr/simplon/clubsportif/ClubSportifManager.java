package fr.simplon.clubsportif;

import fr.simplon.clubsportif.dao.CotisationDAO;
import fr.simplon.clubsportif.dao.LicenceDAO;
import fr.simplon.clubsportif.dao.SportifDAO;
import fr.simplon.clubsportif.jpa.CotisationDO;
import fr.simplon.clubsportif.jpa.LicenceDO;
import fr.simplon.clubsportif.jpa.RetardDeCotisation;
import fr.simplon.clubsportif.jpa.SportifDO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

/**
 * Classe permettant de gérer une asso sportive.
 */
public class ClubSportifManager
{
    private EntityManager mEntityManager;
    private Scanner       mScanner;

    /**
     * Constructeur.
     *
     * @param persistentUnit Nom de la PersistentUnit (comme dans le fichier persistence.xml).
     */
    public ClubSportifManager(String persistentUnit)
    {
        // Initialisation du scanner de la console
        mScanner = new Scanner(System.in);

        // Initialisation du contexte de persistance : création d'une factory d'EntityManager
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistentUnit);

        // Création de l'EntityManager
        mEntityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Affiche les sportifs du club avec la liste de tous leurs règlements de cotisation.
     */
    public void printSportifsWithCotisations()
    {
        clearConsole();

        // Requête : obtenir tous les sportifs de la table "sportif"
        Query query = mEntityManager.createQuery("select sportif from SportifDO sportif");

        // Exécution de la requête
        List<SportifDO> listeSportifs = query.getResultList();

        printLineSeparator();

        // Affichage des résultats
        if (!listeSportifs.isEmpty())
        {
            System.out.println("Liste des adherents du club :");

            // Pour chaque élément 'sportifDO' de la liste 'listeSportifs' on l'affiche
            for (SportifDO sportifDO : listeSportifs)
            {
                System.out.println();
                printCotisations(sportifDO);
            }
        }
        else
        {
            System.out.println("Aucun licencie dans ce club");
        }
        printLineSeparator();
    }

    /**
     * Affiche toutes les cotisations d'un sportif.
     *
     * @param pSportifDO Le sportif dont on veut afficher les cotisations.
     */
    private void printCotisations(SportifDO pSportifDO)
    {
        System.out.printf("[%d] %s %s (%s) %n", //
                          pSportifDO.getId(),  //
                          pSportifDO.getPrenom(),//
                          pSportifDO.getNom().toUpperCase(),  //
                          pSportifDO.getAdresseEmail());

        // On récupère les cotisations du sportif directement sans passer par la BDD
        Collection<CotisationDO> cotisations = pSportifDO.getCotisations();

        // Affichage de toutes les cotisations dans une boucle for
        for (CotisationDO cotisation : cotisations)
        {
            System.out.printf("   - Le %s : reglement de %d euros pour la licence '%s' %n", //
                              cotisation.getDateReglement(), //
                              cotisation.getMontantReglement(), //
                              cotisation.getLicence().getSport().toUpperCase());
        }
    }

    /**
     * Affiche dans la console la liste des sports praticables au club.
     */
    public void printSportsPratiquesDansLeClub()
    {
        clearConsole();

        LicenceDAO dao = new LicenceDAO(mEntityManager);
        List<LicenceDO> allLicences = dao.findAll();

        printLineSeparator();

        if (!allLicences.isEmpty())
        {
            System.out.println("Liste des sports pratiques dans ce club :");
            for (LicenceDO licence : allLicences)
            {
                System.out.printf("- Annee %s : %s (%d euros) %n", //
                                  licence.getAnnee(), //
                                  licence.getSport().toUpperCase(), //
                                  licence.getPrix());
            }
        }
        else
        {
            System.out.println("Aucun sport pour le moment.");
        }
        printLineSeparator();
    }

    /**
     * Affiche la liste des sportifs qui sont en retard de leurs cotisations, sans préciser de détails.
     */
    public void printSportifsEnRetardsDeCotisations()
    {
        clearConsole();

        SportifDAO dao = new SportifDAO(mEntityManager);
        Collection<SportifDO> sportifs = dao.findAllRetardsDeCotisations();

        printLineSeparator();

        if (!sportifs.isEmpty())
        {
            System.out.println("Liste des adherents en retard dans leurs cotisations :");
            for (SportifDO sportif : sportifs)
            {
                System.out.printf("- %s %s %n", //
                                  sportif.getPrenom(), //
                                  sportif.getNom().toUpperCase());
            }
        }
        else
        {
            System.out.println("Tous les adherents sont à jour de leurs cotisations :)");
        }
        printLineSeparator();
    }

    /**
     * Requête vue en TP pour afficher le détail des retards de cotisations.
     */
    public void printRetardsDeCotisations()
    {
        clearConsole();

        CotisationDAO dao = new CotisationDAO(mEntityManager);
        Collection<RetardDeCotisation> retardDeCotisations = dao.findCotisationsEnRetard();

        printLineSeparator();
        if (!retardDeCotisations.isEmpty())
        {
            System.out.println("Liste des adherents en retard dans leurs cotisations :");
            for (RetardDeCotisation retardDeCotisation : retardDeCotisations)
            {
                System.out.printf("- %s %s : en retard de %d euros sur la licence '%s' %n", //
                                  retardDeCotisation.sportif.getPrenom(), //
                                  retardDeCotisation.sportif.getNom().toUpperCase(), //
                                  retardDeCotisation.retard, //
                                  retardDeCotisation.sport.toUpperCase());
            }
        }
        else
        {
            System.out.println("Tous les adherents sont a jour de leurs cotisations.");
        }
        printLineSeparator();
    }

    /**
     * Ajoute un nouveau sport.
     */
    public void menuAjouterSport()
    {
        System.out.print("Nom du sport : ");
        String sport = mScanner.nextLine();

        int prix = -1;
        while (prix < 0)
        {
            System.out.print("Prix de la licence : ");
            String sPrix = mScanner.nextLine();
            try
            {
                prix = Integer.parseInt(sPrix);
            }
            catch (NumberFormatException e)
            {
                System.out.println("Saisie incorrecte");
            }
        }

        LicenceDAO dao = new LicenceDAO(mEntityManager);
        LicenceDO licence = dao.insert(sport, Calendar.getInstance().get(Calendar.YEAR), prix);

        System.out.println("Nouvelle licence ajoutee :");
        System.out.printf("[%d] %s %d : [%s ; %s] (%d euros) %n", //
                          licence.getId(), //
                          licence.getSport(), //
                          licence.getAnnee(), //
                          licence.getDateDebut(), //
                          licence.getDateFin(), //
                          licence.getPrix());

        printLineSeparator();
    }

    /**
     * Ajoute un nouvel adhérent.
     */
    private void menuAjouterAdherent()
    {
        System.out.print("Nom : ");
        String nom = mScanner.nextLine();

        System.out.print("Prenom : ");
        String prenom = mScanner.nextLine();

        SportifDAO dao = new SportifDAO(mEntityManager);
        SportifDO sportif = dao.insert(nom, prenom);

        System.out.println("Nouvel(le) adherent(e) ajoute(e) :");
        System.out.printf("[%d] %s %s %n", //
                          sportif.getId(), //
                          sportif.getNom(), //
                          sportif.getPrenom());

        printLineSeparator();
    }

    /**
     * Affiche une liste des sportifs et demande à l'utilisateur de saisir l'identifiant d'un sportif.
     *
     * @return Le sportif choisi.
     */
    private SportifDO choisirSportif()
    {
        SportifDO sportif = null;
        SportifDAO dao = new SportifDAO(mEntityManager);
        Collection<SportifDO> adherents = dao.findAll();
        for (SportifDO adherent : adherents)
        {
            System.out.printf("[%d] %s %s %n", adherent.getId(), adherent.getPrenom(), adherent.getNom());
        }
        while (sportif == null)
        {
            try
            {
                System.out.print("Num Adherent(e) : ");
                String sId = mScanner.nextLine();
                int idSportif = Integer.parseInt(sId);
                sportif = dao.findById(idSportif);
            }
            catch (NumberFormatException pE)
            {
                System.out.println("Saisie incorrecte");
            }
        }
        return sportif;
    }

    /**
     * Affiche la liste des licences du club et demande à l'utilisateur de choisir celle qu'il souhaite.
     *
     * @return La licence choisi.
     */
    private LicenceDO choisirLicence()
    {
        LicenceDO result = null;
        LicenceDAO licDao = new LicenceDAO(mEntityManager);
        Collection<LicenceDO> licences = licDao.findAll();
        for (LicenceDO licence : licences)
        {
            System.out.printf("[%d] %s %d %n", licence.getId(), licence.getSport(), licence.getAnnee());
        }
        while (result == null)
        {
            try
            {
                System.out.print("Num Sport : ");
                String sId = mScanner.nextLine();
                int idLicence = Integer.parseInt(sId);
                result = licDao.findById(idLicence);
            }
            catch (NumberFormatException pE)
            {
                System.out.println("Saisie incorrecte");
            }
        }
        return result;
    }

    /**
     * Demande à l'utilisateur de choisir le montant de la cotisation réglée.
     *
     * @return Le montant saisi.
     */
    private int choisirMontant(LicenceDO pLicence)
    {
        int montant = -1;
        while (montant < 0)
        {
            System.out.printf("Montant regle (%d) : ", pLicence.getPrix());
            String sMontant = mScanner.nextLine();
            try
            {
                if (sMontant == null || sMontant.isEmpty())
                {
                    montant = pLicence.getPrix();
                }
                else
                {
                    montant = Integer.parseInt(sMontant);
                }
            }
            catch (NumberFormatException pE)
            {
                System.out.println("Saisie incorrecte");
            }
        }
        return montant;
    }

    /**
     * Demande les informations pour ajouter une nouvelle cotisation en BDD.
     */
    private void menuAjouterCotisation()
    {
        SportifDO sportif = choisirSportif();
        LicenceDO licence = choisirLicence();
        int montant = choisirMontant(licence);

        CotisationDAO cotDao = new CotisationDAO(mEntityManager);
        CotisationDO cotisation = cotDao.insert(sportif, licence, montant);

        System.out.println("Nouvel(le) cotisation(e) ajouté(e) :");
        System.out.printf("[%d] Le %s '%s %s' a regle %d euros (licence '%s') %n", //
                          cotisation.getId(), //
                          cotisation.getDateReglement(), //
                          cotisation.getSportif().getPrenom(),//
                          cotisation.getSportif().getNom().toUpperCase(), //
                          cotisation.getMontantReglement(), //
                          cotisation.getLicence().getSport().toUpperCase() //
        );

        printLineSeparator();
    }

    /**
     * Demande à l'utilisateur de choisir une cotisation.
     *
     * @param sportif
     * @return
     */
    private CotisationDO choisirCotisation(SportifDO sportif)
    {
        CotisationDO result = null;
        CotisationDAO dao = new CotisationDAO(mEntityManager);
        Collection<CotisationDO> cotisations = sportif.getCotisations();
        for (CotisationDO cotisation : cotisations)
        {
            System.out.printf("[%d] %s %s %s %s %d %n", //
                              cotisation.getId(), //
                              cotisation.getSportif().getPrenom(), //
                              cotisation.getSportif().getNom().toUpperCase(), //
                              cotisation.getDateReglement(), //
                              cotisation.getLicence().getSport(), //
                              cotisation.getMontantReglement() //
            );
        }
        while (result == null)
        {
            System.out.print("Num cotisation : ");
            String sId = mScanner.nextLine();
            try
            {
                int cotisationId = Integer.parseInt(sId);
                result = dao.findById(cotisationId);
            }
            catch (NumberFormatException pE)
            {
                System.out.println("Saisie incorrecte");
            }
        }
        return result;
    }

    /**
     * Menu de suppression d'une cotisation.
     */
    private void menuSupprimerCotisation()
    {
        SportifDO sportif = choisirSportif();
        CotisationDO cotisation = choisirCotisation(sportif);

        CotisationDAO dao = new CotisationDAO(mEntityManager);
        dao.delete(cotisation);
        System.out.println("==> cotisation supprimee");

        mEntityManager.refresh(sportif);
        printCotisations(sportif);
    }

    /**
     * Menu de suppression d'un(e) adherent.
     */
    private void menuSupprimerAdherent()
    {
        SportifDO sportif = choisirSportif();

        // On commence par supprimer toutes les cotisations du sportif car
        // sinon notre contrainte de clé étrangère va déclencher une erreur.
        CotisationDAO daoCotisations = new CotisationDAO(mEntityManager);
        Collection<CotisationDO> cotisations = sportif.getCotisations();
        for (CotisationDO cotisation : cotisations)
        {
            daoCotisations.delete(cotisation);
        }

        // Ensuite on supprime le sportif
        SportifDAO daoSportifs = new SportifDAO(mEntityManager);
        daoSportifs.delete(sportif);
        System.out.println("==> adherent supprime");

        printSportifsWithCotisations();
    }

    /**
     * Menu principal de l'application.
     */
    public void menuPrincipal()
    {
        clearConsole();

        while (true)
        {
            System.out.println("*************************************************");
            System.out.println("[1] Affiche les licences du club");
            System.out.println("[2] Affiche les cotisations de chaque sportif");
            System.out.println("[3] Afficher les sportifs en retard de cotisation");
            System.out.println("[4] Afficher le detail des retards de cotisations");
            System.out.println("*************************************************");
            System.out.println("[5] Ajouter un nouveau sport");
            System.out.println("[6] Ajouter un(e) adherent(e)");
            System.out.println("[7] Ajouter un paiement de cotisation");
            System.out.println("*************************************************");
            System.out.println("[8] Supprimer un(e) adherent(e)");
            System.out.println("[9] Supprimer un paiement");
            System.out.println();

            int i = -1;
            while (i == -1)
            {
                System.out.print("Choix (q pour quitter) : ");
                String nextLine = mScanner.nextLine();
                if ("q".equalsIgnoreCase(nextLine))
                {
                    System.out.println("Arrêt de l'application");
                    System.exit(0);
                }
                try
                {
                    i = Integer.parseInt(nextLine);
                    switch (i)
                    {
                        case 1:
                            printSportsPratiquesDansLeClub();
                            break;
                        case 2:
                            printSportifsWithCotisations();
                            break;
                        case 3:
                            printSportifsEnRetardsDeCotisations();
                            break;
                        case 4:
                            printRetardsDeCotisations();
                            break;
                        case 5:
                            menuAjouterSport();
                            break;
                        case 6:
                            menuAjouterAdherent();
                            break;
                        case 7:
                            menuAjouterCotisation();
                            break;
                        case 8:
                            menuSupprimerAdherent();
                            break;
                        case 9:
                            menuSupprimerCotisation();
                            break;

                    }
                    pressAnyKey();
                }
                catch (NumberFormatException e)
                {
                    System.out.println("[ERREUR] Attendu : 1, 2 ou 3 (q pour quitter)");
                }
            }
        }
    }

    /**
     * Demande à l'utilisateur d'appuyer sur la touche 'Entrée'.
     */
    public void pressAnyKey()
    {
        System.out.print("Appuyez sur entrée pour continuer...");
        mScanner.nextLine();
        clearConsole();
    }

    /**
     * Efface le texte de la console.
     */
    public static void clearConsole()
    {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Affiche une ligne de séparation.
     */
    private void printLineSeparator()
    {
        System.out.println("-------------------------------------------------------------");
    }

    public static void main(String[] args)
    {
        ClubSportifManager main = new ClubSportifManager("club-sportif");
        main.menuPrincipal();
    }
}
