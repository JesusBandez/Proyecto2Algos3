package ve.usb.grafoLib
import java.util.Arrays

// Implementacion del algoritmo de Floyd-Warshall para encontrar los CCM entre todos los vertices de un grafo
// Se recibe de entrada la matriz nxn (cuadrada) de costos asociados al digrafo del que se quieren obtener los CCM, 
// dicho grafo no debe tener ciclos negativos
// Precondicion : La matriz w debe ser cuadrada y no deben haber ciclos negativos
// Postcondicion : los costos en la matriz de distancias corresponde al CCM entre cada par de vertices del grafo
public class FloydWarshall(val w : Array<Array<Double>>) {

    // Variables a ser utilizadas posteriormente para almacenar las matrices de predecesores y distancias
    var n = w.size
    var distancias : Array<Array<Double>>
    var predecesores : Array<Array<Int>>

    init {

        // Se verifica que la matriz w sea cuadrada viendo el tamaño de cada una de sus filas
        for (i in w){

            if (i.size != n){

                throw RuntimeException("La matriz de costos no es cuadrada")
            }
        }

        // Se crea un arreglo con todos los elementos iguales a -1, que representa el NIL de la implementacion vista en clase
        predecesores = Array(n){Array(n) {-1} }

        // Se le asigna a cada lado su predecesor dentro de la matriz de predecesores
        // Se debe poder alcanzar un vertice desde el otro, caso contrario se mantiene el valor de -1 en la matriz
        // Tiempo de ejecucion : O(V^2)
        for (i in 0..n-1){

            for (j in 0..n-1){

                if ( i != j && w[i][j] < Double.MAX_VALUE){

                    predecesores[i][j] = i
                }
            }
        }

        // Se le asigna la matriz de costos a la matriz de distancias en la iteracion 0
        // Tiempo de ejecucion : O(V^2)
        distancias = w

        for (k in 0..n-1){

            for (i in 0..n-1){

                for (j in 0..n-1){

                    // Se actualiza la matriz de predecesores
                    if (distancias[i][j] <= distancias[i][k] + distancias[k][j]){

                        predecesores[i][j] = predecesores[i][j]

                    }else if (distancias[i][j] > distancias[i][k] + distancias[k][j]){

                        predecesores[i][j] = predecesores[k][j]
                    }

                    // Se actualiza la matriz de distancias
                    distancias[i][j] = minOf( distancias[i][j] , distancias[i][k] + distancias[k][j]) 
                }
            }
        }
        // Tiempo de ejecucion del algoritmo : O(V^3) por los tres ciclos donde se actualizan las matrices
    }
    


    // Retorna la matriz con las distancias de los caminos de costo mínimo
    // entre todos los pares de vértices.
    fun obtenerMatrizDistancia() : Array<Array<Double>> { 

        // Precondicion : true
        // Postcondicion : la matriz de distancias es una matriz cuadrada cuyos elementos representan los costos de los CCM
        // entre cada par de vertices del grafo
        return distancias
        // Tiempo de ejecucion : O(1) ya que solo se retorna una variable
    } 

    // Retorna la matriz con los predecesores de todos los vértices en los caminos de costo mínimo
    // entre todos los pares de vértices.
    fun obtenerMatrizPredecesores() : Array<Array<Int>> { 
        // Precondicion : true
        // Postcondicion : la matriz de predecesores es una matriz cuadrada cuyos elementos representan los predecesores de cada vertice
        return predecesores
        // Tiempo de ejecucion : O(1) ya que solo se retorna una variable
    } 
    
    // Retorna la distancia del camino de costo mínimo desde el vértice u hasta el vértice v,
    //  dicho valor se encuentra en el elemento u,v de la matriz
    // Si no hay un camino entre u y v se retorna infinito
    
    fun costo(u: Int, v: Int) : Double { 
        // Precondicion : u y v pertenecen al grafo
        // Postcondicion : el costo representa el costo del CCM entre u y v

        if( 0>v || v>n || 0>u || u>n){
            throw RuntimeException ("Alguno de los vertices no pertenece al grafo")
        }
        return distancias[u][v]
        // Tiempo de ejecucion : O(1) ya que solo se retorna un valor
    }

    // Retorna true si hay un camino desde u hasta el vértice v, caso contrario retorna false
    // Para saber si hay un camino se observa si el elemento u,v de la matriz de predecesores es distinto a -1
    fun existeUnCamino(u: Int, v: Int) : Boolean { 
        // Precondicion : u y v pertenecen al grafo
        // Postcondicion : Retorna true si hay un camino desde u hasta el vértice v, caso contrario retorna false
        if( 0>v || v>n || 0>u || u>n){
            throw RuntimeException ("Alguno de los vertices no pertenece al grafo")
        }
        return predecesores[u][v] != -1
        // Tiempo de ejecucion : O(1) ya que solo se retorna un valor
    }

    // Retorna los arcos del camino de costo mínimo desde u hasta v.
    // Para obtener el camino se van recorriendo los predecesores de v en la matriz de predecesores hasta llegar a u
    fun obtenerCaminoDeCostoMinimo(u: Int, v: Int) : Iterable<Arco> {
        // Precondicion : u y v pertenecen al grafo
        // Postcondicion : el camino retornado es el CCM entre u y v

        if( 0>v || v>n || 0>u || u>n){
            throw RuntimeException ("Alguno de los vertices no pertenece al grafo")
        }
        var camino : MutableList<Arco> = mutableListOf()
        var actual = v

        while (actual != u){

            camino.add(Arco(predecesores[u][actual],actual))
            actual = predecesores[u][actual]
        }
        return camino.reversed()
        // Tiempo de ejecucion : O(V^2) ya que en el peor caso se debera recorrer toda la matriz para hallar el CCM
    }
}
