package com.sofiadutta.semanticweb.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FactCollection {

    @SerializedName("context_facts")
    public List<ContextFact> contextFacts;

    @Override
    public String toString() {
        String text = "contextFacts:";

        int i = 0;
        for (ContextFact aux : contextFacts) {
            text += "\n\nContextFact " + i + ") " + aux.toString();
            i++;
        }

        return text;
    }
}