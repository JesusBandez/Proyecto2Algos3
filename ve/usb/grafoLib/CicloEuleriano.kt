package ve.usb.grafoLib


public class CicloEuleriano(val g: GrafoDirigido) {

    /*
    Clase para determinar el ciclo euleriano dentro de un grafo.    
    */

    // Indica si el grafo tiene un ciclo euleriano y 
    // lista para guardar el ciclo
    var hayCicloEuleriano: Boolean = true
    var cicloEuleriano: MutableList<Arco> = mutableListOf()

    init {
        /* El algoritmo para conseguir el ciclo Euleriano se ejecuta al instanciar la clase
        precond: g es un grafo dirigido fuertemente conexo
        postcond: se consigue un cicloEuleriano en el grafo o se detecta que no puede ser hallado
        tiempo de ejecucion: O(|E| + |V|)
         */

        // Comprobar la conectividad del grafo
        var esFuertementeConexo: Boolean = CFC(g).numeroDeCFC() == 1

        if (!esFuertementeConexo){
            throw RuntimeException("El grafo no es fuertemente conexo")
        }

        // Ahora se debe comprobar que para todo vertice, su grado interno
        // es igual a su grado externo. Si esto se cumple, hay un ciclo euleriano

        // Se guardan los grados internos de los vertices en el grafo
        var gradoInternoDe: MutableList<Int> = mutableListOf() 

        // Para cada vertice en el grafo
        for (vertice in 0 until g.obtenerNumeroDeVertices()){
            
            // ver si su grado interno es igual al externo
            if (g.gradoExterior(vertice) != g.gradoInterior(vertice)){
                
                // si uno no cumple, no hay ciclo euleriano
                hayCicloEuleriano = false
                break
            }

            // Guardar el grado interno del vertice
            gradoInternoDe.add(g.gradoInterior(vertice))
        }
        
        if (hayCicloEuleriano){
            // marcar todos los lados como inexplorados, se 
            // crea una matriz booleana para representar los arcos
            // explorados
            var arcoYaExplorado: List<MutableList<Boolean>> = List(
                g.obtenerNumeroDeVertices(), {MutableList(g.obtenerNumeroDeVertices(), {false})})

            // Obtener la lista de arcos del grafo
            var listaDeArcos = g.arcos() as MutableList    
                 

            while (listaDeArcos.size != 0){
                // Buscar el primer ciclo
                var arcoInicial: Arco = Arco(-1, -1)
                var indexAgregar = -1

                if (cicloEuleriano.size == 0){
                    arcoInicial = listaDeArcos[0]
                    indexAgregar = 0
                // Si no es posible tomar todos los lados del grafo en un solo ciclo, 
                // se consigue el arco del primer ciclo que comparte vertice con el segundo ciclo. El segundo
                // ciclo se busca a partir de ese vertice
                } else {
                    for ((index, arco) in cicloEuleriano.withIndex()){
                        if (gradoInternoDe[arco.fuente()] > 0){
                            arcoInicial = listaDeArcos.find{ it.fuente() == arco.fuente()}!!
                            indexAgregar = index
                            break
                        }
                    }
                }

                // Conseguir lista de arcos donde v es fuente
                var adyacentesV = g.adyacentes(arcoInicial.fuente()) as List
                var i = 0

                // Iterar por los arcos donde v sea fuente
                while (i < adyacentesV.size){
                    var arco = adyacentesV[i]
                    
                    // Encontrar lado sin explorar
                    if (!arcoYaExplorado[arco.fuente()][arco.sumidero()]){

                        // ELiminar el arco de la lista de arcos y reducir el
                        // grado interno del vertice
                        listaDeArcos.remove(arco)
                        gradoInternoDe[arco.fuente()]--
                        
                        // Se agrega el arco a su respectiva posicion en la lista
                        // de arcos que componen al ciclo Euleriano
                        cicloEuleriano.add(indexAgregar, arco)
                        indexAgregar++       
                        
                        // Marcar el lado del grafo como explorado
                        arcoYaExplorado[arco.fuente()][arco.sumidero()] = true

                        // iterar a los siguientes lados
                        adyacentesV = g.adyacentes(arco.sumidero()) as List
                        i = 0

                    } else {
                        // Si el arco ya fue explorado, se avanza al siguiente arco
                        // en la lista de adyacencias de v
                        i++
                    }                               
                }
            }
        }
    }

    
    fun obtenerCicloEuleriano() : Iterable<Arco> {
        /* Retorna una lista con los arcos en el orden que conforman
        el ciclo Euleriano.
        precond: True
        postcond: Se retorna la lista con el ciclo Euleriano
        Tiempo de ejecicion: O(1) */

        if (!hayCicloEuleriano){
            throw RuntimeException("El grafo no tiene ciclo Euleriano")
        }
        return cicloEuleriano
    }
    
    
    fun tieneCicloEuleriano() : Boolean {
        /* Retorna valor booleano que muestra si el grafo tiene un ciclo Euleriano
        postcod: True
        precond: Retorna si el grafo contiene un ciclo de Euler
        Tiempo de ejecucion: True */
        return hayCicloEuleriano
    }
}
