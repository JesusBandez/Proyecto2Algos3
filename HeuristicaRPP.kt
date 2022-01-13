
import java.io.File

fun main(args: Array<String>) {
    
    // Cuando se lee el txt agregar la arista (u,v) con costo cv1 y la arita (v,u) con costo cv2
    // Modificar agregar arista para este fin

    val inicio = Instant.now().toEpochMilli()

    val algoritmo : String = args[0]
    val instancia : String = args[1]
    var ciclo : MutableList<Arista> = mutableListOf()
    var aristasReq : Int = 0
    var aristasNoReq : Int = 0
    var vertices : Int = 0
    var nombre : String =  ""
    var componentes : Int = 0
    var costo : Int = 0
    

    var archivo = File(instancia).bufferedReader().readLines()

    val linea0 = archivo[0].split(" ").toTypedArray()
    nombre = linea0[2]

    val linea1 = archivo[1].split(" ").toTypedArray()
    componentes = linea1[2].toInt()

    val linea2 = archivo[2].split(" ").toTypedArray()
    vertices = linea2[2].toInt()

    val linea3 = archivo[3].split(" ").toTypedArray()
    aristasReq = linea3[2]

    val linea4 = archivo[4].split(" ").toTypedArray()
    aristasNoReq = linea4[2]

    val gPrim : GrafoNoDirigido = GrafoNoDirigido(vertices)    

    var linea : Array<String>

    val expresionRegular = Regex("[^0-9 ]")

    // Crear grafo Gr . Los lados requeridos se encuentran en el txt de la instancia

    // Se recorren las lineas del archivo a partir de la 6ta linea hasta el valor de aristas requeridas
    // Las aristas no requeridas no nos importan
    // Se les resta 1 a cada vertice para que comiencen en 0  y que no explote el algoritmo xdxdx
    for (i in 6..aristasReq + 6 - 1){
    
        linea = archivo[i].split(" ").toTypedArray()

        //var u = linea[3].dropLast(1).toInt() - 1
        //u = u.replaceAll( "[^\\d]", "" )

        // u en el indice 3
        var u = linea[3]
        u = expresionRegular.replace(u, "").toInt() - 1
        
        // v en el indice 6
        var v = linea[6]
        v = expresionRegular.replace(v, "").toInt() - 1

        // cv1 en el indice 15
        var cv1 = linea[15]
        cv1 = expresionRegular.replace(cv1, "").toInt()

        // cv2 en el indice 22
        var cv2 = linea[22]
        cv2 = expresionRegular.replace(cv2, "").toInt()

        gPrim.agregarArista(Arista(u,v,costo=cv1))
        gPrim.agregarArista(Arista(v,u,costo=cv2))
    }

  

    // Verificar que G' sea conexo (que todos sus vertices esten conectados por un camino)

    // var esFuertementeConexo: Boolean = CFC(g).numeroDeCFC() == 1
    //if( esFuertementeConexo ){
    
    if( componentes == 1 ){

        // Se verifica si el grafo es par
        var par : Boolean = true
        for ( vertice in 0..g.obtenerNumeroDeVertices() - 1){

            if (gPrim.grado(vertice) % 2 != 0){

                par = false
                break
            }
        }

        // Si es par se obtiene el ciclo euleriano
        if (par){

            // Modificar ciclo euleriano para usarlo con grafos no drigidos
            ciclo = CicloEuleriano(gPrim).obtenerCicloEuleriano()
            
        }else{

            //linea 16 en adelante
        }
    }else{
        // Obtenemos las componentes conexas de G' para hacer Gt
        
        val compConexas = ComponentesConexasDFS(gPrim).obtenerComponentesConexas()
    }

    for (arista in ciclo){
        costo += arista.costo()
    }

    val fin = Instant.now().toEpochMilli()

    println("\nInstancia : $nombre")
    println("\nSolucion al RPP :")
    println(ciclo)
    println("\nCosto de la solucion : $costo")
    println("\nTiempo que tomo encontrar la solucion : ${fin-inicio} segundos")

    // Construir Grafo completo Gp    
}