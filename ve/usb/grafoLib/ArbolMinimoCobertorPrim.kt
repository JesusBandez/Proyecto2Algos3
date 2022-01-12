package ve.usb.grafoLib



public class ArbolMinimoCobertorPrim(val g: GrafoNoDirigido, r: Int) {
    /*
 Clase para hallar el Arbol mínimo cobertor de un grafo no dirigido usando prim implementado con un minHeap
*/
   
   // Lista para las aristas del arbol y variable para el peso del arbol
    var aristasDelArbol: MutableList<Arista> = mutableListOf()
    var pesoDelArbol: Double = 0.0

    
    var perteneceACola = MutableList(g.obtenerNumeroDeVertices(), {true})
    init {
      
        // Cola de prioridad basada en un minheap
        var col = colaDePrioridadHeap()
        

        // Algoritmo de Prim
        var listaDeVertices = List(g.obtenerNumeroDeVertices(), 
            {Vertice(it)})
        
        listaDeVertices[r].key = 0.0
  
        for (vertice in listaDeVertices){
            col.minHeapInsert(vertice.identificador, vertice.key)
        }
        
        while(col.size() != 0){
            
            
            
            var u = col.heapExtractMin()
            
            
            // Si el elemento extraido de la cola tiene predecesor, entonces ya pertenece al arbol minimo.
            // Sea agrega el lado seguro a la lista aristasDelArbol
            if (listaDeVertices[u].predecesor != -1){
                aristasDelArbol.add(Arista(listaDeVertices[u].predecesor, u, listaDeVertices[u].key))
                pesoDelArbol += listaDeVertices[u].key
            }

            perteneceACola[u] = false
           
            for (arista in g.adyacentes(u)){
               
                var v = arista.elOtroVertice(u)                
                
                if (perteneceACola[v] && arista.peso() < listaDeVertices[v].key){
                    
                    listaDeVertices[v].predecesor = u
                    listaDeVertices[v].key = arista.peso()
                    
                    col.heapDecreaseKey(listaDeVertices[v].identificador, arista.peso())                    
                }             
            }   
        }
    }


    // Rertorna una lista con las aristas del arbol minimo cobertor
    fun obtenerLados() : Iterable<Arista> {
        return aristasDelArbol
    }
    
    // Retorna el peso del árbol mínimo cobertor. 
    fun obtenerPeso() : Double {
        return pesoDelArbol
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

    /*Clase interna usada para poder llevar las propiedades de los vertices
    en el algoritmo y en el heap */
    inner class Vertice(var identificador: Int){
            var predecesor: Int = -1
            var key: Double = Double.POSITIVE_INFINITY
            
            
        }

}
