package ve.usb.grafoLib
import java.util.PriorityQueue

// clase para guardar las propiedades de los nodos del AMC
data class NodoPrim(var key: Int = Int.MAX_VALUE, 
                    var predecesor: Int = -1);

public class ArbolMinimoCobertorPrim(val g: GrafoNoDirigido, val r : Int) {

    var nodos : MutableList<NodoPrim> = mutableListOf()
    
    var lista : PriorityQueue<Int> = PriorityQueue<Int>()

    var aristas = g.aristas()

    var pesos = 0.0

    var arbol : MutableSet<Arista> = mutableSetOf()

    init{

        for (v in 0..g.lados.size-1){
            nodos.add(NodoPrim())
        }
        // Se le asigna la clave 0 al nodo raiz desde donde se ejecutara el algoritmo
        nodos[r].key = 0

        var lados = g.lados.toMutableList()

        // Se añaden los vertices a la cola de prioridad
        
        for (i in lados){
            lista.add(i)
        }
    
        while (lista.size>0){
            
            var u = lista.remove()
    
            for (v in g.adyacentes(u)){
                if (lista.contains(v.elOtroVertice(u)) && v.peso < nodos[v.elOtroVertice(u)].key){

                    arbol.add(v)
                    pesos += v.peso
                    nodos[v.elOtroVertice(u)].predecesor = u
                    nodos[v.elOtroVertice(u)].key = v.peso.toInt()
                    
                }
            }
        }
        // Tiempo de ejecucion : O(|E|log|V|)
    }
    

    // Retorna un objeto iterable que contiene los lados del árbol mínimo cobertor.
    fun obtenerLados() : Iterable<Arista> {
        return arbol
        // Tiempo de ejecucion : O(1), solo se retorna un valor
    }
    
    // Retorna el peso del árbol mínimo cobertor. 
    fun obtenerPeso() : Double {
        return pesos
        // Tiempo de ejecucion : O(1), solo se retorna un valor
    }
}
