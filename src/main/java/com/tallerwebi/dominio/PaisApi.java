package com.tallerwebi.dominio;

public class PaisApi {
    private Name name;
    private Flags flags;

    public Name getName() { return name; }
    public void setName(Name name) { this.name = name; }

    public Flags getFlags() { return flags; }
    public void setFlags(Flags flags) { this.flags = flags; }

    public static class Name {
        private String common;
        public String getCommon() { return common; }
        public void setCommon(String common) { this.common = common; }
    }

    public static class Flags {
        private String png;
        public String getPng() { return png; }
        public void setPng(String png) { this.png = png; }
    }
}
