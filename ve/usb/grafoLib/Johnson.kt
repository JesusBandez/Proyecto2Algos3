package ve.usb.grafoLib

// Implementacion del Algoritmo de Johnson para hallar los CCM entre todos los pares de vertices de un grafo
// El algoritmo se apoya a su vez en los algoritmos de BellmanFord, usando sus resultados para hallar un grafo equivalente G'
// con lados no negativos en caso de tenerlos para luego llamar al Algoritmo de Dijkstra
// Es una buena opcion para grafos dispersos y es mas rapido y consume menos espacio que el algoritmo de Floyd-Warshall
public class Johnson(val g: GrafoDirigido) {

    // Precondicion : g debe ser un digrafo
    // Postcondicion : se obtienen los CCM entre todos los vertices de G en caso de que este no tengo ciclos negativos

    // Variables para almacenar los datos a usar en el algoritmo
    var g1 : GrafoDirigido
    var bellman : BellmanFord
    var s : Int
    var h : MutableList<Double> = mutableListOf()
    var distancias : Array<Array<Double>>
    var ciclo : Boolean
    var listaDijkstra : MutableList<Dijkstra> = mutableListOf()

    init{
        
        // El vertice s que se añadirá al grafo tendra el valor del numero de lados +1 del grafo original
        s = g.obtenerNumeroDeLados()

        // Se crea el nuevo grafo G' con V + 1 vertices y se le añaden todos los arcos de G
        g1 = GrafoDirigido(s)
        for (arco in g.arcos()){
            g1.agregarArco(arco)
        }
        // Tiempo de esta operacion : O(E) donde E es el numero de arcos de G

        // Posteriormente se le añaden arcos con coste 0 desde el nuevo vertice s hasta todos los demas vertices
        for (v in 0..g.obtenerNumeroDeLados()-1){
            g1.agregarArco(Arco(s,v,0.0))
        }
        // Tiempo de esta operacion : O(E) donde E es el numero de arcos de G

        // Se llama al algoritmo de BellmanFord con G' y el vertice s para obtener los pesos h(v)
        // Que representan el costo de ir desde s hasta v
        bellman = BellmanFord(g1,s)
        // Tiempo de esta operacion : O(VE) donde E es el numero de arcos de G y V el numero de vertices

        // Si se detecta un ciclo negativo no se puede proceder mas. Se termina el algoritmo
        if (bellman.tieneCicloNegativo() == true){
            println("El grafo contiene el siguiente ciclo negativo : ")
            println(bellman.obtenerCicloNegativo())
            ciclo = true
            throw RuntimeException("Como el grafo tiene ciclo negativo, el algoritmo termina")
        }
        ciclo = false

        // Se añaden nodos con el respectivo valor del CCM entre s y v por cada vertice
        for (v in 0..g1.obtenerNumeroDeLados()-1){
            h.add(bellman.costoHasta(v))
        }

        // Se actualizan los pesos de los arcos en el grafo original con los valores obtenidos en BellmanFord
        // Este procedimiento garantiza que todos los arcos tendran pesos no negativos
        // Permitiendo asi hacer uso del algoritmo de Dijkstra 
        for (arco in g.arcos()){

            var u : Int = arco.fuente()
            var v : Int = arco.sumidero()
            arco.peso = arco.peso + h[u] - h[v]
        }
    
        // Tiempo de esta operacion : O(E) donde E es el numero de arcos de G


        // Se crea la matriz donde se almacenaran las distancias entre todos los vertices
        // Dicha matriz tiene dimensiones V x V donde V es el numero de vertices de G
        // Y se crea como arreglos anidados dentro de otro arreglo representando las filas y columnas
        // En un inicio cada valor de la matriz es infinito ya que solo se modificaran cuando haya un camino desde 
        // un vertice u hasta otro vertice v
        // Por lo tanto los valores que queden en infinito representan que no hay camino entre esos dos vertices
        distancias = Array(g.obtenerNumeroDeLados()){Array(g.obtenerNumeroDeLados()) {Double.MAX_VALUE} }

        // Se aplica el algoritmo de Dijkstra desde cada vertice de G con los costos reponderados para obtener los CCM entre todos los vertices
        // Los costos se guardan en la matriz distancias
        for (u in 0..g.obtenerNumeroDeLados()-1){

            var dijkstra = Dijkstra(g,u)

            for (v in 0..g.obtenerNumeroDeLados()-1){

                distancias[u][v] = dijkstra.costoHasta(v) + h[v] - h[u]
            }
            listaDijkstra.add(dijkstra)
        }
        // Tiempo de esta operacion : O(V * E * log V ) ya que Dijkstra se implementa con una cola de prioridad

    }

    // Retorna si existe o no un ciclo negativo mirando lo obtenido por el algoritmo de BellmanFord
    fun hayCicloNegativo() : Boolean {

        // Precondicion : ninguna
        // Postcondicion : se retorna true si existe un ciclo negativo, false en caso contrario

        return ciclo
        // Tiempo de ejecucion : O(1)
    }
    
    // Retorna la matriz con las distancias de los caminos de costo mínimo obtenida aplicando Dijkstra sobre G
    fun obtenerMatrizDistancia() : Array<Array<Double>> { 

        // Precondicion : ninguna
        // Postcondicion : se retorna una matriz donde sus valores corresponden al costo del CCM entre cada par de vertices
        // si no hay camino entre un par de vertices el valor es infinito
        if (hayCicloNegativo()){
            throw RuntimeException("El grafo tiene un ciclo negativo")
        }
        return distancias
        // Tiempo de ejecucion : O(1)
    } 
    
    // Retorna la distancia del camino de costo mínimo desde el vértice u hasta el vértice v. Viendo el respectivo valor
    // en la matriz de distancias
    fun costo(u: Int, v: Int) : Double { 

        // Precondicion : u y v deben pertenecer al grafo
        // Postcondicion : se retorna el costo del CCM entre u y v
        if (hayCicloNegativo()){
            throw RuntimeException("El grafo tiene un ciclo negativo")
        }
        if (u<0 || u>g.obtenerNumeroDeLados()-1 || v<0 || u>g.obtenerNumeroDeLados()-1){
            throw RuntimeException("Alguno de los vertices no pertenece al grafo")
        }
        return distancias[u][v]
        // Tiempo de ejecucion : O(1)
    }

    // Retorna cierto si hay un camino desde u hasta el vértice v.
    // Para determinar si hay un camino entre u y v compara el valor correspondiente en la matriz de distancias
    // Si el valor no es infinito, entonces hay un camino entre u y v
    fun existeUnCamino(u: Int, v: Int) : Boolean { 

        // Precondicion : u y v deben pertenecer al grafo
        // Postcondicion : se retorna true si hay un camino entre u y v, false en caso contrario
        if (hayCicloNegativo()){
            throw RuntimeException("El grafo tiene un ciclo negativo")
        }
        return distancias[u][v] != Double.MAX_VALUE
        // Tiempo de ejecucion : O(1)
    }

    // Retorna los arcos del camino de costo mínimo desde u hasta v llamando a los objetos tipo Dijkstra almacenados
    // durante la ejecucion del algoritmo para apoyarse en su funcion de obtener el CCM
    fun obtenerCaminoDeCostoMinimo(u: Int, v: Int) : Iterable<Arco> { 

        // Precondicion : u y v deben pertenecer al grafo
        // Postcondicion : se retorna el CCM entre u y v de haberlo, caso contrario se retorna un iterable vacio
        return listaDijkstra[u].obtenerCaminoDeCostoMinimo(v)
        // Tiempo de ejecucion : O(V) donde V es en numero de Vertices de G
    }

}
