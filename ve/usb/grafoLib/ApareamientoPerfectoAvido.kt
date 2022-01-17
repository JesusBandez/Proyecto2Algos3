
// Algoritmo 2
package ve.usb.grafoLib
import java.util.PriorityQueue

// Implementacion de la heuristica avida para obtener el apareamiento perfecto de un grafo G
// Se recibe de entrada un grafo no dirigido con numero par de vertices 
// De salida el algoritmo devuelve una lista m, con un conjunto de lados que representan el apareamiento perfecto de G
class ApareamientoPerfectoAvido(val g:GrafoNoDirigido){


    //Variables para almacenar datos usados por el algoritmo
    var m : MutableList<Arista> = mutableListOf()
    var v2 : MutableList<Int> = mutableListOf()
    val l: PriorityQueue<Arista>

    init{

        //Se verifica que el grafo g tenga un numero par de vertices
        //Se lanza una excepcion si el numero de vertices es impar
        if( g.obtenerNumeroDeVertices() % 2 != 0){
            throw RuntimeException("El numero de vertices del grafo no es par")
        }

        // Se añade a la lista v2 todos los vertices de v
        for (vertice in 0..g.obtenerNumeroDeVertices()-1){
            v2.add(vertice)
        }

        // Se crea una cola de prioridad para almacenar los lados del grafo
        // Los lados se ordenan de forma ascendente segun su peso
        // El tope de la cola de prioridad es siempre el lado con menor peso
        val compareByCost: Comparator<Arista> = compareBy { it.peso }
        l = PriorityQueue<Arista>(compareByCost)

        // Se añaden las aristas del grafo a la cola de prioridad
        for (arista in g.aristas()){
            
            l.add(arista)
        }

        
        while (v2.size!=0){

            // Se remueve el tope de la cola de prioridad y se asigna a la variable arista
            var arista : Arista = l.remove()


            // Se asignan a las variables i y j cada vertice de la arista
            var i : Int = arista.cualquieraDeLosVertices()
            var j : Int = arista.elOtroVertice(arista.cualquieraDeLosVertices())

            // Se verifica si los vertices de la arista se encuentran en la lista v2
            if (v2.contains( i ) && v2.contains( j )){

                //añadir el lado a M
                m.add(arista)

                // remover de V' i y j
                v2.remove(i)
                v2.remove(j)
            }
        }
    }
    // Funcion para retornar la lista con el apareamiento de G
    fun  obtenerApareamiento() : Iterable<Arista>{
        return m
    }
    // falta retornar M
}