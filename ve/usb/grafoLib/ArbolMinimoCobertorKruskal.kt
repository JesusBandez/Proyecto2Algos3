package ve.usb.grafoLib
import java.util.PriorityQueue

// Determina el AMC del grafo suministrado basandose en CD
public class ArbolMinimoCobertorKruskal(val g: GrafoNoDirigido) {

    // Se crea el conjunto vacio para el Arbol
    var arbol : MutableList<Arista> = mutableListOf()

    // Al crear este objeto se aplica la operacion make set para todos los vertices del grafo
    //var conjuntosDisjuntos = ConjuntosDisjuntos(g.obtenerNumeroDeVertices()
    var conjuntosDisjuntos = ConjuntosDisjuntos(g.obtenerNumeroDeVertices())
    

    // Ordena las aristas de g segun su peso
    var aristas = g.aristas().sortedBy { it.peso }
    
    
    
    // Variable donde se almacena la suma de los pesos de las aristas del Arbol Minimo Cobertor
    var pesos : Double = 0.0

    
    init{
        // Se recorren cada arista y se van añadiendo al arbol si no se encuentran en la misma componente conexa
        for (arista in aristas){
            var u: Int = arista.cualquieraDeLosVertices()
            var v: Int = arista.elOtroVertice(u)
            if (conjuntosDisjuntos.encontrarConjunto(u) != conjuntosDisjuntos.encontrarConjunto(v)){
                arbol.add(arista)
                conjuntosDisjuntos.union(u,v)
                pesos+=arista.peso()
            }
        }
        // Tiempo de ejecucion : O(|E|log|V|)
    }

    // Retorna un objeto iterable que contiene los lados del árbol mínimo cobertor.
    fun obtenerLados() : Iterable<Arista> {
        return arbol
        // Tiempo de ejecucion : O(1), solo se retorna un valor
    }
    
    // Retorna el peso del árbol mínimo cobertor. El cual es la suma del peso de cada lado del mismo
    fun obtenerPeso() : Double {
        return pesos
        // Tiempo de ejecucion : O(1), solo se retorna un valor
    }
}
