package fr.simplon.clubsportif.jpa;

import jakarta.persistence.Tuple;

public class RetardDeCotisation
{
    public SportifDO sportif;
    public String sport;
    public Long retard;

    public RetardDeCotisation(Tuple pTuple)
    {
        sportif = pTuple.get(0, SportifDO.class);
        sport = pTuple.get(1, String.class);
        retard = pTuple.get(2, Long.class);
    }
}
