package etsisi.ems2020.trabajo3.lineadehorizonte;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;


public class LineaHorizonte {
	
	private ArrayList <Punto> LineaHorizonte ;
    /*
     * Constructor sin par�metros
     */
    public LineaHorizonte()
    {
        LineaHorizonte = new ArrayList <Punto>();
    }
    
    
    public LineaHorizonte(int pi, int pd, ArrayList <Edificio> edificios)
    {
    	 LineaHorizonte =  crearLineaHorizonte(pi,pd,edificios).getLineaHorizonte();
        
    }
    /*
     * m�todo que devuelve un objeto de la clase Punto
     */
    public Punto getPunto(int i) {
        return (Punto)this.LineaHorizonte.get(i);
    }
    
    // A�ado un punto a la l�nea del horizonte
    public void addPunto(Punto p)
    {
        LineaHorizonte.add(p);
    }    
    
    // m�todo que borra un punto de la l�nea del horizonte
    public void borrarPunto(int i)
    {
        LineaHorizonte.remove(i);
    }
    
    public int size()
    {
        return LineaHorizonte.size();
    }
    // m�todo que me dice si la l�nea del horizonte est� o no vac�a
    public boolean isEmpty()
    {
        return LineaHorizonte.isEmpty();
    }
   
    /*
      M�todo al que le pasamos una serie de par�metros para poder guardar 
      la linea del horizonte resultante despu�s de haber resuelto el ejercicio
      mediante la t�cnica de divide y vencer�s.
     */
    
    public void guardaLineaHorizonte (String fichero)
    {
        try
        {
           Punto p = new Punto();
            FileWriter fileWriter = new FileWriter(fichero);
            PrintWriter out = new PrintWriter (fileWriter);
         
            for(int i=0; i<this.size(); i++)
            {
                p=(getPunto(i));
                out.print(p.getX() + " " + p.getY() + "\n");
            }
            out.close();
        }
        catch(Exception e){}
    }
    
    
    public void imprimir (){
    	
    	for(int i=0; i< LineaHorizonte.size(); i++ ){
    		//System.out.println("X: " + LineaHorizonte.get(i).getX() + " Y: " + LineaHorizonte.get(i).getY());
    		System.out.println(cadena(i));
    	}
    }
    
    public String cadena (int i){
    	return LineaHorizonte.get(i).toString();
    	
    }
    
    public static void imprimirLineas (LineaHorizonte s1, LineaHorizonte s2) {
		System.out.println("==== S1 ====");
		s1.imprimir();
		System.out.println("==== S2 ====");
		s2.imprimir();
		System.out.println("\n");
	}
    
    
    public  LineaHorizonte crearLineaHorizonte(int pi, int pd, ArrayList<Edificio> edificios) {
		LineaHorizonte linea = new LineaHorizonte(); // LineaHorizonte de salida

// Caso base, la ciudad solo tiene un edificio, el perfil es el de ese edificio. 
		if (pi == pd) {
			crearLineaBase(pi,linea,edificios);
		} else {
// Edificio mitad
			int medio = (pi + pd) / 2;

			LineaHorizonte s1 = this.crearLineaHorizonte(pi, medio,  edificios);
			LineaHorizonte s2 = this.crearLineaHorizonte(medio + 1, pd, edificios);
			linea = LineaHorizonteFussion(s1, s2);
		}
		return linea;
	}

	private void crearLineaBase(int pi, LineaHorizonte linea, ArrayList <Edificio> edificios) {
		Punto p1 = new Punto(); // punto donde se guardara en su X la Xi del efificio y en su Y la altura del
								// edificio
		Punto p2 = new Punto(); // punto donde se guardara en su X la Xd del efificio y en su Y le pondremos el
								// valor 0
		Edificio edificio = new Edificio();
		edificio = edificios.get(pi); // Obtenemos el único edificio y lo guardo en b
		// En cada punto guardamos la coordenada X y la altura.
		p1.setX(edificio.getXi());
		p1.setY(edificio.getY()); // guardo la altura
		p2.setX(edificio.getXd());
		p2.setY(0); // como el edificio se compone de 3 variables, en la Y de p2 le añadiremos un 0
		// Añado los puntos a la línea del horizonte
		linea.addPunto(p1);
		linea.addPunto(p2);

	}

	/**
	 * Función encargada de fusionar los dos LineaHorizonte obtenidos por la técnica
	 * divide y vencerás. Es una función muy compleja ya que es la encargada de
	 * decidir si un edificio solapa a otro, si hay edificios contiguos, etc. y
	 * solucionar dichos problemas para que el LineaHorizonte calculado sea el
	 * correcto.
	 */
	public LineaHorizonte LineaHorizonteFussion(LineaHorizonte s1, LineaHorizonte s2) {
		// en estas variables guardaremos las alturas de los puntos anteriores, en s1y
		// la del s1, en s2y la del s2
		// y en prev guardaremos la previa del segmento anterior introducido

		int s1y = -1, s2y = -1, prev = -1;
		LineaHorizonte salida = new LineaHorizonte(); // LineaHorizonte de salida

		Punto p1 = new Punto(); // punto donde guardaremos el primer punto del LineaHorizonte s1
		Punto p2 = new Punto(); // punto donde guardaremos el primer punto del LineaHorizonte s2
		imprimirLineas(s1,s2);

		// Mientras tengamos elementos en s1 y en s2
		while ((!s1.isEmpty()) && (!s2.isEmpty())) {
			p1 = s1.getPunto(0); // guardamos el primer elemento de s1
			p2 = s2.getPunto(0); // guardamos el primer elemento de s2
			Punto paux = new Punto(); // Inicializamos la variable paux
			int p1x = p1.getX(); // guardamos el primer elemento de s1
			int p1y =p1.getY(); // guardamos el primer elemento de s1
			int p2x = p2.getX(); // guardamos el primer elemento de s2
			int p2y = p2.getY(); // guardamos el primer elemento de s2

			if (p1x < p2x) // si X del s1 es menor que la X del s2
			{
				paux.setX(p1x); // guardamos en paux esa X
				paux.setY(Math.max(p1y, s2y)); // y hacemos que el maximo entre la Y del s1 y la altura previa del
														// s2 sea la altura Y de paux
				prev=aniadirPuntoSalida(paux,  prev,  salida);
				s1y = p1y; // actualizamos la altura s1y
				s1.borrarPunto(0); // en cualquier caso eliminamos el punto de s1 (tanto si se añade como si no es
									// valido)
			} else if (p1x > p2x) // si X del s1 es mayor que la X del s2
			{
				paux.setX(p2x); // guardamos en paux esa X
				paux.setY(Math.max(p2y, s1y)); // y hacemos que el maximo entre la Y del s2 y la altura previa del
														// s1 sea la altura Y de paux

				prev=aniadirPuntoSalida(paux,prev,salida);
				s2y = p2y; // actualizamos la altura s2y
				s2.borrarPunto(0); // en cualquier caso eliminamos el punto de s2 (tanto si se añade como si no es
									// valido)
			} else // si la X del s1 es igual a la X del s2
			{
				if ((p1y > p2y) && (p1y != prev)) // guardaremos aquel punto que tenga la altura mas
																	// alta
				{
					salida.addPunto(p1);
					prev = p1y;
				}
				else if ((p1y <= p2y) && (p2y != prev)) {
					salida.addPunto(p2);
					prev = p2y;
				}
				s1y = p1y; // actualizamos la s1y e s2y
				s2y = p2y;
				s1.borrarPunto(0); // eliminamos el punto del s1 y del s2
				s2.borrarPunto(0);
			}
		}
		
		quedanElementos(s1,salida,prev);
		quedanElementos(s2,salida,prev);
		return salida;
	}
	
	private int aniadirPuntoSalida(Punto paux, int prev, LineaHorizonte salida) {
		if (paux.getY() != prev) // si este maximo no es igual al del segmento anterior
		{
			salida.addPunto(paux); // añadimos el punto al LineaHorizonte de salida
			prev = paux.getY(); // actualizamos prev
		}
		return prev;
	}
	
	private void quedanElementos(LineaHorizonte s1, LineaHorizonte salida, int prev ) {
		while ((!s1.isEmpty())) // si aun nos quedan elementos en el s1
		{
			Punto paux = s1.getPunto(0); // guardamos en paux el primer punto

			if (paux.getY() != prev) // si paux no tiene la misma altura del segmento previo
			{
				salida.addPunto(paux); // lo añadimos al LineaHorizonte de salida
				prev = paux.getY(); // y actualizamos prev
			}
			s1.borrarPunto(0); // en cualquier caso eliminamos el punto de s1 (tanto si se añade como si no es
								// valido)
	}
	}
	
	private  ArrayList <Punto> getLineaHorizonte (){
		return this.LineaHorizonte;
	}
    
}

