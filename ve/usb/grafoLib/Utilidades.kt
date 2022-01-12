package ve.usb.grafoLib


fun digrafoInverso(g: GrafoDirigido) : GrafoDirigido {
    /* Funcion para conseguir el digrafo inverso de un digrafo 
    g: Un grafo dirigido

    precondicion: g es un grafo dirigido
    postcondicion: se retorna el grafo inverso de g
    tiempo de ejecucion: O(|V| + |E|)
    */

    // Inicializar el grafo inverso
    var gInverso = GrafoDirigido(g.obtenerNumeroDeVertices())

    // Para cada vertice inicial del grafo g, se busca el vertice sumidero
    // de los arcos donde incide el vertice inicial. Luego, se agrega un arco a gInverso
    // con el vertice sumidero siendo el fuente y el fuente siendo el sumidero 
    for (verticeInicial in 0 until g.obtenerNumeroDeVertices()){
        for(arco in g.adyacentes(verticeInicial)){
            gInverso.agregarArco(
                Arco(arco.sumidero(), arco.fuente()))
        }
    }

    return gInverso
}

 
