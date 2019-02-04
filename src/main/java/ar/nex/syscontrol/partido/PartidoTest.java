package ar.nex.syscontrol.partido;

import ar.nex.syscontrol.cliente.Cliente;
import ar.nex.syscontrol.cliente.ClienteJpaController;
import java.util.Collection;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Renzo
 */
public class PartidoTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("SysControl-PU");
        
        PartidoJpaController sPartido = new PartidoJpaController(factory);
        ClienteJpaController sCliente = new ClienteJpaController(factory);
        
        Partido p = new Partido();
        p.setNombre("Partido X");
        p.setFecha("2018/01/22");
        p.setHora("22.00");
        sPartido.create(p);
        p = sPartido.findLastPartido();
        System.err.println(" Partido ID: " + p.getId());
        
        //p = sPartido.findPartido(2);
        
        Collection<Cliente> clienteCollection;
        clienteCollection = sCliente.findClienteEntities();
        for (Cliente cliente : clienteCollection) {
            System.out.println("Cliente: " + cliente.toString());
            //p.getClienteCollection().add(cliente);
        }
        
        p.setClienteCollection(clienteCollection);
        sPartido.edit(p);
        

    }

}
