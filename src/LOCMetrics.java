/**
 * Classe de donnée pour contenir des métriques sur la taille des classes
 * et des paquets, ainsi que sur leur commentaires.  Cette classe est
 * réutilisée pour les deux (classe et paquet) puisque les métriques sont
 * calculées de la même façon à une exception près.
 */
class LOCMetrics{
    private String name; //nom de la classe/du paquet
    private boolean is_package; //vrai si un paquet
    private int loc; //nb de lignes de codes
    private int cloc; //nb de lignes de commentaires
    private float dc; //densité de commentaires cloc/loc
    private int wmc; //weighted methods per class
    private float bc; // hautsi bien commenté : dc/wmc

    /**
     * Seul constructeur de la classe.
     * @param name le nom de l'entitée mesurée (pas un chemin)
     * @param is_package vrai si l'entité est un paquet, faux si une classe
     * @param loc lignes de code de l'entité
     * @param cloc lignes de comentaires de l'entité
     * @param wmc wighted methods per class, avec complexité de McCabe
     */
    public LOCMetrics(String name, boolean is_package, int loc, int cloc, int wmc){
        this.name = name;
        this.is_package = is_package;
        this.loc = loc;
        this.cloc = cloc;
        this.dc = (float)(cloc)/loc;
        this.wmc = wmc;
        this.bc = dc/wmc;
    }

    // @Override
    // public String toString(){
    //     String res = "";
    //     res += "Name: " + name + '\n'
    //         + "Is a package: " + is_package  + '\n'
    //         + "Lines of comments: " + cloc + '\n'
    //         + "Comment density: " + dc + '\n'
    //         + "Weighted Methods per Entity: " + wmc + '\n'
    //         + "Comment score: " + bc;

    //     return res;
    // }

    @Override
    public String toString(){
        return  name + "," + name + "," + loc + "," + cloc + "," + dc + "," + wmc + "," + bc + "\n";
    }

    public float getDc() {
        return dc;
    }

    public int getCloc() {
        return cloc;
    }

    public int getLoc() {
        return loc;
    }

    public int getWmc(){
        return wmc;
    }

    public float getBc(){
        return bc;
    }

    public boolean getIsPackage(){
        return is_package;
    }

    public void setBc(float bc) {
        this.bc = bc;
    }

    public void setCloc(int cloc) {
        this.cloc = cloc;
    }

    public void setLoc(int loc) {
        this.loc = loc;
    }
}