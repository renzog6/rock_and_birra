package ar.nex.app;

/**
 *
 * @author Renzo
 */
public enum Menu {

    CAJA(1),
    ARTICULO(2);

    private int codigo;
    

    Menu(int i) {
        this.codigo = i;    
    }
    
    public int codigo(){
        return codigo;
    }

}
