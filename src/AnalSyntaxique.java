
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class AnalSyntaxique {
    private T_Unilex unilex;

    public List<Integer> getPCode() {
        return PCode;
    }

    private Stack<Integer> PCode = new Stack<>();
    private Stack<Integer> PilOP = new Stack<>();
    private Stack<Integer> Pilex=new Stack<>();
   private Stack<Integer> pile = new Stack<>();

    public Stack<Object> getList() {
        return list;
    }

    private Stack<Object> list= new Stack<>();
   private int indice;
   private LinkedList<Integer> MEM = new LinkedList<>(Arrays.asList(new Integer[1000]));


    public AnalyseLexicale analyseLexicale;
    public int NB_Const_Chaine;
    private ArrayList<String> Val_de_Contante = new ArrayList<>();
    private int DERNIERE_ADRESSE_VAR_GLOB;
    private int CO;
    private int Sompilex=Pilex.size();
    TableIdentificateur table = new TableIdentificateur();

    public void genecode_INST_COND1(){
        PCode.push(14);
        list.push("ALSN");
       // Sompilop++;
        PilOP.push(CO+1);
        list.push(CO+1);
        CO+=2;
        indice+=2;
    }
    public void genecode_INST_COND2(){
        int adresse=PilOP.pop();
        PCode.set(adresse,(CO + 2));
        PCode.set(CO + 1, 15);
        PCode.push( CO + 1);
        CO = CO + 2;

    }
    public void genecode_INST_COND3() {
        int adresse=PilOP.pop();
        PCode.set(adresse,CO);

    }
    public void genecode_Repe1(){
        PCode.push(15);
        PCode.push(CO+1);
        CO+=2;

    }
    public void genecode_repe2(){
        int adresse=PilOP.pop();
        PCode.set(adresse,(CO + 2));
        PCode.set(CO , 15);
        PCode.set(CO+1 , 15);

        CO = CO + 2;


    }

    public void GENCODE_LECTURE(String chaine) {
        Variable variable = (Variable) table.getTable().get(chaine);
        PCode.push(11);
        list.push("EMPI");
        CO++;
        indice++;
        PCode.push(variable.getAdresse());
        list.push(variable.getAdresse());
        CO++;
        indice++;
        PCode.push(6);
        list.push("LIRE");
        CO++;
        indice++;

    }

    public void Genecode_AFF(String chaine) {
        Variable variable = (Variable) table.getTable().get(chaine);
        PCode.push(11);
        list.push("EMPI");
        PCode.push(variable.getAdresse());
        list.push(variable.getAdresse());
        CO = CO + 2;
        indice+=2;
    }

    public void Genecode_AFF2() {
        PCode.push(5);
        list.push("AFFE");
        CO = CO + 1;
        indice++;
    }

    public void Genecode_ecriture() {
        PCode.push(7);
        list.push("ECRL");
        indice++;
        CO = CO + 1;
    }

    public void genecode_ECR_EXP() {
        PCode.push(8);
        list.push("ECRE");
        CO = CO + 1;
        indice++;
    }

    public void GENCODE_ECR_EXP2(String chaine) {
        PCode.push(9);
        list.push("ECRC");
        for (int i = 1; i < chaine.length()-1; i++) {
            Integer c =(int) chaine.charAt(i);
            PCode.push(c );
            list.push(chaine.charAt(i));
        }
        PCode.push(10);
        list.push("FNC");
        CO = CO + chaine.length() + 2;
        indice=indice+chaine.length()+2;
    }
public void genecodeTermeConst(String chaine){
    Constante constante = (Constante) table.getTable().get(chaine);
    PCode.push(11);
    list.push("EMPI");
    PCode.push(constante.getValent());
    list.push(constante.getValent());
  CO+=2;
  indice+=2;
}
    public void gencode_Prog(T_Unilex unilex) {
        PCode.push(13);
        list.push("STOP");


    }

    public void genecode_TERME(int nombre) {
        PCode.push(11);
        list.push("EMPI");
        PCode.push(nombre);
        list.push(nombre);
        CO = CO + 2;
        indice+=2;
    }

    public void genecode_TERME(String chaine) {
        Variable variable = (Variable) table.getTable().get(chaine);
        PCode.push(11);
        list.push("EMPI");
        PCode.push(variable.getAdresse());
        list.push(variable.getAdresse());
        PCode.push(12);
        list.push("CONT");
        CO = CO + 3;
        indice=indice+3;
    }

    public void genecode_TERME_Moin() {
        PCode.push(4);
        list.push("MOIN");
        CO = CO + 1;
        indice++;
    }

    public void genecode_OPBIN(T_Unilex operateur) {
        switch (operateur) {
            case plus:
                PilOP.push(0);
                break;
            case moins:
                PilOP.push(1);
                break;
            case mutl:
                PilOP.push(2);
                break;
            case divi:
                PilOP.push(3);
                break;
            default:
                System.out.println("Opérateur non pris en charge : " + operateur);
                break;
        }
        CO++;
        indice++;

    }


    public void genecode_EXP() {
        if (!PilOP.empty()) {
            list.push(PilOP.getFirst());
            PCode.push(PilOP.pop());

            //list.push(d);
            CO++;
            indice++;
        }
    }

   public void INTERPRETER() {
       pile=reverseStack(PCode);
        while (!pile.empty() && !pile.get(pile.size()-1).equals(13) ) {
            switch ( pile.pop() ){
                case 0:
                    Pilex.push(Pilex.pop() + Pilex.pop());
                    CO++;
                    break;
                case 1:
                    int OP1=Pilex.pop();
                    int OP2=Pilex.pop();
                   int t= Pilex.push(OP2 - OP1);
                    CO++;
                    break;
                case 2:
                    int c=Pilex.push(Pilex.pop() * Pilex.pop());
                    CO++;
                    break;
                case 3:
                    OP1=Pilex.pop();
                    OP2=Pilex.pop();
                    if (OP1 == 0) {
                        System.out.println("Erreur d'exécution : division par zéro");

                    } else {

                        Pilex.push(OP2 / OP1);
                        CO++;
                    }
                    break;
                case 4:
                    Pilex.push(-Pilex.pop());
                    CO++;
                    break;
                case 5:
                    Integer[] tempArray = MEM.toArray(new Integer[0]);

                    int c1 = Pilex.pop();
                    int c2 = Pilex.pop();
                    int indice = c2;

                    if (MEM.contains(c2)) {
                        tempArray[indice] = c1;
                        MEM.clear();


                        for (Integer element : tempArray) {
                            MEM.push(element);
                        }}
                    else {
                       // MEM.push(c1);
                        MEM.set(c2,c1);
                    }


                    CO++;
                    break;
                    /*int c1=Pilex.pop();
                    int c2=Pilex.pop();
                    MEM.push(c1);
                    MEM.push(c2);

                    CO++;
                    break;*/
                case 6:
                    Scanner scanner = new Scanner(System.in);
                    int inputNumber = scanner.nextInt();
                    scanner.nextLine();
                    int address = Pilex.pop();
                    MEM.add(address, inputNumber);
                    CO++;
                    break;
                case 7:
                    System.out.println();
                    CO++;
                    break;
                case 8:

                    System.out.println(Pilex.pop());
                    CO++;
                    break;
                case 9:

                    int valeur =pile.pop();
                    char ch=(char)valeur;

                    while (ch != 10) {
                        System.out.print(ch);

                         valeur =pile.pop();
                         ch=(char)valeur;
                    }
              //      CO = CO + i + 1;
                    break;
                case 11:
                    Pilex.push((Integer) pile.pop());
                    CO = CO + 2;
                    break;
                case 12:
                    int adresse = Pilex.pop();
                    int contenu = MEM.get(adresse);
                    Pilex.push(contenu);
                    CO++;

                    break;
              /*  case 14:
                    CO=Pilex.pop();
                    break;
                case 15:
                    if (Pilex.get(Sompilex)==0){
                        CO=pilex.

                    }*/

            }
            CO++;

        }
    }
    public static Stack<Integer> reverseStack(Stack<Integer> stack) {
        Stack<Integer>  pileInverse = new Stack<>();

        while (!stack.isEmpty()) {
            pileInverse.push(stack.pop());
        }

        return pileInverse;
    }

    public boolean definirVariable(String nom) {
        Variable variable = new Variable();
        if (table.chercher(nom)) {
            System.out.println("Erreur : identificateur déjà déclaré");
            return false;
        } else {
            variable.setNom(nom);
            variable.setType("variable");
            DERNIERE_ADRESSE_VAR_GLOB++;
            variable.setAdresse(DERNIERE_ADRESSE_VAR_GLOB);
            table.Inserer(variable);
            table.afficherTable();
            return true;
        }
    }

    public void CREER_FICHIER_CODE(String Fichier, List<Object> codeGenere) throws IOException {
        String FichierGenere = Fichier.replace(".MP", ".COD");
        FileWriter writer = new FileWriter(FichierGenere);
        for (Object instruction : codeGenere) {
            writer.write(instruction + "\n");
        }
        writer.close();
    }


    public boolean DEFINIR_CONSTANTE(String nom, T_Unilex unilex) {
        Constante constante = new Constante();
        if (table.chercher(nom)) {
            System.out.println("Erreur : identificateur déjà déclaré");

            return false;
        } else {
            constante.setType("constante");
            constante.setNom(nom);
            if (unilex == T_Unilex.ent) {
                constante.setTypc(0);
                constante.setValent(analyseLexicale.getNombre());
            } else {
                constante.setTypc(1);
                NB_Const_Chaine = Val_de_Contante.size();
                Val_de_Contante.add(NB_Const_Chaine, analyseLexicale.getChaine());
                constante.setValch(Val_de_Contante.get(NB_Const_Chaine));
            }
            table.Inserer(constante);
            table.afficherTable();
            return true;
        }
    }
   /* public boolean INSTRUCTION() throws IOException {
        return  ECRITURE() || LECTURE() || AFFECTATION() ||BLOC();
   }*/

   public boolean INSTRUCTION() throws IOException {
            return INST_NON_COND() || INST_COND();
        }
    private boolean INST_NON_COND() throws IOException {
        return AFFECTATION() || LECTURE() || ECRITURE() || BLOC() || INST_REPE();
    }
    public boolean EXP1() throws IOException {
        if (TERME()) {
            if (SUITE_TERME1()) {
                genecode_EXP();
                return true;
            } else {
                return false;
            }
        } else {

            return false;
        }
    }
    public boolean SUITE_TERME1() throws IOException {
        if (unilex!=T_Unilex.motcle || !analyseLexicale.getChaine().equals("ALORS")) {
            if (OP_BIN()) {
                return EXP1();
            } else {

                return false;
            }
        }
        return true;
    }
    public boolean SUITE_TERME2() throws IOException {
        if (unilex!=T_Unilex.motcle || !analyseLexicale.getChaine().equals("FAIRE")) {
            if (OP_BIN()) {
                return EXP2();
            } else {

                return false;
            }
        }
        return true;
    }

    public boolean EXP2() throws IOException {
        if (TERME()) {
            if (SUITE_TERME2()) {
                genecode_EXP();
                return true;
            } else {
                return false;
            }
        } else {

            return false;
        }
    }


    private boolean INST_REPE() throws IOException {
            if (analyseLexicale.getChaine().equals("TANTQUE")) {
                unilex = analyseLexicale.ANALEX();

                if (EXP2() ) {
                    //unilex=analyseLexicale.ANALEX();
                    if (analyseLexicale.getChaine().equals("FAIRE")) {
                        unilex = analyseLexicale.ANALEX();

                        if (INSTRUCTION()) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    private boolean INST_COND() throws IOException {
        if (analyseLexicale.getChaine().equals("SI")) {
            unilex = analyseLexicale.ANALEX();

            if (EXP1()) {
               // unilex=analyseLexicale.ANALEX();
                if (analyseLexicale.getChaine().equals("ALORS")) {
                    unilex = analyseLexicale.ANALEX();

                    if (INST_COND() || INST_NON_COND()) {
                        if (analyseLexicale.getChaine().equals("SINON")) {
                            unilex = analyseLexicale.ANALEX();
                            if (INSTRUCTION()) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    public boolean AFFECTATION() throws IOException {
        if (unilex == T_Unilex.ident) {
            String nom_identificateur = analyseLexicale.getChaine();
            Identificateur identificateur = table.getTable().get(nom_identificateur);
            unilex = analyseLexicale.ANALEX();
            if (table.chercher(nom_identificateur) ){
             if(identificateur.getType().equals("variable")) {
                Genecode_AFF(nom_identificateur);
                if (unilex == T_Unilex.aff) {
                    unilex = analyseLexicale.ANALEX();
                    if (EXP()) {
                        Genecode_AFF2();
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
               return false;
            }
        } return false;
    }

    public boolean LECTURE() throws IOException {
        boolean fin;
        boolean erreur;
        if (unilex == T_Unilex.motcle && analyseLexicale.getChaine().equals("LIRE")) {
            unilex = analyseLexicale.ANALEX();
            if (unilex == T_Unilex.parouv) {
                unilex = analyseLexicale.ANALEX();
                if (unilex == T_Unilex.ident) {
                    String nom_identificateur = analyseLexicale.getChaine();
                    Identificateur identificateur = table.getTable().get(nom_identificateur);
                    if (!table.chercher(nom_identificateur) ){return false;}
                    else if (!identificateur.getType().equals("variable")) {
                        return false;
                    }else{                     GENCODE_LECTURE(nom_identificateur);

                        unilex = analyseLexicale.ANALEX();}
                    fin = false;
                    erreur = false;
                    do {
                        if (unilex == T_Unilex.virg) {
                            unilex = analyseLexicale.ANALEX();
                            if (unilex == T_Unilex.ident) {
                                String nom_identificateur1 = analyseLexicale.getChaine();
                                 if (!table.chercher(nom_identificateur1)){
                                    return false;
                                 }
                                 else {
                                     Identificateur identificateur1 = table.getTable().get(nom_identificateur1);

                                     if (!identificateur1.getType().equals("variable")) {
                                         System.out.println("Erreur : L'identificateur '" + nom_identificateur1 + "' n'est pas une variable déclarée.");
                                         return false;
                                     }

                                 else {                        GENCODE_LECTURE(nom_identificateur1);

                                     unilex = analyseLexicale.ANALEX();}}
                            } else {
                                fin = true;
                                erreur = true;
                            }
                        } else
                            fin = true;
                    } while (!fin);
                    if (erreur) {
                        return false;
                    } else if (unilex == T_Unilex.parfer) {
                        unilex = analyseLexicale.ANALEX();
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    public boolean ECRITURE() throws IOException {
        boolean fin;
        boolean erreur;

        if (unilex == T_Unilex.motcle && analyseLexicale.getChaine().equals("ECRIRE")) {
            unilex = analyseLexicale.ANALEX();
            if (unilex == T_Unilex.parouv) {
                unilex = analyseLexicale.ANALEX();
                if (unilex == T_Unilex.parfer) {
                    Genecode_ecriture();
                    unilex = analyseLexicale.ANALEX();

                    return true;
                } else {
                    erreur = false;
                    if (ECR_EXP()) {
                        fin = false;
                        do {
                            if (unilex == T_Unilex.virg) {
                                unilex = analyseLexicale.ANALEX();
                                System.out.println(unilex);
                                erreur = !ECR_EXP();
                                if (erreur) {
                                    fin = true;
                                }
                            } else {
                                fin = true;
                            }
                        } while (!fin);
                        if (erreur) {
                            return false;
                        } else if (unilex == T_Unilex.parfer) {
                            unilex = analyseLexicale.ANALEX();
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    public boolean ECR_EXP() throws IOException {
        if( unilex == T_Unilex.ch ){
            unilex=analyseLexicale.ANALEX();
            GENCODE_ECR_EXP2(analyseLexicale.getChaine());
            return true ;}
        else{
            boolean exp = EXP();
            if(exp)
            { genecode_ECR_EXP();}
            return exp;
        }
    }

   public boolean EXP() throws IOException {
       if (TERME()) {
           if (SUITE_TERME()) {
              genecode_EXP();
               return true;
           } else {
               return false;
           }
       } else {

           return false;
       }
   }


    public boolean TERME() throws IOException {
        if (unilex == T_Unilex.ent) {
            genecode_TERME(analyseLexicale.getNombre());
            unilex = analyseLexicale.ANALEX();
            return true;
        } else if (unilex == T_Unilex.ident) {
            String nom_identificateur = analyseLexicale.getChaine();
            Identificateur identificateur = table.getTable().get(nom_identificateur);
            if (table.chercher(nom_identificateur)) {
                if (identificateur.getType().equals("constante")) {
                    Constante constante = (Constante) identificateur;
                    int nombre = constante.getValent();
                    if (constante.getTypc() == 0) {
                        genecode_TERME(nombre);
                    } else {
                        System.out.println("Erreur : L'identificateur '" + nom_identificateur + "' n'est pas une constante entière.");
                        return false;
                    }
                } else if (identificateur.getType().equals("variable")) {
                    genecode_TERME(nom_identificateur);
                }
            } else {
                return false;
            }
            unilex = analyseLexicale.ANALEX();
            return true;
        } else if (unilex == T_Unilex.parouv) {
            unilex = analyseLexicale.ANALEX();
            if (EXP()) {
                if (unilex == T_Unilex.parfer) {
                    unilex = analyseLexicale.ANALEX();
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else if (unilex == T_Unilex.moins) {
            genecode_TERME_Moin();
            unilex = analyseLexicale.ANALEX();
            return TERME();
        } else {
            return false;
        }
    }



    public boolean BLOC() throws IOException {
        if (analyseLexicale.getChaine().equals("DEBUT")) {
            unilex = analyseLexicale.ANALEX();

            if (INSTRUCTION()) {
                while (unilex == T_Unilex.ptvirg) {
                    unilex = analyseLexicale.ANALEX();
                    if (!INSTRUCTION()) {
                        return false;
                    }
                }

                if (analyseLexicale.getChaine().equals("FIN")) {
                   unilex = analyseLexicale.ANALEX();
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }



    public boolean PROG() throws IOException {
       if (analyseLexicale.getChaine().equals("PROGRAMME")) {
           unilex = analyseLexicale.ANALEX();

           if (unilex == T_Unilex.ident) {
               unilex = analyseLexicale.ANALEX();

               if (unilex == T_Unilex.ptvirg) {
                   unilex = analyseLexicale.ANALEX();

                   if (analyseLexicale.getChaine().equals("CONST")) {
                       if (!DECL_CONST()) {
                           return false;
                       }
                   }

                   if (analyseLexicale.getChaine().equals("VAR")) {
                       if (!DECL_VAR()) {
                           return false;
                       }
                   }

                   if (!BLOC()) {
                       return false;
                   }

                   if (unilex == T_Unilex.point) {
                       gencode_Prog(unilex);
                    //   unilex = analyseLexicale.ANALEX();
                       return true;
                   } else {
                       return false;
                   }
               } else {
                   return false;
               }
           } else {
               return false;
           }
       } else {
           return false;
       }
   }

    public boolean DECL_VAR() throws IOException {
        String nom_Variable;
        if (unilex==T_Unilex.motcle&&analyseLexicale.getChaine().equals("VAR")) {
            unilex = analyseLexicale.ANALEX();
            if (unilex == T_Unilex.ident) {
                nom_Variable=analyseLexicale.getChaine();
                 if (definirVariable(nom_Variable))
                        unilex = analyseLexicale.ANALEX();
                while (unilex == T_Unilex.virg) {
                    unilex = analyseLexicale.ANALEX();
                    if (unilex == T_Unilex.ident) {
                        nom_Variable=analyseLexicale.getChaine();
                        if (definirVariable(nom_Variable))
                            unilex = analyseLexicale.ANALEX();
                    } else {
                        return false;
                    }
                }
                if (unilex == T_Unilex.ptvirg) {
                    unilex = analyseLexicale.ANALEX();
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public  boolean DECL_CONST() throws IOException {

       String nom_constante;

       if (unilex == T_Unilex.motcle && analyseLexicale.getChaine().equals("CONST")) {
           unilex = analyseLexicale.ANALEX();
           if (unilex == T_Unilex.ident) {
               nom_constante = analyseLexicale.getChaine();
               unilex = analyseLexicale.ANALEX();
               if (unilex == T_Unilex.eg) {
                   unilex=analyseLexicale.ANALEX();
                   if (unilex == T_Unilex.ent || unilex == T_Unilex.ch) {
                       if (DEFINIR_CONSTANTE(nom_constante, unilex)) {
                           unilex = analyseLexicale.ANALEX();

                           while (unilex == T_Unilex.virg){
                                   unilex = analyseLexicale.ANALEX();
                                   if (unilex == T_Unilex.ident) {
                                       nom_constante = analyseLexicale.getChaine();
                                       unilex = analyseLexicale.ANALEX();
                                       if (unilex == T_Unilex.eg) {
                                          unilex=analyseLexicale.ANALEX();
                                           if (unilex == T_Unilex.ent || unilex == T_Unilex.ch) {
                                               if (DEFINIR_CONSTANTE(nom_constante, unilex)) {
                                                   unilex = analyseLexicale.ANALEX();
                                               } else {return false;
                                             }
                                           } else {return false;
                                           }
                                       } else {
                                           return false;
                                       }
                                   } else {
                                       return false;
                                   }}
                           if (unilex == T_Unilex.ptvirg) {
                               unilex = analyseLexicale.ANALEX();
                               return true;
                           } else {
                               return false;
                           }
                       } else {
                           return false;
                       }
                   } else {
                       return false;
                   }
               } else {
                   return false;
               }
           } else {
               return false;
           }
       } else {
           return false;
       }
   }

    public boolean OP_BIN() throws IOException {
        if (unilex == T_Unilex.plus ||unilex == T_Unilex.moins||
                unilex == T_Unilex.mutl || unilex == T_Unilex.divi) {
            genecode_OPBIN(unilex);
            unilex=analyseLexicale.ANALEX();

            return true;
        } else {
            return false;
        }
    }

  public boolean SUITE_TERME() throws IOException {
      if (unilex != T_Unilex.parfer && unilex != T_Unilex.virg && unilex != T_Unilex.ptvirg) {
          if (OP_BIN()) {
              return EXP();
          } else {

              return false;
          }
      }
      return true;
  }

    public void ANASYNT() throws IOException {

        unilex = analyseLexicale.ANALEX();
        if (PROG()) {

            System.out.println("Le programme source est syntaxiquement correct" );
        } else {
           analyseLexicale.ERREUR(4);
        }
    }
    public  void INITIALISER() {
        analyseLexicale = new AnalyseLexicale();
        DERNIERE_ADRESSE_VAR_GLOB=-1;
        NB_Const_Chaine=0;

    }


}


