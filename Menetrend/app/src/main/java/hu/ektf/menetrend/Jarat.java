package hu.ektf.menetrend;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Csaba on 2015.12.13..
 */
public class Jarat {
    private ArrayList<String> varosok;
    private Calendar indulas, erkezes;

    public Jarat(ArrayList<String> var,Calendar ind,Calendar erk) {
        varosok = var;
        indulas = ind;
        erkezes = erk;
    }

    public String getIndulo(){
        return varosok.get(0);
    }

    public String getErkezo(){
        return varosok.get(varosok.size()-1);
    }

    public Calendar getInduloIdo(){
        return indulas;
    }

    public Calendar getErkezoIdo(){
        return erkezes;
    }

    @Override
    public String toString(){
        return getIndulo()+" - " + getErkezo();
    }
}
