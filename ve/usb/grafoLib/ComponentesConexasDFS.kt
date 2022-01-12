package ve.usb.grafoLib


public class ComponentesConexasDFS(val g: GrafoNoDirigido) {
  /*
   Clase para determinar las componentes conexas de un grafo no dirigido usando DFS. 
  Se consiguen las componentes al instanciar esta clase
  */

    /* 
    Lista que contiene objetos de tipo vertice. La clase vertice en una clase interna
    definida al final de este archivo
    
    Esta lista contiene los atributos de los vertices como la distancia,
    color y predecesor. Para conseguir las propiedades del vertice i se busca
    el objeto vertice contenido en propiedadesDeVertice[i]
    Por ejemplo:
    Si el vertice 3 tiene color blanco, entonces la lista propiedadesDeVertice tiene
    un objeto tipo vertice con la propiedad color = blanco en el indice 3
    */
    var propiedadesDeVertice: List<Vertice> = List(0){Vertice()}

    var componentesConexas: MutableList<MutableList<Int>> = mutableListOf()
    var componenteConexaId: Int = 0
    // variable "global" para ejecutar el algoritmo
    var tiempo: Int = 0
    
    init {
       /*
        Se ejecuta DFS al instanciar la clase
        Precondicion: True
        Postcoindicion: BFS aplicado al grafo
        Tiempo de Ejecucion: O(|V| + |E|) 
            siendo V la cantidad de vertices y E la cantidad de lados
            en el grafo
         */
    
        // Crea la lista de vertices, estos ya estan inicializados para ejecutar el
        // algoritmo
        propiedadesDeVertice = List(g.obtenerNumeroDeVertices()){Vertice()}

        
        // Aplicar el algoritmo DFS
        for(indexVertice in 0 until g.obtenerNumeroDeVertices()){
            if (propiedadesDeVertice[indexVertice].color == Color.BLANCO){

                componentesConexas.add(mutableListOf())
                propiedadesDeVertice[indexVertice].componenteConexaId = componenteConexaId

                dfsVisit(g, indexVertice)
                componenteConexaId++
            }
        }
            
    }

    // Metodo dfsVisit del algoritmo DFS
    private fun dfsVisit(g: Grafo, u: Int) {
        tiempo +=  1
        propiedadesDeVertice[u].tiempoInicial = tiempo
        propiedadesDeVertice[u].color = Color.GRIS

        propiedadesDeVertice[u].componenteConexaId = componenteConexaId
        componentesConexas[componenteConexaId].add(u)      
        
        for (lado in g.adyacentes(u)){
            var v: Int = lado.elOtroVertice(u)

            if (propiedadesDeVertice[v].color == Color.BLANCO){
                propiedadesDeVertice[v].predecesor = u
                dfsVisit(g, v)
            }
        }

        propiedadesDeVertice[u].color = Color.NEGRO
        tiempo += 1
        propiedadesDeVertice[u].tiempoFinal = tiempo 
    }

    
    
    fun estanMismaComponente(v: Int, u: Int) : Boolean {
      /*
     Retorna true si los dos vertices están en la misma componente conexa y
     falso en caso contrario.
     */
        if ( ! ((0 <= v && v < g.obtenerNumeroDeVertices()) || 
            (0 < u && u < g.obtenerNumeroDeVertices()))){
                throw RuntimeException("Uno de los vertices no pertenece al grafo")
            }

      if (propiedadesDeVertice[v].componenteConexaId == propiedadesDeVertice[u].componenteConexaId){
        return true
      } else {
        return false
      }
    }

    // Retorna el numero de componentes conexas presentes en el grafo
    fun numeroDeComponentesConexas() : Int {
      return componentesConexas.size
    }

  
    fun obtenerComponente(v: Int) : Int {
  /*
     Retorna el identificador de la componente conexa donde está contenido 
     el vértice v.
     Precondicion: V pertenece a una componente
     Prostcoindicion: Se retornar el id de la componente conexa
     Tiempo O(1)
     */
        if ( !(0 <= v && v < g.obtenerNumeroDeVertices())){
                throw RuntimeException("El vertice ${v} no pertenece al grafo")
            }
      return propiedadesDeVertice[v].componenteConexaId
    }

   

    fun numVerticesDeLaComponente(compID: Int) : Int {
    // Retorna el numero de vertices que pertenecen a una componente conexa
    /* 
     Precondicion: CompID Es el id de alguna componente
     Prostcoindicion: Se retornar el id de la componente conexa
     Tiempo O(1)
    */
      return componentesConexas[compID].size
    }

    /*
    Clase interna que lleva las propiedades de un vertice perteneciente al grafo.
    El vertice que sea representado por entero 6 tiene sus propiedades en la lista
    propiedadesDeVertice[6].

    color: Variable con el color al que se le asigna al vertice durante la ejecucion del
        algoritmo BFS. Se inicializa con blanco porque es necesario asignar ese color a todos
        los vertices cuando se ejecuta el algoritmo
    predecesor: Indica quien es el predecesor del vertice al ejecutar BFS
    tiempoInicial: Momento en el que es descubierto el vertice y cuando empieza
        a explorarse.
    tiempoFinal: Momento en el que se termina de explorar el algoritmo
    ComponenteConexaId: Es el id de la componente conexa a la que pertenece el vertice
    */
    inner class Vertice(
        var color: Color = Color.BLANCO,
        var predecesor: Int? = null,
        var tiempoInicial: Int = -1, 
        var tiempoFinal: Int = -1, 
        var componenteConexaId: Int = -1
        ){
         
            
        }


}
