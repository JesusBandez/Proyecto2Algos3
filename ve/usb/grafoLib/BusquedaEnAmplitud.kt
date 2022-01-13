package ve.usb.grafoLib

import java.util.Queue
import java.util.LinkedList

// Clase nodo para facilitar el control de los datos de cada vertice a la hora de ejecutar
// los algoritmos BFS y DFS
data class Node(var color: Color, 
                var distancia: Int = Int.MAX_VALUE, 
                var predecesor: Int = -1,
                var ti : Int = 0,
                var tf : Int = 0);



// Implementacion de la Busqueda en Amplitud, se realiza la busqueda desde el vertice s
// y se encuentra la distancia a la que está cada nodo al que se pueda llegar desde s
// si no hay camino entre el nodo s y otro nodo v, la distancia del nodo v es infinita
public class BusquedaEnAmplitud(val g: Grafo, val s: Int) {
    
    // Lista donde se almacenaran los nodos que representan cada vertice del grafo con las
    // propiedades de tiempo, distancia, color y predecesor
    var nodos : MutableList<Node> = mutableListOf()

    init {

        // Precondicion
        // El vertice s debe pertenecer al grafo        
        if(!g.lados.contains(s)){
            throw RuntimeException("El elemento no se encuentra en el grafo")
        }

        // Se les asigna el color blanco a todos los nodos para indicar que no han sido explorados
        for (i in 0..g.obtenerNumeroDeVertices()-1){
            nodos.add(Node(Color.BLANCO))
        }

        // Se le asigna la distancia 0 al nodo s ya que el mismo es desde donde se ejecuta la BFS
        nodos[s].distancia = 0

        // Crea una cola apoyandose en la libreria de listas enlazadas
        val cola : Queue<Int> = LinkedList<Int>()

        // Se añade el elemento s a la cola
        cola.add(s)

        // Mientras que queden elementos en la cola, se ejecuta el BFS
        while (cola.size!=0) {
            
            // El metodo poll() remueve el primer elemento de la cola y lo retorna
            var u = cola.poll()

            // Se verifica cuales nodos faltan por visitar y se trabaja sobre ellos
           for (ady in g.adyacentes(u)) {
               if (nodos[ady.elOtroVertice(u)].color == Color.BLANCO){
                   nodos[ady.elOtroVertice(u)].color = Color.GRIS
                   nodos[ady.elOtroVertice(u)].distancia = nodos[u].distancia + 1
                   nodos[ady.elOtroVertice(u)].predecesor = u
                   cola.add(ady.elOtroVertice(u))
               }
           }
           // Se marca con negro los nodos ya explorados
           nodos[u].color = Color.NEGRO
        }
        // Tiempo de ejecucion : O(V+E) donde V son los vertices y E son los lados del grafo
    }
    

    
    // Retorna el predecesor del vertice v, es decir, el vertice al cual v es adyacente
    // Si no tiene predecesor se retorna -1    
    fun obtenerPredecesor(v: Int) : Int { 
        if(!g.lados.contains(v)){
            throw RuntimeException("El elemento no se encuentra en el grafo")
        }
        return nodos[v].predecesor
        // Tiempo de ejecucion : O(n) se devuelve solamente un valor
    }

    
    // Retorna la distancia desde el nodo s hasta v.
    // Al aplicar BFS se garantiza que esta distancia es la minima posible
    fun obtenerDistancia(v: Int) : Int { 

        if(!g.lados.contains(v)){
            throw RuntimeException("El elemento no se encuentra en el grafo")
        }

        return nodos[v].distancia
        // Tiempo de ejecucion : O(n) se devuelve solamente un valor
    }

    
    // Se retorna true si existe un camino desde s hasta v
    // Esto se hace viendo si la distancia de v no es infinita
    fun hayCaminoHasta(v: Int) : Boolean {

        if(!g.lados.contains(v)){
            throw RuntimeException("El elemento no se encuentra en el grafo")
        }

        if (nodos[v].distancia<Int.MAX_VALUE){
            return true
        }else{
            return false
        }

        // Tiempo de ejecucion : O(n) se devuelve solamente un valor
    }

    
    
    // Retorna el camino con menos lados desde s hasta v
    // Si hay camino desde s hasta v entonces basta con ir revisando los predecesores de v hasta llegar a s
    fun caminoConMenosLadosHasta(v: Int) : Iterable<Int> {  

        if (!hayCaminoHasta(v)){
            throw RuntimeException("No hay camino desde $s hasta $v")
        }

        if(!g.lados.contains(v)){
            throw RuntimeException("El elemento no se encuentra en el grafo")
        }
        var camino : MutableList<Int> = mutableListOf(v)
        var actual : Int = v
        while (actual != s) {
            camino.add(nodos[actual].predecesor)
            actual = nodos[actual].predecesor
        }

        return camino.reversed()
    }
    // Tiempo de ejecucion : O(V)  donde V son todos los vertices del grafo en el peor caso
}
