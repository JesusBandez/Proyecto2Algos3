package ve.usb.grafoLib


interface Grafo {

    // Retorna el número de lados del grafo
    fun obtenerNumeroDeLados() : Int

    // Retorna el número de vértices del grafo
    fun obtenerNumeroDeVertices() : Int

    /* 
     Retorna los adyacentes de v, en este caso los lados que tienen como vértice inicial a v. 
     Si el vértice no pertenece al grafo se lanza una RuntimeException
     */
    fun adyacentes(v: Int) : Iterable<Lado>

    // Retorna el grado del vertice
    fun grado(v: Int) : Int
}
