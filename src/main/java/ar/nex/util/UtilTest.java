package ar.nex.util;

import ar.nex.articulo.Articulo;
import ar.nex.stock.Stock;

/**
 *
 * @author Renzo
 */
public class UtilTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
                 
        Articulo a = new Articulo();
        
         
        //Integer i = new GetPK().Ultimo(a.getClass());
        
        GetPK pk = new GetPK();
        Integer i = pk.Nuevo(Articulo.class);
        
        System.out.println("ar.nex.util.UtilTest.main() : " + i );
    }
    
}
