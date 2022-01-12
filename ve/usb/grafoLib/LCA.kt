package ve.usb.grafoLib

public class LCA(val g: GrafoDirigido) {

    // Precondicion : g es un digrafo
    // Postcondicion : el vertice obtenido es LCA de u y v

    // Se obtiene el ancestro comun mas bajo (LCA) de dos vertices u y v que deben suministrarse al llamar a la funcion obtenerLCA
    // Para obtenerlo se usa el grafo inverso de g y se aplica BFS desde los dos vertices para determinar los ancestros en el grafo original
    // Estos ancestros, que son los vertices que son alcanzables desde el grafo inverso, se añaden a una lista
    // luego de añadir todos los ancestros de cada vertice se filtra por ancestros comunes
    // y finalmente se busca el lca como el vertice desde el cual ninguno de los otros ancestros son alcanzables usando BFS nuevamente

    var gInverso = digrafoInverso(g)
    
    fun obtenerLCA(v: Int, u: Int) : Int {

        // Se llama bfs desde ambos vertices en el grafo inverso
        var bfsV = BusquedaEnAmplitud(gInverso,v)
        var bfsU = BusquedaEnAmplitud(gInverso,u)

        var alcanzablesV : MutableList<Int> = mutableListOf()
        var alcanzablesU : MutableList<Int> = mutableListOf()

        // Se añaden a la lista los vertices alcanzables desde ambos vertices
        for (lado in 0 until g.obtenerNumeroDeVertices()){
            if (bfsV.hayCaminoHasta(lado)){
                alcanzablesV.add(lado)
            }
        }

        for (lado in 0 until g.obtenerNumeroDeVertices()){
            if (bfsU.hayCaminoHasta(lado)){
                alcanzablesU.add(lado)
            }
        }
                
        // Se filtran los ancestros en comun
        var ancestro = alcanzablesV.filter { alcanzablesU.contains(it) }

        // Si solo hay un elemento en la lista significa que hay solo un ancestro comun para ambos vertices.
        // Por tanto ese es el LCA
        if (ancestro.size == 1){
            return ancestro[0]
        }

        // Si no hay ningun elemento en la lista significa que no hay ancestro comun entre los vertices
        if (ancestro.size == 0){
            return -1
        }


        // Se llama bfs desde los ancestros de v y u, para luego hallar al LCA mirando cuales vertices son alcanzables entre si
        var listaBFS : MutableList<BusquedaEnAmplitud> = mutableListOf()
        for (vertice in ancestro){
            listaBFS.add(BusquedaEnAmplitud(g,vertice))
        }

        var x = 0
        var lca: Int
        var distancias : MutableList<MutableList<Boolean>> = mutableListOf()


        // Se busca el ancestro desde el cual no se puede alcanzar ninguno de los otros ancestros, por tanto debe ser el LCA 
        // De no serlo, entonces otro vertice seria el LCA y por tanto seria alcanzable ya que todos son ancestros de v y u
        while (true){

            distancias.add(mutableListOf())
            var y = 0

            for (i in ancestro){
                if (y == x){
                    y++
                    continue
                }
                distancias[x].add(listaBFS[x].hayCaminoHasta(i))
                y++
            }

            if (distancias[x].all {it == false} ){
                lca = ancestro[x]
                return lca
            }
            x++
        }
        
    }

    // Tiempo de ejecucion : O(|V|+|E|) por las llamadas a BFS, el ultimo bucle se realiza un numero reducido de veces
}