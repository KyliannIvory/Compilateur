import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        AnalyseLexicale analyseLexicale = new AnalyseLexicale();
        TableIdentificateur table = new TableIdentificateur();
           AnalSyntaxique analSyntaxique = new AnalSyntaxique();
           analyseLexicale.initialiser();
           analSyntaxique.INITIALISER();
           analSyntaxique.analyseLexicale = analyseLexicale;
           analSyntaxique.ANASYNT();
           analyseLexicale.Terminer();
           table.afficherTable();
           analSyntaxique.CREER_FICHIER_CODE("Simple", analSyntaxique.getList());
           analSyntaxique.INTERPRETER();
       }
    }









