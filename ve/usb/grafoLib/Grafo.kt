package ve.usb.grafoLib

// interfaz donde se definen algunas propiedades en comun entre los grafos dirigidos y no dirigidos
interface Grafo {

    var lados: HashSet<Int>
    
    // Retorna el número de lados del grafo
    fun obtenerNumeroDeLados() : Int

    // Retorna el número de vértices del grafo
    fun obtenerNumeroDeVertices() : Int

    // Retorna los adyacentes de v
    fun adyacentes(v: Int) : Iterable<Lado>

    // Retorna el grado del grafo
    fun grado(v: Int) : Int

    fun lista() : Iterable<Lado>
}
