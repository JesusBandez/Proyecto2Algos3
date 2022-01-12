package ve.usb.grafoLib


public class BellmanFord(val g: GrafoDirigido, val s: Int) {
    /*Clase que contiene la implementación del algoritmo Bellman-Ford para encontrar los
 caminos de costo mínimo desde un vértice s. El algoritmo se ejecuta al instanciar la clase
 */
    
    // Representacion del infinito para el algoritmo
    var infinito: Double = Double.POSITIVE_INFINITY

    // Variables usadas para los ciclos negativos
    var hayCicloNegativo = true
    lateinit var arcoQueGeneraCicloNegativo: Arco 

    // Listas para llevar las propiedades de los vertices
    var distanciaDe: MutableList<Double> = MutableList(g.obtenerNumeroDeVertices(), {infinito})
    var predecesorDe: MutableList<Int?> = MutableList(g.obtenerNumeroDeVertices(), {null})


    init {
        // Algoritmo de Belman Ford
        // precondicion: g es un grafo no dirigido y s pertenece al grafo
        // postcondicion: Se le aplica el algoritmo al grafo
        // Tiempo de ejecucion: O(|V| + |E|)

        inicializarFuenteFija(g, s)


        for (i in 1 until g.obtenerNumeroDeVertices()-1){
            for (arco in g.arcos()){

                relajacion(arco.fuente(), arco.sumidero(), arco.peso())
            }
        }

        hayCicloNegativo = false
        for (arco in g.arcos()){
            if (distanciaDe[arco.sumidero()] > distanciaDe[arco.fuente()] + arco.peso()){
                hayCicloNegativo = true
                arcoQueGeneraCicloNegativo = arco                
                distanciaDe[arco.sumidero()] = -infinito
                
            }
        }        
    }


    fun inicializarFuenteFija(g:GrafoDirigido, s: Int){
        /* Funcion para el algoritmo de Bellman. Se usa para inicializar los vertices
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
        /* Funcion para el algoritmo de Bellman. Se usa para relajar los lados en el grafo
        precond: u y v pertenece al grafo, w es el peso del arco que contiene a u y v
        postcond: Se reduce la distancia (o costo) del vertice v si la distancia del vertice u mas el peso
            del arco es menor
        Tiempo de ejecicion: O(|1|) */
        if (distanciaDe[v] > distanciaDe[u] + w ){
            distanciaDe[v] = distanciaDe[u] + w
            predecesorDe[v] = u
        } 
    }

   
    fun tieneCicloNegativo() : Boolean { 
        /* Retorna true si existe un ciclo negativo en el camino de s a los vertices alcanzables
        precond: true
        postcond: Dice si el grafo tiene o no un ciclo negativo
        tiempo de ejecucion: O(1)
        */
        return hayCicloNegativo
    }

    
    fun obtenerCicloNegativo() : Iterable<Arco> { 
        /* Si el grafo tiene un ciclo negativo, retorna los arcos del ciclo. En caso contrario, retorna
        una lista vacia
        precond: true
        postcond: iterable con los arcos del ciclo negativo o iterable vacio si no existe el ciclo 
        tiempo de ejecucion: O(E)
        */

        // Si no existe un ciclo negativo, se retorna un iterable vacio
        if (!this.tieneCicloNegativo()){
            return emptyList()
        }


        // Se inicializa verticeActual con el vertice fuente del arco que genera
        // el ciclo negativo
        var verticeActual = arcoQueGeneraCicloNegativo.fuente()

        // Se inicializa el cicloNegativo con el arco que lo genera
        var cicloNegativo: MutableList<Arco> = mutableListOf(arcoQueGeneraCicloNegativo)

        // Mientras el vertice actual no sea el vertice sumidero del arco que genera el ciclo 
        // negativo
        while (verticeActual != arcoQueGeneraCicloNegativo.sumidero()){

            // Se agrega al ciclo negativo el arco cuya fuente sea el predecesor del vertice actual
            // y cuyo sumidero sea el vertice actual
            cicloNegativo.add(g.adyacentes(predecesorDe[verticeActual]!!).find {
                it.fuente() == predecesorDe[verticeActual] && it.sumidero() == verticeActual
            }!!)

            // retroceder hacia el siguiente arco del ciclo
            verticeActual = predecesorDe[verticeActual]!!
        }
        // El ciclo se tiene al reves, se invierte y se retorna
        return cicloNegativo.reversed()
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

        // Si la distancia de v no es infinito ni -infinito es alcanzable        
        return distanciaDe[v] != infinito && distanciaDe[v] != -infinito

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
