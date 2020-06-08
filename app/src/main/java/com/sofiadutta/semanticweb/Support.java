package com.sofiadutta.semanticweb;

import com.google.gson.annotations.SerializedName;

public class Support {

    @SerializedName("fact")
    public String fact;

    @SerializedName("source")
    public String source;

    @SerializedName("confidence")
    public double confidence;

    @Override
    public String toString() {
        return "fact: " + fact + ", source: " + source + ", confidence: " + confidence;
    }
}