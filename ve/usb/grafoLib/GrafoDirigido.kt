package ve.usb.grafoLib

import java.io.File

public class GrafoDirigido : Grafo {

    // Se construye un grafo a partir del número de vértices
    var adjacencyList: List<MutableList<Arco>> = List(10){mutableListOf()}
    var archivo = List(0){""}

    // Se sobreescribe la variable lados declarada en la interfaz Grafo
    override var lados: HashSet<Int> = hashSetOf()

    // Se construye la lista de adyacencias a partir del numero de vertices suministrados
    constructor(numDeVertices: Int) {

        // Precondicion, el numero de vertices debe ser mayor a 0
        !!(numDeVertices>0)
        adjacencyList = List(numDeVertices+1){mutableListOf()}
        // Tiempo de ejecucion : O(1) se crea la lista pero no se le añade ningun elemento
    }

    // Se contruye el grafo desde el archivo suministrado dependiendo de si se indica que tiene peso o no
    constructor(nombreArchivo: String, conPeso: Boolean) {

        //Se verifica que el archivo con los datos exista
        if(!File(nombreArchivo).exists()){
            println("El archivo $nombreArchivo no existe")
            return
        }
        // Se leen los datos del archivo .txt usando buffreader y el metodo readLines
        archivo = File(nombreArchivo).readLines()
            
        // Se crea la lista de adyacencias con el tamaño requerido y el arreglo linea donde mas adelante 
        // se trabajara con los datos del archivo
        adjacencyList = List(archivo[0].toInt()){mutableListOf()}
        var linea : Array<String>
    
        // Se recorren las lineas del archivo a partir de la 3ra, donde se encuentran los arcos
        for (i in 2..(archivo.size-1)){
        
            linea = archivo[i].split(" ").toTypedArray()

            // Se procesan los datos del archivo .txt y se añaden los arcos a la lista de adyacencias
            // Dependiendo de si el archivo incluye el peso o no
            if (conPeso) {
                agregarArco(Arco(linea[0].toInt(),linea[1].toInt(),linea[2].toDouble()))
            }else{
                agregarArco(Arco(linea[0].toInt(),linea[1].toInt()))
            }
        }
        // Tiempo de ejecucion : O(n) donde n es la cantidad de arcos indicados en el archivo
    }

    // Agrega un lado al digrafo
    fun agregarArco(a: Arco) {

        // Precondicion , debe existir el archivo .txt
        //!!(File(archivo).exists())

        // Se verifica si un arco ya ha sido añadido y retorna
        /* if(adjacencyList[a.fuente()].find {it.sumidero() == a.sumidero()} !=null ){
            return
        } */

        adjacencyList[a.inicio].add(a)

        // Se añade a la lista lados cada vertice del grafo para llevar un control sobre ellos
        // Como se trata de un conjunto no pueden haber elementos repetidos
        lados.add(a.inicio)
        lados.add(a.fin)
        
        // Tiempo de ejecucion : O(1) se añade en la posicion deseada un elemento
    }

    // Retorna el grado del vertice v.
    // Es la suma de los grados exteriores e interiores
    override fun grado(v: Int) : Int {
        if(!lados.contains(v)){
            throw RuntimeException("El elemento no se encuentra en el grafo")
        }
        // return adyacentes(v).size
        return gradoExterior(v) + gradoInterior(v)
        // Tiempo de ejecucion : O(1) se añade en la posicion deseada un elemento
    }

    override fun lista() : Iterable<Arco>{     
        return adjacencyList.flatten()
        // Tiempo de ejecucion : O(n) donde n es la cantidad de arcos
    }


    // Retorna el grado exterior del vertice v.
    // No es mas que el numero de vertices adyacentes a v
    fun gradoExterior(v: Int) : Int {

        if(!lados.contains(v)){
            throw RuntimeException("El elemento no se encuentra en el grafo")
        }

        var grado : Int = 0
        for (i in adyacentes(v)){
            grado++
        }
        return grado
        // Tiempo de ejecucion : O(n) donde n es el numero de vertices adyacentes a v
    }

    // Retorna el grado interior del vertice v.
    // Representa el numero de arcos en los cuales v es el final
    fun gradoInterior(v: Int) : Int {

        if(!lados.contains(v)){
            throw RuntimeException("El elemento no se encuentra en el grafo")
        }

        var verticesIncidentes : MutableList<Int> = mutableListOf()
        for (arco in arcos()){
            if (arco.fin == v){
                verticesIncidentes.add(arco.inicio)
            }
        }
        // Tiempo de ejecucion : O(n) donde n es el numero de arcos del grafo
        return verticesIncidentes.size
    }

    // Retorna el número de lados del grafo
    override fun obtenerNumeroDeLados() : Int {
        // Tiempo de ejecucion : O(1) ya que se retorna solamente el elemento deseado
        return lados.size
    }

    // Retorna el número de vértices del grafo
    override fun obtenerNumeroDeVertices() : Int {

        // Precondicion , el grafo debe tener algun elemento
        !!(!adjacencyList.isEmpty())

        // Tiempo de ejecucion : O(1) se retorna el tamaño de la lista solamente
        return adjacencyList.size
    }

    
    // Retorna los adyacentes de v viendo la lista de adyacencias
    override fun adyacentes(v: Int) : Iterable<Arco> {
        //Precondicion
        !!(!adjacencyList.isEmpty())

        if(!lados.contains(v)){
            throw RuntimeException("El elemento no se encuentra en el grafo")
        }
        
        // Tiempo de ejecucion : O(1) ya que se retorna solamente el elemento deseado
        return adjacencyList[v]
    }

    // Retorna todos los lados del digrafo
    fun arcos() : Iterable<Arco> {

        // Precondicion , el grafo debe tener arcos
        !!(!adjacencyList.isEmpty())

        
        // Se usa el metodo flatten para obtener todos los elementos en un mismo arreglo sin estar anidados

        // Tiempo de ejecucion : O(n) ya que se deben recorrer todos los n arcos de la lista
        return adjacencyList.flatten()
    }
    
    // String que muestra el contenido del grafo
    override fun toString() : String {

        // Se crea un string donde de iran almacenando los datos
        var str : String = ""
        
        // Se recorre la lista de adyacencias y se añaden al string los datos dependiendo de si tienen peso o no
        for((inicio,arcos) in adjacencyList.withIndex()){

            str += "${inicio} ------>"

            for (arco in arcos){

                if(arco.obtenerPeso()!=0.0){
                    str += "[${arco.sumidero()} ; (Peso : ${arco.obtenerPeso()})] "
                }else{
                    str += "[${arco.sumidero()}]"
                }
            }
            // Se inserta un salto de linea para separar la informacion de cada vertice
            str += "\n"
        }

        // Tiempo de ejecucion : O(n) ya que se deben recorrer todos los n arcos de la lista
        return str
    }
}
