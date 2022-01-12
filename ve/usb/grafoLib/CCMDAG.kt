package ve.usb.grafoLib


public class CCMDAG(val g: GrafoDirigido, val s: Int) {
    /*Clase con la implementación del algoritmo para encontrar los
    caminos de costo mínimo en un DAG desde una fuente s. 
    Si el digrafo de entrada no es DAG, entonces se lanza una RuntimeException
    */
    
    // representacion del infinito
    var infinito: Double = Double.POSITIVE_INFINITY
    
    // Listas para llevar las propiedades de los vertices en el grafo
    var distanciaDe: MutableList<Double> = MutableList(g.obtenerNumeroDeVertices(), {infinito})
    var predecesorDe: MutableList<Int?> = MutableList(g.obtenerNumeroDeVertices(), {null})


    init{
        /* Se ejecuta el algoritmo al instanciar la clase
        precond: g es un grafo dirigido sin ciclos y s pertenece a g
        postcond: se aplica al algoritmo sobre el grafo
        tiempo de ejecucion: O(|V| + |E|) 
        */

        // Si existe un ciclo en el digrafo, se arroja una exception
        if (CicloDigrafo(g).existeUnCiclo()){
            throw RuntimeException("El grafo contiene un ciclo")
        }

        var ordenTopo = OrdenTopologico(g).obtenerOrdenTopologico()

        inicializarFuenteFija(g, s)

        for (u in ordenTopo){
            for (arco in g.adyacentes(u)){
                relajacion(u, arco.sumidero(), arco.peso())
            }
        }

    }

    fun inicializarFuenteFija(g:GrafoDirigido, s: Int){
       /* Funcion para inicializar los vertices
        precond: g es un grafo dirigido y s pertenece al grafo
        postcond: Todos los vertices tienen asignada infinito como distancia y nulo como predecesor,
            excepto la fuente, que tiene como distancia 0
        Tiempo de ejecicion: O(|V|) */


        for (vertice in 0 until g.obtenerNumeroDeVertices()){
            distanciaDe[vertice] = infinito
            predecesorDe[vertice] = null
        }

        distanciaDe[s] = 0.0
    }

    fun relajacion(u: Int, v:Int, w:Double){
        /* Funcion para relajar los lados en el grafo
        precond: u y v pertenece al grafo, w es el peso del arco que contiene a u y v
        postcond: Se reduce la distancia (o costo) del vertice v si la distancia del vertice u mas el peso
            del arco es menor
        Tiempo de ejecicion: O(|1|) */
        if (distanciaDe[v] > distanciaDe[u] + w ){
            distanciaDe[v] = distanciaDe[u] + w
            predecesorDe[v] = u
        } 
    }


    fun existeUnCamino(v: Int) : Boolean {
        /* Retora true si existe un camino desde el vertice fuente s hasta el vertice v. Si v no pertenece
        al grafo arroja una exception.
        precond: v pertenece al grafo
        postcond: Dice si existe un camino hasta v
        tiempo de ejecucion: O(1)        
         */

        if (!(0 <= v && v < g.obtenerNumeroDeVertices())){
            throw RuntimeException("No existe el vertice ${v} en el grafo")
        }

        // Si la distancia de v no es infinito es porque se le asigno una distancia
        // Por tanto, es alcanzable
        return distanciaDe[v] != infinito
     }

 
    fun costoHasta(v: Int) : Double {
        /* Retorna la distancia del camino de costo mínimo desde s hasta v
            Si el vértice v no existe, se retorna un RuntimeException. 
        precond: v pertenece al grafo
        postcond: Se retorna el costo */

        if (!(0 <= v && v < g.obtenerNumeroDeVertices())){
            throw RuntimeException("No existe el vertice ${v} en el grafo")
        }

        return distanciaDe[v]
     }


    fun obtenerCaminoDeCostoMinimo(v: Int) : Iterable<Arco> {
        /* Retorna un iterable con los arcos del camino de costo mínimo hasta v.
        En caso de que v no exista en el grafo, se arroja una exception 
        precond: v pertenece al grafo
        postcond: Se retorna un iterable con el camino de costo minimo hasta v
        tiempo de ejecucion: O(|V|)
        */

        // Si no existe el vertice, se arroja una exception
        if (!(0 <= v && v < g.obtenerNumeroDeVertices())){
            throw RuntimeException("No existe el vertice ${v} en el grafo")
        }

        // Se inicializa una lista vacia.
        var caminoDeCostoMinimo: MutableList<Arco> = mutableListOf()

        // Si no existe un camino hasta v, entonces se retorna el camino vacio
        if (!this.existeUnCamino(v)){
            return caminoDeCostoMinimo
        }

        // Se inicializa verticeActual como v
        var verticeActual: Int = v

        // Mientras el predecesor de v no sea null
        while (predecesorDe[verticeActual] != null){

            // Agregar al camimo de coste minimo el arco cuya fuente sea el predecesor del vertice actual
            // y cuyo sumidero sea el vertice actual
            caminoDeCostoMinimo.add(g.adyacentes(predecesorDe[verticeActual]!!).find 
                {
                it.fuente() == predecesorDe[verticeActual] && it.sumidero() == verticeActual}
                !!)

            // Se asigna el siguiente vertice actual
            verticeActual = predecesorDe[verticeActual]!!
        }

        // El camino de costo minimo se consigue al reves, por tanto, se invierte
        // y luego se retorna
        return caminoDeCostoMinimo.reversed()
     }
}
