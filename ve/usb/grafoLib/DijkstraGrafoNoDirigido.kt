/*Clase que implementa el algoritmo de dijkstra para grafo no dirigidos */
package ve.usb.grafoLib


public class DijkstraGrafoNoDirigido(val g: GrafoNoDirigido, val s: Int){

    // Representacion del infinito para el algoritmo
    var infinito: Double = Double.POSITIVE_INFINITY

    // Listas para llevar las propiedades de los vertices
    var distanciaDe: MutableList<Double> = MutableList(g.obtenerNumeroDeVertices(), {infinito})
    var predecesorDe: MutableList<Int?> = MutableList(g.obtenerNumeroDeVertices(), {null})
    var cola = colaDePrioridadHeap()

    // Grafo dirigido asociado
    var gDirigidoAsociado: GrafoDirigido

    // Matriz de pesos de los lados que conforman los caminos de peso minimo
    // Como hay lados multiples, es necesario guardar cual es el peso del lado
    // que menor peso tiene al ir de un vertice i al vertice j tal que i e j son 
    // vertices incidentes en un lado
    var pesosDeCaminos: Array<Array<Double>>

    init {
        // Se convierte el grafo no dirigido en su grafo dirigido
        // asociado y se ejecuta el algoritmo dijkstra sobre él

        // Crear grafo dirigido
        gDirigidoAsociado = GrafoDirigido(g.obtenerNumeroDeVertices())

        // Para cada vertice en el grafo no dirigido, se consiguen los arcos que salen de el
        // y se agregan al grafo dirigo asociado
        for (verticeInicial in 0 until g.obtenerNumeroDeVertices()){
            for (lado in g.adyacentes(verticeInicial)){

                gDirigidoAsociado.agregarArco(Arco(verticeInicial, lado.elOtroVertice(verticeInicial), lado.peso()))
            }
        }

        // Comprobar si hay un arco negativo
        for (arco in gDirigidoAsociado.arcos()){
            if (arco.obtenerPeso()<0){
                RuntimeException("El grafo tiene un arco de peso negativo")
            }
        }

        // Inicializar matriz de pesos
        pesosDeCaminos = Array(g.obtenerNumeroDeVertices(), 
            {Array(g.obtenerNumeroDeVertices(), {infinito})})

        // Ejecutar el algoritmo:
        inicializarFuenteFija(gDirigidoAsociado, s)
        var verticeYaExplorados: MutableList<Int> = mutableListOf()

        

        for (i in 0 until gDirigidoAsociado.obtenerNumeroDeVertices()){
            cola.minHeapInsert(i, distanciaDe[i])
        }

        while (cola.size() != 0){
            var u = cola.heapExtractMin()
            verticeYaExplorados.add(u)

            for (arco in gDirigidoAsociado.adyacentes(u)){
                relajacion(u, arco.sumidero(), arco.obtenerPeso())
                
            }
        }
        
    }


    fun existeUnCamino(v: Int) : Boolean {
        /*Retorna true si hay un camino desde s hasta el vertice v.
        Si el vertice v no existe, se retorna un RuntimeException.
        Precondicion: v pertenece al grafo
        Postcondicion: Se retorna un booleano que indica si existe un camino
        Tiempo de ejecucion: O(1)
        */
        if (!(0 <= v && v < g.obtenerNumeroDeVertices())){
                throw RuntimeException("No existe el vertice ${v} en el grafo")
            }

            return distanciaDe[v] != infinito
    }

    fun costoHasta(v: Int) : Double {
        /* Retorna la distancia del camino de costo minimo desde s hasta el vertice v
            Si el vertice v no existe, se arroja un RuntimeException. */
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
            caminoDeCostoMinimo.add(
                Arco(predecesorDe[verticeActual]!!, verticeActual, 
                    pesosDeCaminos[predecesorDe[verticeActual]!!][verticeActual])
                )

            // Se asigna el siguiente vertice actual
            verticeActual = predecesorDe[verticeActual]!!
        }

        // El camino de costo minimo se consigue al reves, por tanto, se invierte
        // y luego se retorna
        return caminoDeCostoMinimo.reversed()
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

            // Guardar el peso del lado que se usó
            pesosDeCaminos[u][v] = w

            cola.heapDecreaseKey(v, distanciaDe[v])
        } 
    }


    // Clase interna para la cola de prioridad basada en min heap
    inner class colaDePrioridadHeap(){
        var heap: MutableList<Int> = mutableListOf()
        var heapKeys: MutableList<Double> = mutableListOf()
        var posEnHeapDe: MutableList<Int> = mutableListOf()
        var heapSize: Int = 0
       
        // metodo para insertar un elemento en la cola
        fun minHeapInsert(vertice: Int, key: Double){
            
            heap.add(vertice)
            heapKeys.add(Double.POSITIVE_INFINITY)
            posEnHeapDe.add(heapSize)     

           
            heapSize++
            this.heapDecreaseKey(heap[heapSize-1], key)
            
        }

        // Metodo para reordenar el heap al decrementar una key
        // de un vertice de la cola
        fun heapDecreaseKey(i: Int, key:Double){
            var k = posEnHeapDe[i]

            if (key > heapKeys[k]){
                throw RuntimeException("La clave nueva es mas grande que la actual")
            }

            heapKeys[k] = key
           
            
            while (k > 0 && heapKeys[parent(k)] > heapKeys[k]){
                // swap posicion en heap y claves.             
                posEnHeapDe[heap[parent(k)]] = posEnHeapDe[heap[k]].also { posEnHeapDe[heap[k]] = posEnHeapDe[heap[parent(k)]] }
                heap[parent(k)] = heap[k].also { heap[k] = heap[parent(k)] }
                heapKeys[parent(k)] = heapKeys[k].also { heapKeys[k] = heapKeys[parent(k)] }

                k = parent(k)
            }
        }
        // Reordenar el min heap
        fun minHeapify(i: Int){            
            var l = left(i)
            var r = right(i)
            var smallest = i            
            
            if (l < heapSize && heapKeys[l] < heapKeys[i]){
                
                smallest = l 
            }

            if (r < heapSize && heapKeys[r] < heapKeys[smallest]){
                smallest = r
            }

            if (smallest != i){

                posEnHeapDe[heap[i]] = posEnHeapDe[heap[smallest]].also { posEnHeapDe[heap[smallest]] = posEnHeapDe[heap[i]] }
                heap[i] = heap[smallest].also { heap[smallest] = heap[i] }
                heapKeys[i] = heapKeys[smallest].also { heapKeys[smallest] = heapKeys[i] }
                
                minHeapify(smallest)
            }
        }

        // Extraer el elemento mas alto en el heap. Y luego reordenar el heap
        fun heapExtractMin(): Int{
            var min = heap[0]
            posEnHeapDe[heap[0]] = posEnHeapDe[heap[heapSize-1]] .also{ posEnHeapDe[heap[heapSize-1]] = posEnHeapDe[heap[0]]}

            heap[0] = heap[heapSize-1]
            heapKeys[0] = heapKeys[heapSize-1]        

            heapSize--

            minHeapify(0)
            return min
        }


        // retorna el tamanio del heap
        fun size(): Int {
            return heapSize
        }

        // retorna el elemento mas alto del heap
        fun heapMinimun(): Int {
            return heap[0]
        }

        // Funciones para conseguir los elementos parent, el left y el right de un elemento.
        // Se necesita la posicion del elemento en el heap
        fun parent(i: Int): Int{
            return (i-1)/2
        }

        fun left(i: Int): Int {
            return 2*i +1
        }

        fun right(i: Int): Int{
            return 2*i + 2
        }

}


}
