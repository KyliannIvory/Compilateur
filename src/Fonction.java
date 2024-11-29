class Fonction extends Identificateur {
    int nombreParametres;
    String typeRetour;

    public Fonction(String nom) {
        super();
        this.nombreParametres = nombreParametres;
        this.typeRetour = typeRetour;
    }
    public String toString() {
        return "Fonction : nom=" + getNom() + ", nbParametres=" + nombreParametres + ", typeRetour=" + typeRetour;
    }
}