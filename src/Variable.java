class Variable extends Identificateur {
    public int getAdresse() {
        return adresse;
    }

    public void setAdresse(int adresse) {
        this.adresse = adresse;
    }

    private int adresse;



    public String toString() {
        return "Variable: nom=" + getNom() + ", type=variable, adresse=" + adresse;
    }
}
