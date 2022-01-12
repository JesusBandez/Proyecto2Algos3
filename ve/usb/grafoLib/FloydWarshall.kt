package ve.usb.grafoLib

public class FloydWarshall(val W : Array<Array<Double>>) {
/*
 Implementación del algoritmo de Floyd-Warshall para encontrar los
 caminos de costo mínimo todos los pares de vértices de un grafo.
 El algoritmo se ejecuta sobre la matriz de pesos que se pasa como parametros 
 */

    // Matriz que lleva las distancias de los caminos mas cortos de los vertices
    var matrizDistancia: Array<Array<Double>>

    // Matriz que lleva los predecesores de los vertices
    var matrizPredecesores: Array<Array<Int?>>

    init{
        // Comprobar que la matriz es cuadrada
        var n = W.size
        var m = W[0].size

        if (n != m){
            throw RuntimeException("La matriz de pesos no es cuadrada")
        }


        // Se crea una copia de la matriz de pesos
        var matrizD0: Array<Array<Double>> = W.copyOf()

        // Se genera la matriz de predecesores inicial
        var predecesoresInicial: Array<Array<Int?>> = Array(n, {Array(n, {null})})
        for (i in 0 until n){
            for (j in 0 until n){
                if (i!=j && matrizD0[i][j] < Double.POSITIVE_INFINITY){
                    predecesoresInicial[i][j] = i
                }
            }
        }
        
        // Lista de matrices que llevan la distancia de los vertices
        var matricesD = mutableListOf(matrizD0)
        // Lista de las matrices que llevan los predecesores de los vertices
        var matricesDePredecesores = mutableListOf(predecesoresInicial)

        // Ejecutar el algoritmo:
        for (k in 0 until n){
            
            var matrizDAnterior = matricesD[k]
            var matrizDNueva: Array<Array<Double>> = Array(n, {Array(n, {0.0})})
            
            var matrizDePredecesoresVieja = matricesDePredecesores[k]  
            var matrizPredecesoresNueva: Array<Array<Int?>> = Array(n, {Array(n, {null})})
                         

            for (i in 0 until n){           
                for (j in 0 until n){
                    if (i == j){
                        continue
                    }                 
                    if (matrizDAnterior[i][j] <=  matrizDAnterior[i][k] + matrizDAnterior[k][j]){
                        matrizDNueva[i][j] = matrizDAnterior[i][j]
                        matrizPredecesoresNueva[i][j] = matrizDePredecesoresVieja[i][j]

                    } else {
                        matrizDNueva[i][j] = matrizDAnterior[i][k] + matrizDAnterior[k][j]
                        matrizPredecesoresNueva[i][j] = matrizDePredecesoresVieja[k][j]
                    }
                }
            }

            // Agregar la matriz creada a la lista de matrices. Igualmente para 
            // la matriz de predecesores
            matricesD.add(matrizDNueva)            
            matricesDePredecesores.add(matrizPredecesoresNueva)
            
        }

        // Conseguir la matriz de distancias y la matriz de predecesores final
        matrizDistancia = matricesD.last()
        matrizPredecesores = matricesDePredecesores.last()       
    }

 
    fun obtenerMatrizDistancia() : Array<Array<Double>> {
        /* Retorna la matriz con las distancias de los caminos de costo mínimo
        entre todos los pares de vértices. 
        Precond: True
        Postcond: retorna la matriz de distancias
        tiempoDeEjecucion: O(1)
        */
        return matrizDistancia
    } 


    fun obtenerMatrizPredecesores() : Array<Array<Int?>> { 
        /*  Retorna la matriz con los predecesores de todos los vértices en los caminos de costo mínimo.       
        Precond: True
        Postcond: retorna la matriz de predecesores
        tiempoDeEjecucion: O(1)*/
        return matrizPredecesores
    } 
    

    fun costo(u: Int, v: Int) : Double {
        /* Retorna la distancia del camino de costo mínimo desde el vértice u hasta el vértice v.
            Si alguno de los dos vértices  no existe, arroja una RuntimeException.
        Precond: u y v deben ser vertices del grafo
        Postcond: retorna la distancia del camino mas corto entre u y v
        tiempoDeEjecucion: O(1)*/
        if (!(0 <= u && u < W.size) || !(0 <= v && v < W.size) ) {
            throw RuntimeException("${u} o ${v} no pertenece al grafo")
        } 

        return matrizDistancia[u][v]
    }


    fun existeUnCamino(u: Int, v: Int) : Boolean { 
        /* Retorna true si hay un camino desde u hasta el vértice v.
            Si alguno de los dos vértices  no existe, se arroja un RuntimeException.
        Precond: u y v deben ser vertices del grafo
        Postcond: retorna un booleano que indica si existe un camino de u a v
        tiempoDeEjecucion: O(1)*/ 

        if (!(0 <= u && u < W.size) || !(0 <= v && v < W.size) ) {
            throw RuntimeException("${u} o ${v} no pertenece al grafo")
        }  
        return matrizDistancia[u][v] != Double.POSITIVE_INFINITY
    }


    fun obtenerCaminoDeCostoMinimo(u: Int, v: Int) : Iterable<Arco> {
     /*Retorna los arcos del camino de costo mínimo desde u hasta v.
     Si alguno de los dos vértices  no existe, se arroja un RuntimeException.
        Precond: u y v deben ser vertices del grafo
        Postcond: Retorna una lista con los arcos que conforman el camino de costo minimo
        tiempoDeEjecucion: O(m), siendo m la cantidad de arcos que tenga el camino*/

        if (!(0 <= u && u < W.size) || !(0 <= v && v < W.size) ) {
            throw RuntimeException("${u} o ${v} no pertenece al grafo")
        }

        // Se crea una lista vacia
        var camino: MutableList<Arco> = mutableListOf()

        // Si existe un camino, se llena la lista camino con los arcos.
        // Si no, se retorna la lista vacia
        if (existeUnCamino(u, v)){
            caminoDeCostoMinimoRecursivo(u, v, camino)
        } 

        return camino
     }

    fun caminoDeCostoMinimoRecursivo(u: Int, v: Int?, arcos: MutableList<Arco>){
        /* Funcion que navega de manera recursiva por los predecesores de los vertices para ir construyendo
        el camino de costo minimo
        precondicion: u y v pertenecen al grafo, arcos es una lista de arcos
        postcondicion: True
        tiempoDeEjecucion: O(m), siendo m la cantidad de arcos que tenga el camino
        */
        if (u == v){
            
        } else if(matrizPredecesores[u][v!!] == null) {
            arcos.clear()

        } else {
            caminoDeCostoMinimoRecursivo(u, matrizPredecesores[u][v], arcos)
            
            arcos.add(Arco(matrizPredecesores[u][v]!!, v, W[matrizPredecesores[u][v]!!][v]))
        }
        
    }
}
