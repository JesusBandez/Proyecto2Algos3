package ve.usb.grafoLib


public class CicloEulerianoGrafoNoDirigido(val g: GrafoNoDirigido) {

    /*
    Clase para determinar el ciclo euleriano dentro de un grafo no dirigido. La estrategia
    consiste en conseguir el grafo dirigido asociado y cada vez que se selecciona un arco se 
    elimina el arco que va en sentido contrario. Ademas, esta implementacion permite que hayan
    lados repetidos  
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

        // Conseguir el digrafo asociado
        // Se convierte el grafo no dirigido en su grafo dirigido
        // asociado y se ejecuta el algoritmo dijkstra sobre él

        // Crear grafo dirigido
        var gDirigidoAsociado = GrafoDirigido(g.obtenerNumeroDeVertices())

        // Para cada vertice en el grafo no dirigido, se consiguen los arcos que salen de el
        // y se agregan al grafo dirigo asociado. Tambien se cuentan la cantidad de arcos que
        // van de un vertice i a un vertice j en la matriz cantidadDeArcos

        var cantidadDeArcos = Array(g.obtenerNumeroDeVertices(), {Array(g.obtenerNumeroDeVertices(), {0})})

        for (verticeInicial in 0 until g.obtenerNumeroDeVertices()){
            for (lado in g.adyacentes(verticeInicial)){

                gDirigidoAsociado.agregarArco(Arco(verticeInicial, lado.elOtroVertice(verticeInicial), lado.peso()))
                cantidadDeArcos[verticeInicial][lado.elOtroVertice(verticeInicial)]++
            }
        }

        // Comprobar la conectividad del grafo
        var esFuertementeConexo: Boolean = CFC(gDirigidoAsociado).numeroDeCFC() == 1

        if (!esFuertementeConexo){
            throw RuntimeException("El grafo no es fuertemente conexo")
        }

        // Ahora se debe comprobar que para todo vertice en el grafo g, este debe tener grado par

        for (vertice in 0 until g.obtenerNumeroDeVertices()){
            if (g.grado(vertice)%2 != 0){
                hayCicloEuleriano = false
            }
        }

        // Se guardan los grados internos de los vertices en el grafo
        var gradoInternoDe: MutableList<Int> = mutableListOf() 

        // Variable que lleva la cuenta de las sumas de los grados internos.
        var sumaDeGradosInternos: Int = 0
        // Para cada vertice en el grafo
        for (vertice in 0 until gDirigidoAsociado.obtenerNumeroDeVertices()){
            
            // ver si su grado interno es igual al externo
            if (gDirigidoAsociado.gradoExterior(vertice) != gDirigidoAsociado.gradoInterior(vertice)){
                
                // si uno no cumple, no hay ciclo euleriano
                hayCicloEuleriano = false
                break
            }

            // Guardar el grado interno del vertice
            gradoInternoDe.add(gDirigidoAsociado.gradoInterior(vertice))
            sumaDeGradosInternos += gDirigidoAsociado.gradoInterior(vertice)
        }
        
        if (hayCicloEuleriano){            
          
            // Obtener la lista de arcos del grafo
            var listaDeArcos = gDirigidoAsociado.arcos() as MutableList    
            

            while (sumaDeGradosInternos>0){
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
                var adyacentesV = gDirigidoAsociado.adyacentes(arcoInicial.fuente()) as List
                var i = 0

                // Iterar por los arcos donde v sea fuente
                while (i < adyacentesV.size){
                    var arco = adyacentesV[i]
                    
                    // Encontrar lado sin explorar
                    if (cantidadDeArcos[arco.fuente()][arco.sumidero()] > 0){

                        // ELiminar el arco de la lista de arcos y reducir el
                        // grado interno del vertice
                        listaDeArcos.remove(arco)
                        gradoInternoDe[arco.fuente()]--

                        // reducir el grado del vertice sumidero
                        gradoInternoDe[arco.sumidero()]--

                        //reducir la suma de grados internos
                        sumaDeGradosInternos -= 2
                        
                        // Se agrega el arco a su respectiva posicion en la lista
                        // de arcos que componen al ciclo Euleriano
                        cicloEuleriano.add(indexAgregar, arco)
                        indexAgregar++       
                        
                        // reducir la cantidad de arcos que van de i a j y de j a i
                        cantidadDeArcos[arco.fuente()][arco.sumidero()]--
                        cantidadDeArcos[arco.sumidero()][arco.fuente()]--

                        // iterar a los siguientes lados
                        adyacentesV = gDirigidoAsociado.adyacentes(arco.sumidero()) as List
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
