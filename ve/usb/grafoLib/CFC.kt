package ve.usb.grafoLib


// Determina las componentes fuertementes conexas de un grafo
// Se utiliza una version modificada del DFS sobre el grafo inverso 
public class CFC(val g: GrafoDirigido) {

  // Se crea la lista donde se almacenaran las CFC del grafo y demas variables a ser usadas
  val componentes : MutableList<MutableSet<Int>> = mutableListOf(mutableSetOf())
  var tiempo = 0
  var nodos : MutableList<Node> = mutableListOf()
  val busqueda : BusquedaEnProfundidad = BusquedaEnProfundidad(g)
  var i = 0
  var ordenTopologico = busqueda.orden

  init{
    // Se crea el grafo inverso de g
    val grafoInverso = digrafoInverso(g)

    // Se le asigna el color blanco a cada nodo para indicar que no han sido visitados aun
    for (i in ordenTopologico){
      nodos.add(Node(Color.BLANCO))
    }

    // Se llama recursivamente a dfsVisit con todos los nodos guiandose por el orden topologico
    for (nodo in ordenTopologico){
      if (nodos[nodo].color == Color.BLANCO){
        dfsVisit(grafoInverso,nodo)
        i++
        componentes.add(mutableSetOf())            
      }
    }
    // Se remueve la lista vacia que queda al final del arreglo luego de todas las visitas a los vertices
    componentes.removeAt(componentes.size-1)
  }

    // Parte recursiva de la DFS
    // Se recorre cada nodo del grafo y luego se le asigna el color negro para indicar que ya han sido visitados
  private fun dfsVisit(g: Grafo, u: Int) {

    tiempo ++
    nodos[u].ti = tiempo
    nodos[u].color = Color.GRIS
    
    // Se añaden los vertices de la actual componente fuertemente conexa guiando por el DFS
    componentes[i].add(u)

    for (ady in g.adyacentes(u)){
      if (nodos[ady.elOtroVertice(u)].color == Color.BLANCO){
        nodos[ady.elOtroVertice(u)].predecesor = u
        dfsVisit(g,ady.elOtroVertice(u))   
      }      
    }
    nodos[u].color = Color.NEGRO
    tiempo++
    nodos[u].tf = tiempo
  }

  // Retorna true si los dos vertices estan fuertemente conectados
  // Es decir, que esten en la misma componente fuertemente conexa
  fun estanFuertementeConectados(v: Int, u: Int) : Boolean {

    // Se verifica que los vertices esten en el grafo
    if(!g.lados.contains(v) || !g.lados.contains(u) ){
        throw RuntimeException("El elemento no se encuentra en el grafo")
    }
    
    for (componente in componentes){
      if (componente.contains(v) && componente.contains(u)){
        return true
      }
    }
    return false
  }

  // Indica el número de componentes fuertemente conexas
  fun numeroDeCFC() : Int {
    var i = 0
    for (j in componentes){
      i++
    }
    return i
    // Tiempo de ejecucion : O(1) se retorna un valor solamente
  }

  // Retorna el identificador de la componente fuertemente conexa donde está contenido 
  // el vértice v 
  fun obtenerIdentificadorCFC(v: Int) : Int {

    if(!g.lados.contains(v)){
      throw RuntimeException("El elemento no se encuentra en el grafo")
    }
    // Se verifica componente por componente hasta encontrar el vertice v y se devuelve
    // el indice de la componente donde se encontro
    var identificador = -1
    for (i in 0..numeroDeCFC()-1){
      for (j in 0..(componentes[i].size-1)){
        if (componentes[i].contains(v)){
          identificador = i
        }
      } 
    }
    return identificador
    // Tiempo de ejecucion : O(V) donde V son los vertices del grafo
  }
  // Se devuelve la variable donde se almacenaron las CFC
  fun  obtenerCFC() : Iterable<MutableSet<Int>> {
    return componentes
    // Tiempo de ejecucion : O(1) solo se retorna una variable
  }

  

  
  // Retorna el grafo componente asociado a las componentes fuertemente conexas. 
  fun obtenerGrafoComponente() : GrafoDirigido {

    var grafoComponente : GrafoDirigido = GrafoDirigido(numeroDeCFC()-1)

    for (i in 0 until g.obtenerNumeroDeVertices()) {

      for (arco in g.adyacentes(i)){
        var j = arco.sumidero()

        if (obtenerIdentificadorCFC(i) != obtenerIdentificadorCFC(j)){
          grafoComponente.agregarArco(Arco(obtenerIdentificadorCFC(i),obtenerIdentificadorCFC(j)))
        }
      }            
    }
    return grafoComponente
  }
}

