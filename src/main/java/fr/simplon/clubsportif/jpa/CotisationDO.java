package fr.simplon.clubsportif.jpa;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "cotisation", schema = "club-sportif")
@NamedQuery(name="retardsCotisations",
        query="select spo, lic.sport, lic.prix - sum(cot.montantReglement) " + //
        "from CotisationDO cot " + //
        "join cot.sportif spo " + //
        "join cot.licence lic " +//
        "GROUP BY spo, lic, lic.prix " + //
        "having lic.prix - sum(cot.montantReglement) > 0")
public class CotisationDO
{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int       id;
    @Basic
    @Column(name = "montant_reglement", nullable = false)
    private int       montantReglement;
    @Basic
    @Column(name = "date_reglement", nullable = false)
    private Date      dateReglement;
    @ManyToOne
    @JoinColumn(name = "id_licence", referencedColumnName = "id", nullable = false)
    private LicenceDO licence;
    @ManyToOne
    @JoinColumn(name = "id_sportif", referencedColumnName = "id", nullable = false)
    private SportifDO sportif;

    public int getId()
    {
        return id;
    }

    public void setId(int pId)
    {
        id = pId;
    }

    public int getMontantReglement()
    {
        return montantReglement;
    }

    public void setMontantReglement(int pMontantReglement)
    {
        montantReglement = pMontantReglement;
    }

    public Date getDateReglement()
    {
        return dateReglement;
    }

    public void setDateReglement(Date pDateReglement)
    {
        dateReglement = pDateReglement;
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

        CotisationDO that = (CotisationDO) pO;

        if (id != that.id)
        {
            return false;
        }
        if (montantReglement != that.montantReglement)
        {
            return false;
        }
        if (dateReglement != null ? !dateReglement.equals(that.dateReglement) : that.dateReglement != null)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = id;
        result = 31 * result + montantReglement;
        result = 31 * result + (dateReglement != null ? dateReglement.hashCode() : 0);
        return result;
    }

    public LicenceDO getLicence()
    {
        return licence;
    }

    public void setLicence(LicenceDO pLicence)
    {
        licence = pLicence;
    }

    public SportifDO getSportif()
    {
        return sportif;
    }

    public void setSportif(SportifDO pSportif)
    {
        sportif = pSportif;
    }
}
