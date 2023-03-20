package fr.simplon.clubsportif.jpa;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.Collection;

@Entity
@Table(name = "licence", schema = "club-sportif", catalog = "")
@NamedQuery(name="tousLesSports", query="from LicenceDO")
public class LicenceDO
{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int                      id;
    @Basic
    @Column(name = "sport", nullable = false, length = 64)
    private String                   sport;
    @Basic
    @Column(name = "annee", nullable = false)
    private int                      annee;
    @Basic
    @Column(name = "prix", nullable = false)
    private int                      prix;
    @Basic
    @Column(name = "date_debut", nullable = false)
    private Date                     dateDebut;
    @Basic
    @Column(name = "date_fin", nullable = false)
    private Date                     dateFin;
    @OneToMany(mappedBy = "licence")
    private Collection<CotisationDO> cotisations;

    public int getId()
    {
        return id;
    }

    public void setId(int pId)
    {
        id = pId;
    }

    public String getSport()
    {
        return sport;
    }

    public void setSport(String pSport)
    {
        sport = pSport;
    }

    public int getAnnee()
    {
        return annee;
    }

    public void setAnnee(int pAnnee)
    {
        annee = pAnnee;
    }

    public int getPrix()
    {
        return prix;
    }

    public void setPrix(int pPrix)
    {
        prix = pPrix;
    }

    public Date getDateDebut()
    {
        return dateDebut;
    }

    public void setDateDebut(Date pDateDebut)
    {
        dateDebut = pDateDebut;
    }

    public Date getDateFin()
    {
        return dateFin;
    }

    public void setDateFin(Date pDateFin)
    {
        dateFin = pDateFin;
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

        LicenceDO licenceDO = (LicenceDO) pO;

        if (id != licenceDO.id)
        {
            return false;
        }
        if (annee != licenceDO.annee)
        {
            return false;
        }
        if (prix != licenceDO.prix)
        {
            return false;
        }
        if (sport != null ? !sport.equals(licenceDO.sport) : licenceDO.sport != null)
        {
            return false;
        }
        if (dateDebut != null ? !dateDebut.equals(licenceDO.dateDebut) : licenceDO.dateDebut != null)
        {
            return false;
        }
        if (dateFin != null ? !dateFin.equals(licenceDO.dateFin) : licenceDO.dateFin != null)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = id;
        result = 31 * result + (sport != null ? sport.hashCode() : 0);
        result = 31 * result + annee;
        result = 31 * result + prix;
        result = 31 * result + (dateDebut != null ? dateDebut.hashCode() : 0);
        result = 31 * result + (dateFin != null ? dateFin.hashCode() : 0);
        return result;
    }

    public Collection<CotisationDO> getCotisations()
    {
        return cotisations;
    }

    public void setCotisations(Collection<CotisationDO> pCotisationsById)
    {
        cotisations = pCotisationsById;
    }
}
