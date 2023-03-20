package fr.simplon.clubsportif.jpa;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
@Table(name = "sportif", schema = "club-sportif", catalog = "")
@NamedQuery(name = "sportifDO.retardsDePaiement", //
        query = "select spo " + //
                "from SportifDO spo " + //
                "join spo.cotisations cot " +//
                "join cot.licence lic " +//
                "group by spo, lic.sport, lic.prix " + //
                "having lic.prix - sum(cot.montantReglement) > 0")
public class SportifDO
{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int                      id;
    @Basic
    @Column(name = "prenom", nullable = true, length = 64)
    private String                   prenom;
    @Basic
    @Column(name = "nom", nullable = false, length = 64)
    private String                   nom;
    @Basic
    @Column(name = "adresse_email", nullable = true, length = 256)
    private String                   adresseEmail;
    @OneToMany(mappedBy = "sportif")
    private Collection<CotisationDO> cotisations;

    public int getId()
    {
        return id;
    }

    public void setId(int pId)
    {
        id = pId;
    }

    public String getPrenom()
    {
        return prenom;
    }

    public void setPrenom(String pPrenom)
    {
        prenom = pPrenom;
    }

    public String getNom()
    {
        return nom;
    }

    public void setNom(String pNom)
    {
        nom = pNom;
    }

    public String getAdresseEmail()
    {
        return adresseEmail;
    }

    public void setAdresseEmail(String pAdresseEmail)
    {
        adresseEmail = pAdresseEmail;
    }

    @Override
    public boolean equals(Object pO)
    {
        if (this == pO)
        {
            return true;
        }
        if (pO == null || getClass() != pO.getClass())
        {
            return false;
        }

        SportifDO sportifDO = (SportifDO) pO;

        if (id != sportifDO.id)
        {
            return false;
        }
        if (prenom != null ? !prenom.equals(sportifDO.prenom) : sportifDO.prenom != null)
        {
            return false;
        }
        if (nom != null ? !nom.equals(sportifDO.nom) : sportifDO.nom != null)
        {
            return false;
        }
        if (adresseEmail != null ? !adresseEmail.equals(sportifDO.adresseEmail) : sportifDO.adresseEmail != null)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = id;
        result = 31 * result + (prenom != null ? prenom.hashCode() : 0);
        result = 31 * result + (nom != null ? nom.hashCode() : 0);
        result = 31 * result + (adresseEmail != null ? adresseEmail.hashCode() : 0);
        return result;
    }

    public Collection<CotisationDO> getCotisations()
    {
        return cotisations;
    }

    public void setCotisations(Collection<CotisationDO> pCotisations)
    {
        cotisations = pCotisations;
    }
}
