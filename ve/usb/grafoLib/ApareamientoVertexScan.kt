
// Algoritmo 3
package ve.usb.grafoLib

// Implementacion de la heuristica Vertex-Scan para obtener el apareamiento perfecto de un grafo G
// Se recibe de entrada un grafo no dirigido con numero par de vertices 
// De salida el algoritmo devuelve una lista m, con un conjunto de lados que representan el apareamiento perfecto de G
class ApareamientoVertexScan(val g:GrafoNoDirigido){

    

    //Variables para almacenar datos usados por el algoritmo
    var v2 : MutableList<Int> = mutableListOf()
    var e2 : MutableList<Arista> = mutableListOf()
    val m : MutableList<Arista> = mutableListOf()

    

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

        // Se añaden todas las arista de g a la variable e2
        e2 = g.aristas() as MutableList

        // Se ordenan las aristas por costo, esto significa que si tengo el vertice i y quiero encontrar la arista (i,j)
        // con menor costo, este lado sera el primero que contenga a i
        
        e2.sortBy{it.peso}
        
        while (v2.size>0){
            
            
            //Escoger vertice aleatorio
            var i : Int = v2.random()
            lateinit var aristaMin : Arista

            // Se busca en e2 la primera arista en la que incida i
            for (arista in e2){
                if (arista.cualquieraDeLosVertices() == i 
                    || arista.elOtroVertice(arista.cualquieraDeLosVertices()) == i){
                    aristaMin = arista
                    break
                }
            }

            // Se agrega a la variable j el valor del otro vertice de la arista
            var j = aristaMin.elOtroVertice(i)

            // Se añade a la lista m la arista encontrada
            m.add(aristaMin)

            // Se remueven de v2 los vertices i y j
            v2.remove(i)
            v2.remove(j)
            
            //Eliminar todos los lados que tengan como adyacentes a i y j
            e2.removeAll{it.cualquieraDeLosVertices() == i || it.elOtroVertice(it.cualquieraDeLosVertices()) == i
                || it.cualquieraDeLosVertices() == j || it.elOtroVertice(it.cualquieraDeLosVertices()) == j}
                   
        }
    }

    // Funcion para retornar la lista con el apareamiento de G   
    fun  obtenerApareamiento() : Iterable<Arista>{
        return m
    }
}