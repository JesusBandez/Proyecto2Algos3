package ve.usb.grafoLib


public class Johnson(val g: GrafoDirigido) {
/*
 Clase con la implementación del algoritmo de Johnson para conseguir los 
 costos minimos entre todos los pares de vertices. En caso de que hay un ciclo negativo.
 no es posible encontrar estos caminos por lo que se arroja una runtime exception cuando
 se intente acceder a esos metodos
 */

    // Matriz de las distancias entre los pares
    var d: Array<Array<Double>> = Array(g.obtenerNumeroDeVertices(), {Array(g.obtenerNumeroDeVertices(), {Double.POSITIVE_INFINITY})})
    // Lista con los clases dijkstras que se crean para cada vertice
    var objetosDijs: MutableList<Dijkstra> = mutableListOf()

    // Variable para saber si hay un ciclo negativo
    var hayCicloNegativo: Boolean = false

    // Variable que lleva la clase bellmanford
    var bellman: BellmanFord

    // Arreglo que hace de funcion h
    var h: Array<Double> = Array(g.obtenerNumeroDeVertices()+1, {-1.0})

    init {
        // Crear grafo G'        
        var gPrima = GrafoDirigido(g.obtenerNumeroDeVertices()+1)
        // el vertice s es el ultimo vertice del grafo
        var s: Int = g.obtenerNumeroDeVertices()

        // Agregar a gPrima los arcos en g    
        for (arco in g.arcos()){
            gPrima.agregarArco(
                Arco(arco.fuente(), arco.sumidero(), arco.peso())
            )
        }
        
        // Agregar los arcos que van de s a todos los vertices del grafo
        for (vertice in 0 until g.obtenerNumeroDeVertices()){
            gPrima.agregarArco(
                Arco(s, vertice, 0.0))
        }

        // Ejecutar bellmanFord y comprobar si hay ciclo negativo
        bellman = BellmanFord(gPrima, s)
        if (bellman.tieneCicloNegativo()){
            hayCicloNegativo = true

        } else {
            // En caso de no haber ciclo negativo

            // Se consigue los valores de la funcion h
            for (vertice in 0 until gPrima.obtenerNumeroDeVertices()){
                h[vertice] = bellman.costoHasta(vertice)
            }

            // Se crea un tercer grafo que contiene los pesos modificados con los nuevos
            // valores de tal forma que los arcos tengan pesos mayores o iguales a 0
            var gPesosModificados = GrafoDirigido(gPrima.obtenerNumeroDeVertices())
            for (arco in gPrima.arcos()){

                gPesosModificados.agregarArco(Arco(arco.fuente(),
                    arco.sumidero(),
                    arco.peso() + h[arco.fuente()] - h[arco.sumidero()]))
            }        
            
            // Ejecutar Dijkstra para cada vertice en el grafo g
            for (verticeFuente in 0 until g.obtenerNumeroDeVertices()){
                var dijkstra = Dijkstra(gPesosModificados, verticeFuente)
                for (verticeFinal in 0 until g.obtenerNumeroDeVertices()){
                    d[verticeFuente][verticeFinal] = dijkstra.costoHasta(verticeFinal) + h[verticeFinal] - h[verticeFuente]
                }

                objetosDijs.add(dijkstra)
            }
        }

    }

    
    fun hayCicloNegativo() : Boolean {
        /*Retorna true si hay un ciclo negativo en el grafo
        precondi: True
        postcond: Dice si hay un ciclo negativo en el grafo
        tiempo de ejecucion: O(1)
        */
        return hayCicloNegativo
     }
    
    fun obtenerMatrizDistancia() : Array<Array<Double>> { 
        /* Retorna la matriz con las distancias de los caminos de costo mínimo
        entre todos los pares de vértices. Arroha runtime Exception si hay un lado negativo
        precondi: true
        postcond: retorna la matriz de distancias
        tiempo de ejecucion: O(1)
        */

        if(this.hayCicloNegativo()){
            throw RuntimeException("El grafo tiene un ciclo negativo")
        }

        return d
    } 
    

    fun costo(u: Int, v: Int) : Double {
        /*  Metodo para conseguir la distancia del camino de costo mínimo desde u hasta v.
        En caso de que alguno no exista o hay un ciclo negativo en el grafo, se arroja RuntimeException
        precondi: u y v pertenecen al grafo, y el grafo no tiene ciclos negativos
        postcond: Se retorna la distancia entre u y v
        tiempo de ejecucion: O(1)
        */
        if (!(0 <= u && u < g.obtenerNumeroDeVertices()) || !(0 <= v && v < g.obtenerNumeroDeVertices()) ) {
            throw RuntimeException("${u} o ${v} no pertenece al grafo")
        }
        if (this.hayCicloNegativo()){
            throw RuntimeException("El grafo tiene un ciclo negativo")
        }

        return d[u][v]

     }

    fun existeUnCamino(u: Int, v: Int) : Boolean {
        /* Retorna un booleano que afirma si hay un camino desde u hasta v.
        Si alguno de los dos vértices  no existe, se arroja un RuntimeException.
        Igualmente si hay un ciclo negativo se lanza una RuntimeException 
        precondi: u y v pertenecen al grafo, y el grafo no tiene ciclos negativos
        postcond: Se retorna si existe un camino entre u y v
        tiempo de ejecucion: O(1)
        */
        if (!(0 <= u && u < g.obtenerNumeroDeVertices()) || !(0 <= v && v < g.obtenerNumeroDeVertices()) ) {
            throw RuntimeException("${u} o ${v} no pertenece al grafo")
        }
        if (this.hayCicloNegativo()){
            throw RuntimeException("El grafo tiene un ciclo negativo")
        }

        return d[u][v] != Double.POSITIVE_INFINITY
    }     

    fun obtenerCaminoDeCostoMinimo(u: Int, v: Int) : Iterable<Arco> { 
        /* Retorna los arcos del camino de costo mínimo que va desde u hasta v.
        Si un vertice no existe en el grafo o hay un ciclo negativo, se arroja una runtime exception
        precondicion: u y v pertenecen al grafo
        postcondicion: se retorna un iterable con los arcos del camino de costo minimo
        Tiempo de ejecucion: O(n), con n siendo la cantidad de arcos que tiene el camino de costo minimo
 */
        if (!(0 <= u && u < g.obtenerNumeroDeVertices()) || !(0 <= v && v < g.obtenerNumeroDeVertices()) ) {
            throw RuntimeException("${u} o ${v} no pertenece al grafo")
        }  
    
       if (this.hayCicloNegativo()){
            throw RuntimeException("El grafo tiene un ciclo negativo")
        }
        
        var camino: MutableList<Arco> = mutableListOf()
        var caminoEnDijkstra = objetosDijs[u].obtenerCaminoDeCostoMinimo(v)
        
        for (arco in caminoEnDijkstra){
            camino.add(Arco(arco.fuente(), 
            arco.sumidero(), 
            d[arco.fuente()][arco.sumidero()]))
        }

        return camino
    }
}
