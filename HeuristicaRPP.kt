
import java.io.File
import java.time.Instant

fun main(args: Array<String>) {
    
    // Cuando se lee el txt agregar la arista (u,v) con costo cv1 y la arita (v,u) con costo cv2
    // Modificar agregar arista para este fin

    val inicio = Instant.now().toEpochMilli()

    val algoritmo : String = args[0]
    val instancia : String = args[1]
    var ciclo : MutableList<Arco> = mutableListOf()
    var aristasReq : Int
    var aristasNoReq : Int
    var vertices : Int
    var nombre : String
    var componentes : Int
    var costo : Double = 0.0
    

    var archivo = File(instancia).bufferedReader().readLines()

    var linea0 = archivo[0].split(" ").toList()
    linea0 = linea0.filter { it != "" && it != " "}
    nombre = linea0[2]

    var linea1 = archivo[1].split(" ").toList()
    linea1 = linea1.filter { it != "" && it != " "}
    componentes = linea1[2].toInt() 

    var linea2 = archivo[2].split(" ").toList()
    linea2 = linea2.filter { it != "" && it != " "}
    vertices = linea2[2].toInt()

    var linea3 = archivo[3].split(" ").toList()
    linea3 = linea3.filter { it != "" && it != " "}
    aristasReq = linea3[2].toInt()

    var linea4 = archivo[4].split(" ").toList()
    linea4 = linea4.filter { it != "" && it != " "}
    aristasNoReq = linea4[2].toInt()

    println(nombre)
    println(componentes)
    /*  
    println(vertices)    
    println(aristasReq)  
    println(aristasNoReq) */

    
    var linea : List<String>

    // Grafo que contiene solo los lados requeridos
    val gRequerido : GrafoNoDirigido = GrafoNoDirigido(vertices)

    

    // Se recorren las lineas del archivo a partir de la 6ta linea hasta el valor de aristas requeridas
    // Las aristas no requeridas no nos importan
    // Se les resta 1 a cada vertice para que comiencen en 0  y que no explote el algoritmo xdxdx
    for (i in 6..aristasReq + 6 - 1){
    
        linea = archivo[i].split(" ").toList()
        linea = linea.filter { it != "" && it != " "}
        //var lineaM = linea[1].split(",")
        
        //println("Linea ${i + 1}")
        //println(linea)

        var u : Int
        var v : Int
        var cv1 : Double
        var cv2 : Double 
        
        if (linea.size <= 5){
            // Cuando los numeros de los vertices son muy grandes explotaba sin esto xd
            var lineaM = linea[1].split(",")

            u = lineaM[0].toInt() - 1
            v = lineaM[1].dropLast(1).toInt() - 1
            cv1 = linea[3].toDouble()
            cv2 = linea[4].toDouble()    

        }else{

            u = linea[1].dropLast(1).toInt() - 1
            v = linea[2].dropLast(1).toInt() - 1
            cv1 = linea[4].toDouble()
            cv2 = linea[5].toDouble()
        }

        //println("u : $u , v : $v , cv1 : ${cv1} , cv2 : ${cv2} ")  
        // Por ahor asumamos que cv1 = cv2. Agregar un arista agrega los dos arcos
        gRequerido.agregarArista(Arista(u,v,cv1))
        if (cv1 != cv2 ){
            print("error, pesos distintos")
        }
        
    }

    // Se establece un "isomorfismo" entre gRequerido y gPrima
    // Diccionarios usados para conservar la relacion entre un vertice en grafo gRequerido y
    // un vertice en el grafo gPrima. En caso de que se quiera buscar el vertice i de gRequerido
    // en gPrima, se usa vertice = deGRequeridoAGPrima.get(i). Si se quiere lo contrario, se usa
    // vertice = deGprimaAGRequerido(i)

    var deGprimaAGRequerido: MutableMap<Int, Int> = mutableMapOf()
    var deGRequeridoAGPrima: MutableMap<Int, Int> = mutableMapOf()
    var contadorDeVerticesRequeridos: Int = 0

    // Ahora se consigue gPrim

    
    for (vertice in 0 until vertices){
        // Si el vertice tiene grado mayor a 0, entonces
        // es incidente en un lado requerido
        if (gRequerido.grado(vertice)>0){
            // Se establece la relacion que tiene el vertice de gRequeridos
            // con el que tendra gPrima
            deGprimaAGRequerido.put(contadorDeVerticesRequeridos, vertice)
            deGRequeridoAGPrima.put(vertice, contadorDeVerticesRequeridos)

            contadorDeVerticesRequeridos++
        }
    }

    // Crear gPrima con solo los vertices que son incidentes en un lado requerido
    var gPrim = GrafoNoDirigido(contadorDeVerticesRequeridos)

    // Conseguir los lados de gPrima usando las relaciones entre los vertices
    for (arista in gRequerido.aristas()){
        var unoDeLosVertices = arista.cualquieraDeLosVertices()

        var vertice = deGRequeridoAGPrima.get(unoDeLosVertices)!!
        var otroVertice = deGRequeridoAGPrima.get(arista.elOtroVertice(unoDeLosVertices))!!
        
        gPrim.agregarArista(Arista(vertice, otroVertice, arista.peso()))
    }



    // Verificar que G' sea conexo (que todos sus vertices esten conectados por un camino)    
    // Conseguir el grafo dirigido asociado a gPrim
    var gPrimDirigidoAsociado = GrafoDirigido(gPrim.obtenerNumeroDeVertices())

    // Para cada vertice en el grafo no dirigido, se consiguen los arcos que salen de el
    // y se agregan al grafo dirigo asociado
    for (verticeInicial in 0 until gPrim.obtenerNumeroDeVertices()){
        for (lado in gPrim.adyacentes(verticeInicial)){
            gPrimDirigidoAsociado.agregarArco(Arco(verticeInicial, lado.elOtroVertice(verticeInicial), lado.peso()))
        }
    }
 
    // Se ejecuta el algoritmo CFC sobre el grafo dirigido asociado  
    
    var esFuertementeConexo: Boolean = CFC(gPrimDirigidoAsociado).numeroDeCFC() == 1   
    println(esFuertementeConexo)
    if( esFuertementeConexo){

        // Se verifica si el grafo es par
        var par : Boolean = true
        for ( vertice in 0 until gPrim.obtenerNumeroDeVertices()){
            
            if (gPrim.grado(vertice) % 2 != 0){

                par = false
                break
            }
        }

        // Si es par se obtiene el ciclo euleriano
        if (par){
            
            
            ciclo = CicloEuleriano(gPrimDirigidoAsociado).obtenerCicloEuleriano() as MutableList<Arco>
            
        }else{
            println("no es par") 
            //linea 16 en adelante
        }
    }else{
        // Obtenemos las componentes conexas de G' para hacer Gt
        
        val compConexas = ComponentesConexasDFS(gPrim)

    }

    for (arco in ciclo){
        costo += arco.peso()
    }

    val fin = Instant.now().toEpochMilli()

    //println("\nInstancia : $nombre")
    //println("\nSolucion al RPP :")
    //println(ciclo.joinToString(" "))
    println("\nCosto de la solucion : $costo")

    println("\nTiempo que tomo encontrar la solucion : ${(fin-inicio).toDouble()/1000} segundos")

    // Construir Grafo completo Gp    
}
