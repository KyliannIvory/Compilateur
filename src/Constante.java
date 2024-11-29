class Constante extends Identificateur {
    public int getValent() {
        return valent;
    }

    public void setValent(int valent) {
        this.valent = valent;
    }

    public String getValch() {
        return valch;
    }

    public void setValch(String valch) {
        this.valch = valch;
    }

    public int getTypc() {
        return typc;
    }

    public void setTypc(int typc) {
        this.typc = typc;
    }

    public TableIdentificateur getTable() {
        return table;
    }

    private int valent;
    private String valch;
    private int typc;
    private final TableIdentificateur table=new TableIdentificateur();

    public Constante() {
        super();
        this.valent = 0;
        this.valch = "";
        this.typc = 0;
    }


    public String toString() {
        String type;
        String valeur;
        if (typc == 0) {
            type = "entier";
            return "Constante: nom=" + getNom() + ", type=" + type + ", valeur=" + String.valueOf(valent);

        } else {
            type = "chaîne";
            return "Constante: nom=" + getNom() + ", type=" + type + ", valeur=" + valch;

        }
    }

}