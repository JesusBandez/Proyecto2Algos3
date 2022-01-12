package ve.usb.grafoLib

// Clase para hallar los valores de excentricidad, diametro, radio y centro de un grafo no dirigido
public class MetricasDeGrafo (val g: GrafoNoDirigido){

    // Precondicion : de entrada g debe ser un grafo no dirigido

    // Obtiene la excentricidad de un vertice s , es decir, entre los vertices alcanzables desde s el que tiene una distancia mayor
    // Para hacerlo añade a una lista las distancias de los vertices alcanzables desde s y retorna la mayor
    fun excentricidad(s: Int) : Int {

        // Precondicion : s es un numero natural
        // Postcondicion : se retorna el valor del camino mas largo desde s, como se usa BFS se garantiza que este es el camino mas
        // corto desde s hasta el vertice encontrado
        
        val bfs = BusquedaEnAmplitud(g,s)
        var nodos = bfs.nodos
        // lista donde se almacenan las distancias de cada vertice desde s
        var vertices : MutableList<Pair<Int,Int>> = mutableListOf()

        for ((indice,nodo) in nodos.withIndex()){
            if (nodo.distancia != Int.MAX_VALUE){
                vertices.add(Pair(indice,nodo.distancia))
            }
        }
        vertices.sortByDescending{it.second}

        // Tiempo de ejecucion : O(V + E) donde V es el numero de vertices de g y E es el numero de lados
        
        return vertices[0].second
    } 
    
    // Obtiene el diametro del grafo g buscando la excentricidad de todos sus vertices y retornando el mayor valor
    fun diametro() : Int {

        // Precondicion : ninguna
        // Postcondicion : el valor obtenido es el mayor valor de excentricidad que se pueda obtener en el grafo

        var excentricidades : MutableList<Pair<Int,Int>> = mutableListOf()
        
        for (i in 0 until g.obtenerNumeroDeVertices()){
            var exc = excentricidad(i)
            excentricidades.add(Pair(i,exc))
        }
        excentricidades.sortByDescending{it.second}
        
        // Tiempo de ejecucion : O(V + E) donde V es el numero de vertices de g y E es el numero de lados

        return excentricidades[0].second
    }

    // Obtiene el diametro del grafo g buscando la excentricidad de todos sus vertices y retornando el menor valor
    fun radio() : Int {

        // Precondicion : ninguna
        // Postcondicion : el valor obtenido es el menor valor de excentricidad que se pueda obtener en el grafo

        var excentricidades : MutableList<Pair<Int,Int>> = mutableListOf()
        for (i in 0 until g.obtenerNumeroDeVertices()){
            var exc = excentricidad(i)
            excentricidades.add(Pair(i,exc))
        }
        excentricidades.sortBy{it.second}

        // Tiempo de ejecucion : O(V + E) donde V es el numero de vertices de g y E es el numero de lados
        
        return excentricidades[0].second
    } 
    
    // Retorna el vértice centro de un grafo, que es el vertice desde donde se obtiene el valor del radio
    fun centro() : Int {

        // Precondicion : ninguna
        // Postcondicion : el vertice obtenido es el vertice desde el cual se obtiene el valor del centro

        var excentricidades : MutableList<Pair<Int,Int>> = mutableListOf()
        for (i in 0 until g.obtenerNumeroDeVertices()){
            var exc = excentricidad(i)
            excentricidades.add(Pair(i,exc))
        }

        excentricidades.sortBy{it.second}
        
        // Tiempo de ejecucion : O(V + E) donde V es el numero de vertices de g y E es el numero de lados
        return excentricidades[0].first
    } 

    // Retorna el valor de la suma de todas las distancias entre los vertices de g, como se usa BFS se garantiza que son las distancias mas cortas
    fun indiceWiener() : Int {

        // Precondicion : ninguna
        // Postcondicion : el valor obtenido es la suma de las menores distancias entre cada vertice del grafo

        var listaBFS : MutableList<BusquedaEnAmplitud> = mutableListOf()
        var distancias : MutableList<Int> = mutableListOf()

        for (i in 0 until g.obtenerNumeroDeVertices()){
            listaBFS.add(BusquedaEnAmplitud(g,i))
        }

        for (i in listaBFS){
            for (nodo in i.nodos){
                distancias.add(nodo.distancia)
            }
        }

        // Tiempo de ejecucion : O(V * V) donde V es el numero de vertices del grafo
        return distancias.sum()
    }  
}

