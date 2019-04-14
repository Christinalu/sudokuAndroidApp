package com.omicron.android.cmpt276_1191e1_omicron.Model;

import java.io.Serializable;
import java.util.HashMap;

public class EventMap implements Serializable {

    private HashMap<String, Integer> prgToPkg = new HashMap<>();
    private HashMap<String, Integer> modeToState = new HashMap<>();
    private HashMap<HashMap<String, Integer>,HashMap<String, Integer>> prgPkgTomodeState = new HashMap<>();
    private HashMap<String, HashMap<HashMap<String, Integer>,HashMap<String, Integer>>> dateTopkg = new HashMap<>();

    public void setPrgToPkg(String packageName, int progress) {
        prgToPkg.put(packageName, progress);
    }

    public void setModeToState(String modeName, int ifFinished) {
        modeToState.put(modeName, ifFinished);
    }

    public HashMap<String, Integer> getPrgToPkg() {
        return prgToPkg;
    }

    public HashMap<String, Integer> getModeToState() {
        return modeToState;
    }

    public void setPrgPkgTomodeState() {
        prgPkgTomodeState.put(getPrgToPkg(), getModeToState());
    }

    public HashMap<HashMap<String, Integer>, HashMap<String, Integer>> getPrgPkgTomodeState() {
        return prgPkgTomodeState;
    }

    public void setDateTopkg(String activityDates) {
        dateTopkg.put(activityDates, getPrgPkgTomodeState());
    }

    public HashMap<String, HashMap<HashMap<String, Integer>, HashMap<String, Integer>>> getDateTopkg() {
        return dateTopkg;
    }
}
