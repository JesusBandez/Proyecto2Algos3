package ve.usb.grafoLib


public class OrdenTopologico(val g: GrafoDirigido) {
  /* Clase para determinar el orden topologico de un grafo dirigido aciclico 
  g: Grafo dirigido aciclico
  */

    // Para saber si el grafo es un Digrafo Aciclico
    private var esDAG: Boolean = false

    // Lista que llevara el ordenamientoTopologico
    private var ordenamientoTopologico: MutableList<Int> = mutableListOf()

    // variables para dfs
    var tiempo = 0
    var propiedadesDeVertice = List(g.obtenerNumeroDeVertices()){Vertice()}

    init {
      var cicloDigrafo  = CicloDigrafo(g)

      esDAG = !cicloDigrafo.existeUnCiclo()

      if (esDAG){
        /*
          Se ejecuta DFS modificado al instanciar la clase.A medida que se 
          consigue un tiempo final, se agrega a la primera posicion de la lista
          el vertice.
          Precondicion: True
          Postcoindicion: BFS aplicado al grafo y ordenamientoTopologico contiene el
            orden topologico de los vertices del grafo
          Tiempo de Ejecucion: O(|V| + |E|) 
              siendo V la cantidad de vertices y E la cantidad de lados
              en el grafo
          */
      
        


          // Aplicar el algoritmo DFS
          for(indexVertice in 0 until g.obtenerNumeroDeVertices()){
              if (propiedadesDeVertice[indexVertice].color == Color.BLANCO){
                  dfsVisit(g, indexVertice)
              }
          }

      } else {
          // Si el grafo g no es DAG, se salta la ejecucion de DFS
      }

    }
    // Metodo dfsVisit del algoritmo DFS modificado. Cuando se consigue
    // el tiempo final del vertice, este se guarda en la lista 
    // ordenamientoTopologico
    private fun dfsVisit(g: Grafo, u: Int) {
        tiempo +=  1
        propiedadesDeVertice[u].tiempoInicial = tiempo
        propiedadesDeVertice[u].color = Color.GRIS
        
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
        ordenamientoTopologico.add(0, u)

    }

    
    fun esUnGrafoAciclicoDirecto() : Boolean {
      /* Metodo para retornar si el grafo g es un DAG 
      precondicion: True
      postcondicion: Retorna si true si el grafo es un DAG, falso en
        otro caso
      tiempo de ejecucion: tiempoDeEjecucion: O(|V| + |E| + |E|)
      */

      return esDAG
	
    }


    fun obtenerOrdenTopologico() : Iterable<Int> {
      /* Funcion que retorna el orden topologico conseguido en el grafo.
        En caso de que el grafo no sea un DAG, arroja una exception
        precondicion: El grafo es un DAG
        postcondicion: Se retorna el orden topologico del grafo
        Tiempo de ejecucion: O(1)
        */

      if (this.esDAG){
        return ordenamientoTopologico
      } else {
        throw RuntimeException("El grafo no es un grafo directo aciclico")
      }
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
    */
    inner class Vertice(
        var color: Color = Color.BLANCO,
        var predecesor: Int? = null,
        var tiempoInicial: Int = -1, 
        var tiempoFinal: Int = -1){
            
        }


  
}
