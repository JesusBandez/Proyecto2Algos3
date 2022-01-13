package ve.usb.grafoLib



// Determina el orden topológico de un DAG
public class OrdenTopologico(val g: GrafoDirigido) {
  
  // Se crean variables a ser usadas posteriormente
  var ciclo : CicloDigrafo = CicloDigrafo(g)
  var busqueda = BusquedaEnProfundidad(g)

  // Retorna true si el grafo g es un DAG, de lo contrario retorna false
  fun esUnGrafoAciclicoDirecto() : Boolean {
    return !ciclo.existeUnCiclo()
    // Tiempo de ejecucion : O(1) solamente retorna un valor
  }

  // Retorna el ordenamiento topológico del grafo g. Si el grafo G no es DAG,
  // entonces se lanza una RuntineException()
  fun obtenerOrdenTopologico() : Iterable<Int> {
    if(!esUnGrafoAciclicoDirecto()){
      throw RuntimeException("El grafo no es un DAG")
    }else{
      return busqueda.orden()
    }
    // Tiempo de ejecucion : O(1) solamente se retorna una variable previamente obtenido
  }
}
