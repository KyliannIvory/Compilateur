import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class AnalyseLexicale {
    private  final int LONG_Max_Ident = 20;
    private final int LONG_Max_Chaine = 50;
    private final int NB_Mots_Reserves = 12;
    private  final int MAXINT = 32767;
    private  BufferedReader source;
    private char CarLu;

    public int getNombre() {
        return Nombre;
    }

    public void setNombre(int nombre) {
        Nombre = nombre;
    }

    private int Nombre;
    private  String chaine;
    private int Num_Ligne;
    private final ArrayList<String> Table_Mots_Reserves=new ArrayList<>(NB_Mots_Reserves);
    public  ArrayList<String> table = new ArrayList<>();
    public  void ERREUR(int numeroErreur) {
        String message;
        switch (numeroErreur) {
            case 1:
                message = "Fin de fichier atteinte.";
                break;
            case 2:
                message = "Nombre très grand";
                break;
            case 3:
                message = "Mot très long";
                break;
            case 4:
                message="Erreur syntaxique";
                break;
            default:
                message = "Une autre erreur s'est produite.";
        }

        System.out.println(" Ligne " + Num_Ligne + ": Erreur " + numeroErreur + ": " + message);
        System.exit(0);

    }


    public String getChaine() {
        return chaine;
    }

    public void lireCar() throws IOException {
            int caractere = source.read();
            if(caractere != -1){
                CarLu = (char) caractere;
                if(CarLu =='\n'){
                    Num_Ligne++;
                 }
            } else{
                ERREUR(1);
            }
        }


   public void sauterSeparateurs() throws IOException {

        while( Character.isWhitespace(CarLu) || CarLu== ' ' || CarLu == '\n' || CarLu == '\t' || CarLu == '{'){
            if (CarLu == '{')
                sauterCommentaire();
            else {
                lireCar();
            }
        }
    }

    //pocedure sauterCommentaire
    public  void sauterCommentaire() throws IOException {
        while(CarLu != '}'){
            lireCar();
            if(CarLu == '{')
                sauterCommentaire();
        }
        lireCar();
    }

    public  T_Unilex recoEntier() throws IOException {
        Nombre = 0;
        while (Character.isDigit(CarLu)) {
            Nombre = Nombre * 10 + Character.getNumericValue(CarLu);
            lireCar();
        }

        if (Nombre > MAXINT) {
            ERREUR(2);
        }
        System.out.println( Nombre+ " : "+T_Unilex.ent);
        return T_Unilex.ent;
    }

   public T_Unilex recoChaine() throws IOException {
       StringBuilder chaineBuilder = new StringBuilder();
       chaineBuilder.append(CarLu);
       lireCar();
     while (true ) {

           if (CarLu == '\'') {
               chaineBuilder.append('\'');
               lireCar();

               if (CarLu == '\'') {
                   chaineBuilder.append('\'');
                   lireCar();
               } else {
                   break;
               }
           } else {
               chaineBuilder.append(CarLu);
               lireCar();
           }
       }
       chaine = chaineBuilder.toString();

       if (chaine.length() >= LONG_Max_Chaine) {
           ERREUR(3);
       }

       System.out.println(chaine + " : " + T_Unilex.ch);
       return T_Unilex.ch;
   }

    public BufferedReader getSource() {
        return source;
    }

    public  T_Unilex recoIdentOuMotReserve() throws IOException {
        StringBuilder chaineBuilder = new StringBuilder();
        while(Character.isDigit(CarLu) || CarLu == '_' || Character.isLetter(CarLu)){
            chaineBuilder.append(CarLu);
            lireCar();
        }
        if(chaineBuilder.length() > LONG_Max_Ident)
            chaine = chaineBuilder.substring(0,LONG_Max_Ident-1).toUpperCase();
        else{
            chaine = chaineBuilder.toString().toUpperCase();
        }
        if (Table_Mots_Reserves.contains(chaine)){
            System.out.println( chaine +" : "+ T_Unilex.motcle );
            return T_Unilex.motcle;
        }
        else{

            System.out.println( chaine +": "+ T_Unilex.ident );
            return T_Unilex.ident;
        }
    }

  public T_Unilex recoSymb() throws IOException {
      char symbole = CarLu;

      switch (symbole) {
          case ';':
              System.out.println(symbole + " :"+T_Unilex.ptvirg);
              lireCar();
              return T_Unilex.ptvirg;

          case '.':
              System.out.println(symbole + " :"+T_Unilex.point);
              lireCar();
              return T_Unilex.point;

          case '(':
              System.out.println(symbole + " :"+T_Unilex.parouv);
              lireCar();
              return T_Unilex.parouv;

          case ')':
              System.out.println(symbole + " :"+T_Unilex.parfer);
              lireCar();
              return T_Unilex.parfer;

          case '<':
              lireCar();
              if (CarLu == '=') {
                  lireCar();
                  System.out.println(" <= :"+T_Unilex.parfer);
                  return T_Unilex.infe;
              }
              if (CarLu == '>') {
                  lireCar();
                  System.out.println("<>" + " :" + T_Unilex.diff);
                  return T_Unilex.diff;
              }

              System.out.println(symbole + " :" + T_Unilex.inf);
              lireCar();
              return T_Unilex.inf;

          case ',':
              System.out.println(symbole + " :"+T_Unilex.virg);
              lireCar();
              return T_Unilex.virg;

          case '>':
              lireCar();
              if (CarLu == '=') {
                  lireCar();
                  System.out.println(symbole + ">= :"+T_Unilex.supe);
                  return T_Unilex.supe;
              }
              System.out.println(symbole + " :"+T_Unilex.sup);
              lireCar();
              return T_Unilex.sup;

          case '=':
              System.out.println(symbole + " :"+T_Unilex.eg);
              lireCar();
              return T_Unilex.eg;

          case '+':
              System.out.println(symbole + " :"+T_Unilex.plus);
              lireCar();
              return T_Unilex.plus;

          case '-':
              System.out.println(symbole +": "+T_Unilex.moins);
              lireCar();
              return T_Unilex.moins;

          case '*':
              System.out.println(symbole +": "+T_Unilex.mutl);
              lireCar();
              return T_Unilex.mutl;

          case '/':
              System.out.println(symbole +": "+T_Unilex.divi);
              lireCar();
              return T_Unilex.divi;

          case ':':
              lireCar();
              if (CarLu == '=') {
                  System.out.println(":= :"+T_Unilex.aff);
                  lireCar();
                  return T_Unilex.aff;
              }
              break;

          default:
              System.out.println("Caractère non reconnu");
      } lireCar();
      return null;
  }
    public  T_Unilex ANALEX() throws IOException {

        sauterSeparateurs();

        if (Character.isDigit(CarLu)) {
            sauterSeparateurs();
            return recoEntier();
        } else if (CarLu == '\'') {
           sauterSeparateurs();
            return recoChaine();
        } else if (Character.isAlphabetic(CarLu)) {
            sauterSeparateurs();
            return recoIdentOuMotReserve();
        } else if (CarLu == ';' || CarLu == '.' || CarLu == ':' || CarLu == '(' || CarLu == ')' ||
                CarLu == '<' || CarLu == '>' || CarLu == '=' || CarLu == '+' || CarLu == '-' ||
                CarLu == '*' || CarLu == '/'|| CarLu == ',' ) {
            sauterSeparateurs();
          return recoSymb();
        }

        return null;
    }
    public void initialiser() throws IOException {
        try {
            source = new BufferedReader(new FileReader("src/Simple"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Num_Ligne = 1;
        lireCar();
        Table_Mots_Reserves.add("PROGRAMME");
        Table_Mots_Reserves.add("DEBUT");
        Table_Mots_Reserves.add("FIN");
        Table_Mots_Reserves.add("CONST");
        Table_Mots_Reserves.add("VAR");
        Table_Mots_Reserves.add("ECRIRE");
        Table_Mots_Reserves.add("LIRE");
        Table_Mots_Reserves.add("SI");
        Table_Mots_Reserves.add("ALORS");
        Table_Mots_Reserves.add("SINON");
        Table_Mots_Reserves.add("TANTQUE");
        Table_Mots_Reserves.add("FAIRE");
        Table_Mots_Reserves.sort(null);
    }
    public  void Terminer() throws IOException {
        if (source != null) {
            source.close();
        }
    }


}