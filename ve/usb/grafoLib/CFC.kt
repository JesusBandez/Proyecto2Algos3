
package ve.usb.grafoLib


public class CFC(val g: GrafoDirigido) {
  /* Clase para conseguir las componentes conexas de un grafo. Se consiguen al instanciar la clase 
  g: Grafo Dirigido donde se consiguen las componentes conexas
  */
    
    // Lista qie contendra los vertices ordenados de mayor a menor con respecto a su
    //  tiempo final despues de aplicar DFS
    var mayorTiempoFinalAMenor: MutableList<Int> = mutableListOf()


    // variables para el algoritmo dfs
    var tiempo = 0
    var propiedadesDeVertice = List(g.obtenerNumeroDeVertices()){Vertice()}

    // Lista de conjuntos que contiene las componentes conexas del grafo
    var cfc: MutableList<MutableSet<Int>> = mutableListOf()

    // variable para separar los arbolesi al aplicar DFS sobre del grafo dirigido
    // inverso
    var arboli: Int = 0
    init {    
        /*
          Se ejecuta DFS al instanciar la clase para conseguir la lista de vertices ordenados
          de mayor a menor segun tiempo final. Luego, se aplica DFS modificado sobre el inverso
          del grafo g para conseguir las componentes conexas del grafo g          
          Precondicion: g es un grafo dirigido
          Postcoindicion: cfc contiene las componentes conexas del grafo g
          Tiempo de Ejecucion: O(|V| + |E|) 
              siendo V la cantidad de vertices y E la cantidad de lados
              en el grafo
          */
      

          // Aplicar el algoritmo DFS para conseguir el orden
          for(indexVertice in 0 until g.obtenerNumeroDeVertices()){
              if (propiedadesDeVertice[indexVertice].color == Color.BLANCO){
                  dfsVisitConseguirOrden(g, indexVertice)
              }
          }          

          

          // Reiniciar la lista de propiedades de vertices y tiempo para aplicar 
          // DFS modificado sobre g Inverso
          var gInverso = digrafoInverso(g)

          propiedadesDeVertice = List(g.obtenerNumeroDeVertices()){Vertice()}
          tiempo = 0
          // DFS modificado para conseguir las partes conexas del grafo
          for (indexVertice in mayorTiempoFinalAMenor){
              if (propiedadesDeVertice[indexVertice].color == Color.BLANCO){
                
                cfc.add(mutableSetOf())
                dfsVisit(gInverso, indexVertice)
                
                arboli++               
                  
              }
          }
        

    }

   private fun dfsVisitConseguirOrden(g: Grafo, u: Int) {
        /* Modificacion de DFS para conseguir el orden de mayor a menor 
        de los vertices segun su tiempo final

        precondicion: g es un grafo y u pertenece al grafo
        postcondicion: mayorTiempoFinalAmenor contiene los vertices ordenados segun la
          magnitud de su tiempo final. De mayor a menor
        Tiempo de Ejecucion: O(|V| + |E|) 
        
         */

        tiempo +=  1
        propiedadesDeVertice[u].tiempoInicial = tiempo
        propiedadesDeVertice[u].color = Color.GRIS
        
        for (lado in g.adyacentes(u)){
            var v: Int = lado.elOtroVertice(u)

            if (propiedadesDeVertice[v].color == Color.BLANCO){
                propiedadesDeVertice[v].predecesor = u
                dfsVisitConseguirOrden(g, v)
            }
        }

        propiedadesDeVertice[u].color = Color.NEGRO
        tiempo += 1
        propiedadesDeVertice[u].tiempoFinal = tiempo
        mayorTiempoFinalAMenor.add(0, u)

    }

   private fun dfsVisit(g: Grafo, u: Int) {
        /* Modificacion de DFS para conseguir las componentes conexas
        del digrafo g

        precondicion: g es un grafo y u pertenece al grafo
        postcondicion: cfc contiene las componentes conexas del grafo
        Tiempo de Ejecucion: O(|V| + |E|) 
        
         */
        tiempo +=  1
        propiedadesDeVertice[u].tiempoInicial = tiempo
        propiedadesDeVertice[u].color = Color.GRIS

        cfc[arboli].add(u)
        
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

    



    fun estanFuertementeConectados(v: Int, u: Int) : Boolean {
      /* Retorna true si dos vertices pertenecen a la misma componente conexa
      y false en caso contrario. Si v o u no pertenecen al grafo, se arroja exception 
      precondicion: v y u pertenecen al grafo
      postcondicion: Se retorna si ambos vertices pertenecen a la misma componente conexa
      Tiempo de Ejecucion: O(|C|), con |C| la cantidad de conjuntos en CFC
      */
      

      // comprobar que ambos vertices pertenezcan al grafo
      if (! (0 <= v && v < g.obtenerNumeroDeVertices() 
          ||  0 <= u && u < g.obtenerNumeroDeVertices() )){
        throw RuntimeException("Uno de los vertices no pertenecen al grafo")
      }


      // Por cada conjunto en cfc, buscar al que contenga a v. Al encontrarlo,
      // ver si tiene a u. Si lo tiene, retorna true. Si no, retorna false
      for (conjunto in cfc){
        if (conjunto.contains(v)){
          if (conjunto.contains(u)){
            return true
          } else {
            return false
          }
        }
      }
      return false
    }

   
    fun numeroDeCFC() : Int {
      /* retorna la cantidad de componentes conexas del grafo.
      Precond: True
      Postcond: retorna la cantidad de componentes conexas
      Tiempo de ejecucion: O(1)
       */

      return cfc.size
    }

    fun obtenerIdentificadorCFC(v: Int) : Int {
    /* Retorna el indice de la lista en cfc en el que se guarda la componente conexa a la que 
    pertenece el vertice v.
    precondicion: v pertenece al grafo
    postcondicion: retorna en qué posicion de la lista se encuentra la componente a la que
      pertenece v
    tiempo de ejecucion: O(|C|) con c siendo la cantidad de componentes conexas del grafo
     */

      // comprobar precondicion
      if( !(0 <= v && v < g.obtenerNumeroDeVertices())){
        throw RuntimeException("El vertice ${v} no pertenece al grafo")
      }
      
      // Buscar en las componentes a cual pertenece v. Al encontrarla, se retorna su indice
      for ((index, componente) in cfc.withIndex()){
        if (componente.contains(v)){
          return index
        }
      }

    return -1 //jamas retornara -1
    }
    
    fun  obternerCFC() : Iterable<MutableSet<Int>> {
      /* Retorna una lista de conjuntos con las componentes conexas del grafo
      precondicion: True
      postcondicion: Se retorna las componentes conexas del grafo
      tiempo de ejecucion: O(1)
       */
      return cfc
    }

    
    fun obtenerGrafoComponente() : GrafoDirigido {
      /* Retorna el Digrafo componente del grafo g. Este grafo tiene como vertice las componentes conexas
      de g y como arcos los arcos que unen a las componentes 
      precondicion: True
      postcondicion: se retorna el grafo componente
      tiempo de ejecucion: O(|V| + |E|)
      */

      // crear un grafo con la cantidad de componentes conexas de g como numero de vertices
      var grafoComponente = GrafoDirigido(this.numeroDeCFC())

      // Iterar entre todos los arcos de todos los vertices del grafo g
      for (verticeInicial in 0 until g.obtenerNumeroDeVertices()){
        for (arco in g.adyacentes(verticeInicial)){
          
          // Si un arco tiene como fuente a un vertice de una componente y como sumidero
          // a un vertice de otra componente conexa, se agrega un arco que va desde el 
          // vertice de la componente a la cual pertenece el vertice fuente hasta el vertice
          // de la componente a la cual pertenece el vertice sumidero siempre y cuando este arco no se 
          // haya agregado antes
          
          var componenteInicial = this.obtenerIdentificadorCFC(verticeInicial)
          var componenteFinal = this.obtenerIdentificadorCFC(arco.sumidero())

          if ( componenteInicial !=  componenteFinal){

            // Si ya se ha agregado el arco, se continua en la siguiente iteracion
            if (grafoComponente.adyacentes(componenteInicial).find {it.sumidero() == componenteFinal} != null ){

              continue

            } else {

              grafoComponente.agregarArco(Arco(
                componenteInicial,
                componenteFinal)
              )
            }
          }
        }
      }
      
    
      return grafoComponente
    }

    /* Clase interna que lleva las propiedades de un vertice perteneciente al grafo.
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