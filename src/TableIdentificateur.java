import java.util.HashMap;

class TableIdentificateur {
    public HashMap<String, Identificateur> getTable() {
        return table;
    }

    public void setTable(HashMap<String, Identificateur> table) {
        this.table = table;
    }

    private HashMap<String, Identificateur> table;

    public TableIdentificateur() {
        table = new HashMap<>();
    }
    public boolean chercher(String nom) {
        return table.containsKey(nom);
    }
    public int Inserer (Identificateur identificateur) {
        table.put(identificateur.getNom(), identificateur);
        return table.size() - 1;
    }
    public void afficherTable() {
       // System.out.println("Table des identificateurs :");
        for (String nom : table.keySet()) {
            Identificateur identificateur = table.get(nom);
            System.out.println(identificateur.toString());
        }
    }
}

